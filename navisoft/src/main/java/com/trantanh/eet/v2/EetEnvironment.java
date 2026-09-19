package com.trantanh.eet.v2;

public enum EetEnvironment {
    PLAYGROUND("https://pg.trzbyeet.gov.cz/eet/services/EETServiceSOAP/v4"),
    PRODUCTION("https://trzbyeet.gov.cz/eet/services/EETServiceSOAP/v4");

    private final String endpoint;

    EetEnvironment(String endpoint) {
        this.endpoint = endpoint;
    }

    public String endpoint() {
        return endpoint;
    }

    public static EetEnvironment parse(String value) {
        return value == null || value.isBlank() ? PLAYGROUND : valueOf(value.trim().toUpperCase());
    }
}
