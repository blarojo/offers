package uk.worldpay.offers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import uk.worldpay.offers.controller.OfferController;
import uk.worldpay.offers.domain.Offer;
import uk.worldpay.offers.service.OfferService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(OfferController.class)
public class offersRestTest {

    @MockBean
    private OfferService offerService;


    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    private static String BASE_PATH = "http://localhost/api/v1";
    private static final long ID = 1L;
    private Offer offer;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


    private void setupOffer()  {
        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,3);
        Date endDate = c.getTime();

        offer = new Offer();
        offer.setId(ID);
        offer.setItemId(1L);
        offer.setDescription("Last units opportunity. Half Price!!!");
        offer.setPrice(10.10);
        offer.setStartDate(startDate);
        offer.setEndDate(endDate);
    }

    @Test
    public void contextLoads()  {
        assertThat(offerService).isNotNull();
    }




    @Test
    public void getOfferReturnsCorrectResponse() throws Exception {
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        setupOffer();

        log.info("Offer value" + offer);

        given(offerService.findById(ID)).willReturn(offer);
        final ResultActions result = mockMvc.perform(get(BASE_PATH + "/offers/" + ID));

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("id", is(offer.getId().intValue())))
                .andExpect(jsonPath("description", is(offer.getDescription())))
                .andExpect(jsonPath("itemId", is(offer.getItemId().intValue())))
                .andExpect(jsonPath("price", is(offer.getPrice())))
                .andExpect(jsonPath("startDate", is(df.format(offer.getStartDate()))))
                .andExpect(jsonPath("status", is(offer.getStatus().getTextualStatus())))
                .andExpect(jsonPath("_links.self.href", is(BASE_PATH + "/offers/" + offer.getId().intValue())))
                .andExpect(jsonPath("_links.offers.href", is(BASE_PATH + "/offers")))
                ;
    }

    @Test
    public void getAllOffersReturnsCorrectResponse() throws Exception {

        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        setupOffer();
        given(offerService.findAll()).willReturn(Arrays.asList(offer));
        final ResultActions result = mockMvc.perform(get(BASE_PATH + "/offers"));
        result.andExpect(status().isOk());
        result
                .andExpect(jsonPath("_links.self.href", is(BASE_PATH + "/offers")))
                .andExpect(jsonPath("_embedded.offerList[0].id", is(offer.getId().intValue())))
                .andExpect(jsonPath("_embedded.offerList[0].description", is(offer.getDescription())))
                .andExpect(jsonPath("_embedded.offerList[0].itemId", is(offer.getItemId().intValue())))
                .andExpect(jsonPath("_embedded.offerList[0].price", is(offer.getPrice())))
                .andExpect(jsonPath("_embedded.offerList[0].startDate",  is(df.format(offer.getStartDate()))))
                .andExpect(jsonPath("_embedded.offerList[0].status", is(offer.getStatus().getTextualStatus())))
                .andExpect(jsonPath("_embedded.offerList[0]._links.self.href", is(BASE_PATH + "/offers/" + offer.getId().intValue())))
                .andExpect(jsonPath("_embedded.offerList[0]._links.offers.href", is(BASE_PATH + "/offers")))
        ;
    }

    @Test
    public void createNewOfferReturnsCorrectResponse() throws Exception {
       df.setTimeZone(TimeZone.getTimeZone("GMT"));
        setupOffer();

        given(offerService.create(offer)).willReturn(offer);

        final ResultActions result =
                mockMvc.perform(
                        post(BASE_PATH + "/offers")
                                .content(mapper.writeValueAsBytes(offer))
                                .contentType(MediaType.APPLICATION_JSON_UTF8));
        result.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("id", is(offer.getId().intValue())))
                .andExpect(jsonPath("description", is(offer.getDescription())))
                .andExpect(jsonPath("itemId", is(offer.getItemId().intValue())))
                .andExpect(jsonPath("price", is(offer.getPrice())))
                .andExpect(jsonPath("startDate", is(df.format(offer.getStartDate()))))
                .andExpect(jsonPath("status", is(offer.getStatus().getTextualStatus())))
                .andExpect(jsonPath("_links.self.href", is(BASE_PATH + "/offers/" + offer.getId().intValue())))
                .andExpect(jsonPath("_links.offers.href", is(BASE_PATH + "/offers")))
        ;
    }

    @Test
    public void cancelOfferReturnsCorrectResponse() throws Exception {
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        setupOffer();

        given(offerService.findById(ID)).willReturn(offer);

        mockMvc.perform(delete(BASE_PATH + "/offers/" + ID + "/cancel"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }



}