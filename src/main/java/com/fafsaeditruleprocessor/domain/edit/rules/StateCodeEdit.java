package com.fafsaeditruleprocessor.domain.edit.rules;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.edit.UsStateCodes;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import org.springframework.stereotype.Component;

@Component
public class StateCodeEdit implements EditRule {

    private static final String CODE = "STATE_CODE";
    private static final String NAME = "State Code";
    private static final String FAILURE_MESSAGE =
            "State code must be a valid US state abbreviation";

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
        if (UsStateCodes.isValid(application.stateOfResidence())) {
            return EditOutcome.passed(CODE, NAME, severity());
        }
        return EditOutcome.failed(CODE, NAME, severity(), FAILURE_MESSAGE);
    }
}
