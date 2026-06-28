package com.fafsaeditruleprocessor.domain.model;

import java.time.LocalDate;

public record StudentInfo(
        String firstName,
        String lastName,
        String ssn,
        LocalDate dateOfBirth) {
}
