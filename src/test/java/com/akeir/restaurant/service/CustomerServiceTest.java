package com.akeir.restaurant.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.akeir.restaurant.model.Customer;
import com.akeir.restaurant.repository.CustomerRepository;

public class CustomerServiceTest {

    @Test
    public void createShouldNormalizeFields() throws SQLException {
        StubCustomerRepository repository = new StubCustomerRepository();
        CustomerService service = new CustomerService(repository);

        Customer created = service.create("  Maria Souza  ", "  12345678901  ", "  +55 11 98888-7777  ", "  maria.souza@example.com  ");

        assertNotNull(created);
        assertEquals(Long.valueOf(301L), created.getId());
        assertEquals("Maria Souza", created.getName());
        assertEquals("12345678901", created.getDocument());
        assertEquals("+55 11 98888-7777", created.getPhone());
        assertEquals("maria.souza@example.com", created.getEmail());
        assertEquals("Maria Souza", repository.getLastCreated().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldRejectBlankName() throws SQLException {
        CustomerService service = new CustomerService(new StubCustomerRepository());
        service.create("   ", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldRejectInvalidEmailFormat() throws SQLException {
        CustomerService service = new CustomerService(new StubCustomerRepository());
        service.create("Valid Name", null, null, "invalid-email-format");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateShouldRejectNullCustomer() throws SQLException {
        CustomerService service = new CustomerService(new StubCustomerRepository());
        service.update(null);
    }

    private static final class StubCustomerRepository extends CustomerRepository {

        private final List<Customer> customers = new ArrayList<Customer>();
        private Customer lastCreated;

        @Override
        public List<Customer> findAll() {
            return new ArrayList<Customer>(customers);
        }

        @Override
        public Optional<Customer> findById(long id) {
            for (Customer customer : customers) {
                if (customer.getId() != null && customer.getId().longValue() == id) {
                    return Optional.of(customer);
                }
            }
            return Optional.empty();
        }

        @Override
        public long create(Customer customer) {
            Customer stored = new Customer(301L, customer.getName(), customer.getDocument(), customer.getPhone(), customer.getEmail());
            lastCreated = stored;
            customers.add(stored);
            return 301L;
        }

        @Override
        public boolean update(Customer customer) {
            return customer != null && customer.getId() != null;
        }

        @Override
        public boolean deleteById(long id) {
            return true;
        }

        @Override
        public int countAll() {
            return customers.size();
        }

        private Customer getLastCreated() {
            return lastCreated;
        }
    }
}
