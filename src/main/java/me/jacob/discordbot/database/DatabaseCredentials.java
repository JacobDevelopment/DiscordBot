package me.jacob.discordbot.database;

import me.jacob.discordbot.core.Config;
import me.jacob.discordbot.exception.ConfigException;

public class DatabaseCredentials {

    private final String url;
    private final String username;
    private final String password;

    public DatabaseCredentials(Config config) {
        if (config == null)
            throw new ConfigException("The config object was null!");

        this.url = getUrl(config);
        this.username = getUsername(config);
        this.password = getPassword(config);
    }

    private String getUrl(final Config config) {
        return config.getDatabase().getString("url");
    }

    private String getUsername(Config config) {
        return config.getDatabase().getString("username");
    }

    private String getPassword(Config config) {
        return config.getDatabase().getString("password");
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
