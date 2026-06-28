package com.fafsaeditruleprocessor.domain.edit;

import java.util.Locale;
import java.util.Set;

public final class UsStateCodes {

    private static final Set<String> VALID_CODES = Set.of(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
            "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY",
            "DC");

    private UsStateCodes() {
    }

    public static boolean isValid(String stateCode) {
        if (stateCode == null || stateCode.isBlank()) {
            return false;
        }
        return VALID_CODES.contains(stateCode.trim().toUpperCase(Locale.US));
    }

    public static String normalize(String stateCode) {
        return stateCode.trim().toUpperCase(Locale.US);
    }
}
