package com.fafsaeditruleprocessor.domain.model;

import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.edit.EditSeverity;
import java.util.List;

public record ValidationResult(OverallStatus overallStatus, List<EditOutcome> edits) {

    public static ValidationResult fromEdits(List<EditOutcome> edits) {
        boolean invalid = edits.stream()
                .anyMatch(edit -> !edit.passed() && edit.severity() == EditSeverity.ERROR);
        OverallStatus status = invalid ? OverallStatus.INVALID : OverallStatus.VALID;
        return new ValidationResult(status, List.copyOf(edits));
    }
}
