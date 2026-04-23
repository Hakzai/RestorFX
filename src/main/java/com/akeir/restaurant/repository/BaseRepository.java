package com.akeir.restaurant.repository;

import java.sql.Connection;
import java.sql.SQLException;

import com.akeir.restaurant.config.DatabaseConnectionManager;

public abstract class BaseRepository {

    protected Connection openConnection() throws SQLException {
        return DatabaseConnectionManager.getConnection();
    }
}
