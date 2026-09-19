package com.trantanh.eet.v2;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;

@Service
public class EetSubmissionService {
    private static final Logger logger = LoggerFactory.getLogger(EetSubmissionService.class);
    private static final Duration LEGAL_RETRY_LIMIT = Duration.ofHours(48);

    private final EetSubmissionRepository repository;
    private final EetConfiguration configuration;
    private final EetGateway gateway;
    private final Executor executor;

    public EetSubmissionService(EetSubmissionRepository repository,
                                EetConfiguration configuration,
                                EetGateway gateway,
                                @Qualifier("eetExecutor") Executor executor) {
        this.repository = repository;
        this.configuration = configuration;
        this.gateway = gateway;
        this.executor = executor;
    }

    @PostConstruct
    void recoverInterruptedWork() {
        repository.recoverInterruptedSubmissions();
    }

    public Optional<Long> enqueue(int billId, String sequence, OffsetDateTime transactionTime, BigDecimal amount) {
        Optional<EetConfiguration.ConfiguredCashDesk> cashDesk = configuration.cashDesk();
        if (cashDesk.isEmpty()) {
            logger.error("Účtenku {} nelze zařadit do EET: chybí konfigurace nebo certifikát", billId);
            return Optional.empty();
        }
        EetConfiguration.ConfiguredCashDesk configured = cashDesk.get();
        long id = repository.enqueue(billId, configured.eic(), configured.unitId(), configured.cashDeskId(),
                sequence, transactionTime, amount);
        executor.execute(() -> send(id));
        return Optional.of(id);
    }

    @Scheduled(fixedDelayString = "${navisoft.eet.retry-interval:60s}")
    public void retryReady() {
        for (Long id : repository.findReadyIds(50)) {
            executor.execute(() -> send(id));
        }
    }

    void send(long id) {
        UUID messageUuid = UUID.randomUUID();
        if (!repository.claim(id, messageUuid)) {
            return;
        }
        EetSubmission submission = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("EET záznam " + id + " nebyl nalezen"));
        Optional<EetConfiguration.ConfiguredCashDesk> cashDesk = configuration.cashDesk();
        if (cashDesk.isEmpty()) {
            scheduleRetry(submission, null, "Chybí konfigurace EET nebo certifikát");
            return;
        }
        EetSale sale = new EetSale(
                submission.id(), submission.eicPopl(), submission.idJednotky(), submission.idPokl(),
                submission.poradCis(), submission.datTrzby(), submission.celkTrzba(), messageUuid,
                submission.attemptCount() == 1);
        try {
            EetGatewayResponse response = gateway.send(sale, cashDesk.get().credentials());
            if (response.accepted()) {
                repository.confirm(id, response);
                logger.info("Tržba účtenky {} byla potvrzena EET, POK={}", submission.billId(), response.pok());
            } else if (response.isTemporaryFailure()) {
                scheduleRetry(submission, response.errorCode(), response.errorMessage());
            } else {
                repository.reject(id, response.errorCode(), response.errorMessage());
                logger.error("EET odmítla účtenku {}: {} {}", submission.billId(),
                        response.errorCode(), response.errorMessage());
            }
        } catch (RuntimeException exception) {
            String message = rootMessage(exception);
            if (hasCause(exception, EetDataValidationException.class)) {
                repository.reject(id, null, message);
                logger.error("EET data účtenky {} jsou neplatná: {}", submission.billId(), message);
            } else {
                scheduleRetry(submission, null, message);
            }
        }
    }

    private void scheduleRetry(EetSubmission submission, Integer errorCode, String message) {
        long delaySeconds = Math.min(1800L, 1L << Math.min(submission.attemptCount(), 10));
        boolean overdue = Duration.between(submission.createdAt().toInstant(), Instant.now())
                .compareTo(LEGAL_RETRY_LIMIT) > 0;
        String storedMessage = overdue ? "PO LHŮTĚ 48 HODIN: " + message : message;
        repository.retry(submission.id(), errorCode, storedMessage, Instant.now().plusSeconds(delaySeconds));
        logger.warn("EET odeslání účtenky {} selhalo; další pokus za {} s: {}",
                submission.billId(), delaySeconds, message);
    }

    private String rootMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage() == null ? current.getClass().getSimpleName() : current.getMessage();
    }

    private boolean hasCause(Throwable throwable, Class<? extends Throwable> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
