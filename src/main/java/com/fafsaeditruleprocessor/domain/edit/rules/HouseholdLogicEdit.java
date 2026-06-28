package com.fafsaeditruleprocessor.domain.edit.rules;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import org.springframework.stereotype.Component;

@Component
public class HouseholdLogicEdit implements EditRule {

    private static final String CODE = "HOUSEHOLD_LOGIC";
    private static final String NAME = "Household Logic";
    private static final String FAILURE_MESSAGE =
            "Number in college cannot exceed number in household";

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
        return true;
    }

    @Override
    public EditOutcome evaluate(FafsaApplication application) {
        int inHousehold = application.household().numberInHousehold();
        int inCollege = application.household().numberInCollege();
        if (inCollege <= inHousehold) {
            return EditOutcome.passed(CODE, NAME, severity());
        }
        return EditOutcome.failed(CODE, NAME, severity(), FAILURE_MESSAGE);
    }
}
