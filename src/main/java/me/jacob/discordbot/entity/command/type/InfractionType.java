package me.jacob.discordbot.entity.command.type;


import org.jooq.generated.enums.Infraction;

public enum InfractionType {


	BANNED(),
	MUTED(),
	KICKED(),
	SOFTBANNED(),
	TEMPBAN(),
	TEMP_MUTE();

	public String getName() {
		return (name().charAt(0) + name().substring(1)).toLowerCase();
	}

	public Infraction getConverted() {
		switch (this) {
			case BANNED -> {
				return Infraction.BANNED;
			}
			case KICKED -> {
				return Infraction.KICKED;
			}
			case MUTED -> {
				return Infraction.MUTED;
			}
			case SOFTBANNED -> {
				return Infraction.SOFTBANNED;
			}
		}
		return null;
	}

}
