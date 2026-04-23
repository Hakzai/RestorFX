package com.akeir.restaurant.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.akeir.restaurant.model.MenuItem;
import com.akeir.restaurant.repository.MenuItemRepository;

public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService() {
        this(new MenuItemRepository());
    }

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> findAll() throws SQLException {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> findById(long id) throws SQLException {
        return menuItemRepository.findById(id);
    }

    public MenuItem create(String name, String description, int priceCents, boolean active) throws SQLException {
        MenuItem menuItem = new MenuItem(null, normalizeName(name), normalizeDescription(description), priceCents, active);
        validate(menuItem);

        long id = menuItemRepository.create(menuItem);
        menuItem.setId(Long.valueOf(id));
        return menuItem;
    }

    public boolean update(MenuItem menuItem) throws SQLException {
        validate(menuItem);
        return menuItemRepository.update(menuItem);
    }

    public boolean deleteById(long id) throws SQLException {
        return menuItemRepository.deleteById(id);
    }

    public int countActiveItems() throws SQLException {
        return menuItemRepository.countActive();
    }

    private void validate(MenuItem menuItem) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }

        if (menuItem.getName() == null || menuItem.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name is required");
        }

        if (menuItem.getPriceCents() < 0) {
            throw new IllegalArgumentException("Menu item price cannot be negative");
        }
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim();
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }

        String trimmedDescription = description.trim();
        return trimmedDescription.isEmpty() ? null : trimmedDescription;
    }
}
