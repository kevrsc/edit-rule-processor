package com.fafsaeditruleprocessor.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record StudentInfoDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String ssn,
        @NotNull LocalDate dateOfBirth) {
}
