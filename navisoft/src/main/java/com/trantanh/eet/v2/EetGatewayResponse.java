package com.trantanh.eet.v2;

import java.time.OffsetDateTime;
import java.util.List;

public record EetGatewayResponse(
        boolean accepted,
        String pok,
        Integer errorCode,
        String errorMessage,
        OffsetDateTime responseTime,
        boolean test,
        List<String> warnings
) {
    public EetGatewayResponse {
        warnings = warnings == null ? List.of() : List.copyOf(warnings);
    }

    public boolean isTemporaryFailure() {
        return !accepted && (errorCode == null || errorCode == -1 || errorCode == 8);
    }
}
