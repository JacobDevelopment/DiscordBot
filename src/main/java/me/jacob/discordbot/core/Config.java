package me.jacob.discordbot.core;

import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {

    private final DataObject instance;

    public Config(String path) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            instance = DataObject.fromJson(bufferedReader);
        }
    }

    public DataObject getBot() {
        return instance.getObject("bot");
    }

    public DataObject getDatabase() {
        return instance.getObject("database");
    }


}
