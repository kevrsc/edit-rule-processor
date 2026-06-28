package com.fafsaeditruleprocessor.domain.edit.rules;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.marriedWithSpouseInfo;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.marriedWithoutSpouseInfo;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaritalStatusEditTest {

    private MaritalStatusEdit edit;

    @BeforeEach
    void setUp() {
        edit = new MaritalStatusEdit();
    }

    @Test
    void appliesOnlyToMarriedApplicants() {
        assertFalse(edit.appliesTo(validDependentApplication()));
        assertTrue(edit.appliesTo(marriedWithoutSpouseInfo()));
    }

    @Test
    void passesWhenSpouseInfoComplete() {
        assertTrue(edit.evaluate(marriedWithSpouseInfo()).passed());
    }

    @Test
    void failsWhenSpouseInfoMissingForMarriedApplicant() {
        var outcome = edit.evaluate(marriedWithoutSpouseInfo());

        assertFalse(outcome.passed());
        assertTrue(outcome.message().toLowerCase().contains("spouse"));
    }

    @Test
    void hasStableCodeAndSeverity() {
        assertTrue(edit.code().equals("MARITAL_STATUS"));
        assertTrue(edit.name().equals("Marital Status"));
        assertTrue(edit.severity() == EditSeverity.ERROR);
    }
}
