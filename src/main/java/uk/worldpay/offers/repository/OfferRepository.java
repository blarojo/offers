package uk.worldpay.offers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.worldpay.offers.domain.Offer;

import java.util.List;

/**
 * Interface OfferRepository
 *
 * Provides the CRUD operations for the Offer domain
 *
 * @author laurinf
 *
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByItemIdAndStatus(Long itemId, Offer.Status status);


}