package com.trantanh.eet.v2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Eet2ResponseParserTest {
    private final Eet2ResponseParser parser = new Eet2ResponseParser();

    @Test
    void parsesTemporaryErrorWithoutRequiringAResponseSignature() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:eet="http://fs.gov.cz/eet/schema/v4">
                  <soap:Body>
                    <eet:Odpoved>
                      <eet:Hlavicka dat_odmit="2027-03-04T18:25:21+01:00"/>
                      <eet:Chyba kod="-1">Docasna technicka chyba zpracovani</eet:Chyba>
                    </eet:Odpoved>
                  </soap:Body>
                </soap:Envelope>
                """;

        EetGatewayResponse response = parser.parse(xml);

        assertFalse(response.accepted());
        assertEquals(-1, response.errorCode());
        assertTrue(response.isTemporaryFailure());
        assertEquals("Docasna technicka chyba zpracovani", response.errorMessage());
    }
}
