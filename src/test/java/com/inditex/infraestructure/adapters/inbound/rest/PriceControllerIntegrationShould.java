package com.inditex.infraestructure.adapters.inbound.rest;

import com.inditex.TestSecurityConfig;
import com.inditex.application.PriceService;
import com.inditex.domain.model.Price;
import com.inditex.infraestructure.adapters.inbound.rest.model.PriceRestResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestSecurityConfig.class)
@AutoConfigureMockMvc
public class PriceControllerIntegrationShould {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceService priceService;

    @Test
    @DisplayName("return unknow endpoint")
    void return_404_given_a_path_incorrect() throws Exception {
        //when then
        mockMvc.perform(get("/price/1995"))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @CsvSource({ "1,35455,2020-06-14T10:00", "1,35455,2020-06-14T16:00", "1,35455,2020-06-14T21:00",
            "1,35455,2020-06-15T10:00", "1,35455,2020-06-16T21:00" })
    @DisplayName("return price information when filter by productId, brandId and applicationDate")
    void return_price_information_given_brandId_productId_and_applicationDate(Long brandId, Long productId, LocalDateTime applicationDate)
            throws Exception {
        //given
        Price price = new Price(productId, brandId, 2L, 1, 35.45, "EUR", applicationDate.minusHours(1), applicationDate.plusHours(4));
        given(priceService.getPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate)).willReturn(price);

        //when then
        String path = "/price/" + brandId + "/" + productId + "?applicationDate=" + applicationDate;
        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("price").value("35.45"));
    }
}
