package com.fafsaeditruleprocessor.domain.edit.rules;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import org.springframework.stereotype.Component;

@Component
public class SsnFormatEdit implements EditRule {

    private static final String CODE = "SSN_FORMAT";
    private static final String NAME = "SSN Format";
    private static final String FAILURE_MESSAGE = "SSN must be exactly 9 digits";

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
        String ssn = application.studentInfo().ssn();
        if (ssn != null && ssn.matches("\\d{9}")) {
            return EditOutcome.passed(CODE, NAME, severity());
        }
        return EditOutcome.failed(CODE, NAME, severity(), FAILURE_MESSAGE);
    }
}
