package com.akeir.restaurant.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akeir.restaurant.config.DatabaseConnectionManager;
import com.akeir.restaurant.config.DatabaseInitializer;
import com.akeir.restaurant.model.Customer;
import com.akeir.restaurant.model.MenuItem;
import com.akeir.restaurant.model.Order;
import com.akeir.restaurant.model.OrderItem;

public class OrderRepositoryTest {

    private static final String TEST_DATABASE_DIRECTORY = "target/test-db";
    private static final String TEST_DATABASE_FILE = "restaurant-test.db";

    private final OrderRepository repository = new OrderRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final MenuItemRepository menuItemRepository = new MenuItemRepository();

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
            statement.executeUpdate("DELETE FROM order_item");
            statement.executeUpdate("DELETE FROM order_header");
            statement.executeUpdate("DELETE FROM menu_item");
            statement.executeUpdate("DELETE FROM customer");
        }
    }

    @Test
    public void shouldPersistOrderHeaderAndItems() throws Exception {
        Customer customer = new Customer(null, "Paula Lima", null, null, null);
        long customerId = customerRepository.create(customer);

        MenuItem burger = new MenuItem(null, "Burger", "Classic burger", 2890, true);
        MenuItem fries = new MenuItem(null, "Fries", "Side portion", 1090, true);
        long burgerId = menuItemRepository.create(burger);
        long friesId = menuItemRepository.create(fries);

        Order order = new Order(null, Long.valueOf(customerId), null, "REGISTERED", 0, null, Arrays.asList(
            new OrderItem(null, null, Long.valueOf(burgerId), null, 2, 2890, 5780),
            new OrderItem(null, null, Long.valueOf(friesId), null, 1, 1090, 1090)
        ));
        order.setTotalCents(6870);

        long orderId = repository.create(order);
        assertTrue(orderId > 0);

        Optional<Order> loaded = repository.findById(orderId);
        assertTrue(loaded.isPresent());
        assertEquals("REGISTERED", loaded.get().getStatus());
        assertEquals(Long.valueOf(customerId), loaded.get().getCustomerId());
        assertEquals(2, loaded.get().getItems().size());
        assertEquals(6870, loaded.get().getTotalCents());

        List<Order> recent = repository.findRecent(10);
        assertEquals(1, recent.size());
        assertEquals(Long.valueOf(orderId), recent.get(0).getId());

        assertEquals(1, repository.countAll());
    }

    @Test
    public void shouldReturnEmptyWhenOrderDoesNotExist() throws Exception {
        assertFalse(repository.findById(9999L).isPresent());
    }
}