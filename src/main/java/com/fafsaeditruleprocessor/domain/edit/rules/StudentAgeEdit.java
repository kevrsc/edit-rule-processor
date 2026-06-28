package com.fafsaeditruleprocessor.domain.edit.rules;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;

@Component
public class StudentAgeEdit implements EditRule {

    private static final String CODE = "STUDENT_AGE";
    private static final String NAME = "Student Age";
    private static final String FAILURE_MESSAGE = "Student must be at least 14 years old";

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
        int age = Period.between(application.studentInfo().dateOfBirth(), LocalDate.now())
                .getYears();
        if (age >= 14) {
            return EditOutcome.passed(CODE, NAME, severity());
        }
        return EditOutcome.failed(CODE, NAME, severity(), FAILURE_MESSAGE);
    }
}
