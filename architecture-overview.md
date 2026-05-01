# Architecture Overview

## Current State (Post EPIC 5)

System style:

- Desktop monolith (Java 8 + JavaFX + SQLite)

Primary technologies:

- Java 8
- JavaFX 8 (UI framework)
- Maven (build automation)
- SQLite 3 with JDBC driver (persistence)
- SLF4J (structured logging)
- JUnit 4 (testing)

Layered architecture (implemented):

- `controller`: JavaFX event handlers, UI state management (MainController)
- `service`: business logic, validation, orchestration (MenuItemService, CustomerService, FiscalDocumentService, OrderService, NFeProviderConfigurationService)
- `repository`: JDBC-based data access with transaction support (extends BaseRepository)
- `model`: domain entities (MenuItem, Customer, Order, OrderItem, FiscalDocument)
- `dto`: data transfer objects (NFeEmissionRequest, NFeEmissionResult, NFeProviderSetupRequest)
- `integration/nfe`: fiscal provider adapters (MockNFeService, RealNFeService, NFeServiceFactory)
- `config`: bootstrap and feature flags (DatabaseInitializer, DatabaseConnectionManager, FeatureFlags)

Runtime flow:

1. `App.main()` initializes DatabaseConnectionManager and DatabaseInitializer
2. Schema bootstrap runs once (menu_item, customer, order_header, order_item, fiscal_document tables)
3. JavaFX Stage loads FXML layout and wires MainController
4. MainController initializes with services and populates initial data
5. User interactions trigger controller methods → service calls → repository persistence

Key design principles:

- No business logic in UI layer
- Services validate and enrich data (N+1 query eliminated via batch fetching)
- Repositories handle only JDBC/persistence concerns
- Transactions managed at repository level (commit/rollback)
- Foreign keys enforced at SQLite level (PRAGMA foreign_keys = ON)
- Error messages propagated through exception chain

Current module coverage:

- ✅ EPIC 1: Database Layer
- ✅ EPIC 2: Menu Management
- ✅ EPIC 3: Customer Management
- ✅ EPIC 4: Fiscal Document Audit
- ✅ EPIC 5: Order/Sales Flow (completed with 11 tests)
- ✅ EPIC 6: NFe Mock Integration
- 🔄 EPIC 7: Real NFe Integration (in progress)
