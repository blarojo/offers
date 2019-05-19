package uk.worldpay.offers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.worldpay.offers.exceptions.CancelNotAllowedException;
import uk.worldpay.offers.exceptions.ExistingActiveOfferException;
import uk.worldpay.offers.exceptions.OfferNotFoundException;;
import uk.worldpay.offers.domain.Offer;
import uk.worldpay.offers.repository.OfferRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Class OfferServiceImpl
 *
 * Implements the services related to the Offer domain in OfferService.
 *
 * @author laurinf
 *
 */
@RequiredArgsConstructor

@Service
@Transactional
public class OfferServiceImpl implements OfferService {


    private final OfferRepository offerRepository;

    /**
     * Creates a new offer for an item in a defined period
     *
     * @param offer the offer to be created
     * @return a new offer
     * @throws ExistingActiveOfferException - if there is already another offer for that item in the given period
     */
    @Override
    public Offer create(final Offer offer) throws ExistingActiveOfferException {
        List<Offer> offersForItem =  Stream.concat(offerRepository.findByItemIdAndStatus(offer.getItemId(),Offer.Status.CREATED).stream(),
                offerRepository.findByItemIdAndStatus(offer.getItemId(),Offer.Status.ACTIVE).stream()).collect(Collectors.toList());
        for (Offer createdOffer: offersForItem) {
            if ((offer.getStartDate().toInstant().isBefore(createdOffer.getStartDate().toInstant()))
                    && (offer.getStartDate().toInstant().isAfter(offer.getEndDate().toInstant()))) {
                        throw new ExistingActiveOfferException(offer.getItemId());
            }
        }
        return offerRepository.save(offer);
    }


    /**
     * Returns a list containing all the offers
     *
     * @return a list of offers
     */
    @Override
    public List<Offer> findAll() {
        List<Offer> offers =  offerRepository.findAll();
        offers.forEach(o -> this.updateStatus(o));
        return offers;

    }


    /**
     * @param offer an offer
     * @param newStatus the new status for the given offer
     * @return the offer updated with the new status
     */
    protected Offer updateStatus(Offer offer, Offer.Status newStatus) {
        offer.setStatus(newStatus);
        return offerRepository.save(offer);
    }


    /**
     * Updates the status of an offer as follows:
     *
     *   if the offer is in CREATED status and the starting date is before the current date, it will be updated to ACTIVE
     *   if the offer is in CREATED or ACTIVE status and the expiring date is before the current date, it will be updated to EXPIRED
     *   if the offer is in EXPIRED or CANCELLED status, it will do nothing.
     *
     * This method has to be invoked before returning the offer, to avoid data clashing between the dates and status
     *
     * @param offer the offer whose status has to be updated
     * @return the offer with the status updated
     */
    protected Offer updateStatus(Offer offer) {
           if (offer.getStatus().equals(Offer.Status.CREATED)) {
               if (offer.getStartDate().toInstant().isAfter(Instant.now())) {
                   return updateStatus(offer, Offer.Status.ACTIVE);
               }
               if (offer.getEndDate().toInstant().isBefore(Instant.now())) {
                   return updateStatus(offer, Offer.Status.EXPIRED);
               }
           }
           if (offer.getStatus().equals(Offer.Status.ACTIVE)) {
               if (offer.getEndDate().toInstant().isBefore(Instant.now())) {
                   return updateStatus(offer, Offer.Status.EXPIRED);
               }
           }

          return offer;
    }

    /**
     * Finds an offer from its id
     *
     * @param id the id of the offer to be returned
     * @return the offer with the given id
     * @throws OfferNotFoundException - if the given id does not match any offer
     */
    @Override
    public Offer findById(final Long id) throws OfferNotFoundException {

            Offer o = offerRepository.findById(id)
                    .orElseThrow(() -> new OfferNotFoundException(id));
            return this.updateStatus(o);

        }

    /**
     * Cancels an offer with a given id. This method doesn't delete the persistent representation of the object.
     * It will update the status to CANCELLED and create a cancellation date.
     *
     * @param idOffer the id of the offer to be cancelled
     *
     * @throws CancelNotAllowedException - if the idOffer is already in EXPIRED or CANCELLED status
     * @throws OfferNotFoundException - if the given id does not match any offer
     */
    @Override
    public void cancel(final Long idOffer) throws CancelNotAllowedException, OfferNotFoundException {
        Offer offer = this.findById(idOffer);
        if (offer.getStatus().equals(Offer.Status.CREATED) || (offer.getStatus().equals(Offer.Status.ACTIVE))) {
            offer.setCancelDate(new Date());
            offer.setStatus(Offer.Status.CANCELLED);
            offerRepository.save(offer);
        } else {
            throw new CancelNotAllowedException(offer.getId(),offer.getStatus());
        }
    }

}
