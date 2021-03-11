package me.jacob.discordbot.entity.command;

import me.jacob.discordbot.core.DiscordBot;
import me.jacob.discordbot.entity.command.impl.bot_owner.StatsCommand;
import me.jacob.discordbot.entity.command.impl.moderation.KickCommand;
import me.jacob.discordbot.entity.command.impl.utility.*;
import me.jacob.discordbot.entity.command.type.ErrorType;
import me.jacob.discordbot.pojo.GuildData;
import me.jacob.discordbot.util.MiscUtil;
import me.jacob.discordbot.util.MsgUtil;
import me.jacob.discordbot.util.StringUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CommandRegistry {

	private final Map<String, AbstractCommand> COMMAND_MAP = new HashMap<>();
	private final ThreadGroup COMMAND_THREAD = new ThreadGroup("Command-Thread");
	private final ExecutorService EXECUTOR = Executors.newCachedThreadPool(
			v -> new Thread(COMMAND_THREAD, v, "Command-Pool")
	);

	private final DiscordBot DiscordBot;

	public int totalCommands = 0;

	public CommandRegistry(DiscordBot DiscordBot) {
		this.DiscordBot = DiscordBot;
		putCommand(new TestCommand());
		putCommand(new PingCommand());
		putCommand(new UserInfoCommand());
		putCommand(new HelpCommand(this));
		putCommand(new StatsCommand(this));
		putCommand(new AboutCommand());
		putCommand(new KickCommand());
	}

	public void runCommand(@NotNull GuildMessageReceivedEvent event, String prefix, GuildData guildData) {
		EXECUTOR.submit(() -> {
			if (event.getMember() == null) // In the event this **SOMEHOW** happens.
				return;

			final Message message = event.getMessage();
			final String rawContent = message.getContentRaw();
			final List<String> args = Arrays.asList(rawContent.substring(prefix.length())
					.split("\\s+"));

			final AbstractCommand command = getCommand(args.get(0).toLowerCase());

			if (command == null)
				return;

			if (guildData.isAutoDelete()) {
				event.getMessage()
						.delete()
						.queue(null,
								new ErrorHandler()
										.ignore(ErrorResponse.MISSING_PERMISSIONS)
										.ignore(ErrorResponse.UNKNOWN_MESSAGE)
						);
			}

			final PermissionSet permissionSet = command.getPermissionSet();

			final Member self = event.getGuild().getSelfMember();

			final TextChannel textChannel = event.getChannel();
			if (!textChannel.canTalk(self)) {
				MsgUtil.sendPrivateMessageFormatted(event.getAuthor(), "I do not have permission to talk in the following channel: **%s**", textChannel.getName());
				return;
			}

			if (!self.hasPermission(permissionSet.getSelfPerms()) && !self.hasPermission(Permission.ADMINISTRATOR)) {
				MsgUtil.replyError(
						textChannel,
						event.getMember(),
						ErrorType.MISSING_PERMISSION,
						self.getUser().getAsTag(), StringUtil.toString(permissionSet.getSelfPerms())
				);
				return;
			}

			final Member member = event.getMember();

			// TODO: Logically, we need to think about handling whether the issuing member has the permissions, or they simply have the role.
			// TODO: Not sure this will entirely work, still needs to be tested.
			final Role moderatorRole = event.getGuild().getRoleById(guildData.getModeratorId());
			if (permissionSet.getUserPerms() != null) {
				if ((moderatorRole != null && MiscUtil.hasRole(member, moderatorRole.getIdLong()) || member.hasPermission(permissionSet.getUserPerms()) && !member.hasPermission(Permission.ADMINISTRATOR))) {
					MsgUtil.replyError(
							textChannel, member,
							ErrorType.MISSING_PERMISSION,
							member.getUser().getAsTag(), StringUtil.toString(permissionSet.getUserPerms())
					);
					return;
				}
			}

			// TODO: Need to implement the CooldownHandler as well, in the event it ever happens.

			command.runCommand(new CommandExecutor(event, DiscordBot, guildData), args.subList(1, args.size()));
			totalCommands++;
		});
	}

	public AbstractCommand getCommand(@NotNull String invoke) {
		return COMMAND_MAP.get(invoke);
	}

	private void putCommand(AbstractCommand command) {
		if (COMMAND_MAP.containsKey(command.getInvoke()))
			throw new IllegalArgumentException(command + " already exists!");

		COMMAND_MAP.put(command.getInvoke(), command);
		if (!command.getAliases().isEmpty()) {
			for (String alias : command.getAliases()) {
				COMMAND_MAP.put(alias, command);
			}
		}
	}

	public List<AbstractCommand> getCommandsByCategory(String category) {
		return COMMAND_MAP.values()
				.stream()
				.distinct()
				.filter(command -> command.getCategory().getName().equalsIgnoreCase(category))
				.sorted(Comparator.comparing(AbstractCommand::getInvoke))
				.collect(Collectors.toUnmodifiableList());
	}


}
