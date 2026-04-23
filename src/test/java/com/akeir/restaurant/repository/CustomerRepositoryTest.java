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
import com.akeir.restaurant.model.Customer;

public class CustomerRepositoryTest {

    private static final String TEST_DATABASE_DIRECTORY = "target/test-db";
    private static final String TEST_DATABASE_FILE = "restaurant-test.db";

    private final CustomerRepository repository = new CustomerRepository();

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
            statement.executeUpdate("DELETE FROM order_header");
            statement.executeUpdate("DELETE FROM customer");
        }
    }

    @Test
    public void shouldCreateFindUpdateAndDeleteCustomer() throws Exception {
        Customer customer = new Customer(null, "Ana Costa", "11122233344", "+55 11 96666-0000", "ana.costa@example.com");

        long id = repository.create(customer);
        assertTrue(id > 0);

        Optional<Customer> created = repository.findById(id);
        assertTrue(created.isPresent());
        assertEquals("Ana Costa", created.get().getName());

        Customer toUpdate = created.get();
        toUpdate.setName("Ana C. Costa");
        toUpdate.setPhone("+55 11 95555-0000");

        assertTrue(repository.update(toUpdate));

        Optional<Customer> updated = repository.findById(id);
        assertTrue(updated.isPresent());
        assertEquals("Ana C. Costa", updated.get().getName());
        assertEquals("+55 11 95555-0000", updated.get().getPhone());

        List<Customer> allCustomers = repository.findAll();
        assertEquals(1, allCustomers.size());

        assertTrue(repository.deleteById(id));
        assertFalse(repository.findById(id).isPresent());
    }

    @Test
    public void shouldCountAllCustomers() throws Exception {
        repository.create(new Customer(null, "Carlos", null, null, null));
        repository.create(new Customer(null, "Julia", "123", "+55 11 90000-0000", "julia@example.com"));

        int customerCount = repository.countAll();
        assertEquals(2, customerCount);
    }
}
