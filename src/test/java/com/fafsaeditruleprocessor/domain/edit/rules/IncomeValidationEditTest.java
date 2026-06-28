package com.fafsaeditruleprocessor.domain.edit.rules;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.withIncome;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncomeValidationEditTest {

    private IncomeValidationEdit edit;

    @BeforeEach
    void setUp() {
        edit = new IncomeValidationEdit();
    }

    @Test
    void appliesToAllApplications() {
        assertTrue(edit.appliesTo(validDependentApplication()));
    }

    @Test
    void passesForNonNegativeIncomes() {
        assertTrue(edit.evaluate(validDependentApplication()).passed());
    }

    @Test
    void passesForZeroIncomes() {
        assertTrue(edit.evaluate(withIncome(BigDecimal.ZERO, BigDecimal.ZERO)).passed());
    }

    @Test
    void failsForNegativeStudentIncome() {
        var outcome = edit.evaluate(withIncome(BigDecimal.valueOf(-1), BigDecimal.valueOf(1000)));

        assertFalse(outcome.passed());
        assertTrue(outcome.message().toLowerCase().contains("income")
                || outcome.message().contains("negative"));
    }

    @Test
    void failsForNegativeParentIncome() {
        var outcome = edit.evaluate(withIncome(BigDecimal.valueOf(1000), BigDecimal.valueOf(-1)));

        assertFalse(outcome.passed());
    }

    @Test
    void returnsActionableFailureMessageForNegativeStudentIncome() {
        var outcome = edit.evaluate(withIncome(BigDecimal.valueOf(-1000), BigDecimal.valueOf(65000)));

        assertEquals("Income values cannot be negative", outcome.message());
    }

    @Test
    void returnsActionableFailureMessageForNegativeParentIncome() {
        var outcome = edit.evaluate(withIncome(BigDecimal.valueOf(1000), BigDecimal.valueOf(-1)));

        assertEquals("Income values cannot be negative", outcome.message());
    }

    @Test
    void hasStableCodeAndSeverity() {
        assertTrue(edit.code().equals("INCOME_VALIDATION"));
        assertTrue(edit.name().equals("Income Validation"));
        assertTrue(edit.severity() == EditSeverity.ERROR);
    }
}
