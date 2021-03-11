package me.jacob.discordbot.entity.command;


import me.jacob.discordbot.entity.command.type.ErrorType;
import me.jacob.discordbot.entity.command.type.InfractionType;
import me.jacob.discordbot.util.MiscUtil;
import net.dv8tion.jda.api.entities.Member;
import org.jooq.DSLContext;
import org.jooq.generated.tables.GuildInfractions;

import java.util.List;

public interface Infraction {


	InfractionType getType();

	default void startInfraction(CommandExecutor executor, List<String> args) {
		final Member target = executor.getMentionedMember();
		if (target == null && args.isEmpty()) {
			executor.replyError(ErrorType.INVALID_ARGS);
			return;
		}

		final String joined = String.join(" ", args.subList(1, args.size()));
		final String reason = joined.isEmpty() ? "No reason provided." : joined;

		if (target != null) {
			onInfraction(executor, target, executor.getMember(), reason);
			return;
		}

		final String targetId = args.get(0);
		if (!MiscUtil.isSnowflake(targetId)) {
			executor.replyError(ErrorType.INVALID_ARGS, targetId);
			return;
		}

		executor.getGuild().retrieveMemberById(targetId).queue(
				member -> onInfraction(executor, member, executor.getMember(), reason),
				error -> executor.replyError(ErrorType.COULD_NOT_FIND)
		);

	}

	default void onInfraction(CommandExecutor executor, Member target, Member issuing, String reason) {
		final String targetTag = target.getUser().getAsTag();
		if (!issuing.canInteract(target)) {
			executor.replyError(ErrorType.CANT_INTERACT, "You", targetTag);
			return;
		}

		final Member self = executor.getSelfMember();
		if (!self.canInteract(target)) {
			executor.replyError(ErrorType.CANT_INTERACT, "I", targetTag);
			return;
		}
		switch (getType()) {
			case KICKED -> kickInfraction(executor, target, issuing, reason);
			case BANNED -> banInfraction(executor, target, issuing, reason);
		}
	}

	default void kickInfraction(CommandExecutor executor, Member target, Member issuing, String reason) {
		target.kick(reason).queue(
				success -> {
					executor.replyAndLog(getType(), target, issuing, reason);
					insertInfraction(executor, target, issuing, reason);
				}, error -> executor.replyError(ErrorType.GENERIC, error.getMessage())
		);
	}

	default void banInfraction(CommandExecutor executor, Member target, Member issuing, String reason) {
		target.ban(7, reason).queue(
				success -> {
					executor.replyAndLog(getType(), target, issuing, reason);
					insertInfraction(executor, target, issuing, reason);
				}, error -> executor.replyError(ErrorType.GENERIC, error.getMessage())
		);
	}

	default void insertInfraction(CommandExecutor executor, Member target, Member issuing, String reason) {
		final DSLContext context = executor.getDiscordBot().getDatabaseManager().getDatabaseConnection().getDSL();
		if (context == null) {
			executor.replyError(ErrorType.GENERIC, "Something happened when trying to insert the infraction data, however it still processed.");
			return;
		}

		final org.jooq.generated.enums.Infraction jooqInfraction = getType().getConverted();
		context.insertInto(GuildInfractions.GUILD_INFRACTIONS)
				.values(target.getGuild().getIdLong(), target.getIdLong(), jooqInfraction,
						null, issuing.getIdLong(), reason, false)
				.executeAsync();
	}
}
