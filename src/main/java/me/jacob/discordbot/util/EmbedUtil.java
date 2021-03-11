package me.jacob.discordbot.util;

import me.jacob.discordbot.core.Colors;
import me.jacob.discordbot.entity.command.type.ErrorType;
import me.jacob.discordbot.entity.command.type.InfractionType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;

public class EmbedUtil {

	private EmbedUtil() {
	}

	public static EmbedBuilder getLog(InfractionType infractionType, Member target, Member issuer, String reason) {
		return new EmbedBuilder()
				.setTitle("Infraction Issued")
				.setDescription(String.format("**%s** was %s by **%s** for the following reason: ```%s```",
						target.getUser().getAsTag(), infractionType.getName(), issuer.getUser().getAsTag(), reason))
				.setColor(Colors.INFRACTION)
				.setTimestamp(Instant.now())
				.setThumbnail(target.getUser().getEffectiveAvatarUrl())
				.setFooter("Target ID: " + target.getIdLong());
	}

	public static EmbedBuilder getError(Member member, ErrorType errorType, Object... objects) {
		return new EmbedBuilder()
				.setAuthor(errorType.getName())
				.setDescription(String.format("Uh oh **%s**, it looks like an error occurred.\n%s",
						member.getUser().getAsTag(), errorType.getContentFormatted(objects)))
				.setColor(Colors.ERROR);
	}

	public static EmbedBuilder getError(Member member, ErrorType errorType) {
		return new EmbedBuilder()
				.setAuthor(errorType.getName())
				.setDescription(String.format("Uh oh **%s**, it looks like an error occurred.\n%s",
						member.getUser().getAsTag(), errorType.getContent()))
				.setColor(Colors.ERROR);
	}

	public static EmbedBuilder getError(ErrorType errorType, Object... objects) {
		return new EmbedBuilder()
				.setAuthor(errorType.getName())
				.setDescription(String.format("Uh oh, it looks like an error occurred.\n%s",
						errorType.getContent().formatted(objects)))
				.setColor(Colors.ERROR);
	}

	public static EmbedBuilder getDefault(Member member, String content) {
		return member == null ?
				new EmbedBuilder().setDescription(content).setColor(Colors.PRIMARY) :
				new EmbedBuilder().setDescription(String.format("**%s:** %s",
						member.getUser().getAsTag(), content)).setColor(Colors.PRIMARY);

	}

	public static EmbedBuilder getDefault() {
		return new EmbedBuilder().setColor(Colors.PRIMARY);
	}


}
