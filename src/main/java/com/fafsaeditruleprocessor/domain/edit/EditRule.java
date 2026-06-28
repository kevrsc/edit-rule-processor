package com.fafsaeditruleprocessor.domain.edit;

import com.fafsaeditruleprocessor.domain.model.FafsaApplication;

public interface EditRule {

    String code();

    String name();

    EditSeverity severity();

    boolean appliesTo(FafsaApplication application);

    EditOutcome evaluate(FafsaApplication application);
}
