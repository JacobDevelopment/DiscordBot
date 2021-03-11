DO $$ BEGIN
    CREATE TYPE temp_type AS ENUM ('TEMP_MUTE', 'TEMP_BAN');
EXCEPTION
    WHEN duplicate_object THEN null;
end $$;
CREATE TABLE IF NOT EXISTS guild_temps
(
    guild_id   BIGINT    NOT NULL,
    target_id  BIGINT    NOT NULL,
    temp       temp_type NOT NULL,
    issuer_id  BIGINT    NOT NULL,
    temp_until TIMESTAMP NOT NULL
);