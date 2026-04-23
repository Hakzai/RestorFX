package com.akeir.restaurant.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatabaseInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);

    private DatabaseInitializer() {
    }

    public static void initialize() {
        String schemaSql = loadSchemaSql();
        List<String> statements = splitStatements(schemaSql);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");
            for (String sqlStatement : statements) {
                statement.execute(sqlStatement);
            }

            LOGGER.info("Database schema initialization completed with {} statements", statements.size());
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to initialize database schema", exception);
        }
    }

    private static String loadSchemaSql() {
        InputStream inputStream = DatabaseInitializer.class.getResourceAsStream("/db/schema.sql");
        if (inputStream == null) {
            throw new IllegalStateException("Schema file /db/schema.sql was not found in classpath");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append('\n');
            }
            return sqlBuilder.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to read schema SQL file", exception);
        }
    }

    private static List<String> splitStatements(String rawSql) {
        StringBuilder filteredSql = new StringBuilder();
        String[] lines = rawSql.split("\\r?\\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("--") || trimmedLine.isEmpty()) {
                continue;
            }
            filteredSql.append(line).append('\n');
        }

        String[] statementParts = filteredSql.toString().split(";");
        List<String> statements = new ArrayList<String>();
        for (String statement : statementParts) {
            String trimmedStatement = statement.trim();
            if (!trimmedStatement.isEmpty()) {
                statements.add(trimmedStatement);
            }
        }

        return statements;
    }
}
