DO
$$
    BEGIN
        CREATE TYPE infraction AS ENUM ('BANNED', 'MUTED', 'KICKED', 'SOFTBANNED', 'TEMP_BAN', 'TEMP_MUTE');
    EXCEPTION
        WHEN duplicate_object THEN null;
    end
$$;
CREATE TABLE IF NOT EXISTS guild_infractions
(
    guild_id             BIGINT     NOT NULL,
    target_id            BIGINT     NOT NULL,
    infraction_type      infraction NOT NULL,
    infraction_timestamp text       NOT NULL DEFAULT current_timestamp,
    issuer_id            BIGINT     NOT NULL,
    reason               TEXT       NOT NULL,
    cleared              BOOLEAN    NOT NULL DEFAULT FALSE
);

