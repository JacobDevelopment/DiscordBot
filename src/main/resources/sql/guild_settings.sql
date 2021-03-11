CREATE TABLE IF NOT EXISTS guild_settings
(
    guild_id        BIGINT NOT NULL PRIMARY KEY,
    welcome_message BOOLEAN         DEFAULT false,
    welcome_id      BIGINT NOT NULL DEFAULT -1,
    goodbye_message BOOLEAN         DEFAULT false,
    goodbye_id      BIGINT NOT NULL DEFAULT -1,
    anti_nopfp      BOOLEAN         DEFAULT false,
    anti_alt        BOOLEAN         DEFAULT false,
    prune_role      BIGINT NOT NULL DEFAULT -1,
    log_commands    BOOLEAN         DEFAULT false
);