package me.jacob.discordbot.entity.command.impl.utility;
import me.jacob.discordbot.entity.command.type.Category;
import me.jacob.discordbot.entity.command.type.ErrorType;
import me.jacob.discordbot.util.EmbedUtil;
import me.jacob.discordbot.util.MiscUtil;
import me.jacob.discordbot.entity.command.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.jooq.tools.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class HelpCommand extends AbstractCommand {

	private final CommandRegistry commandRegistry;

	public HelpCommand(CommandRegistry commandRegistry) {
		super(
				"help",
				"Shows information about commands.",
				Category.UTILITY,
				PermissionSet.ofDefault(),
				"A helper command that shows information about commands.",
				Collections.singletonList("h"),
				List.of("%s%s", "%s%s [CATEGORY]", "%s%s [COMMAND_NAME]"),
				0
		);
		this.commandRegistry = commandRegistry;
	}

	@Override
	public void runCommand(CommandExecutor executor, List<String> args) {
		final Set<Category> permittedCategories = getCategories(Category.values(), executor.getMember())
				.stream()
				.sorted(Comparator.comparing(Category::getName))
				.collect(Collectors.toCollection(LinkedHashSet::new));

		if (args.isEmpty()) {
			executor.reply(getCategories(permittedCategories, executor.getPrefix()));
			return;
		}

		final String targetOption = args.get(0);
		if (MiscUtil.isOption(targetOption, permittedCategories.stream()
				.map(Category::getName)
				.filter(category -> getCategory().isPermitted(executor.getMember()))
				.toArray(String[]::new))) {
			executor.reply(getCommands(targetOption, executor.getPrefix()));
			return;
		}

		final AbstractCommand targetCommand = commandRegistry.getCommand(targetOption);
		if (targetCommand == null) {
			executor.replyError(ErrorType.GENERIC, "It seems that command does not exist!");
			return;
		}

		executor.reply(new CommandHelper(executor.getPrefix(), targetCommand).getEmbed());
	}

	private EmbedBuilder getCommands(String targetOption, String guildPrefix) {
		final List<AbstractCommand> commandList = commandRegistry.getCommandsByCategory(targetOption);
		if (commandList.isEmpty())
			return EmbedUtil.getError(ErrorType.GENERIC, "No commands were found for the specified category!");

		final int longestSize = getLongestSize(commandList);
		final StringBuilder stringBuilder = new StringBuilder()
				.append(String.format("Type **`%shelp [command_name]`** to retrieve information about a specific command.",
						guildPrefix))
				.append("```m\n");
		for (AbstractCommand command : commandList) {
			stringBuilder
					.append(StringUtils.rightPad(command.getInvoke(), longestSize, " "))
					.append(" = ")
					.append(command.getDescription())
					.append("\n");
		}
		stringBuilder.append("```");
		return EmbedUtil.getDefault().setDescription(stringBuilder);
	}

	private EmbedBuilder getCategories(Set<Category> permittedCategories, String guildPrefix) {
		final StringBuilder stringBuilder = new StringBuilder()
				.append(String.format("Type **`%shelp [category]`** to retrieve a list of commands per category.",
						guildPrefix))
				.append("```m\n");
		final int longestSize = getLongestSize(permittedCategories) + 1;
		for (Category category : permittedCategories) {
			stringBuilder
					.append(StringUtils.rightPad(category.getName(), longestSize, " "))
					.append(" = ")
					.append(category.getDescription())
					.append("\n");
		}
		stringBuilder.append("```");
		return EmbedUtil.getDefault().setDescription(stringBuilder);
	}

	private Set<Category> getCategories(Category[] categories, Member member) {
		final Set<Category> categorySet = new HashSet<>();
		for (Category category : categories) {
			if (category.isPermitted(member))
				categorySet.add(category);
		}
		return categorySet;
	}

	private int getLongestSize(List<AbstractCommand> commands) {
		int size = 0;
		for (AbstractCommand command : commands) {
			if (command.getInvoke().length() > size) {
				size = command.getInvoke().length();
			}
		}
		return size;
	}

	private int getLongestSize(Set<Category> categories) {
		int size = 0;
		for (Category category : categories) {
			if (category.getName().length() > size) {
				size = category.getName().length();
			}
		}
		return size;
	}
}
