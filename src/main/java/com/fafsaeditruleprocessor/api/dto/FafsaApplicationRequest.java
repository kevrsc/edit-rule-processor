package com.fafsaeditruleprocessor.api.dto;

public record FafsaApplicationRequest(
        StudentInfoDto studentInfo,
        String dependencyStatus,
        String maritalStatus,
        SpouseInfoDto spouseInfo,
        HouseholdDto household,
        IncomeDto income,
        String stateOfResidence) {
}
