package com.akeir.restaurant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.akeir.restaurant.model.MenuItem;

public class MenuItemRepository extends BaseRepository {

    public List<MenuItem> findAll() throws SQLException {
        String sql = "SELECT id, name, description, price_cents, active FROM menu_item ORDER BY name";
        List<MenuItem> items = new ArrayList<MenuItem>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                items.add(mapRow(resultSet));
            }
        }

        return items;
    }

    public Optional<MenuItem> findById(long id) throws SQLException {
        String sql = "SELECT id, name, description, price_cents, active FROM menu_item WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public long create(MenuItem menuItem) throws SQLException {
        String sql = "INSERT INTO menu_item (name, description, price_cents, active) VALUES (?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, menuItem.getName());
            statement.setString(2, menuItem.getDescription());
            statement.setInt(3, menuItem.getPriceCents());
            statement.setInt(4, menuItem.isActive() ? 1 : 0);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Menu item creation failed: no row was inserted.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }

            throw new SQLException("Menu item creation failed: no generated key returned.");
        }
    }

    public boolean update(MenuItem menuItem) throws SQLException {
        if (menuItem.getId() == null) {
            throw new IllegalArgumentException("Menu item id is required for update");
        }

        String sql = "UPDATE menu_item SET name = ?, description = ?, price_cents = ?, active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, menuItem.getName());
            statement.setString(2, menuItem.getDescription());
            statement.setInt(3, menuItem.getPriceCents());
            statement.setInt(4, menuItem.isActive() ? 1 : 0);
            statement.setLong(5, menuItem.getId().longValue());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(long id) throws SQLException {
        String sql = "DELETE FROM menu_item WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM menu_item WHERE active = 1";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;
        }
    }

    private MenuItem mapRow(ResultSet resultSet) throws SQLException {
        return new MenuItem(
            Long.valueOf(resultSet.getLong("id")),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getInt("price_cents"),
            resultSet.getInt("active") == 1
        );
    }
}
