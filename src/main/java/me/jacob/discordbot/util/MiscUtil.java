package me.jacob.discordbot.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class MiscUtil {

	private MiscUtil() {
	}

	public static boolean hasRole(Member member, Role role) {
		return member.getRoles()
				.stream()
				.anyMatch(str -> str.getIdLong() == role.getIdLong());
	}

	public static boolean hasRole(Member member, long id) {
		return member.getRoles()
				.stream()
				.anyMatch(role -> role.getIdLong() == id);
	}

	public static boolean isSnowflake(String id) {
		try {
			net.dv8tion.jda.api.utils.MiscUtil.parseSnowflake(id);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	public static boolean isOption(String targetOption, String... options) {
		for (String option : options) {
			if (targetOption.equalsIgnoreCase(option))
				return true;
		}
		return false;
	}

	public static int getUserCount(JDA jda) {
		int count = 0;
		for (Guild guild : jda.getGuildCache()) {
			count += guild.getMemberCount();
		}
		return count;
	}
}
