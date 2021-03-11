CREATE TABLE IF NOT EXISTS guild_words
(
    guild_id    BIGINT NOT NULL,
    banned_word TEXT   NOT NULL UNIQUE
);
