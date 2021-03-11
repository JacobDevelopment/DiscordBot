package me.jacob.discordbot.entity.command.impl.bot_owner;

import me.jacob.discordbot.core.Colors;
import me.jacob.discordbot.entity.command.AbstractCommand;
import me.jacob.discordbot.entity.command.CommandExecutor;
import me.jacob.discordbot.entity.command.CommandRegistry;
import me.jacob.discordbot.entity.command.type.Category;
import me.jacob.discordbot.util.MiscUtil;
import net.dv8tion.jda.api.JDAInfo;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.List;

public class StatsCommand extends AbstractCommand {
	private final static int DATA_SIZE = 1024 * 1024;

	private final CommandRegistry commandRegistry;

	public StatsCommand(CommandRegistry commandRegistry) {
		super("stats", "Shows statistics about the bot.", Category.BOT_OWNER);
		this.commandRegistry = commandRegistry;
	}

	@Override
	public void runCommand(CommandExecutor executor, List<String> args) {
		final double memory = (double) getTotalMemory() / DATA_SIZE;
		final double usage = Math.round((double) getUsedMemory());
		final String percentage = new DecimalFormat("#.##").format((usage / memory) * 100);

		final PercentageBar percentageBar = new PercentageBar(usage, memory);
		executor.reply(embedBuilder -> embedBuilder.setColor(Colors.PRIMARY)
				.addField("Commands Ran:", commandRegistry.totalCommands + "", true)
				.addField("Thread Count:", ManagementFactory.getThreadMXBean().getThreadCount() + "", true)
				.addField("JDK:", System.getProperty("java.version"), true)
				.addField("JDA:", JDAInfo.VERSION, true)
				.addField("Server Count:", executor.getJDA().getGuildCache().size() + "", true)
				.addField("Member Count:", MiscUtil.getUserCount(executor.getJDA()) + "", true)
				.addField("Memory Usage", String.format("%s / %s MB (%s%%)\n%s", usage, memory, percentage, percentageBar.toString()), false)
		);
	}

	@Deprecated
	private String getMemoryString() {
		return getUsedMemory() + " / " + getTotalMemory() / DATA_SIZE + " MB";
	}

	private long getUsedMemory() {
		return (getTotalMemory() - getFreeMemory()) / DATA_SIZE;
	}

	private long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	private long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	@Deprecated
	private long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	private static class PercentageBar {
		private static final char[] bar = "----------------------------------".toCharArray();
		private static final int BASE = 1180;
		private final double percentage;

		public PercentageBar(double used, double total) {
			this.percentage = (used / total);
		}

		public String toString() {
			return String.format("```[%s]```", getUsage());
		}

		public String getUsage() {
			final long count = Math.round(percentage * BASE) / bar.length;
			for (int i = 0; i < count; i++) {
				bar[i] = '#';
			}
			return new String(bar);
		}
	}

}
