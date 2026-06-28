package com.fafsaeditruleprocessor.domain.edit;

import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import com.fafsaeditruleprocessor.domain.model.ValidationResult;
import java.util.ArrayList;
import java.util.List;

public class EditEngine {

    private final List<EditRule> rules;

    public EditEngine(List<EditRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public ValidationResult validate(FafsaApplication application) {
        List<EditOutcome> outcomes = new ArrayList<>(rules.size());
        for (EditRule rule : rules) {
            if (rule.appliesTo(application)) {
                outcomes.add(rule.evaluate(application));
            } else {
                outcomes.add(EditOutcome.passed(rule.code(), rule.name(), rule.severity()));
            }
        }
        return ValidationResult.fromEdits(outcomes);
    }
}
