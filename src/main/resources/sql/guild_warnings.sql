CREATE TABLE IF NOT EXISTS guild_warnings
(
    guild_id          BIGINT    NOT NULL,
    target_id         BIGINT    NOT NULL,
    warning_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    warning_reason    TEXT      NOT NULL DEFAULT 'There was no reason provided.',
    cleared           BOOLEAN            DEFAULT FALSE,
    issuer_id         BIGINT    NOT NULL
);