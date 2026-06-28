package com.fafsaeditruleprocessor.api.dto;

import java.util.List;

public record ValidationResponse(String overallStatus, List<EditOutcomeDto> edits) {
}
