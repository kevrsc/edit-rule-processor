package com.fafsaeditruleprocessor.application;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.independentSingleApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.invalidMultiViolationApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.model.OverallStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ValidationServiceTest {

    @Autowired
    private ValidationService validationService;

    @Test
    void validDependentApplicationReturnsValidResult() {
        var result = validationService.validate(validDependentApplication());

        assertEquals(OverallStatus.VALID, result.overallStatus());
        assertEquals(7, result.edits().size());
        assertTrue(result.edits().stream().allMatch(edit -> edit.passed()));
    }

    @Test
    void invalidApplicationReturnsAllViolations() {
        var result = validationService.validate(invalidMultiViolationApplication());

        assertEquals(OverallStatus.INVALID, result.overallStatus());
        assertEquals(7, result.edits().size());
        assertEquals(7, result.edits().stream().filter(edit -> !edit.passed()).count());
    }

    @Test
    void independentApplicationSkipsConditionalEdits() {
        var result = validationService.validate(independentSingleApplication());

        assertEquals(OverallStatus.VALID, result.overallStatus());
        assertTrue(result.edits().stream()
                .filter(edit -> edit.id().equals("DEPENDENT_PARENT_INCOME")
                        || edit.id().equals("MARITAL_STATUS"))
                .allMatch(edit -> edit.passed()));
    }
}
