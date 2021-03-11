package me.jacob.discordbot.entity.command;

import me.jacob.discordbot.core.DiscordBot;
import me.jacob.discordbot.entity.command.type.ErrorType;
import me.jacob.discordbot.entity.command.type.InfractionType;
import me.jacob.discordbot.pojo.GuildData;
import me.jacob.discordbot.util.EmbedUtil;
import me.jacob.discordbot.util.MsgUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class CommandExecutor {

	private final GuildMessageReceivedEvent event;
	private final DiscordBot DiscordBot;
	private final GuildData guildData;

	public CommandExecutor(@NotNull GuildMessageReceivedEvent event, @NotNull DiscordBot DiscordBot, GuildData guildData) {
		this.event = event;
		this.DiscordBot = DiscordBot;
		this.guildData = guildData;
	}

	public GuildMessageReceivedEvent getEvent() {
		return event;
	}

	public DiscordBot getDiscordBot() {
		return DiscordBot;
	}

	public JDA getJDA() {
		return this.getEvent().getJDA();
	}

	public Guild getGuild() {
		return this.getEvent().getGuild();
	}

	public TextChannel getChannel() {
		return this.event.getChannel();
	}

	public Message getMessage() {
		return this.getEvent().getMessage();
	}

	public Member getMember() {
		return this.getMessage().getMember();
	}

	public User getAuthor() {
		return this.getMessage().getAuthor();
	}

	public List<Member> getMentionedMembers() {
		return this.getMessage().getMentionedMembers();
	}

	public Member getSelfMember() {
		return this.getGuild().getSelfMember();
	}

	public User getSelfUser() {
		return this.getJDA().getSelfUser();
	}

	public TextChannel getLogChannel() {
		final long guildId = this.getGuild().getIdLong();
		final GuildData guildData = DiscordBot.getCache().getGuildCache().retrieveById(guildId);
		if (guildData != null) {
			return this.getGuild().getTextChannelById(guildData.getLogsId());
		}
		return null;
	}

	public Member getMentionedMember() {
		return this.getMentionedMembers().size() == 1
				? this.getMentionedMembers().get(0) : null;
	}

	public GuildData getData() {
		return guildData;
	}

	public String getPrefix() {
		return guildData.getPrefix();
	}

	public void reply(String content) {
		MsgUtil.reply(getChannel(), getMember(), content);
	}

	public void reply(EmbedBuilder embedBuilder) {
		MsgUtil.reply(getChannel(), embedBuilder);
	}

	public void reply(Consumer<EmbedBuilder> embedBuilderConsumer) {
		final EmbedBuilder defaultEmbed = EmbedUtil.getDefault();
		embedBuilderConsumer.accept(defaultEmbed);
		MsgUtil.reply(getChannel(), defaultEmbed);
	}

	public void replyDefault(boolean addMemberTag, String content) {
		if (addMemberTag)
			MsgUtil.reply(getChannel(), EmbedUtil.getDefault(getMember(), content));
		else
			MsgUtil.reply(getChannel(), EmbedUtil.getDefault(null, content));
	}

	public void replyDefault(boolean addMemberTag, String content, Object... objects) {
		if (addMemberTag)
			MsgUtil.reply(getChannel(), EmbedUtil.getDefault(getMember(), content.formatted(objects)));
		else
			MsgUtil.reply(getChannel(), EmbedUtil.getDefault(null, content.formatted(objects)));
	}


	public void replyError(ErrorType errorType, Object... objects) {
		MsgUtil.replyError(getChannel(), getMember(), errorType, objects);
	}

	public void replyError(ErrorType errorType) {
		MsgUtil.replyError(getChannel(), getMember(), errorType);
	}

	public void replyAndLog(InfractionType infractionType, Member target, Member issuing, String reason) {
		final EmbedBuilder embedBuilder = EmbedUtil.getLog(infractionType, target, issuing, reason);
		reply(embedBuilder);

		final TextChannel logChannel = getLogChannel();
		if (logChannel != null) {
			logChannel.sendMessage(embedBuilder.build()).queue();
		}
	}


}
