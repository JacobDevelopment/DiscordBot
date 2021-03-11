package me.jacob.discordbot.entity.command.type;


import me.jacob.discordbot.core.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public enum Category {

	BOT_OWNER("Commands visible to the bot owner only.", Constants.OWNER_ID),
	OWNER(Permission.ADMINISTRATOR, "Commands accessible by the server owner."),
	ADMIN(Permission.ADMINISTRATOR, "Administrative commands."),
	MODERATOR("Moderation commands."),
	UTILITY("Utility commands."),
	;

	private final String description;
	private Permission permission;
	private long id;

	Category(String description, long id) {
		this.description = description;
		this.id = id;
	}

	Category(Permission permission, String description) {
		this.permission = permission;
		this.description = description;
	}

	Category(String description) {
		this.description = description;
	}

	public Permission getPermission() {
		return permission;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPermitted(Member member) {
		if (this == Category.BOT_OWNER)
			return isBotOwner(member.getIdLong());
		return permission == null || member.hasPermission(permission);
	}

	public String getName() {
		return name().charAt(0) + name().substring(1);
	}

	private boolean isBotOwner(long memberId) {
		return memberId == id;
	}
}
