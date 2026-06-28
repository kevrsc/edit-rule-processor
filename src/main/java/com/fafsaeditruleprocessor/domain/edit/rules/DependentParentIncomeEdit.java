package com.fafsaeditruleprocessor.domain.edit.rules;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.DependencyStatus;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import org.springframework.stereotype.Component;

@Component
public class DependentParentIncomeEdit implements EditRule {

    private static final String CODE = "DEPENDENT_PARENT_INCOME";
    private static final String NAME = "Dependent Parent Income";
    private static final String FAILURE_MESSAGE = "Parent income is required for dependent applicants";

    @Override
    public String code() {
        return CODE;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public EditSeverity severity() {
        return EditSeverity.ERROR;
    }

    @Override
    public boolean appliesTo(FafsaApplication application) {
        return application.dependencyStatus() == DependencyStatus.DEPENDENT;
    }

    @Override
    public EditOutcome evaluate(FafsaApplication application) {
        if (application.income().parentIncome() != null) {
            return EditOutcome.passed(CODE, NAME, severity());
        }
        return EditOutcome.failed(CODE, NAME, severity(), FAILURE_MESSAGE);
    }
}
