package uk.worldpay.offers.exceptions;

/**
 * Class ExistingActiveOfferException
 *
 * This exception will be thrown if there is any attempt to create a new offer of an item
 * in a period when there already exists another offer
 *
 * @author laurinf
 *
 */
public class ExistingActiveOfferException extends RuntimeException {
    public ExistingActiveOfferException(Long id) {
        super("There is already an active offer for item " + id);
    }
}
