package me.jacob.discordbot.entity.command;

import me.jacob.discordbot.core.Colors;
import me.jacob.discordbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandHelper {

	private final String guildPrefix;
	private final AbstractCommand command;

	private String builtUsages;

	public CommandHelper(String guildPrefix, AbstractCommand command) {
		this.guildPrefix = guildPrefix;
		this.command = command;
		if (!command.getAliases().isEmpty())
			this.builtUsages = buildUsages();
	}

	public EmbedBuilder getEmbed() {
		final String alias = command.getAliases().isEmpty() ? "No aliases." : StringUtil.toString(command.getAliases());
		final EmbedBuilder embedBuilder = new EmbedBuilder()
				.appendDescription("**Command Name:** `")
				.appendDescription(command.getInvoke())
				.appendDescription("` **Aliases:** ")
				.appendDescription("`")
				.appendDescription(alias)
				.appendDescription("`\n")
				.appendDescription("**Description:** ```")
				.appendDescription(getDescription())
				.appendDescription("```\n")
				.setFooter("<> - Required | [] - Optional")
				.setColor(Colors.PRIMARY);

		if (!buildUsages().isEmpty())
			embedBuilder.addField("Usages:", "```" + getBuiltUsages() + "```", true);
		return embedBuilder;
	}

	private String buildUsages() {
		final Set<String> usageSet = new HashSet<>();
		for (String usage : command.getUsages()) {
			usageSet.add(usage.formatted(guildPrefix, command.getInvoke()));
			for (String alias : command.getAliases()) {
				usageSet.add(usage.formatted(guildPrefix, alias));
			}
		}
		return usageSet.stream()
				.sorted()
				.collect(Collectors.joining("\n"));
	}

	private String getDescription() {
		return (command.getLongDescription() == null || command.getLongDescription().isEmpty())
				? command.getDescription() : command.getLongDescription();
	}

	public String getBuiltUsages() {
		return builtUsages;
	}
}
