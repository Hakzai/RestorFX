package com.akeir.restaurant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.akeir.restaurant.model.Order;
import com.akeir.restaurant.model.OrderItem;

public class OrderRepository extends BaseRepository {

    public List<Order> findRecent(int limit) throws SQLException {
        String sql = "SELECT oh.id, oh.customer_id, c.name AS customer_name, oh.status, oh.total_cents, oh.issued_at "
            + "FROM order_header oh "
            + "LEFT JOIN customer c ON c.id = oh.customer_id "
            + "ORDER BY oh.issued_at DESC, oh.id DESC LIMIT ?";

        List<Order> orders = new ArrayList<Order>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Math.max(limit, 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrderRow(resultSet));
                }
            }
        }

        return orders;
    }

    public Optional<Order> findById(long id) throws SQLException {
        String sql = "SELECT oh.id, oh.customer_id, c.name AS customer_name, oh.status, oh.total_cents, oh.issued_at "
            + "FROM order_header oh "
            + "LEFT JOIN customer c ON c.id = oh.customer_id "
            + "WHERE oh.id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Order order = mapOrderRow(resultSet);
                order.setItems(findItemsByOrderId(connection, id));
                return Optional.of(order);
            }
        }
    }

    public long create(Order order) throws SQLException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        String insertHeaderSql = "INSERT INTO order_header (customer_id, status, total_cents) VALUES (?, ?, ?)";
        String insertItemSql = "INSERT INTO order_item (order_id, menu_item_id, quantity, unit_price_cents, total_price_cents) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement headerStatement = connection.prepareStatement(insertHeaderSql, Statement.RETURN_GENERATED_KEYS)) {
                if (order.getCustomerId() == null) {
                    headerStatement.setNull(1, java.sql.Types.INTEGER);
                } else {
                    headerStatement.setLong(1, order.getCustomerId().longValue());
                }
                headerStatement.setString(2, order.getStatus());
                headerStatement.setInt(3, order.getTotalCents());

                int headerRows = headerStatement.executeUpdate();
                if (headerRows == 0) {
                    throw new SQLException("Order creation failed: no header row was inserted.");
                }

                long orderId;
                try (ResultSet generatedKeys = headerStatement.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("Order creation failed: no generated order id returned.");
                    }
                    orderId = generatedKeys.getLong(1);
                }

                try (PreparedStatement itemStatement = connection.prepareStatement(insertItemSql)) {
                    for (OrderItem item : order.getItems()) {
                        itemStatement.setLong(1, orderId);
                        itemStatement.setLong(2, item.getMenuItemId().longValue());
                        itemStatement.setInt(3, item.getQuantity());
                        itemStatement.setInt(4, item.getUnitPriceCents());
                        itemStatement.setInt(5, item.getTotalPriceCents());
                        itemStatement.addBatch();
                    }
                    itemStatement.executeBatch();
                }

                connection.commit();
                order.setId(Long.valueOf(orderId));
                return orderId;
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM order_header";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;
        }
    }

    private List<OrderItem> findItemsByOrderId(Connection connection, long orderId) throws SQLException {
        String sql = "SELECT oi.id, oi.order_id, oi.menu_item_id, mi.name AS menu_item_name, oi.quantity, oi.unit_price_cents, oi.total_price_cents "
            + "FROM order_item oi "
            + "LEFT JOIN menu_item mi ON mi.id = oi.menu_item_id "
            + "WHERE oi.order_id = ? "
            + "ORDER BY oi.id";

        List<OrderItem> items = new ArrayList<OrderItem>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapItemRow(resultSet));
                }
            }
        }

        return items;
    }

    private Order mapOrderRow(ResultSet resultSet) throws SQLException {
        return new Order(
            Long.valueOf(resultSet.getLong("id")),
            resultSet.getObject("customer_id") == null ? null : Long.valueOf(resultSet.getLong("customer_id")),
            resultSet.getString("customer_name"),
            resultSet.getString("status"),
            resultSet.getInt("total_cents"),
            resultSet.getString("issued_at"),
            new ArrayList<OrderItem>()
        );
    }

    private OrderItem mapItemRow(ResultSet resultSet) throws SQLException {
        return new OrderItem(
            Long.valueOf(resultSet.getLong("id")),
            Long.valueOf(resultSet.getLong("order_id")),
            Long.valueOf(resultSet.getLong("menu_item_id")),
            resultSet.getString("menu_item_name"),
            resultSet.getInt("quantity"),
            resultSet.getInt("unit_price_cents"),
            resultSet.getInt("total_price_cents")
        );
    }
}