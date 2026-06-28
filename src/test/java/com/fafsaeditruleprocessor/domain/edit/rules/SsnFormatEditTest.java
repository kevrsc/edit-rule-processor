package com.fafsaeditruleprocessor.domain.edit.rules;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.withSsn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SsnFormatEditTest {

    private SsnFormatEdit edit;

    @BeforeEach
    void setUp() {
        edit = new SsnFormatEdit();
    }

    @Test
    void appliesToAllApplications() {
        assertTrue(edit.appliesTo(validDependentApplication()));
    }

    @Test
    void passesForNineDigitSsn() {
        assertTrue(edit.evaluate(validDependentApplication()).passed());
    }

    @Test
    void failsForNonNumericSsn() {
        var outcome = edit.evaluate(withSsn("invalid"));

        assertFalse(outcome.passed());
        assertTrue(outcome.message().toLowerCase().contains("ssn")
                || outcome.message().contains("9"));
    }

    @Test
    void failsForWrongLengthSsn() {
        assertFalse(edit.evaluate(withSsn("12345")).passed());
        assertFalse(edit.evaluate(withSsn("1234567890")).passed());
    }

    @Test
    void returnsActionableFailureMessage() {
        var outcome = edit.evaluate(withSsn("invalid"));

        assertEquals("SSN must be exactly 9 digits", outcome.message());
    }

    @Test
    void hasStableCodeAndSeverity() {
        assertTrue(edit.code().equals("SSN_FORMAT"));
        assertTrue(edit.name().equals("SSN Format"));
        assertTrue(edit.severity() == EditSeverity.ERROR);
    }
}
