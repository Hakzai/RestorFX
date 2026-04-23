package com.akeir.restaurant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.akeir.restaurant.model.Customer;

public class CustomerRepository extends BaseRepository {

    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT id, name, document, phone, email FROM customer ORDER BY name";
        List<Customer> customers = new ArrayList<Customer>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                customers.add(mapRow(resultSet));
            }
        }

        return customers;
    }

    public Optional<Customer> findById(long id) throws SQLException {
        String sql = "SELECT id, name, document, phone, email FROM customer WHERE id = ?";

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

    public long create(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (name, document, phone, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getDocument());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Customer creation failed: no row was inserted.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }

            throw new SQLException("Customer creation failed: no generated key returned.");
        }
    }

    public boolean update(Customer customer) throws SQLException {
        if (customer.getId() == null) {
            throw new IllegalArgumentException("Customer id is required for update");
        }

        String sql = "UPDATE customer SET name = ?, document = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getDocument());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());
            statement.setLong(5, customer.getId().longValue());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(long id) throws SQLException {
        String sql = "DELETE FROM customer WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM customer";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;
        }
    }

    private Customer mapRow(ResultSet resultSet) throws SQLException {
        return new Customer(
            Long.valueOf(resultSet.getLong("id")),
            resultSet.getString("name"),
            resultSet.getString("document"),
            resultSet.getString("phone"),
            resultSet.getString("email")
        );
    }
}
