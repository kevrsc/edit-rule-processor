package com.fafsaeditruleprocessor.api;

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
}
