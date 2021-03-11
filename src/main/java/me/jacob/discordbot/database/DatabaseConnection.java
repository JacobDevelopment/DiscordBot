package me.jacob.discordbot.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.jacob.discordbot.core.Checks;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private final Logger logger;

    private final HikariDataSource hikariDataSource;

    public DatabaseConnection(final DatabaseCredentials databaseCredentials) {
        this.logger = LoggerFactory.getLogger(DatabaseConnection.class);
        this.hikariDataSource = getPooling(databaseCredentials);
    }

    private HikariDataSource getPooling(final DatabaseCredentials databaseCredentials) {
        Checks.notNull(databaseCredentials, "The database credentials object was found to be null!");
        logger.info("Establishing Connection.");
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl(databaseCredentials.getUrl());
        hikariConfig.setUsername(databaseCredentials.getUsername());
        hikariConfig.setPassword(databaseCredentials.getPassword());
        return new HikariDataSource(hikariConfig);
    }

    public HikariDataSource getDataSource() {
        return hikariDataSource;
    }

    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            return null;
        }
    }

    public DSLContext getDSL() {
        return DSL.using(getConnection(), SQLDialect.POSTGRES);
    }




}
