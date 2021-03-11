package me.jacob.discordbot.discord;

import me.jacob.discordbot.core.Constants;
import me.jacob.discordbot.core.DiscordBot;
import me.jacob.discordbot.database.DatabaseConnection;
import me.jacob.discordbot.entity.command.CommandRegistry;
import me.jacob.discordbot.pojo.GuildData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildListener extends ListenerAdapter {

    private final org.jooq.generated.tables.GuildData GUILD_DATA = org.jooq.generated.tables.GuildData.GUILD_DATA;

    private final Logger logger;
    private final DiscordBot DiscordBot;

    private DatabaseConnection databaseConnection;
    private CommandRegistry commandRegistry;

    public GuildListener(DiscordBot DiscordBot) {
        this.logger = LoggerFactory.getLogger(GuildListener.class);
        this.DiscordBot = DiscordBot;
        if (DiscordBot != null) {
            this.databaseConnection = DiscordBot.getDatabaseManager().getDatabaseConnection();
            this.commandRegistry = new CommandRegistry(DiscordBot);
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        final DSLContext context = databaseConnection.getDSL();
        final long guildId = event.getGuild().getIdLong();
        final Result<Record> result = context.select()
                .from(GUILD_DATA)
                .where(GUILD_DATA.GUILD_ID.eq(guildId))
                .fetch();

        final long ownerId = event.getGuild().getOwnerIdLong();
        if (result.isEmpty()) {
            logger.warn("Guild {}: Not found in database, inserting now.", guildId);
            context.insertInto(GUILD_DATA, GUILD_DATA.GUILD_ID, GUILD_DATA.OWNER_ID)
                    .values(guildId, ownerId)
                    .executeAsync();
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final DSLContext context = databaseConnection.getDSL();
        final long guildId = event.getGuild().getIdLong();
        final long ownerId = event.getGuild().getOwnerIdLong();
        context.insertInto(GUILD_DATA, GUILD_DATA.GUILD_ID, GUILD_DATA.OWNER_ID)
                .values(guildId, ownerId)
                .onConflictDoNothing()
                .executeAsync();
        logger.info("Guild {}: Just added DiscordBot, inserting into table.", guildId);
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        final DSLContext context = databaseConnection.getDSL();
        final long guildId = event.getGuild().getIdLong();

        context.deleteFrom(GUILD_DATA).where(GUILD_DATA.GUILD_ID.eq(guildId))
                .executeAsync();
        logger.warn("Leaving Guild: {}", guildId);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final Message message = event.getMessage();

        if (message.getAuthor().isBot() || message.isWebhookMessage())
            return;

        final long guildId = event.getGuild().getIdLong();
        final GuildData guildData = DiscordBot.getCache().getGuildCache().retrieveById(guildId);
        final String prefix = guildData == null ? Constants.PREFIX : guildData.getPrefix();
        final String rawContent = event.getMessage().getContentRaw();

        if (rawContent.startsWith(prefix)) {
            commandRegistry.runCommand(event, prefix, guildData);
        }

    }
}
