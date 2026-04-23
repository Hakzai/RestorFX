package com.akeir.restaurant.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.akeir.restaurant.model.Customer;
import com.akeir.restaurant.repository.CustomerRepository;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService() {
        this(new CustomerRepository());
    }

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() throws SQLException {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(long id) throws SQLException {
        return customerRepository.findById(id);
    }

    public Customer create(String name, String document, String phone, String email) throws SQLException {
        Customer customer = new Customer(
            null,
            normalizeRequiredField(name),
            normalizeOptionalField(document),
            normalizeOptionalField(phone),
            normalizeOptionalField(email)
        );

        validate(customer);
        long id = customerRepository.create(customer);
        customer.setId(Long.valueOf(id));
        return customer;
    }

    public boolean update(Customer customer) throws SQLException {
        if (customer != null) {
            customer.setName(normalizeRequiredField(customer.getName()));
            customer.setDocument(normalizeOptionalField(customer.getDocument()));
            customer.setPhone(normalizeOptionalField(customer.getPhone()));
            customer.setEmail(normalizeOptionalField(customer.getEmail()));
        }

        validate(customer);
        return customerRepository.update(customer);
    }

    public boolean deleteById(long id) throws SQLException {
        return customerRepository.deleteById(id);
    }

    public int countAllCustomers() throws SQLException {
        return customerRepository.countAll();
    }

    private void validate(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        if (customer.getEmail() != null && !customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Customer email format is invalid");
        }
    }

    private String normalizeRequiredField(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private String normalizeOptionalField(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
