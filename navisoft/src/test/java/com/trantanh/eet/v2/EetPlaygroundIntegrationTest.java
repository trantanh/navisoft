package com.trantanh.eet.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfEnvironmentVariable(named = "EET_PLAYGROUND_CERTIFICATE", matches = ".+")
class EetPlaygroundIntegrationTest {
    @Test
    void sendsSignedVersionFourMessageAndValidatesSignedConfirmation() {
        EetConfiguration configuration = new EetConfiguration(
                "playground", "", "", Duration.ofSeconds(15), Duration.ofSeconds(15));
        HttpEetGateway gateway = new HttpEetGateway(configuration);
        UUID messageUuid = UUID.randomUUID();
        String sequence = "TEST-" + messageUuid.toString().substring(0, 8);
        EetSale sale = new EetSale(0, "CZ00000019", 1, "NAVISOFT-TEST", sequence,
                OffsetDateTime.now(ZoneId.of("Europe/Prague")).withNano(0),
                new BigDecimal("1.00"), messageUuid, true);
        EetCredentials credentials = new EetCredentials(
                Path.of(System.getenv("EET_PLAYGROUND_CERTIFICATE")),
                System.getenv("EET_PLAYGROUND_PASSWORD").toCharArray());
        String signedRequest = new Eet2RequestSigner().sign(sale, credentials);
        new Eet2ResponseParser().verifySignature(signedRequest);

        EetGatewayResponse response = gateway.send(sale, credentials);

        assertTrue(response.accepted(), () -> response.errorCode() + ": " + response.errorMessage());
        assertTrue(response.test());
        assertNotNull(response.pok());
    }
}
