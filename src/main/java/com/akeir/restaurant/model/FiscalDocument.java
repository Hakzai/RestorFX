package com.akeir.restaurant.model;

public class FiscalDocument {

    private Long id;
    private String documentType;
    private String status;
    private String customerName;
    private String customerDocument;
    private int totalCents;
    private String accessKey;
    private String protocol;
    private String xml;
    private String errorMessage;
    private String createdAt;

    public FiscalDocument() {
    }

    public FiscalDocument(Long id,
                          String documentType,
                          String status,
                          String customerName,
                          String customerDocument,
                          int totalCents,
                          String accessKey,
                          String protocol,
                          String xml,
                          String errorMessage,
                          String createdAt) {
        this.id = id;
        this.documentType = documentType;
        this.status = status;
        this.customerName = customerName;
        this.customerDocument = customerDocument;
        this.totalCents = totalCents;
        this.accessKey = accessKey;
        this.protocol = protocol;
        this.xml = xml;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerDocument() {
        return customerDocument;
    }

    public void setCustomerDocument(String customerDocument) {
        this.customerDocument = customerDocument;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public void setTotalCents(int totalCents) {
        this.totalCents = totalCents;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
