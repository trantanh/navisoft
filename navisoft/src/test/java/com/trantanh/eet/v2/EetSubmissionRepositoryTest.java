package com.trantanh.eet.v2;

import com.trantanh.navipos.CashDeskApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = CashDeskApp.class,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:navisoft-eet-test;MODE=MySQL;DB_CLOSE_DELAY=-1",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.flyway.enabled=true"
        }
)
@Transactional
class EetSubmissionRepositoryTest {
    @Autowired
    private EetSubmissionRepository repository;

    @Test
    void persistsClaimsAndConfirmsSubmission() {
        long id = repository.enqueue(987654, "CZ00000019", 1, "POKLADNA-1", "42",
                OffsetDateTime.parse("2027-01-10T10:15:30+01:00"), new BigDecimal("123.45"));

        assertEquals(List.of(id), repository.findReadyIds(10));
        assertTrue(repository.claim(id, UUID.randomUUID()));
        EetSubmission sending = repository.findById(id).orElseThrow();
        assertEquals(EetSubmissionStatus.SENDING, sending.status());
        assertEquals(1, sending.attemptCount());

        repository.confirm(id, new EetGatewayResponse(true,
                "12345678-1234-4123-8123-123456789012-aa", null, null,
                OffsetDateTime.now(), true, List.of()));

        assertTrue(repository.findReadyIds(10).isEmpty());
    }
}
