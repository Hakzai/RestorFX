# Restaurant Management - Iteration 05 (EPIC 5)

## Goal

Deliver a complete order/sales flow with persistence, validation, and comprehensive test coverage.

## Scope

### Models
- `Order`: domain model with id, customerId, customerName, status, totalCents, issuedAt, and items list
- `OrderItem`: line item with menuItemId, menuItemName, quantity, unitPriceCents, totalPriceCents
- Defensive copying in constructors to prevent external list mutation

### Persistence Layer
- `OrderRepository`: JDBC-based with transaction support
  - `findRecent(int limit)`: SELECT with LEFT JOIN customer name
  - `findById(long id)`: Loads header and all items in single transaction
  - `create(Order)`: Atomic insert of header + batch insert of items (rollback on failure)
  - `countAll()`: Aggregate count for statistics
- Transaction management: commit/rollback on multi-row operations
- NULL-safe handling for optional customer_id (LEFT JOIN, getObject checks)

### Business Logic Layer
- `OrderService`: orchestration and validation
  - `create(Long customerId, List<OrderItem> items)`: Core flow
  - Batch fetch menu items (N+1 optimization via HashMap lookup)
  - Validation: customer presence, item non-emptiness, quantity > 0, menu item existence
  - Enrichment: populate menu item names and prices from catalog
  - Total calculation: sum of (quantity × unit_price)
  - Timestamp population: issued_at stored as `yyyy-MM-dd HH:mm:ss`
  - Default status: "REGISTERED"
  - Supports null customer (anonymous orders)

### UI Integration
- JavaFX Orders tab with 2-panel layout:
  - **Left panel**: Recent orders table (5 columns: id, customer, status, total, issuedAt)
  - **Right panel**: New order form with customer selection, menu item picker, quantity input, pending item table, and buttons
- Controller methods:
  - `onAddPendingOrderItem()`: Add to in-memory pending list with duplicate check
  - `onRemovePendingOrderItem()`: Remove from pending list
  - `onSaveOrder()`: Validate, create, persist, refresh, clear form
  - `onRefreshOrders()`: Reload recent orders from database
  - `onClearOrderForm()`: Reset pending list and input fields
- Input validation:
  - Quantity field accepts only positive integers (regex: `\d+`)
  - Menu item selection required before adding
  - Minimum 1 item required before saving
  - Customer selection optional (null allowed)

### Testing
- **OrderServiceTest** (7 tests): Happy path, null customer, zero/negative quantity, unknown menu item
- **OrderRepositoryTest** (4 tests): End-to-end persistence, null customer, customer name JOIN
- Uses StubOrderRepository and StubServices for isolation testing
- Uses real SQLite test database with schema bootstrap

## Code Review Improvements

### Issues Fixed
1. **N+1 Query Pattern**: Refactored to batch-fetch menu items (single `findAll()` + HashMap lookup)
2. **Missing Timestamp**: Added `getCurrentTimestamp()` helper (issued_at no longer NULL)
3. **No Input Validation**: Enhanced quantity parser with regex validation (`\d+`)
4. **Duplicate Prevention**: Added check to prevent same menu item twice in pending order
5. **Test Coverage**: Expanded to 7 service tests + 4 repository tests (edge cases)

## Exit Criteria

- ✅ Orders can be created from the UI
- ✅ Order items are persisted with the order header
- ✅ Service validates item presence, quantity > 0, and customer existence
- ✅ Repository can load recent orders and their items
- ✅ Full test coverage (11 tests covering happy path and edge cases)
- ✅ Timestamp audit trail (issued_at populated)
- ✅ Input validation prevents invalid data entry
- ✅ All 52 tests passing (0 failures, 0 regressions)

## Known Limitations

1. Pending order state is in-memory only (lost on app close)
2. No order cancellation or status transitions implemented
3. Customer name disambiguation: ComboBox displays name only