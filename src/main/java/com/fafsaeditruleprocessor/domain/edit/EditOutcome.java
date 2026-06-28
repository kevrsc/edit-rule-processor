package com.fafsaeditruleprocessor.domain.edit;

import org.jspecify.annotations.Nullable;

public record EditOutcome(
        String id,
        String name,
        boolean passed,
        EditSeverity severity,
        @Nullable String message) {

    public static EditOutcome passed(String id, String name, EditSeverity severity) {
        return new EditOutcome(id, name, true, severity, null);
    }

    public static EditOutcome failed(String id, String name, EditSeverity severity, String message) {
        return new EditOutcome(id, name, false, severity, message);
    }
}
