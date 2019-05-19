package uk.worldpay.offers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class ControllerAdvice
 *
 * Class in charge of creating HTTP error values for the operations in OfferController
 *
 * @author laurinf
 *
 */
@ControllerAdvice
class OfferControllerAdvice {

    @ResponseBody
    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String offerNotFoundHandler(OfferNotFoundException ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(CancelNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String cancelNotAllowedHandler(CancelNotAllowedException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ExistingActiveOfferException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String existingActiveOfferHandler(ExistingActiveOfferException ex) {
        return ex.getMessage();
    }
}