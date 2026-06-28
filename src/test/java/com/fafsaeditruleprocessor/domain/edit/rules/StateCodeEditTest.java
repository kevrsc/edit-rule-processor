package com.fafsaeditruleprocessor.domain.edit.rules;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.withStateOfResidence;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateCodeEditTest {

    private StateCodeEdit edit;

    @BeforeEach
    void setUp() {
        edit = new StateCodeEdit();
    }

    @Test
    void appliesToAllApplications() {
        assertTrue(edit.appliesTo(validDependentApplication()));
    }

    @Test
    void passesForValidStateCode() {
        assertTrue(edit.evaluate(validDependentApplication()).passed());
    }

    @Test
    void passesForLowercaseValidStateCode() {
        assertTrue(edit.evaluate(withStateOfResidence("ca")).passed());
    }

    @Test
    void failsForInvalidStateCode() {
        var outcome = edit.evaluate(withStateOfResidence("XX"));

        assertFalse(outcome.passed());
        assertTrue(outcome.message().toLowerCase().contains("state"));
    }

    @Test
    void returnsActionableFailureMessage() {
        var outcome = edit.evaluate(withStateOfResidence("XX"));

        assertEquals("State code must be a valid US state abbreviation", outcome.message());
    }

    @Test
    void hasStableCodeAndSeverity() {
        assertTrue(edit.code().equals("STATE_CODE"));
        assertTrue(edit.name().equals("State Code"));
        assertTrue(edit.severity() == EditSeverity.ERROR);
    }
}
