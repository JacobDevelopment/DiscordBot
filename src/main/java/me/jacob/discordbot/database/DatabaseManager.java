package me.jacob.discordbot.database;

import me.jacob.discordbot.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private final Logger logger;
    private final DatabaseConnection databaseConnection;

    public DatabaseManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        this.logger = LoggerFactory.getLogger(DatabaseManager.class);
        System.getProperties().setProperty("org.jooq.no-logo", "true");
    }

    public DatabaseManager tableCreator(String... paths) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        for (String path : paths) {
            final String SQL = IOUtil.inputStreamToString(classLoader.getResourceAsStream(path));
            try (Connection connection = databaseConnection.getConnection()) {
                final Statement statement = connection.createStatement();
                statement.execute(SQL);
                logger.info("Creating Table: {}", path);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
