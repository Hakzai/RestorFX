package com.akeir.restaurant.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akeir.restaurant.config.DatabaseConnectionManager;
import com.akeir.restaurant.config.DatabaseInitializer;
import com.akeir.restaurant.model.MenuItem;

public class MenuItemRepositoryTest {

    private static final String TEST_DATABASE_DIRECTORY = "target/test-db";
    private static final String TEST_DATABASE_FILE = "restaurant-test.db";

    private final MenuItemRepository repository = new MenuItemRepository();

    @BeforeClass
    public static void beforeAll() throws Exception {
        System.setProperty("restaurant.db.directory", TEST_DATABASE_DIRECTORY);
        System.setProperty("restaurant.db.file", TEST_DATABASE_FILE);

        Path dbDirectory = Paths.get(TEST_DATABASE_DIRECTORY);
        Files.createDirectories(dbDirectory);
        Files.deleteIfExists(dbDirectory.resolve(TEST_DATABASE_FILE));

        DatabaseInitializer.initialize();
    }

    @AfterClass
    public static void afterAll() {
        System.clearProperty("restaurant.db.directory");
        System.clearProperty("restaurant.db.file");
    }

    @Before
    public void beforeEach() throws Exception {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM menu_item");
        }
    }

    @Test
    public void shouldCreateFindUpdateAndDeleteMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem(null, "Pasta", "Creamy sauce", 3490, true);

        long id = repository.create(menuItem);
        assertTrue(id > 0);

        Optional<MenuItem> created = repository.findById(id);
        assertTrue(created.isPresent());
        assertEquals("Pasta", created.get().getName());

        MenuItem toUpdate = created.get();
        toUpdate.setName("Pasta Deluxe");
        toUpdate.setPriceCents(3990);
        toUpdate.setActive(false);

        assertTrue(repository.update(toUpdate));

        Optional<MenuItem> updated = repository.findById(id);
        assertTrue(updated.isPresent());
        assertEquals("Pasta Deluxe", updated.get().getName());
        assertEquals(3990, updated.get().getPriceCents());
        assertFalse(updated.get().isActive());

        List<MenuItem> allItems = repository.findAll();
        assertEquals(1, allItems.size());

        assertTrue(repository.deleteById(id));
        assertFalse(repository.findById(id).isPresent());
    }

    @Test
    public void shouldCountOnlyActiveItems() throws Exception {
        repository.create(new MenuItem(null, "Soup", "Daily special", 1590, true));
        repository.create(new MenuItem(null, "Iced Tea", "No sugar", 890, false));

        int activeCount = repository.countActive();
        assertEquals(1, activeCount);
    }
}
