package com.fafsaeditruleprocessor.domain.edit.rules;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.withHousehold;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HouseholdLogicEditTest {

    private HouseholdLogicEdit edit;

    @BeforeEach
    void setUp() {
        edit = new HouseholdLogicEdit();
    }

    @Test
    void appliesToAllApplications() {
        assertTrue(edit.appliesTo(validDependentApplication()));
    }

    @Test
    void passesWhenCollegeCountWithinHousehold() {
        assertTrue(edit.evaluate(validDependentApplication()).passed());
    }

    @Test
    void passesWhenCollegeCountEqualsHousehold() {
        assertTrue(edit.evaluate(withHousehold(3, 3)).passed());
    }

    @Test
    void failsWhenCollegeCountExceedsHousehold() {
        var outcome = edit.evaluate(withHousehold(2, 5));

        assertFalse(outcome.passed());
        assertTrue(outcome.message().toLowerCase().contains("college")
                || outcome.message().toLowerCase().contains("household"));
    }

    @Test
    void hasStableCodeAndSeverity() {
        assertTrue(edit.code().equals("HOUSEHOLD_LOGIC"));
        assertTrue(edit.name().equals("Household Logic"));
        assertTrue(edit.severity() == EditSeverity.ERROR);
    }
}
