package com.akeir.restaurant.model;

public class MenuItem {

    private Long id;
    private String name;
    private String description;
    private int priceCents;
    private boolean active;

    public MenuItem() {
    }

    public MenuItem(Long id, String name, String description, int priceCents, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceCents = priceCents;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(int priceCents) {
        this.priceCents = priceCents;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "Menu item";
        }

        return name + " - " + String.format("%.2f", Double.valueOf(priceCents) / 100.0d);
    }
}
