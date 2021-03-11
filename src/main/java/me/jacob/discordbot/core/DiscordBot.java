package me.jacob.discordbot.core;

import me.jacob.discordbot.database.DatabaseManager;
import me.jacob.discordbot.discord.GuildListener;
import me.jacob.discordbot.entity.cache.Cache;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Collection;
import java.util.List;

public class DiscordBot {
	private final Config config;
	private final DatabaseManager databaseManager;
	private final Cache cache;

	private final JDA jda;

	public DiscordBot(Config config, DatabaseManager databaseManager) throws LoginException, InterruptedException {
		this.config = config;
		this.databaseManager = databaseManager;
		this.jda = build();
		this.cache = new Cache(this);
	}

	private JDA build() throws LoginException, InterruptedException {
		LoggerFactory.getLogger(this.getClass()).info("Building the bot!");
		final String token = config.getBot().getString("token");
		Checks.notEmpty(token, "token");

		final Collection<GatewayIntent> INTENTS = List.of(GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_MESSAGES);

		final Collection<CacheFlag> DISABLED_CACHE = List.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOTE,
				CacheFlag.CLIENT_STATUS);

		return JDABuilder.create(token, INTENTS)
				.setActivity(Activity.watching(" Jacob develop."))
				.setMemberCachePolicy(MemberCachePolicy.OWNER)
				.disableCache(DISABLED_CACHE)
				.addEventListeners(new GuildListener(this))
				.setChunkingFilter(ChunkingFilter.NONE)
				.build()
				.awaitReady();
	}

	public Config getConfig() {
		return config;
	}

	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public JDA getJda() {
		return jda;
	}

	public Cache getCache() {
		return cache;
	}


}

