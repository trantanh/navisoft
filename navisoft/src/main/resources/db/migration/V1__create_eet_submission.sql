CREATE TABLE IF NOT EXISTS eet_submission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    bill_id INT NOT NULL,
    eic_popl VARCHAR(12) NOT NULL,
    id_jednotky INT NOT NULL,
    id_pokl VARCHAR(20) NOT NULL,
    porad_cis VARCHAR(25) NOT NULL,
    dat_trzby VARCHAR(35) NOT NULL,
    celk_trzba DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    message_uuid CHAR(36),
    pok VARCHAR(39),
    attempt_count INT NOT NULL DEFAULT 0,
    error_code INT,
    error_message VARCHAR(1000),
    next_attempt_at TIMESTAMP NULL,
    last_sent_at TIMESTAMP NULL,
    confirmed_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_eet_submission_bill UNIQUE (bill_id),
    CONSTRAINT uq_eet_submission_sale UNIQUE
        (eic_popl, id_jednotky, id_pokl, porad_cis, dat_trzby, celk_trzba)
);

CREATE INDEX idx_eet_submission_retry
    ON eet_submission (status, next_attempt_at);
