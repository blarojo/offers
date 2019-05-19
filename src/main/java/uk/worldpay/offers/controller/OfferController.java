package uk.worldpay.offers.controller;

import org.springframework.hateoas.Resource;
import uk.worldpay.offers.domain.Offer;
import java.util.List;
import java.util.stream.Collectors;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.worldpay.offers.exceptions.CancelNotAllowedException;
import uk.worldpay.offers.exceptions.ExistingActiveOfferException;
import uk.worldpay.offers.exceptions.OfferNotFoundException;
import uk.worldpay.offers.service.OfferService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequiredArgsConstructor
@Slf4j

/**
 * Class OfferController
 *
 * Class providing RESTFull webServices over offers according to the HAL API model
 *
 * @author laurinf
 *
 */
@RestController
@RequestMapping("/api/v1")
public
class OfferController {

    private final OfferService service;
    private final OfferResourceAssembler assembler;


    /**
     * test mapping to verify the system is running
     *
     * /api/v1/
     *
     * @return string OK if the system is running
     */
    @RequestMapping("/")
    String ControllerRunning() {
        return "OK";
    }


    /**
     * returns the complete list of offers no matter what the status is
     *
     * HTTP GET /api/v1/offers
     *
     * @return a list of resources containing the offers
     */
    @GetMapping("/offers")
    Resources<Resource<Offer>> all() {

        List<Resource<Offer>> offers = service.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(offers,
                linkTo(methodOn(OfferController.class).all()).withSelfRel());
    }

    /**
     * Returns the offer corresponding to the given id
     *
     * GET /api/v1/offers/{id}
     *
     * @param id the id of the offer
     * @return Status 200 and the offer corresponding with the given id if present. Status 404 if not found.
     * @throws OfferNotFoundException if the offer can not be found
     */
    @GetMapping("/offers/{id}")
    Resource<Offer> getOffer(@PathVariable Long id) throws OfferNotFoundException {

        Offer offer = service.findById(id);

        return assembler.toResource(offer);
    }


    /**
     * creates a new offer from the offer received in the body of the HTTP request
     *
     * HTTP POST /api/v1/offers
     *
     *
     * @param newOffer
     * @return ResponseEntity with :
     *                      status 201 and the offer if the offer has correctly been created
     *                      status 405 if there is already a running offer for that item and that period
     *
     * @throws URISyntaxException if any link could not be parsed as a URI reference
     * @throws ExistingActiveOfferException if there already exists an offer for the given item and period
     */
    @PostMapping("/offers")
    ResponseEntity<?> newOffer(@RequestBody Offer newOffer) throws URISyntaxException, ExistingActiveOfferException {

        Resource<Offer> resource = assembler.toResource(service.create(newOffer));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }


    /**
     * Cancels an offer
     *
     * DELETE /offers/{id}
     *
     * @param id the id of the offer to be cancelled
     * @return Status 204 is the offer has been correctly cancelled.
     * @throws CancelNotAllowedException returns status 405 if the offer was previously cancelled or it expired.
     * @throws OfferNotFoundException returns status 404 if the given id does not correspond with any offer.
     */
    @DeleteMapping("/offers/{id}/cancel")
    ResponseEntity<?> cancel(@PathVariable Long id) throws CancelNotAllowedException, OfferNotFoundException {

        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}