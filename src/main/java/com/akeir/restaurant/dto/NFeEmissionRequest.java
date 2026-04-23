package com.akeir.restaurant.dto;

public class NFeEmissionRequest {

    private final String customerName;
    private final String customerDocument;
    private final int totalCents;
    private final String notes;

    public NFeEmissionRequest(String customerName, String customerDocument, int totalCents, String notes) {
        this.customerName = customerName;
        this.customerDocument = customerDocument;
        this.totalCents = totalCents;
        this.notes = notes;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerDocument() {
        return customerDocument;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public String getNotes() {
        return notes;
    }
}