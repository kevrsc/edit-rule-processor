package com.fafsaeditruleprocessor.domain.edit.rules;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class IncomeValidationEdit implements EditRule {

    private static final String CODE = "INCOME_VALIDATION";
    private static final String NAME = "Income Validation";
    private static final String FAILURE_MESSAGE = "Income values cannot be negative";

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
        if (isNegative(application.income().studentIncome())
                || isNegative(application.income().parentIncome())) {
            return EditOutcome.failed(CODE, NAME, severity(), FAILURE_MESSAGE);
        }
        return EditOutcome.passed(CODE, NAME, severity());
    }

    private static boolean isNegative(BigDecimal amount) {
        return amount != null && amount.signum() < 0;
    }
}
