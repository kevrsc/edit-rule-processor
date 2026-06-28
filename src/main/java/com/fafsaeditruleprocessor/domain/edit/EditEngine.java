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
            outcomes.add(evaluateRule(rule, application));
        }
        return ValidationResult.fromEdits(outcomes);
    }

    private EditOutcome evaluateRule(EditRule rule, FafsaApplication application) {
        if (!rule.appliesTo(application)) {
            return EditOutcome.passed(rule.code(), rule.name(), rule.severity());
        }
        return rule.evaluate(application);
    }
}
