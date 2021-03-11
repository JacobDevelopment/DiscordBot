CREATE TABLE IF NOT EXISTS guild_data
(
    guild_id     BIGINT     NOT NULL PRIMARY KEY,
    owner_id     BIGINT     NOT NULL,
    prefix       VARCHAR(8) NOT NULL DEFAULT 'd!',
    moderator_id BIGINT     NOT NULL DEFAULT -1,
    muted_id     BIGINT     NOT NULL DEFAULT -1,
    logs_id      BIGINT     NOT NULL DEFAULT -1,
    auto_delete  BOOLEAN    NOT NULL DEFAULT TRUE
);