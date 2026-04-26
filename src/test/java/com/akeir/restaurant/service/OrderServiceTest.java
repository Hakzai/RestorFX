package com.akeir.restaurant.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.akeir.restaurant.model.Customer;
import com.akeir.restaurant.model.MenuItem;
import com.akeir.restaurant.model.Order;
import com.akeir.restaurant.model.OrderItem;
import com.akeir.restaurant.repository.OrderRepository;

public class OrderServiceTest {

    @Test
    public void createShouldCalculateTotalsAndPersistCanonicalItems() throws SQLException {
        StubOrderRepository orderRepository = new StubOrderRepository();
        StubMenuItemService menuItemService = new StubMenuItemService();
        StubCustomerService customerService = new StubCustomerService();

        OrderService service = new OrderService(orderRepository, menuItemService, customerService);

        Order created = service.create(Long.valueOf(11L), asItems(
            new OrderItem(null, null, Long.valueOf(101L), null, 2, 0, 0),
            new OrderItem(null, null, Long.valueOf(202L), null, 1, 0, 0)
        ));

        assertNotNull(created);
        assertEquals(Long.valueOf(701L), created.getId());
        assertEquals("REGISTERED", created.getStatus());
        assertEquals(2, created.getItems().size());
        assertEquals(5960, created.getTotalCents());
        assertEquals("Burger", created.getItems().get(0).getMenuItemName());
        assertEquals(202L, menuItemService.getLastRequestedId());
        assertEquals(11L, customerService.getLastRequestedId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldRejectMissingItems() throws SQLException {
        OrderService service = new OrderService(new StubOrderRepository(), new StubMenuItemService(), new StubCustomerService());
        service.create(Long.valueOf(11L), new ArrayList<OrderItem>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShouldRejectUnknownCustomer() throws SQLException {
        StubCustomerService customerService = new StubCustomerService();
        customerService.setExists(false);

        OrderService service = new OrderService(new StubOrderRepository(), new StubMenuItemService(), customerService);
        service.create(Long.valueOf(11L), asItems(new OrderItem(null, null, Long.valueOf(101L), null, 1, 0, 0)));
    }

    private List<OrderItem> asItems(OrderItem... items) {
        List<OrderItem> list = new ArrayList<OrderItem>();
        for (OrderItem item : items) {
            list.add(item);
        }
        return list;
    }

    private static final class StubOrderRepository extends OrderRepository {

        private Order lastCreated;

        @Override
        public long create(Order order) {
            lastCreated = order;
            order.setId(Long.valueOf(701L));
            return 701L;
        }

        @Override
        public Optional<Order> findById(long id) {
            return Optional.ofNullable(lastCreated);
        }

        @Override
        public List<Order> findRecent(int limit) {
            List<Order> orders = new ArrayList<Order>();
            if (lastCreated != null) {
                orders.add(lastCreated);
            }
            return orders;
        }

        @Override
        public int countAll() {
            return lastCreated == null ? 0 : 1;
        }
    }

    private static final class StubMenuItemService extends MenuItemService {

        private long lastRequestedId;

        @Override
        public Optional<MenuItem> findById(long id) {
            lastRequestedId = id;
            if (id == 101L) {
                return Optional.of(new MenuItem(Long.valueOf(101L), "Burger", null, 2890, true));
            }
            if (id == 202L) {
                return Optional.of(new MenuItem(Long.valueOf(202L), "Fries", null, 180, true));
            }
            return Optional.empty();
        }

        long getLastRequestedId() {
            return lastRequestedId;
        }
    }

    private static final class StubCustomerService extends CustomerService {

        private long lastRequestedId;
        private boolean exists = true;

        @Override
        public Optional<Customer> findById(long id) {
            lastRequestedId = id;
            if (!exists) {
                return Optional.empty();
            }
            return Optional.of(new Customer(Long.valueOf(id), "Guest", null, null, null));
        }

        void setExists(boolean exists) {
            this.exists = exists;
        }

        long getLastRequestedId() {
            return lastRequestedId;
        }
    }
}