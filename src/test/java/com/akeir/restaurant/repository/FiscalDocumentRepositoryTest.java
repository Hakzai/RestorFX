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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akeir.restaurant.config.DatabaseConnectionManager;
import com.akeir.restaurant.config.DatabaseInitializer;
import com.akeir.restaurant.model.FiscalDocument;

public class FiscalDocumentRepositoryTest {

    private static final String TEST_DATABASE_DIRECTORY = "target/test-db";
    private static final String TEST_DATABASE_FILE = "restaurant-test.db";

    private final FiscalDocumentRepository repository = new FiscalDocumentRepository();

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
            statement.executeUpdate("DELETE FROM fiscal_document");
        }
    }

    @Test
    public void shouldCreateAndListRecentFiscalDocuments() throws Exception {
        FiscalDocument first = new FiscalDocument(null, "NFE", "AUTHORIZED_MOCK", "Maria", "123", 1500, "A1", "P1", "<xml>1</xml>", null, null);
        FiscalDocument second = new FiscalDocument(null, "NFE", "AUTHORIZED_MOCK", "Joao", "456", 2700, "A2", "P2", "<xml>2</xml>", null, null);

        long firstId = repository.create(first);
        long secondId = repository.create(second);

        assertTrue(firstId > 0);
        assertTrue(secondId > firstId);

        List<FiscalDocument> recent = repository.findRecent(10);
        assertEquals(2, recent.size());
        assertEquals("Joao", recent.get(0).getCustomerName());
        assertEquals("Maria", recent.get(1).getCustomerName());
    }

    @Test
    public void shouldApplyRecentLimitAndCountAll() throws Exception {
        repository.create(new FiscalDocument(null, "NFE", "AUTHORIZED_MOCK", "A", null, 100, "K1", "P1", "<xml/>", null, null));
        repository.create(new FiscalDocument(null, "NFE", "AUTHORIZED_MOCK", "B", null, 200, "K2", "P2", "<xml/>", null, null));
        repository.create(new FiscalDocument(null, "NFE", "AUTHORIZED_MOCK", "C", null, 300, "K3", "P3", "<xml/>", null, null));

        List<FiscalDocument> recentTwo = repository.findRecent(2);
        assertEquals(2, recentTwo.size());
        assertEquals("C", recentTwo.get(0).getCustomerName());
        assertEquals("B", recentTwo.get(1).getCustomerName());

        int count = repository.countAll();
        assertEquals(3, count);

        assertFalse(recentTwo.isEmpty());
    }
}
