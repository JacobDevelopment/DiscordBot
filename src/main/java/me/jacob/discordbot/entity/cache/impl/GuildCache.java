package me.jacob.discordbot.entity.cache.impl;

import me.jacob.discordbot.core.Checks;
import me.jacob.discordbot.core.DiscordBot;
import me.jacob.discordbot.entity.cache.ICache;
import me.jacob.discordbot.pojo.GuildData;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GuildCache implements ICache<Long, GuildData> {

	private final org.jooq.generated.tables.GuildData guildData = org.jooq.generated.tables.GuildData.GUILD_DATA;

	private final Map<Long, GuildData> guildCache;
	private DSLContext context;

	public GuildCache(DiscordBot discordBot) {
		LoggerFactory.getLogger(GuildCache.class).info("Initiating Guild Cache...");
		final int guildSize = ((int) Math.ceil(discordBot.getJda().getGuildCache().size() * .8));

		Checks.check(guildSize < 1, "guildSize < 1");

		this.guildCache = ExpiringMap.builder()
				.maxSize(guildSize)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(10, TimeUnit.MINUTES)
				.build();

		if (discordBot.getDatabaseManager().getDatabaseConnection() != null) {
			this.context = discordBot.getDatabaseManager().getDatabaseConnection().getDSL();
		}
	}

	@Override
	public void put(Long guildId, GuildData value) {
		guildCache.put(guildId, value);
	}

	@Override
	public void delete(Long guildId) {
		guildCache.remove(guildId);
		context.deleteFrom(guildData)
				.where(guildData.GUILD_ID.eq(guildId))
				.executeAsync();
	}

	@Override
	public void update(Long guildId, @NotNull GuildData updatedValue) {
		context.update(guildData)
				.set(guildData.OWNER_ID, updatedValue.getOwnerId())
				.set(guildData.PREFIX, updatedValue.getPrefix())
				.set(guildData.MODERATOR_ID, updatedValue.getModeratorId())
				.set(guildData.MUTED_ID, updatedValue.getMutedId())
				.set(guildData.LOGS_ID, updatedValue.getLogsId())
				.set(guildData.AUTO_DELETE, updatedValue.isAutoDelete())
				.where(guildData.GUILD_ID.eq(guildId))
				.executeAsync();
		guildCache.put(guildId, updatedValue);
	}

	@Override
	public GuildData getById(Long guildId) {
		return guildCache.get(guildId);
	}

	@Override
	public GuildData retrieveById(Long guildId) {
		if (guildCache.get(guildId) == null) {
			final Result<Record> retrieved = context.select()
					.from(guildData)
					.where(guildData.GUILD_ID.eq(guildId))
					.fetch();
			final GuildData retrievedData = new GuildData(
					retrieved.get(0).get(guildData.GUILD_ID),
					retrieved.get(0).get(guildData.OWNER_ID),
					retrieved.get(0).get(guildData.PREFIX),
					retrieved.get(0).get(guildData.MODERATOR_ID),
					retrieved.get(0).get(guildData.MUTED_ID),
					retrieved.get(0).get(guildData.LOGS_ID),
					retrieved.get(0).get(guildData.AUTO_DELETE)
			);
			guildCache.put(guildId, retrievedData);
		}
		return guildCache.get(guildId);
	}
}
