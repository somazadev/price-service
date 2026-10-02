package com.ecommerce.price_service.infrastructure.adapter.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerIntegrationTest {

    private static final String PRICES_URL = "/api/v1/prices";
    private static final String PRODUCT_ID = "35455";
    private static final String BRAND_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Required scenarios for product 35455 and brand 1")
    @ParameterizedTest(name = "{0}: request at {1} -> price list {2}, {5} EUR")
    @CsvSource({
            "Test 1, 2020-06-14T10:00:00, 1, 2020-06-14T00:00:00, 2020-12-31T23:59:59, 35.50",
            "Test 2, 2020-06-14T16:00:00, 2, 2020-06-14T15:00:00, 2020-06-14T18:30:00, 25.45",
            "Test 3, 2020-06-14T21:00:00, 1, 2020-06-14T00:00:00, 2020-12-31T23:59:59, 35.50",
            "Test 4, 2020-06-15T10:00:00, 3, 2020-06-15T00:00:00, 2020-06-15T11:00:00, 30.50",
            "Test 5, 2020-06-16T21:00:00, 4, 2020-06-15T16:00:00, 2020-12-31T23:59:59, 38.95"
    })
    void returnsTheApplicablePrice(String scenario, String applicationDate, int expectedPriceList,
                                   String expectedStartDate, String expectedEndDate, double expectedPrice)
            throws Exception {

        mockMvc.perform(get(PRICES_URL)
                        .param("applicationDate", applicationDate)
                        .param("productId", PRODUCT_ID)
                        .param("brandId", BRAND_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(expectedPriceList))
                .andExpect(jsonPath("$.startDate").value(expectedStartDate))
                .andExpect(jsonPath("$.endDate").value(expectedEndDate))
                .andExpect(jsonPath("$.price").value(expectedPrice))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returnsNotFoundWhenNoPriceApplies() throws Exception {
        mockMvc.perform(get(PRICES_URL)
                        .param("applicationDate", "2019-01-01T00:00:00")
                        .param("productId", PRODUCT_ID)
                        .param("brandId", BRAND_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Price not found"))
                .andExpect(jsonPath("$.detail").value(containsString("35455")));
    }

    @Test
    void returnsNotFoundForUnknownProduct() throws Exception {
        mockMvc.perform(get(PRICES_URL)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "99999")
                        .param("brandId", BRAND_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Price not found"))
                .andExpect(jsonPath("$.detail").value(containsString("99999")));
    }

    @Test
    void returnsBadRequestWhenAParameterIsMissing() throws Exception {
        mockMvc.perform(get(PRICES_URL)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", PRODUCT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Invalid request"))
                .andExpect(jsonPath("$.detail").value("Required parameter 'brandId' is missing"));
    }

    @Test
    void returnsBadRequestWhenTheDateIsMalformed() throws Exception {
        mockMvc.perform(get(PRICES_URL)
                        .param("applicationDate", "14/06/2020 10:00")
                        .param("productId", PRODUCT_ID)
                        .param("brandId", BRAND_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Invalid request"))
                .andExpect(jsonPath("$.detail").value(containsString("applicationDate")));
    }

    @Test
    void returnsBadRequestWhenAnIdentifierIsNotPositive() throws Exception {
        mockMvc.perform(get(PRICES_URL)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", PRODUCT_ID)
                        .param("brandId", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Invalid request"))
                .andExpect(jsonPath("$.detail").value("brandId must be a positive number"));
    }
}
