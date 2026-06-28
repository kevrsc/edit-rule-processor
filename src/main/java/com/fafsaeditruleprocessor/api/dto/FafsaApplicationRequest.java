package com.fafsaeditruleprocessor.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FafsaApplicationRequest(
        @NotNull @Valid StudentInfoDto studentInfo,
        @NotBlank
                @Pattern(
                        regexp = "(?i)dependent|independent",
                        message = "must be dependent or independent")
                String dependencyStatus,
        @NotBlank
                @Pattern(regexp = "(?i)single|married", message = "must be single or married")
                String maritalStatus,
        SpouseInfoDto spouseInfo,
        @NotNull @Valid HouseholdDto household,
        @NotNull @Valid IncomeDto income,
        @NotBlank String stateOfResidence) {
}
