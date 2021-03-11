package me.jacob.discordbot;

import me.jacob.discordbot.core.Config;
import me.jacob.discordbot.core.DiscordBot;
import me.jacob.discordbot.database.DatabaseConnection;
import me.jacob.discordbot.database.DatabaseCredentials;
import me.jacob.discordbot.database.DatabaseManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Starter {

    public static void main(String[] args) {
        try {
            final Config config = new Config("config.json");
            final DatabaseCredentials databaseCredentials = new DatabaseCredentials(config);
            final DatabaseConnection databaseConnection = new DatabaseConnection(databaseCredentials);
            final DatabaseManager databaseManager = new DatabaseManager(databaseConnection).tableCreator(
                    "sql/guild_data.sql",
                    "sql/guild_infractions.sql",
                    "sql/guild_settings.sql",
                    "sql/guild_temps.sql",
                    "sql/guild_warnings.sql",
                    "sql/guild_words.sql"
            );

            new DiscordBot(config, databaseManager);
        } catch (IOException | InterruptedException | LoginException e) {
            e.printStackTrace();
        }
    }

}
