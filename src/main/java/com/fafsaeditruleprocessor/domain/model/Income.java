package com.fafsaeditruleprocessor.domain.model;

import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

public record Income(
        @Nullable BigDecimal studentIncome,
        @Nullable BigDecimal parentIncome) {
}
