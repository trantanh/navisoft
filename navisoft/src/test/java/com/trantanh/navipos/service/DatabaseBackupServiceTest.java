package com.trantanh.navipos.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseBackupServiceTest {

    @Test
    void rejectsNonMysqlDatabase() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("spring.datasource.url", "jdbc:h2:mem:test")
                .withProperty("spring.datasource.username", "test");
        DatabaseBackupService service = new DatabaseBackupService(environment);

        assertThrows(
                IllegalStateException.class,
                () -> service.export(Path.of("backup.sql"), Path.of("mysqldump"))
        );
    }
}
