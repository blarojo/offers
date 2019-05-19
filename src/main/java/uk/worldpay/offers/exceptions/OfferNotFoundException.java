package uk.worldpay.offers.exceptions;

/**
 * Class OfferNotFoundException
 *
 * This exception will be thrown if there is any attempt to retrieve an offer whose identifier does not exist.
 *
 * @author laurinf
 *
 */
public class OfferNotFoundException extends RuntimeException {

    public OfferNotFoundException(Long id) {
        super("Could not find offer " + id);
    }
}