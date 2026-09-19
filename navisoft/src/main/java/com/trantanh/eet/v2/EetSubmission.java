package com.trantanh.eet.v2;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EetSubmission(
        long id,
        int billId,
        String eicPopl,
        int idJednotky,
        String idPokl,
        String poradCis,
        OffsetDateTime datTrzby,
        BigDecimal celkTrzba,
        EetSubmissionStatus status,
        int attemptCount,
        OffsetDateTime createdAt
) {
}
