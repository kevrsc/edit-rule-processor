package com.fafsaeditruleprocessor.domain.edit;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.independentSingleApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.invalidMultiViolationApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.rules.DependentParentIncomeEdit;
import com.fafsaeditruleprocessor.domain.edit.rules.HouseholdLogicEdit;
import com.fafsaeditruleprocessor.domain.edit.rules.IncomeValidationEdit;
import com.fafsaeditruleprocessor.domain.edit.rules.MaritalStatusEdit;
import com.fafsaeditruleprocessor.domain.edit.rules.SsnFormatEdit;
import com.fafsaeditruleprocessor.domain.edit.rules.StateCodeEdit;
import com.fafsaeditruleprocessor.domain.edit.rules.StudentAgeEdit;
import com.fafsaeditruleprocessor.domain.model.OverallStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EditEngineTest {

    private EditEngine editEngine;

    @BeforeEach
    void setUp() {
        editEngine = new EditEngine(List.of(
                new StudentAgeEdit(),
                new SsnFormatEdit(),
                new DependentParentIncomeEdit(),
                new IncomeValidationEdit(),
                new HouseholdLogicEdit(),
                new StateCodeEdit(),
                new MaritalStatusEdit()));
    }

    @Test
    void collectsAllFailuresWithoutStoppingAtFirst() {
        var result = editEngine.validate(invalidMultiViolationApplication());

        assertEquals(OverallStatus.INVALID, result.overallStatus());
        assertEquals(7, result.edits().size());
        assertEquals(7, result.edits().stream().filter(edit -> !edit.passed()).count());
    }

    @Test
    void recordsPassedOutcomeForNonApplicableRules() {
        var result = editEngine.validate(independentSingleApplication());

        assertEquals(OverallStatus.VALID, result.overallStatus());
        assertEquals(7, result.edits().size());
        assertTrue(result.edits().stream().allMatch(EditOutcome::passed));
        assertTrue(result.edits().stream()
                .filter(edit -> edit.id().equals("DEPENDENT_PARENT_INCOME")
                        || edit.id().equals("MARITAL_STATUS"))
                .allMatch(EditOutcome::passed));
    }

    @Test
    void includesEveryRegisteredRuleInResult() {
        var result = editEngine.validate(validDependentApplication());

        assertEquals(7, result.edits().size());
        assertTrue(result.edits().stream().allMatch(EditOutcome::passed));
        assertFalse(result.edits().stream().anyMatch(edit -> edit.message() != null));
    }
}
