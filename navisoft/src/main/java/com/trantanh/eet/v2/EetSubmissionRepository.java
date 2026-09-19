package com.trantanh.eet.v2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EetSubmissionRepository {
    private final JdbcTemplate jdbcTemplate;

    public EetSubmissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long enqueue(int billId, String eic, int unitId, String cashDeskId, String sequence,
                        OffsetDateTime transactionTime, BigDecimal amount) {
        Optional<Long> existing = findIdByBillId(billId);
        if (existing.isPresent()) {
            return existing.get();
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO eet_submission
                        (bill_id, eic_popl, id_jednotky, id_pokl, porad_cis, dat_trzby,
                         celk_trzba, status, next_attempt_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING', CURRENT_TIMESTAMP)
                    """, new String[]{"id"});
            statement.setInt(1, billId);
            statement.setString(2, eic);
            statement.setInt(3, unitId);
            statement.setString(4, cashDeskId);
            statement.setString(5, sequence);
            statement.setString(6, transactionTime.withNano(0).toString());
            statement.setBigDecimal(7, amount.setScale(2, RoundingMode.HALF_UP));
            return statement;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Databáze nevrátila ID EET záznamu");
        }
        return key.longValue();
    }

    public List<Long> findReadyIds(int limit) {
        return jdbcTemplate.queryForList("""
                SELECT id FROM eet_submission
                WHERE status IN ('PENDING', 'RETRY')
                  AND (next_attempt_at IS NULL OR next_attempt_at <= CURRENT_TIMESTAMP)
                ORDER BY created_at, id
                LIMIT ?
                """, Long.class, limit);
    }

    public boolean claim(long id, UUID messageUuid) {
        return jdbcTemplate.update("""
                UPDATE eet_submission
                   SET status = 'SENDING', message_uuid = ?, attempt_count = attempt_count + 1,
                       last_sent_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP
                 WHERE id = ? AND status IN ('PENDING', 'RETRY')
                """, messageUuid.toString(), id) == 1;
    }

    public Optional<EetSubmission> findById(long id) {
        return jdbcTemplate.query("""
                SELECT id, bill_id, eic_popl, id_jednotky, id_pokl, porad_cis, dat_trzby,
                       celk_trzba, status, attempt_count, created_at
                  FROM eet_submission WHERE id = ?
                """, (resultSet, row) -> new EetSubmission(
                resultSet.getLong("id"),
                resultSet.getInt("bill_id"),
                resultSet.getString("eic_popl"),
                resultSet.getInt("id_jednotky"),
                resultSet.getString("id_pokl"),
                resultSet.getString("porad_cis"),
                OffsetDateTime.parse(resultSet.getString("dat_trzby")),
                resultSet.getBigDecimal("celk_trzba"),
                EetSubmissionStatus.valueOf(resultSet.getString("status")),
                resultSet.getInt("attempt_count"),
                resultSet.getTimestamp("created_at").toInstant().atOffset(java.time.ZoneOffset.UTC)
        ), id).stream().findFirst();
    }

    public void confirm(long id, EetGatewayResponse response) {
        jdbcTemplate.update("""
                UPDATE eet_submission
                   SET status = 'CONFIRMED', pok = ?, error_code = NULL, error_message = ?,
                       confirmed_at = CURRENT_TIMESTAMP, next_attempt_at = NULL,
                       updated_at = CURRENT_TIMESTAMP
                 WHERE id = ? AND status = 'SENDING'
                """, response.pok(), String.join(" | ", response.warnings()), id);
    }

    public void retry(long id, Integer errorCode, String message, Instant nextAttempt) {
        jdbcTemplate.update("""
                UPDATE eet_submission
                   SET status = 'RETRY', error_code = ?, error_message = ?, next_attempt_at = ?,
                       updated_at = CURRENT_TIMESTAMP
                 WHERE id = ? AND status = 'SENDING'
                """, errorCode, truncate(message), Timestamp.from(nextAttempt), id);
    }

    public void reject(long id, Integer errorCode, String message) {
        jdbcTemplate.update("""
                UPDATE eet_submission
                   SET status = 'REJECTED', error_code = ?, error_message = ?, next_attempt_at = NULL,
                       updated_at = CURRENT_TIMESTAMP
                 WHERE id = ? AND status = 'SENDING'
                """, errorCode, truncate(message), id);
    }

    public void recoverInterruptedSubmissions() {
        jdbcTemplate.update("""
                UPDATE eet_submission
                   SET status = 'RETRY', next_attempt_at = CURRENT_TIMESTAMP,
                       error_message = 'Odesílání bylo přerušeno ukončením aplikace',
                       updated_at = CURRENT_TIMESTAMP
                 WHERE status = 'SENDING'
                """);
    }

    private Optional<Long> findIdByBillId(int billId) {
        return jdbcTemplate.query("SELECT id FROM eet_submission WHERE bill_id = ?",
                (resultSet, row) -> resultSet.getLong(1), billId).stream().findFirst();
    }

    private String truncate(String value) {
        if (value == null || value.length() <= 1000) {
            return value;
        }
        return value.substring(0, 1000);
    }
}
