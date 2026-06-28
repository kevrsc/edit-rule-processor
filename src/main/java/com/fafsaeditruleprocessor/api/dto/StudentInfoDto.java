package com.fafsaeditruleprocessor.api.dto;

import java.time.LocalDate;

public record StudentInfoDto(String firstName, String lastName, String ssn, LocalDate dateOfBirth) {
}
