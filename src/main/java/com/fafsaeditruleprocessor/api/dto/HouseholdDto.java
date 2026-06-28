package com.fafsaeditruleprocessor.api.dto;

import jakarta.validation.constraints.Min;

public record HouseholdDto(
        @Min(value = 1, message = "must be at least 1") int numberInHousehold,
        @Min(value = 0, message = "must be at least 0") int numberInCollege) {
}
