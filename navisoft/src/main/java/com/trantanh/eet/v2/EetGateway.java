package com.trantanh.eet.v2;

public interface EetGateway {
    EetGatewayResponse send(EetSale sale, EetCredentials credentials);
}
