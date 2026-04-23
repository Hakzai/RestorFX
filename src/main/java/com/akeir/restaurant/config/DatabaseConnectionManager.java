package com.akeir.restaurant.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnectionManager {

    private static final String DATABASE_DIRECTORY = "db";
    private static final String DATABASE_FILE_NAME = "restaurant.db";

    private DatabaseConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        ensureDriverLoaded();
        Path databasePath = ensureDatabasePath();
        String jdbcUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath().normalize().toString();
        return DriverManager.getConnection(jdbcUrl);
    }

    private static Path ensureDatabasePath() throws SQLException {
        try {
            Path directoryPath = Paths.get(DATABASE_DIRECTORY);
            Files.createDirectories(directoryPath);
            return directoryPath.resolve(DATABASE_FILE_NAME);
        } catch (Exception exception) {
            throw new SQLException("Failed to prepare SQLite database path", exception);
        }
    }

    private static void ensureDriverLoaded() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("SQLite JDBC driver not available", exception);
        }
    }
}
