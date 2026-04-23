package com.akeir.restaurant.service;

import java.sql.SQLException;
import java.util.List;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;
import com.akeir.restaurant.model.FiscalDocument;
import com.akeir.restaurant.repository.FiscalDocumentRepository;

public class FiscalDocumentService {

    private final FiscalDocumentRepository fiscalDocumentRepository;

    public FiscalDocumentService() {
        this(new FiscalDocumentRepository());
    }

    public FiscalDocumentService(FiscalDocumentRepository fiscalDocumentRepository) {
        this.fiscalDocumentRepository = fiscalDocumentRepository;
    }

    public FiscalDocument recordNFeSuccess(NFeEmissionRequest request, NFeEmissionResult result) throws SQLException {
        validateSuccessInput(request, result);

        FiscalDocument fiscalDocument = new FiscalDocument(
            null,
            "NFE",
            normalizeRequired(result.getStatus()),
            normalizeRequired(request.getCustomerName()),
            normalizeOptional(request.getCustomerDocument()),
            request.getTotalCents(),
            normalizeOptional(result.getAccessKey()),
            normalizeOptional(result.getProtocol()),
            normalizeOptional(result.getXml()),
            null,
            null
        );

        long id = fiscalDocumentRepository.create(fiscalDocument);
        fiscalDocument.setId(Long.valueOf(id));
        return fiscalDocument;
    }

    public List<FiscalDocument> findRecent(int limit) throws SQLException {
        return fiscalDocumentRepository.findRecent(limit);
    }

    public int countAll() throws SQLException {
        return fiscalDocumentRepository.countAll();
    }

    private void validateSuccessInput(NFeEmissionRequest request, NFeEmissionResult result) {
        if (request == null) {
            throw new IllegalArgumentException("NFe request cannot be null");
        }

        if (result == null) {
            throw new IllegalArgumentException("NFe result cannot be null");
        }

        if (normalizeRequired(request.getCustomerName()) == null) {
            throw new IllegalArgumentException("Customer name is required for fiscal audit");
        }

        if (request.getTotalCents() <= 0) {
            throw new IllegalArgumentException("Fiscal audit total amount must be greater than zero");
        }

        if (normalizeRequired(result.getStatus()) == null) {
            throw new IllegalArgumentException("NFe status is required for fiscal audit");
        }
    }

    private String normalizeRequired(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeOptional(String value) {
        return normalizeRequired(value);
    }
}
