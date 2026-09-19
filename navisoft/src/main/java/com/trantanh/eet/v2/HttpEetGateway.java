package com.trantanh.eet.v2;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class HttpEetGateway implements EetGateway {
    private static final String SOAP_ACTION = "http://fs.gov.cz/eet/OdeslaniTrzby";

    private final EetConfiguration configuration;
    private final Eet2RequestSigner signer = new Eet2RequestSigner();
    private final Eet2ResponseParser responseParser = new Eet2ResponseParser();
    private final HttpClient httpClient;

    public HttpEetGateway(EetConfiguration configuration) {
        this.configuration = configuration;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(configuration.connectTimeout())
                .build();
    }

    @Override
    public EetGatewayResponse send(EetSale sale, EetCredentials credentials) {
        try {
            String requestXml = signer.sign(sale, credentials);
            HttpRequest request = HttpRequest.newBuilder(URI.create(configuration.environment().endpoint()))
                    .timeout(configuration.responseTimeout())
                    .header("Content-Type", "text/xml; charset=UTF-8")
                    .header("SOAPAction", SOAP_ACTION)
                    .POST(HttpRequest.BodyPublishers.ofString(requestXml, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("EET vrátila HTTP " + response.statusCode());
            }
            return responseParser.parse(response.body());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Odesílání EET bylo přerušeno", exception);
        } catch (Exception exception) {
            throw new IllegalStateException("Komunikace s EET 2.0 selhala", exception);
        }
    }
}
