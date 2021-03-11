package me.jacob.discordbot.entity.command.impl.moderation;

import me.jacob.discordbot.entity.command.AbstractCommand;
import me.jacob.discordbot.entity.command.CommandExecutor;
import me.jacob.discordbot.entity.command.Infraction;
import me.jacob.discordbot.entity.command.PermissionSet;
import me.jacob.discordbot.entity.command.type.Category;
import me.jacob.discordbot.entity.command.type.InfractionType;
import net.dv8tion.jda.api.Permission;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class KickCommand extends AbstractCommand implements Infraction {

	public KickCommand() {
		super(
				"kick",
				"Kicks a member from the server.",
				Category.MODERATOR,
				PermissionSet.of(EnumSet.of(Permission.KICK_MEMBERS), EnumSet.of(Permission.KICK_MEMBERS)),
				"Kicks a member from the server, you can use a valid ID or member mention.",
				Collections.singletonList("k"),
				List.of("%s%s <MEMBER_ID> [REASON]", "%s%s <@MENTIONED_MEMBER> [REASON]"),
				0
		);
	}

	@Override
	public void runCommand(CommandExecutor executor, List<String> args) {
		startInfraction(executor, args);
	}

	@Override
	public InfractionType getType() {
		return InfractionType.KICKED;
	}
}
