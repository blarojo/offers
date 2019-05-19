package uk.worldpay.offers.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import uk.worldpay.offers.domain.Offer;
/**
 * Class OfferResourceAssembler
 *
 * Class in charge of creating the links to comply with the HAL API model
 *
 * @author laurinf
 *
 */
@Component
class OfferResourceAssembler implements ResourceAssembler<Offer, Resource<Offer>> {

    @Override
    public Resource<Offer> toResource(Offer offer) {

        Resource<Offer> offerResource = new Resource<>(offer,
                linkTo(methodOn(OfferController.class).getOffer(offer.getId())).withSelfRel(),
                linkTo(methodOn(OfferController.class).all()).withRel("offers"));
        if (offer.getStatus() == Offer.Status.ACTIVE || offer.getStatus() == Offer.Status.CREATED) {
            offerResource.add(
                    linkTo(methodOn(OfferController.class)
                            .cancel(offer.getId())).withRel("cancel"));
        }
        return offerResource;
    }
}