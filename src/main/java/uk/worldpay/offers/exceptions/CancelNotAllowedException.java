package uk.worldpay.offers.exceptions;

import uk.worldpay.offers.domain.Offer;

/**
 * Class CancelNotAllowedException
 *
 * This exception will be thrown if there is any attempt to cancel any offer which was already cancelled or was expired.
 *
 * @author laurinf
 *
 */
public class CancelNotAllowedException extends RuntimeException {
    public CancelNotAllowedException(Long id, Offer.Status status) {
        super("Cancellation is not allowed for offer" + id + "with status " + status.getTextualStatus());
    }
}
