package com.fafsaeditruleprocessor.application;

import com.fafsaeditruleprocessor.domain.edit.EditEngine;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import com.fafsaeditruleprocessor.domain.model.ValidationResult;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final EditEngine editEngine;

    public ValidationService(EditEngine editEngine) {
        this.editEngine = editEngine;
    }

    public ValidationResult validate(FafsaApplication application) {
        return editEngine.validate(application);
    }
}
