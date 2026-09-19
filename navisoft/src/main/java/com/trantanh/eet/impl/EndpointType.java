package com.trantanh.eet.impl;

public enum EndpointType {

    PRODUCTION("https://prod.eet.cz:443/eet/services/EETServiceSOAP/v3"),
    PLAYGROUND("https://pg.eet.cz:443/eet/services/EETServiceSOAP/v3");
    public String url;

    EndpointType(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}
