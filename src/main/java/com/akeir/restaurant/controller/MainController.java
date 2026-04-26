package com.akeir.restaurant.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akeir.restaurant.config.FeatureFlags;
import com.akeir.restaurant.dto.NFeProviderSetupRequest;
import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;
import com.akeir.restaurant.integration.nfe.NFeProvider;
import com.akeir.restaurant.integration.nfe.NFeServiceFactory;
import com.akeir.restaurant.integration.nfe.NFeService;
import com.akeir.restaurant.model.Customer;
import com.akeir.restaurant.model.FiscalDocument;
import com.akeir.restaurant.model.MenuItem;
import com.akeir.restaurant.model.Order;
import com.akeir.restaurant.model.OrderItem;
import com.akeir.restaurant.service.CustomerService;
import com.akeir.restaurant.service.FiscalDocumentService;
import com.akeir.restaurant.service.MenuItemService;
import com.akeir.restaurant.service.OrderService;
import com.akeir.restaurant.service.NFeProviderConfigurationService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final MenuItemService menuItemService = new MenuItemService();
    private final CustomerService customerService = new CustomerService();
    private final FiscalDocumentService fiscalDocumentService = new FiscalDocumentService();
    private final OrderService orderService = new OrderService();
    private final NFeProviderConfigurationService nfeProviderConfigurationService = new NFeProviderConfigurationService();
    private final boolean nfeMockEnabled = FeatureFlags.isNFeMockEnabled();
    private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
    private final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> orderMenuItems = FXCollections.observableArrayList();
    private final ObservableList<Customer> orderCustomers = FXCollections.observableArrayList();
    private final ObservableList<Order> orders = FXCollections.observableArrayList();
    private final ObservableList<OrderItem> pendingOrderItems = FXCollections.observableArrayList();
    private NFeProvider activeNFeProvider;
    private NFeService nfeService;

    @FXML
    private Label subtitleLabel;

    @FXML
    private TabPane contentTabPane;

    @FXML
    private Tab nfeMockTab;

    @FXML
    private Tab nfeRealTab;

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, Long> orderIdColumn;

    @FXML
    private TableColumn<Order, String> orderCustomerColumn;

    @FXML
    private TableColumn<Order, String> orderStatusColumn;

    @FXML
    private TableColumn<Order, String> orderTotalColumn;

    @FXML
    private TableColumn<Order, String> orderIssuedAtColumn;

    @FXML
    private ComboBox<Customer> orderCustomerComboBox;

    @FXML
    private ComboBox<MenuItem> orderMenuItemComboBox;

    @FXML
    private TextField orderQuantityField;

    @FXML
    private TableView<OrderItem> pendingOrderTable;

    @FXML
    private TableColumn<OrderItem, Long> pendingOrderItemIdColumn;

    @FXML
    private TableColumn<OrderItem, String> pendingOrderMenuItemColumn;

    @FXML
    private TableColumn<OrderItem, String> pendingOrderQuantityColumn;

    @FXML
    private TableColumn<OrderItem, String> pendingOrderUnitPriceColumn;

    @FXML
    private TableColumn<OrderItem, String> pendingOrderTotalColumn;

    @FXML
    private Label orderSummaryLabel;

    @FXML
    private Label orderFeedbackLabel;

    @FXML
    private TableView<MenuItem> menuTable;

    @FXML
    private TableColumn<MenuItem, Long> idColumn;

    @FXML
    private TableColumn<MenuItem, String> nameColumn;

    @FXML
    private TableColumn<MenuItem, String> priceColumn;

    @FXML
    private TableColumn<MenuItem, String> activeColumn;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Long> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> customerDocumentColumn;

    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;

    @FXML
    private TableColumn<Customer, String> customerEmailColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private Label menuFeedbackLabel;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerSearchField;

    @FXML
    private TextField customerDocumentField;

    @FXML
    private TextField customerPhoneField;

    @FXML
    private TextField customerEmailField;

    @FXML
    private Label customerFeedbackLabel;

    @FXML
    private Label customerFilterSummaryLabel;

    @FXML
    private TextField nfeCustomerNameField;

    @FXML
    private TextField nfeCustomerDocumentField;

    @FXML
    private TextField nfeTotalAmountField;

    @FXML
    private TextArea nfeNotesField;

    @FXML
    private TextArea nfeXmlOutputArea;

    @FXML
    private TextArea nfeAuditHistoryArea;

    @FXML
    private Label nfeFeedbackLabel;

    @FXML
    private ComboBox<String> nfeProviderComboBox;

    @FXML
    private TextField nfeRealEnvironmentField;

    @FXML
    private TextField nfeRealCertificatePathField;

    @FXML
    private TextField nfeRealCertificatePasswordField;

    @FXML
    private TextArea nfeRealPreparationNotesArea;

    @FXML
    private Label nfeRealFeedbackLabel;

    @FXML
    public void initialize() {
        configureMenuTable();
        configureCustomerTable();
        configureOrderTables();
        initializeNFeProviderSelection();
        configureNFeTabs();

        menuTable.setItems(menuItems);
        customerTable.setItems(customers);
        orderTable.setItems(orders);
        pendingOrderTable.setItems(pendingOrderItems);

        menuTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateForm(newValue));
        customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateCustomerForm(newValue));

        activeCheckBox.setSelected(true);
        loadMenuItems();
        loadCustomers();
        loadOrders();
        updatePendingOrderSummary();
        loadNFeAuditHistory();
    }

    @FXML
    public void onAddItem() {
        try {
            MenuItem createdItem = menuItemService.create(
                nameField.getText(),
                descriptionField.getText(),
                parsePriceToCents(priceField.getText()),
                activeCheckBox.isSelected()
            );

            setMenuFeedback("Menu item created: " + createdItem.getName(), true);
            clearForm();
            loadMenuItems();
        } catch (IllegalArgumentException exception) {
            setMenuFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to add menu item", exception);
            setMenuFeedback("Failed to create menu item", false);
        }
    }

    @FXML
    public void onUpdateItem() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            setMenuFeedback("Select an item before updating", false);
            return;
        }

        try {
            selectedItem.setName(normalizeText(nameField.getText()));
            selectedItem.setDescription(normalizeNullableText(descriptionField.getText()));
            selectedItem.setPriceCents(parsePriceToCents(priceField.getText()));
            selectedItem.setActive(activeCheckBox.isSelected());

            boolean updated = menuItemService.update(selectedItem);
            if (updated) {
                setMenuFeedback("Menu item updated", true);
                clearForm();
                loadMenuItems();
            } else {
                setMenuFeedback("Menu item not found for update", false);
            }
        } catch (IllegalArgumentException exception) {
            setMenuFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to update menu item", exception);
            setMenuFeedback("Failed to update menu item", false);
        }
    }

    @FXML
    public void onDeleteItem() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getId() == null) {
            setMenuFeedback("Select an item before deleting", false);
            return;
        }

        try {
            boolean deleted = menuItemService.deleteById(selectedItem.getId().longValue());
            if (deleted) {
                setMenuFeedback("Menu item deleted", true);
                clearForm();
                loadMenuItems();
            } else {
                setMenuFeedback("Menu item not found for deletion", false);
            }
        } catch (SQLException exception) {
            LOGGER.error("Failed to delete menu item", exception);
            setMenuFeedback("Failed to delete menu item", false);
        }
    }

    @FXML
    public void onClearForm() {
        clearForm();
        setMenuFeedback("Form cleared", true);
    }

    @FXML
    public void onAddCustomer() {
        try {
            Customer createdCustomer = customerService.create(
                customerNameField.getText(),
                customerDocumentField.getText(),
                customerPhoneField.getText(),
                customerEmailField.getText()
            );

            setCustomerFeedback("Customer created: " + createdCustomer.getName(), true);
            clearCustomerForm();
            loadCustomers();
        } catch (IllegalArgumentException exception) {
            setCustomerFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to add customer", exception);
            setCustomerFeedback("Failed to create customer", false);
        }
    }

    @FXML
    public void onUpdateCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            setCustomerFeedback("Select a customer before updating", false);
            return;
        }

        try {
            selectedCustomer.setName(normalizeText(customerNameField.getText()));
            selectedCustomer.setDocument(normalizeNullableText(customerDocumentField.getText()));
            selectedCustomer.setPhone(normalizeNullableText(customerPhoneField.getText()));
            selectedCustomer.setEmail(normalizeNullableText(customerEmailField.getText()));

            boolean updated = customerService.update(selectedCustomer);
            if (updated) {
                setCustomerFeedback("Customer updated", true);
                clearCustomerForm();
                loadCustomers();
            } else {
                setCustomerFeedback("Customer not found for update", false);
            }
        } catch (IllegalArgumentException exception) {
            setCustomerFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to update customer", exception);
            setCustomerFeedback("Failed to update customer", false);
        }
    }

    @FXML
    public void onDeleteCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null || selectedCustomer.getId() == null) {
            setCustomerFeedback("Select a customer before deleting", false);
            return;
        }

        try {
            boolean deleted = customerService.deleteById(selectedCustomer.getId().longValue());
            if (deleted) {
                setCustomerFeedback("Customer deleted", true);
                clearCustomerForm();
                loadCustomers();
            } else {
                setCustomerFeedback("Customer not found for deletion", false);
            }
        } catch (SQLException exception) {
            LOGGER.error("Failed to delete customer", exception);
            setCustomerFeedback("Failed to delete customer", false);
        }
    }

    @FXML
    public void onClearCustomerForm() {
        clearCustomerForm();
        setCustomerFeedback("Customer form cleared", true);
    }

    @FXML
    public void onCustomerSearchChanged() {
        applyCustomerFilter();
    }

    @FXML
    public void onClearCustomerSearch() {
        customerSearchField.clear();
        applyCustomerFilter();
    }

    @FXML
    public void onAddPendingOrderItem() {
        MenuItem selectedMenuItem = orderMenuItemComboBox == null ? null : orderMenuItemComboBox.getValue();
        if (selectedMenuItem == null || selectedMenuItem.getId() == null) {
            setOrderFeedback("Select a menu item before adding it", false);
            return;
        }

        int quantity = parsePositiveQuantity(orderQuantityField.getText());
        OrderItem orderItem = new OrderItem(
            null,
            null,
            selectedMenuItem.getId(),
            selectedMenuItem.getName(),
            quantity,
            selectedMenuItem.getPriceCents(),
            selectedMenuItem.getPriceCents() * quantity
        );

        pendingOrderItems.add(orderItem);
        orderQuantityField.clear();
        if (orderMenuItemComboBox != null) {
            orderMenuItemComboBox.getSelectionModel().clearSelection();
        }
        updatePendingOrderSummary();
        setOrderFeedback("Added " + quantity + " x " + selectedMenuItem.getName(), true);
    }

    @FXML
    public void onRemovePendingOrderItem() {
        OrderItem selectedItem = pendingOrderTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            setOrderFeedback("Select a pending item before removing it", false);
            return;
        }

        pendingOrderItems.remove(selectedItem);
        updatePendingOrderSummary();
        setOrderFeedback("Pending item removed", true);
    }

    @FXML
    public void onSaveOrder() {
        if (pendingOrderItems.isEmpty()) {
            setOrderFeedback("Add at least one item before saving the order", false);
            return;
        }

        Customer selectedCustomer = orderCustomerComboBox == null ? null : orderCustomerComboBox.getValue();
        Long customerId = selectedCustomer == null ? null : selectedCustomer.getId();

        try {
            Order createdOrder = orderService.create(customerId, new ArrayList<OrderItem>(pendingOrderItems));
            pendingOrderItems.clear();
            updatePendingOrderSummary();
            loadOrders();
            if (orderMenuItemComboBox != null) {
                orderMenuItemComboBox.getSelectionModel().clearSelection();
            }
            orderQuantityField.clear();
            setOrderFeedback("Order #" + createdOrder.getId() + " saved with total " + formatCents(createdOrder.getTotalCents()), true);
        } catch (IllegalArgumentException exception) {
            setOrderFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to persist order", exception);
            setOrderFeedback("Failed to save order", false);
        }
    }

    @FXML
    public void onRefreshOrders() {
        loadOrders();
    }

    @FXML
    public void onClearOrderForm() {
        pendingOrderItems.clear();
        updatePendingOrderSummary();
        if (orderCustomerComboBox != null) {
            orderCustomerComboBox.getSelectionModel().clearSelection();
        }
        if (orderMenuItemComboBox != null) {
            orderMenuItemComboBox.getSelectionModel().clearSelection();
        }
        orderQuantityField.clear();
        setOrderFeedback("Order form cleared", true);
    }

    @FXML
    public void onEmitNFeMock() {
        if (nfeService == null) {
            setNFeFeedback("Select an NFe provider before emission", false);
            return;
        }

        try {
            NFeEmissionRequest request = new NFeEmissionRequest(
                normalizeText(nfeCustomerNameField.getText()),
                normalizeNullableText(nfeCustomerDocumentField.getText()),
                parsePriceToCents(nfeTotalAmountField.getText()),
                normalizeNullableText(nfeNotesField.getText())
            );

            NFeEmissionResult result = nfeService.emit(request);
            FiscalDocument fiscalDocument = fiscalDocumentService.recordNFeSuccess(request, result);
            nfeXmlOutputArea.setText(result.getXml());
            loadNFeAuditHistory();
            setNFeFeedback(
                "NFe emitted via " + activeNFeProvider + " and audited (#" + fiscalDocument.getId() + ") - access key: " + result.getAccessKey(),
                true
            );
        } catch (IllegalArgumentException exception) {
            setNFeFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to persist fiscal audit for NFe emission", exception);
            setNFeFeedback("NFe emitted but fiscal audit persistence failed", false);
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to emit NFe via provider {}", activeNFeProvider, exception);
            setNFeFeedback("Failed to emit NFe via selected provider", false);
        }
    }

    @FXML
    public void onClearNFeForm() {
        nfeCustomerNameField.clear();
        nfeCustomerDocumentField.clear();
        nfeTotalAmountField.clear();
        nfeNotesField.clear();
        nfeXmlOutputArea.clear();
        setNFeFeedback("NFe form cleared", true);
    }

    @FXML
    public void onRefreshNFeAudit() {
        loadNFeAuditHistory();
    }

    @FXML
    public void onValidateRealNFeSetup() {
        try {
            NFeProviderSetupRequest request = new NFeProviderSetupRequest(
                nfeProviderComboBox == null ? null : normalizeText(nfeProviderComboBox.getValue()),
                normalizeText(nfeRealEnvironmentField.getText()),
                normalizeText(nfeRealCertificatePathField.getText()),
                normalizeText(nfeRealCertificatePasswordField.getText())
            );

            String summary = nfeProviderConfigurationService.validateAndBuildSummary(request, nfeMockEnabled);
            nfeRealPreparationNotesArea.setText(summary);
            setRealNFeFeedback("EPIC 7.2 setup validated. Provider flow is ready for concrete adapter wiring.");
        } catch (IllegalArgumentException exception) {
            setRealNFeFeedback(exception.getMessage());
        }
    }

    @FXML
    public void onApplyNFeProvider() {
        String providerValue = nfeProviderComboBox == null ? null : normalizeText(nfeProviderComboBox.getValue());
        if (providerValue == null || providerValue.isEmpty()) {
            setRealNFeFeedback("Select a provider before applying");
            return;
        }

        try {
            NFeProvider resolvedProvider = NFeServiceFactory.resolveProvider(providerValue, nfeMockEnabled);
            activeNFeProvider = resolvedProvider;
            nfeService = NFeServiceFactory.create(resolvedProvider);
            setRealNFeFeedback("Provider activated: " + activeNFeProvider);
        } catch (IllegalArgumentException exception) {
            setRealNFeFeedback(exception.getMessage());
        }
    }

    @FXML
    public void onClearRealNFeSetup() {
        if (nfeProviderComboBox != null && activeNFeProvider != null) {
            nfeProviderComboBox.setValue(activeNFeProvider.name());
        }
        nfeRealEnvironmentField.clear();
        nfeRealCertificatePathField.clear();
        nfeRealCertificatePasswordField.clear();
        nfeRealPreparationNotesArea.clear();
        setRealNFeFeedback("EPIC 7 setup cleared");
    }

    private void configureMenuTable() {
        idColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<Long>(cell.getValue().getId()));
        nameColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getName()));
        priceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatCents(cell.getValue().getPriceCents())));
        activeColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().isActive() ? "Yes" : "No"));
    }

    private void configureNFeTabs() {
        if (nfeMockTab != null) {
            nfeMockTab.setDisable(!nfeMockEnabled);
            if (!nfeMockEnabled) {
                nfeMockTab.setText("NFe Mock (disabled by feature flag)");
            }
        }

        if (nfeRealFeedbackLabel != null) {
            if (nfeMockEnabled) {
                nfeRealFeedbackLabel.setText("Use this tab to prepare EPIC 7. Active provider: " + activeNFeProvider + ".");
            } else {
                nfeRealFeedbackLabel.setText("Legacy mock mode is disabled. Active provider: " + activeNFeProvider + ".");
            }
        }

        if (!nfeMockEnabled && contentTabPane != null && nfeRealTab != null) {
            contentTabPane.getSelectionModel().select(nfeRealTab);
        }
    }

    private void initializeNFeProviderSelection() {
        activeNFeProvider = NFeServiceFactory.resolveDefaultProvider(nfeMockEnabled);
        nfeService = NFeServiceFactory.create(activeNFeProvider);

        if (nfeProviderComboBox != null) {
            ObservableList<String> providers = FXCollections.observableArrayList();
            if (nfeMockEnabled) {
                providers.add(NFeProvider.MOCK.name());
            }
            providers.add(NFeProvider.REAL_PROVIDER.name());
            nfeProviderComboBox.setItems(providers);
            nfeProviderComboBox.setValue(activeNFeProvider.name());
        }
    }

    private void configureCustomerTable() {
        customerIdColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<Long>(cell.getValue().getId()));
        customerNameColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getName()));
        customerDocumentColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(valueOrDash(cell.getValue().getDocument())));
        customerPhoneColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(valueOrDash(cell.getValue().getPhone())));
        customerEmailColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(valueOrDash(cell.getValue().getEmail())));
    }

    private void configureOrderTables() {
        orderIdColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<Long>(cell.getValue().getId()));
        orderCustomerColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(valueOrDash(cell.getValue().getCustomerName())));
        orderStatusColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(valueOrDash(cell.getValue().getStatus())));
        orderTotalColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatCents(cell.getValue().getTotalCents())));
        orderIssuedAtColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(valueOrDash(cell.getValue().getIssuedAt())));

        pendingOrderItemIdColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<Long>(cell.getValue().getId()));
        pendingOrderMenuItemColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(valueOrDash(cell.getValue().getMenuItemName())));
        pendingOrderQuantityColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getQuantity())));
        pendingOrderUnitPriceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatCents(cell.getValue().getUnitPriceCents())));
        pendingOrderTotalColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatCents(cell.getValue().getTotalPriceCents())));
    }

    private void loadMenuItems() {
        try {
            List<MenuItem> items = menuItemService.findAll();
            menuItems.setAll(items);
            refreshOrderSelections();
            updateSubtitle();
        } catch (SQLException exception) {
            LOGGER.error("Failed to load menu items", exception);
            setMenuFeedback("Failed to load menu items", false);
            refreshOrderSelections();
            updateSubtitle();
        }
    }

    private void loadCustomers() {
        try {
            List<Customer> loadedCustomers = customerService.findAll();
            allCustomers.setAll(loadedCustomers);
            orderCustomers.setAll(loadedCustomers);
            applyCustomerFilter();
            refreshOrderSelections();
        } catch (SQLException exception) {
            LOGGER.error("Failed to load customers", exception);
            setCustomerFeedback("Failed to load customers", false);
            allCustomers.clear();
            orderCustomers.clear();
            customers.clear();
            updateCustomerFilterSummary();
            refreshOrderSelections();
            updateSubtitle();
        }
    }

    private void loadOrders() {
        try {
            List<Order> recentOrders = orderService.findRecent(10);
            orders.setAll(recentOrders);
            updateOrderSummary();
        } catch (SQLException exception) {
            LOGGER.error("Failed to load orders", exception);
            setOrderFeedback("Failed to load orders", false);
            orders.clear();
            updateOrderSummary();
        }
    }

    private void updateSubtitle() {
        int activeItems = 0;
        for (MenuItem item : menuItems) {
            if (item.isActive()) {
                activeItems++;
            }
        }

        String customerScope = customers.size() == allCustomers.size()
            ? String.valueOf(allCustomers.size())
            : customers.size() + " of " + allCustomers.size();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        subtitleLabel.setText("EPIC 7 kickoff - customers: " + customerScope + " - active menu items: " + activeItems + " of " + menuItems.size() + " - " + now);
    }

    private void loadNFeAuditHistory() {
        try {
            List<FiscalDocument> recentDocuments = fiscalDocumentService.findRecent(8);
            if (recentDocuments.isEmpty()) {
                nfeAuditHistoryArea.setText("No fiscal documents emitted yet.");
                return;
            }

            StringBuilder historyBuilder = new StringBuilder();
            for (FiscalDocument fiscalDocument : recentDocuments) {
                historyBuilder
                    .append("#")
                    .append(fiscalDocument.getId())
                    .append(" | ")
                    .append(valueOrDash(fiscalDocument.getCreatedAt()))
                    .append(" | ")
                    .append(valueOrDash(fiscalDocument.getStatus()))
                    .append(" | ")
                    .append(valueOrDash(fiscalDocument.getCustomerName()))
                    .append(" | Total: ")
                    .append(formatCents(fiscalDocument.getTotalCents()))
                    .append(" | Key: ")
                    .append(valueOrDash(fiscalDocument.getAccessKey()))
                    .append("\n");
            }

            nfeAuditHistoryArea.setText(historyBuilder.toString().trim());
        } catch (SQLException exception) {
            LOGGER.error("Failed to load NFe audit history", exception);
            nfeAuditHistoryArea.setText("Failed to load fiscal audit history.");
        }
    }

    private void setRealNFeFeedback(String message) {
        if (nfeRealFeedbackLabel != null) {
            nfeRealFeedbackLabel.setText(message);
        }
    }

    private void populateForm(MenuItem item) {
        if (item == null) {
            return;
        }

        nameField.setText(item.getName());
        descriptionField.setText(item.getDescription() == null ? "" : item.getDescription());
        priceField.setText(formatCents(item.getPriceCents()));
        activeCheckBox.setSelected(item.isActive());
    }

    private void clearForm() {
        menuTable.getSelectionModel().clearSelection();
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        activeCheckBox.setSelected(true);
    }

    private void populateCustomerForm(Customer customer) {
        if (customer == null) {
            return;
        }

        customerNameField.setText(customer.getName());
        customerDocumentField.setText(valueOrEmpty(customer.getDocument()));
        customerPhoneField.setText(valueOrEmpty(customer.getPhone()));
        customerEmailField.setText(valueOrEmpty(customer.getEmail()));

        if (nfeCustomerNameField.getText() == null || nfeCustomerNameField.getText().trim().isEmpty()) {
            nfeCustomerNameField.setText(customer.getName());
        }
        if (nfeCustomerDocumentField.getText() == null || nfeCustomerDocumentField.getText().trim().isEmpty()) {
            nfeCustomerDocumentField.setText(valueOrEmpty(customer.getDocument()));
        }
    }

    private void clearCustomerForm() {
        customerTable.getSelectionModel().clearSelection();
        customerNameField.clear();
        customerDocumentField.clear();
        customerPhoneField.clear();
        customerEmailField.clear();
    }

    private void applyCustomerFilter() {
        String query = normalizeText(customerSearchField.getText());
        if (query == null || query.isEmpty()) {
            customers.setAll(allCustomers);
        } else {
            String normalizedQuery = query.toLowerCase();
            customers.clear();
            for (Customer customer : allCustomers) {
                if (matchesCustomerQuery(customer, normalizedQuery)) {
                    customers.add(customer);
                }
            }
        }

        updateCustomerFilterSummary();
        updateSubtitle();
    }

    private boolean matchesCustomerQuery(Customer customer, String query) {
        return containsIgnoreCase(customer.getName(), query)
            || containsIgnoreCase(customer.getDocument(), query)
            || containsIgnoreCase(customer.getPhone(), query)
            || containsIgnoreCase(customer.getEmail(), query);
    }

    private boolean containsIgnoreCase(String value, String query) {
        if (value == null || query == null) {
            return false;
        }
        return value.toLowerCase().contains(query);
    }

    private void updateCustomerFilterSummary() {
        customerFilterSummaryLabel.setText("Showing " + customers.size() + " of " + allCustomers.size() + " customers");
    }

    private void refreshOrderSelections() {
        orderMenuItems.clear();
        for (MenuItem menuItem : menuItems) {
            if (menuItem.isActive()) {
                orderMenuItems.add(menuItem);
            }
        }

        if (orderMenuItemComboBox != null) {
            orderMenuItemComboBox.setItems(orderMenuItems);
        }

        if (orderCustomerComboBox != null) {
            orderCustomerComboBox.setItems(orderCustomers);
        }
    }

    private void updateOrderSummary() {
        if (orderSummaryLabel == null) {
            return;
        }

        int pendingTotal = 0;
        for (OrderItem orderItem : pendingOrderItems) {
            pendingTotal += orderItem.getTotalPriceCents();
        }

        orderSummaryLabel.setText("Pending items: " + pendingOrderItems.size() + " | Total: " + formatCents(pendingTotal));
    }

    private void updatePendingOrderSummary() {
        updateOrderSummary();
    }

    private int parsePositiveQuantity(String rawQuantity) {
        String normalized = normalizeText(rawQuantity);
        if (normalized == null || normalized.isEmpty()) {
            throw new IllegalArgumentException("Quantity is required");
        }

        try {
            int quantity = Integer.parseInt(normalized);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
            return quantity;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Quantity format is invalid");
        }
    }

    private int parsePriceToCents(String rawPrice) {
        String normalized = normalizeText(rawPrice);
        if (normalized == null || normalized.isEmpty()) {
            throw new IllegalArgumentException("Price is required");
        }

        try {
            BigDecimal value = new BigDecimal(normalized.replace(',', '.'));
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }

            return value.movePointRight(2).setScale(0, RoundingMode.HALF_UP).intValueExact();
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Price format is invalid");
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("Price value is out of range");
        }
    }

    private String formatCents(int cents) {
        return BigDecimal.valueOf(cents, 2).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private String normalizeNullableText(String value) {
        String trimmed = normalizeText(value);
        if (trimmed == null || trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }

    private String valueOrDash(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }
        return value;
    }

    private String valueOrEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    private void setMenuFeedback(String message, boolean success) {
        menuFeedbackLabel.setText(message);
        if (success) {
            menuFeedbackLabel.setStyle("-fx-text-fill: #2f6b3f;");
        } else {
            menuFeedbackLabel.setStyle("-fx-text-fill: #8f1d21;");
        }
    }

    private void setCustomerFeedback(String message, boolean success) {
        customerFeedbackLabel.setText(message);
        if (success) {
            customerFeedbackLabel.setStyle("-fx-text-fill: #2f6b3f;");
        } else {
            customerFeedbackLabel.setStyle("-fx-text-fill: #8f1d21;");
        }
    }

    private void setOrderFeedback(String message, boolean success) {
        orderFeedbackLabel.setText(message);
        if (success) {
            orderFeedbackLabel.setStyle("-fx-text-fill: #2f6b3f;");
        } else {
            orderFeedbackLabel.setStyle("-fx-text-fill: #8f1d21;");
        }
    }

    private void setNFeFeedback(String message, boolean success) {
        nfeFeedbackLabel.setText(message);
        if (success) {
            nfeFeedbackLabel.setStyle("-fx-text-fill: #2f6b3f;");
        } else {
            nfeFeedbackLabel.setStyle("-fx-text-fill: #8f1d21;");
        }
    }
}
