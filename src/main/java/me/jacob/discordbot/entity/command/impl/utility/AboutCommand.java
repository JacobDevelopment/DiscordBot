package me.jacob.discordbot.entity.command.impl.utility;

import me.jacob.discordbot.core.DiscordBotInfo;
import me.jacob.discordbot.entity.command.AbstractCommand;
import me.jacob.discordbot.entity.command.CommandExecutor;
import me.jacob.discordbot.entity.command.type.Category;

import java.util.List;

public class AboutCommand extends AbstractCommand {

	public AboutCommand() {
		super("about", "Provides information about the bot.", Category.UTILITY);
	}

	@Override
	public void runCommand(CommandExecutor executor, List<String> args) {
		executor.reply(embedBuilder -> embedBuilder.setFooter("Developed by " + DiscordBotInfo.OWNER_TAG)
				.setTitle("DiscordBot (Version: " + DiscordBotInfo.VERSION + ")")
				.setDescription(
						"""
								**Summary**
								> I was developed to be a multi-purpose bot, that heavily dabbles in 
								> moderation tools and utilities to help maintain a server.
								"""
				)
				.addField("GitHub", String.format("[Click Me](%s)", DiscordBotInfo.GITHUB_URL), true)
				.addField("API", String.format("[JDA](%s)", DiscordBotInfo.API), true)
				.addField("Support", String.format("[Server](%s)", executor.getDiscordBot().getConfig().getBot().getString("support_server")), true)
		);
	}
}
