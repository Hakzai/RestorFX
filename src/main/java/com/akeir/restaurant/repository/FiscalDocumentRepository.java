package com.akeir.restaurant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.akeir.restaurant.model.FiscalDocument;

public class FiscalDocumentRepository extends BaseRepository {

    public long create(FiscalDocument fiscalDocument) throws SQLException {
        String sql = "INSERT INTO fiscal_document "
            + "(document_type, status, customer_name, customer_document, total_cents, access_key, protocol, xml, error_message) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, fiscalDocument.getDocumentType());
            statement.setString(2, fiscalDocument.getStatus());
            statement.setString(3, fiscalDocument.getCustomerName());
            statement.setString(4, fiscalDocument.getCustomerDocument());
            statement.setInt(5, fiscalDocument.getTotalCents());
            statement.setString(6, fiscalDocument.getAccessKey());
            statement.setString(7, fiscalDocument.getProtocol());
            statement.setString(8, fiscalDocument.getXml());
            statement.setString(9, fiscalDocument.getErrorMessage());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Fiscal document creation failed: no row was inserted.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }

            throw new SQLException("Fiscal document creation failed: no generated key returned.");
        }
    }

    public List<FiscalDocument> findRecent(int limit) throws SQLException {
        int safeLimit = limit > 0 ? limit : 10;
        String sql = "SELECT id, document_type, status, customer_name, customer_document, total_cents, access_key, protocol, xml, error_message, created_at "
            + "FROM fiscal_document ORDER BY id DESC LIMIT ?";

        List<FiscalDocument> documents = new ArrayList<FiscalDocument>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, safeLimit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    documents.add(mapRow(resultSet));
                }
            }
        }

        return documents;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM fiscal_document";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;
        }
    }

    private FiscalDocument mapRow(ResultSet resultSet) throws SQLException {
        return new FiscalDocument(
            Long.valueOf(resultSet.getLong("id")),
            resultSet.getString("document_type"),
            resultSet.getString("status"),
            resultSet.getString("customer_name"),
            resultSet.getString("customer_document"),
            resultSet.getInt("total_cents"),
            resultSet.getString("access_key"),
            resultSet.getString("protocol"),
            resultSet.getString("xml"),
            resultSet.getString("error_message"),
            resultSet.getString("created_at")
        );
    }
}
