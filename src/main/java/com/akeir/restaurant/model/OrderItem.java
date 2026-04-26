package com.akeir.restaurant.model;

public class OrderItem {

    private Long id;
    private Long orderId;
    private Long menuItemId;
    private String menuItemName;
    private int quantity;
    private int unitPriceCents;
    private int totalPriceCents;

    public OrderItem() {
    }

    public OrderItem(Long id, Long orderId, Long menuItemId, String menuItemName, int quantity, int unitPriceCents, int totalPriceCents) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.unitPriceCents = unitPriceCents;
        this.totalPriceCents = totalPriceCents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPriceCents() {
        return unitPriceCents;
    }

    public void setUnitPriceCents(int unitPriceCents) {
        this.unitPriceCents = unitPriceCents;
    }

    public int getTotalPriceCents() {
        return totalPriceCents;
    }

    public void setTotalPriceCents(int totalPriceCents) {
        this.totalPriceCents = totalPriceCents;
    }
}