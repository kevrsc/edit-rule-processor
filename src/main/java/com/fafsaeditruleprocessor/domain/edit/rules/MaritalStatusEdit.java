package com.fafsaeditruleprocessor.domain.edit.rules;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import com.fafsaeditruleprocessor.domain.model.MaritalStatus;
import com.fafsaeditruleprocessor.domain.model.SpouseInfo;
import org.springframework.stereotype.Component;

@Component
public class MaritalStatusEdit implements EditRule {

    private static final String CODE = "MARITAL_STATUS";
    private static final String NAME = "Marital Status";
    private static final String FAILURE_MESSAGE =
            "Spouse information is required for married applicants";

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
        return application.maritalStatus() == MaritalStatus.MARRIED;
    }

    @Override
    public EditOutcome evaluate(FafsaApplication application) {
        if (hasCompleteSpouseInfo(application.spouseInfo())) {
            return EditOutcome.passed(CODE, NAME, severity());
        }
        return EditOutcome.failed(CODE, NAME, severity(), FAILURE_MESSAGE);
    }

    private static boolean hasCompleteSpouseInfo(SpouseInfo spouseInfo) {
        if (spouseInfo == null) {
            return false;
        }
        return isPresent(spouseInfo.firstName())
                && isPresent(spouseInfo.lastName())
                && isPresent(spouseInfo.ssn());
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }
}
