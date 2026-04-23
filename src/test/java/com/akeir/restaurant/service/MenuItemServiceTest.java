package com.akeir.restaurant.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.akeir.restaurant.model.MenuItem;
import com.akeir.restaurant.repository.MenuItemRepository;

public class MenuItemServiceTest {

    @Test
    public void createShouldNormalizeNameAndDescription() throws SQLException {
        StubMenuItemRepository repository = new StubMenuItemRepository();
        MenuItemService service = new MenuItemService(repository);

        MenuItem created = service.create("  Burger  ", "  House special  ", 1990, true);

        assertNotNull(created);
        assertEquals(Long.valueOf(101L), created.getId());
        assertEquals("Burger", created.getName());
        assertEquals("House special", created.getDescription());
        assertEquals(1990, created.getPriceCents());
        assertEquals("Burger", repository.getLastCreated().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldRejectBlankName() throws SQLException {
        MenuItemService service = new MenuItemService(new StubMenuItemRepository());
        service.create("   ", "Any", 1000, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldRejectNegativePrice() throws SQLException {
        MenuItemService service = new MenuItemService(new StubMenuItemRepository());
        service.create("Burger", "Any", -1, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateShouldRejectNullItem() throws SQLException {
        MenuItemService service = new MenuItemService(new StubMenuItemRepository());
        service.update(null);
    }

    private static final class StubMenuItemRepository extends MenuItemRepository {

        private final List<MenuItem> items = new ArrayList<MenuItem>();
        private MenuItem lastCreated;

        @Override
        public List<MenuItem> findAll() {
            return new ArrayList<MenuItem>(items);
        }

        @Override
        public Optional<MenuItem> findById(long id) {
            for (MenuItem item : items) {
                if (item.getId() != null && item.getId().longValue() == id) {
                    return Optional.of(item);
                }
            }
            return Optional.empty();
        }

        @Override
        public long create(MenuItem menuItem) {
            MenuItem storedItem = new MenuItem(101L, menuItem.getName(), menuItem.getDescription(), menuItem.getPriceCents(), menuItem.isActive());
            lastCreated = storedItem;
            items.add(storedItem);
            return 101L;
        }

        @Override
        public boolean update(MenuItem menuItem) {
            return menuItem != null && menuItem.getId() != null;
        }

        @Override
        public boolean deleteById(long id) {
            return true;
        }

        @Override
        public int countActive() {
            int count = 0;
            for (MenuItem item : items) {
                if (item.isActive()) {
                    count++;
                }
            }
            return count;
        }

        private MenuItem getLastCreated() {
            return lastCreated;
        }
    }
}
