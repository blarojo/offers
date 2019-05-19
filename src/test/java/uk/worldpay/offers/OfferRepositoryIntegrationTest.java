package uk.worldpay.offers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import uk.worldpay.offers.domain.Offer;
import uk.worldpay.offers.repository.OfferRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
    @DataJpaTest
    public class OfferRepositoryIntegrationTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private OfferRepository offerRepository;

        private Offer getOffer1()  {
            Date startDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE,3);
            Date endDate = c.getTime();

            Offer offer = new Offer();
            offer.setItemId(1L);
            offer.setDescription("Last units opportunity. Half Price!!!");
            offer.setPrice(10.10);
            offer.setStartDate(startDate);
            offer.setEndDate(endDate);

            return offer;
        }


        private Offer getOffer2()  {
            Date startDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE,5);
            Date endDate = c.getTime();

            Offer offer = new Offer();
            offer.setItemId(2L);
            offer.setDescription("Last units opportunity. Half Price!!!");
            offer.setPrice(199.95);
            offer.setStartDate(startDate);
            offer.setEndDate(endDate);
            return offer;
        }


    @Test
    public void whenFindById_thenReturnOffer() {
        // given
        Offer offer = this.getOffer1();
        entityManager.persist(offer);
        entityManager.flush();

        // when
        Optional<Offer> found = offerRepository.findById(offer.getId());
        // then
        assertThat(found.get().getId())
                .isEqualTo(offer.getId());
    }


    @Test
    public void whenFindByItemIdAndStatus_thenReturnOffer() {
        // given
        Offer offer = this.getOffer1();
        entityManager.persist(offer);
        entityManager.flush();

        // when
        List<Offer> results = offerRepository.findByItemIdAndStatus(offer.getItemId(),offer.getStatus());
        Offer found = results.get(0);
        // then
        assertThat(offer)
                .isEqualTo(found);
    }

    @Test
    public void whenFindAll_thenReturnOffers() {
        // given
        Offer offer1 = this.getOffer1();
        entityManager.persist(offer1);
        entityManager.flush();

        Offer offer2 = this.getOffer2();
        entityManager.persist(offer2);
        entityManager.flush();
        //when

        List<Offer> results = offerRepository.findAll();

        //then
        assertThat(results).containsExactlyInAnyOrder(offer1,offer2);

    }

}
