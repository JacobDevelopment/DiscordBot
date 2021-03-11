package me.jacob.discordbot.entity.command.impl.utility;

import me.jacob.discordbot.core.Colors;
import me.jacob.discordbot.entity.command.AbstractCommand;
import me.jacob.discordbot.entity.command.CommandExecutor;
import me.jacob.discordbot.entity.command.PermissionSet;
import me.jacob.discordbot.entity.command.type.Category;
import me.jacob.discordbot.entity.command.type.ErrorType;
import me.jacob.discordbot.util.MiscUtil;
import me.jacob.discordbot.util.TimeUtil;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class UserInfoCommand extends AbstractCommand {

	public UserInfoCommand() {
		super(
				"userinfo",
				"Shows information about a specific member",
				Category.UTILITY,
				PermissionSet.ofDefault(),
				"Shows information about a specific member, if no mention or id is provided, it will display your own information.",
				List.of("ui"),
				List.of("%s%s [@MENTIONED_MEMBER]", "%s%s [USER_ID]", "%s%s"),
				0
		);
	}

	@Override
	public void runCommand(CommandExecutor executor, List<String> args) {
		final Member target = executor.getMentionedMember();
		if (target == null && args.isEmpty()) {
			sendInfo(executor, executor.getMember());
			return;
		}


		if (target != null) {
			sendInfo(executor, target);
			return;
		}

		final String targetId = args.get(0);
		if (!MiscUtil.isSnowflake(targetId)) {
			executor.replyError(ErrorType.INVALID_ID, targetId);
			return;
		}

		executor.getGuild().retrieveMemberById(targetId).queue(
				member -> sendInfo(executor, member),
				error -> executor.replyError(ErrorType.COULD_NOT_FIND, error.getMessage())
		);
	}

	private void sendInfo(CommandExecutor executor, Member target) {
		final String nickname = target.getNickname() == null ? "No nickname set." : target.getNickname();
		executor.reply(embedBuilder -> embedBuilder.setAuthor(target.getUser().getAsTag())
				.setThumbnail(target.getUser().getEffectiveAvatarUrl())
				.setDescription(
						String.format(
								"""
										• **Creation Date:** %s (**%s** days old)  
										• **Join Date:** %s (**%s** days ago)
										• **Nickname:** %s                                 
										""",
								TimeUtil.dateToString(target.getTimeCreated()), TimeUtil.toDays(target.getTimeCreated()),
								TimeUtil.dateToString(target.getTimeJoined()), TimeUtil.toDays(target.getTimeJoined()),
								nickname)
				)
				.setFooter("ID: " + target.getId())
				.setColor(Colors.PRIMARY)
		);
	}
}
