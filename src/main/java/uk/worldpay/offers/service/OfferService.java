package uk.worldpay.offers.service;

import uk.worldpay.offers.exceptions.CancelNotAllowedException;
import uk.worldpay.offers.exceptions.ExistingActiveOfferException;
import uk.worldpay.offers.exceptions.OfferNotFoundException;
import uk.worldpay.offers.domain.Offer;

import java.util.List;

/**
 * Interface OfferService
 *
 * Provides the services related to the Offer domain
 *
 * @author laurinf
 *
 */
public interface OfferService {

    /**
     * Creates a new offer for an item in a defined period
     *
     * @param offer the offer to be created
     * @return a new offer
     * @throws ExistingActiveOfferException - if there is already another offer for that item in the given period
     */
    public Offer create(final Offer offer) throws ExistingActiveOfferException;

    /**
     * Returns a list containing all the offers
     *
     * @return a list of offers
     */
    public List<Offer> findAll();


    /**
     * Finds an offer from its id
     *
     * @param id the id of the offer to be returned
     * @return the offer with the given id
     * @throws OfferNotFoundException - if the given id does not match any offer
     */
    public Offer findById(final Long id)  throws OfferNotFoundException;

    /**
     * Cancels an offer with a given id. This method doesn't delete the persistent representation of the object.
     * It will update the status to CANCELLED and create a cancellation date.
     *
     * @param idOffer the id of the offer to be cancelled
     *
     * @throws CancelNotAllowedException - if the idOffer is already in EXPIRED or CANCELLED status
     * @throws OfferNotFoundException - if the given id does not match any offer
     */
    public void cancel(final Long idOffer) throws CancelNotAllowedException, OfferNotFoundException;

}
