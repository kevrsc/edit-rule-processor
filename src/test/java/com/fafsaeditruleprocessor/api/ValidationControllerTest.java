package com.fafsaeditruleprocessor.api;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.INDEPENDENT_SINGLE_SAMPLE_JSON;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.INVALID_SAMPLE_JSON;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.MARRIED_WITHOUT_SPOUSE_SAMPLE_JSON;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.VALID_SAMPLE_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class ValidationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void validSampleReturnsValidWithAllEditsPassed() throws Exception {
        mockMvc.perform(post("/api/v1/applications/validate")
                        .contentType(APPLICATION_JSON)
                        .content(VALID_SAMPLE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("VALID"))
                .andExpect(jsonPath("$.edits.length()").value(7))
                .andExpect(jsonPath("$.edits[?(@.id == 'STUDENT_AGE')].passed").value(true))
                .andExpect(jsonPath("$.edits[?(@.id == 'SSN_FORMAT')].passed").value(true))
                .andExpect(jsonPath("$.edits[?(@.id == 'DEPENDENT_PARENT_INCOME')].passed")
                        .value(true))
                .andExpect(jsonPath("$.edits[?(@.id == 'INCOME_VALIDATION')].passed").value(true))
                .andExpect(jsonPath("$.edits[?(@.id == 'HOUSEHOLD_LOGIC')].passed").value(true))
                .andExpect(jsonPath("$.edits[?(@.id == 'STATE_CODE')].passed").value(true))
                .andExpect(jsonPath("$.edits[?(@.id == 'MARITAL_STATUS')].passed").value(true));
    }

    @Test
    void invalidSampleReturnsInvalidWithAllSevenFailedEdits() throws Exception {
        mockMvc.perform(post("/api/v1/applications/validate")
                        .contentType(APPLICATION_JSON)
                        .content(INVALID_SAMPLE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("INVALID"))
                .andExpect(jsonPath("$.edits.length()").value(7))
                .andExpect(jsonPath("$.edits[?(@.id == 'STUDENT_AGE')].passed").value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'SSN_FORMAT')].passed").value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'DEPENDENT_PARENT_INCOME')].passed")
                        .value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'INCOME_VALIDATION')].passed").value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'HOUSEHOLD_LOGIC')].passed").value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'STATE_CODE')].passed").value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'MARITAL_STATUS')].passed").value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'STUDENT_AGE')].message")
                        .value("Student must be at least 14 years old"))
                .andExpect(jsonPath("$.edits[?(@.id == 'SSN_FORMAT')].message")
                        .value("SSN must be exactly 9 digits"))
                .andExpect(jsonPath("$.edits[?(@.id == 'DEPENDENT_PARENT_INCOME')].message")
                        .value("Parent income is required for dependent applicants"))
                .andExpect(jsonPath("$.edits[?(@.id == 'INCOME_VALIDATION')].message")
                        .value("Income values cannot be negative"))
                .andExpect(jsonPath("$.edits[?(@.id == 'HOUSEHOLD_LOGIC')].message")
                        .value("Number in college cannot exceed number in household"))
                .andExpect(jsonPath("$.edits[?(@.id == 'STATE_CODE')].message")
                        .value("State code must be a valid US state abbreviation"))
                .andExpect(jsonPath("$.edits[?(@.id == 'MARITAL_STATUS')].message")
                        .value("Spouse information is required for married applicants"));
    }

    @Test
    void malformedJsonReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/applications/validate")
                        .contentType(APPLICATION_JSON)
                        .content("{not valid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("MALFORMED_REQUEST"))
                .andExpect(jsonPath("$.message").value("Request body is not valid JSON"));
    }

    @Test
    void missingRequiredFieldsReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/applications/validate")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void independentSingleSkipsParentIncomeAndMaritalStatusEdits() throws Exception {
        mockMvc.perform(post("/api/v1/applications/validate")
                        .contentType(APPLICATION_JSON)
                        .content(INDEPENDENT_SINGLE_SAMPLE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("VALID"))
                .andExpect(jsonPath("$.edits[?(@.id == 'DEPENDENT_PARENT_INCOME')].passed")
                        .value(true))
                .andExpect(jsonPath("$.edits[?(@.id == 'MARITAL_STATUS')].passed").value(true));
    }

    @Test
    void marriedWithoutSpouseFailsMaritalStatusEdit() throws Exception {
        mockMvc.perform(post("/api/v1/applications/validate")
                        .contentType(APPLICATION_JSON)
                        .content(MARRIED_WITHOUT_SPOUSE_SAMPLE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("INVALID"))
                .andExpect(jsonPath("$.edits[?(@.id == 'MARITAL_STATUS')].passed").value(false))
                .andExpect(jsonPath("$.edits[?(@.id == 'MARITAL_STATUS')].message")
                        .value("Spouse information is required for married applicants"));
    }
}
