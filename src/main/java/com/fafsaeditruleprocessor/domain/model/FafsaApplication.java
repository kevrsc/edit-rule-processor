package com.fafsaeditruleprocessor.domain.model;

import org.jspecify.annotations.Nullable;

public record FafsaApplication(
        StudentInfo studentInfo,
        DependencyStatus dependencyStatus,
        MaritalStatus maritalStatus,
        @Nullable SpouseInfo spouseInfo,
        Household household,
        Income income,
        String stateOfResidence) {
}
