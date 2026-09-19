package com.trantanh.eet.v2;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Eet2RequestSignerTest {
    private final Eet2RequestSigner signer = new Eet2RequestSigner();

    @Test
    void validatesDataAcceptedByVersionFourSchema() {
        assertDoesNotThrow(() -> signer.validate(sale("CZ00000019", "POKLADNA-1", new BigDecimal("123.45"))));
    }

    @Test
    void rejectsLegacyOrMalformedIdentifiers() {
        assertThrows(IllegalArgumentException.class,
                () -> signer.validate(sale("00000019", "POKLADNA-1", new BigDecimal("123.45"))));
        assertThrows(IllegalArgumentException.class,
                () -> signer.validate(sale("CZ00000019", "pokladna s nepovoleným ě", new BigDecimal("123.45"))));
    }

    @Test
    void rejectsZeroAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> signer.validate(sale("CZ00000019", "POKLADNA-1", BigDecimal.ZERO)));
    }

    private EetSale sale(String eic, String cashDesk, BigDecimal amount) {
        return new EetSale(1, eic, 1, cashDesk, "42", OffsetDateTime.now(), amount,
                UUID.randomUUID(), true);
    }
}
