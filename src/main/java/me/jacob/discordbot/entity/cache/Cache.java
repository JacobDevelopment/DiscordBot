package me.jacob.discordbot.entity.cache;

import me.jacob.discordbot.core.DiscordBot;
import me.jacob.discordbot.entity.cache.impl.GuildCache;

public class Cache {

    private final GuildCache guildCache;

    public Cache(DiscordBot DiscordBot) {
        this.guildCache = new GuildCache(DiscordBot);
    }

    public GuildCache getGuildCache() {
        return guildCache;
    }
}
