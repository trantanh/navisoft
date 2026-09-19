package com.trantanh.navipos.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseBackupService {

    private static final String JDBC_PREFIX = "jdbc:";

    private final Environment environment;

    public DatabaseBackupService(Environment environment) {
        this.environment = environment;
    }

    public void export(Path outputFile, Path mysqldumpExecutable) {
        validateFilePath(outputFile, "Cesta k záloze databáze není nastavena");
        validateFilePath(mysqldumpExecutable, "Cesta k programu mysqldump není nastavena");

        String jdbcUrl = environment.getRequiredProperty("spring.datasource.url");
        URI databaseUri = parseMysqlUri(jdbcUrl);
        String databaseName = databaseUri.getPath().replaceFirst("^/", "");
        if (databaseName.isBlank()) {
            throw new IllegalStateException("V databázové URL chybí název databáze");
        }

        try {
            Path parent = outputFile.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            List<String> command = new ArrayList<>();
            command.add(mysqldumpExecutable.toString());
            command.add("--host=" + defaultValue(databaseUri.getHost(), "localhost"));
            command.add("--port=" + (databaseUri.getPort() > 0 ? databaseUri.getPort() : 3306));
            command.add("--user=" + environment.getRequiredProperty("spring.datasource.username"));
            command.add("--result-file=" + outputFile.toAbsolutePath());
            command.add("--single-transaction");
            command.add("--routines");
            command.add("--triggers");
            command.add(databaseName);

            ProcessBuilder processBuilder = new ProcessBuilder(command)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT);
            String password = environment.getProperty("spring.datasource.password", "");
            if (!password.isBlank()) {
                processBuilder.environment().put("MYSQL_PWD", password);
            }

            int exitCode = processBuilder.start().waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("Záloha databáze skončila s kódem " + exitCode);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Zálohu databáze se nepodařilo spustit", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Záloha databáze byla přerušena", exception);
        }
    }

    private static URI parseMysqlUri(String jdbcUrl) {
        if (!jdbcUrl.startsWith(JDBC_PREFIX)) {
            throw new IllegalStateException("Neplatná databázová URL");
        }
        URI uri = URI.create(jdbcUrl.substring(JDBC_PREFIX.length()));
        if (!"mysql".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalStateException("Databázový export podporuje pouze MySQL");
        }
        return uri;
    }

    private static void validateFilePath(Path path, String message) {
        if (path.toString().isBlank()) {
            throw new IllegalStateException(message);
        }
    }

    private static String defaultValue(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
