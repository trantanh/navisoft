package com.trantanh.eet.v2;

import com.trantanh.navipos.dao.impl.EetDaoImpl;
import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.model.EetConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

@Component
public class EetConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(EetConfiguration.class);

    private final String environmentOverride;
    private final String configuredCertificatePath;
    private final String configuredCertificatePassword;
    private final Duration connectTimeout;
    private final Duration responseTimeout;

    public EetConfiguration(
            @Value("${navisoft.eet.environment:playground}") String environment,
            @Value("${navisoft.eet.certificate-path:}") String certificatePath,
            @Value("${navisoft.eet.certificate-password:}") String certificatePassword,
            @Value("${navisoft.eet.connect-timeout:2s}") Duration connectTimeout,
            @Value("${navisoft.eet.response-timeout:2s}") Duration responseTimeout
    ) {
        this.environmentOverride = environment;
        this.configuredCertificatePath = certificatePath;
        this.configuredCertificatePassword = certificatePassword;
        this.connectTimeout = connectTimeout.compareTo(Duration.ofSeconds(2)) < 0
                ? Duration.ofSeconds(2) : connectTimeout;
        this.responseTimeout = responseTimeout.compareTo(Duration.ofSeconds(2)) < 0
                ? Duration.ofSeconds(2) : responseTimeout;
    }

    public EetEnvironment environment() {
        if (environmentOverride != null && !environmentOverride.isBlank()) {
            return EetEnvironment.parse(environmentOverride);
        }
        try {
            return "1".equals(new ConfigManager().getSpaceEet())
                    ? EetEnvironment.PRODUCTION : EetEnvironment.PLAYGROUND;
        } catch (RuntimeException exception) {
            logger.warn("Nelze načíst prostředí EET z nastavení, používá se Playground", exception);
            return EetEnvironment.PLAYGROUND;
        }
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }

    public Duration responseTimeout() {
        return responseTimeout;
    }

    public Optional<ConfiguredCashDesk> cashDesk() {
        try {
            EetConfigModel legacy = new EetDaoImpl().getEet();
            String path = configuredCertificatePath.isBlank() ? legacy.getPath() : configuredCertificatePath;
            String password = configuredCertificatePassword.isBlank() ? legacy.getPassword() : configuredCertificatePassword;
            if (configuredCertificatePassword.isBlank() && password != null && !password.isBlank()) {
                logger.warn("EET certificate password is read from the legacy database. Configure NAVISOFT_EET_CERTIFICATE_PASSWORD and remove the stored value.");
            }
            if (isBlank(path) || isBlank(password) || isBlank(legacy.getDic())
                    || isBlank(legacy.getProvoz()) || isBlank(legacy.getPokl())) {
                return Optional.empty();
            }
            int unit = Integer.parseInt(legacy.getProvoz());
            return Optional.of(new ConfiguredCashDesk(
                    legacy.getDic(), unit, legacy.getPokl(),
                    new EetCredentials(Path.of(path), password.toCharArray())));
        } catch (RuntimeException exception) {
            logger.error("EET configuration is incomplete", exception);
            return Optional.empty();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record ConfiguredCashDesk(String eic, int unitId, String cashDeskId, EetCredentials credentials) {
    }
}
