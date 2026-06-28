package com.fafsaeditruleprocessor.domain.edit.rules;

import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.validDependentApplication;
import static com.fafsaeditruleprocessor.support.FafsaApplicationTestFixtures.withDateOfBirth;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentAgeEditTest {

    private StudentAgeEdit edit;

    @BeforeEach
    void setUp() {
        edit = new StudentAgeEdit();
    }

    @Test
    void appliesToAllApplications() {
        assertTrue(edit.appliesTo(validDependentApplication()));
    }

    @Test
    void passesWhenStudentIsAtLeast14() {
        FafsaApplication application = withDateOfBirth(LocalDate.now().minusYears(20));

        assertTrue(edit.evaluate(application).passed());
    }

    @Test
    void passesOnMinimumAgeBoundary() {
        FafsaApplication application = withDateOfBirth(LocalDate.now().minusYears(14));

        assertTrue(edit.evaluate(application).passed());
    }

    @Test
    void failsWhenStudentUnder14() {
        FafsaApplication application = withDateOfBirth(LocalDate.now().minusYears(13));

        var outcome = edit.evaluate(application);

        assertFalse(outcome.passed());
        assertTrue(outcome.message().contains("14"));
    }

    @Test
    void hasStableCodeAndSeverity() {
        assertTrue(edit.code().equals("STUDENT_AGE"));
        assertTrue(edit.name().equals("Student Age"));
        assertTrue(edit.severity() == EditSeverity.ERROR);
    }
}
