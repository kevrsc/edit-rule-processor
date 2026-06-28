package com.fafsaeditruleprocessor.domain.edit.rules;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.independentSingleApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.withIncome;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.DependencyStatus;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DependentParentIncomeEditTest {

    private DependentParentIncomeEdit edit;

    @BeforeEach
    void setUp() {
        edit = new DependentParentIncomeEdit();
    }

    @Test
    void appliesOnlyToDependentApplicants() {
        assertTrue(edit.appliesTo(validDependentApplication()));
        assertFalse(edit.appliesTo(independentSingleApplication()));
    }

    @Test
    void passesWhenParentIncomePresent() {
        assertTrue(edit.evaluate(validDependentApplication()).passed());
    }

    @Test
    void passesWhenParentIncomeIsZero() {
        FafsaApplication application =
                withIncome(BigDecimal.valueOf(5000), BigDecimal.ZERO);
        application = new FafsaApplication(
                application.studentInfo(),
                DependencyStatus.DEPENDENT,
                application.maritalStatus(),
                application.spouseInfo(),
                application.household(),
                application.income(),
                application.stateOfResidence());

        assertTrue(edit.evaluate(application).passed());
    }

    @Test
    void failsWhenParentIncomeMissingForDependent() {
        FafsaApplication application =
                withIncome(BigDecimal.valueOf(5000), null);
        application = new FafsaApplication(
                application.studentInfo(),
                DependencyStatus.DEPENDENT,
                application.maritalStatus(),
                application.spouseInfo(),
                application.household(),
                application.income(),
                application.stateOfResidence());

        var outcome = edit.evaluate(application);

        assertFalse(outcome.passed());
        assertTrue(outcome.message().toLowerCase().contains("parent"));
    }

    @Test
    void hasStableCodeAndSeverity() {
        assertTrue(edit.code().equals("DEPENDENT_PARENT_INCOME"));
        assertTrue(edit.name().equals("Dependent Parent Income"));
        assertTrue(edit.severity() == EditSeverity.ERROR);
    }
}
