package com.trantanh.eet.v2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public record EetSale(
        long submissionId,
        String eicPopl,
        int idJednotky,
        String idPokl,
        String poradCis,
        OffsetDateTime datTrzby,
        BigDecimal celkTrzba,
        UUID messageUuid,
        boolean firstSend
) {
    public EetSale {
        Objects.requireNonNull(eicPopl, "eicPopl");
        Objects.requireNonNull(idPokl, "idPokl");
        Objects.requireNonNull(poradCis, "poradCis");
        Objects.requireNonNull(datTrzby, "datTrzby");
        Objects.requireNonNull(celkTrzba, "celkTrzba");
        Objects.requireNonNull(messageUuid, "messageUuid");
        celkTrzba = celkTrzba.setScale(2, RoundingMode.HALF_UP);
    }
}
