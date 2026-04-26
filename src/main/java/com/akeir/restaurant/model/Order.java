package com.akeir.restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private Long customerId;
    private String customerName;
    private String status;
    private int totalCents;
    private String issuedAt;
    private List<OrderItem> items = new ArrayList<OrderItem>();

    public Order() {
    }

    public Order(Long id, Long customerId, String customerName, String status, int totalCents, String issuedAt, List<OrderItem> items) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.totalCents = totalCents;
        this.issuedAt = issuedAt;
        if (items != null) {
            this.items = new ArrayList<OrderItem>(items);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public void setTotalCents(int totalCents) {
        this.totalCents = totalCents;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        if (items == null) {
            this.items = new ArrayList<OrderItem>();
            return;
        }

        this.items = new ArrayList<OrderItem>(items);
    }
}