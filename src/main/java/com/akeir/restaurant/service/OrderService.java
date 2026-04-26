package com.akeir.restaurant.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.akeir.restaurant.model.Customer;
import com.akeir.restaurant.model.MenuItem;
import com.akeir.restaurant.model.Order;
import com.akeir.restaurant.model.OrderItem;
import com.akeir.restaurant.repository.OrderRepository;

public class OrderService {

    private static final String DEFAULT_STATUS = "REGISTERED";

    private final OrderRepository orderRepository;
    private final MenuItemService menuItemService;
    private final CustomerService customerService;

    public OrderService() {
        this(new OrderRepository(), new MenuItemService(), new CustomerService());
    }

    public OrderService(OrderRepository orderRepository, MenuItemService menuItemService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.menuItemService = menuItemService;
        this.customerService = customerService;
    }

    public List<Order> findRecent(int limit) throws SQLException {
        return orderRepository.findRecent(limit);
    }

    public Optional<Order> findById(long id) throws SQLException {
        return orderRepository.findById(id);
    }

    public int countAllOrders() throws SQLException {
        return orderRepository.countAll();
    }

    public Order create(Long customerId, List<OrderItem> requestedItems) throws SQLException {
        validateCustomer(customerId);

        List<OrderItem> items = normalizeItems(requestedItems);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("At least one order item is required");
        }

        int totalCents = 0;
        List<OrderItem> enrichedItems = new ArrayList<OrderItem>();

        for (OrderItem requestedItem : items) {
            if (requestedItem.getMenuItemId() == null) {
                throw new IllegalArgumentException("Menu item is required for each order item");
            }

            if (requestedItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            MenuItem menuItem = menuItemService.findById(requestedItem.getMenuItemId().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + requestedItem.getMenuItemId()));

            int unitPriceCents = menuItem.getPriceCents();
            int itemTotalCents = unitPriceCents * requestedItem.getQuantity();
            totalCents += itemTotalCents;

            enrichedItems.add(new OrderItem(
                null,
                null,
                menuItem.getId(),
                menuItem.getName(),
                requestedItem.getQuantity(),
                unitPriceCents,
                itemTotalCents
            ));
        }

        Order order = new Order(null, customerId, resolveCustomerName(customerId), DEFAULT_STATUS, totalCents, null, enrichedItems);
        orderRepository.create(order);
        return order;
    }

    private void validateCustomer(Long customerId) throws SQLException {
        if (customerId == null) {
            return;
        }

        customerService.findById(customerId.longValue())
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
    }

    private String resolveCustomerName(Long customerId) throws SQLException {
        if (customerId == null) {
            return null;
        }

        Optional<Customer> customer = customerService.findById(customerId.longValue());
        return customer.isPresent() ? customer.get().getName() : null;
    }

    private List<OrderItem> normalizeItems(List<OrderItem> requestedItems) {
        if (requestedItems == null) {
            return new ArrayList<OrderItem>();
        }

        List<OrderItem> items = new ArrayList<OrderItem>();
        for (OrderItem item : requestedItems) {
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}