package com.akeir.restaurant.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akeir.restaurant.model.MenuItem;
import com.akeir.restaurant.service.MenuItemService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final MenuItemService menuItemService = new MenuItemService();
    private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

    @FXML
    private Label subtitleLabel;

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
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private Label feedbackLabel;

    @FXML
    public void initialize() {
        configureTable();
        menuTable.setItems(menuItems);
        menuTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateForm(newValue));

        activeCheckBox.setSelected(true);
        loadMenuItems();
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

            setFeedback("Menu item created: " + createdItem.getName(), true);
            clearForm();
            loadMenuItems();
        } catch (IllegalArgumentException exception) {
            setFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to add menu item", exception);
            setFeedback("Failed to create menu item", false);
        }
    }

    @FXML
    public void onUpdateItem() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            setFeedback("Select an item before updating", false);
            return;
        }

        try {
            selectedItem.setName(normalizeText(nameField.getText()));
            selectedItem.setDescription(normalizeNullableText(descriptionField.getText()));
            selectedItem.setPriceCents(parsePriceToCents(priceField.getText()));
            selectedItem.setActive(activeCheckBox.isSelected());

            boolean updated = menuItemService.update(selectedItem);
            if (updated) {
                setFeedback("Menu item updated", true);
                clearForm();
                loadMenuItems();
            } else {
                setFeedback("Menu item not found for update", false);
            }
        } catch (IllegalArgumentException exception) {
            setFeedback(exception.getMessage(), false);
        } catch (SQLException exception) {
            LOGGER.error("Failed to update menu item", exception);
            setFeedback("Failed to update menu item", false);
        }
    }

    @FXML
    public void onDeleteItem() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getId() == null) {
            setFeedback("Select an item before deleting", false);
            return;
        }

        try {
            boolean deleted = menuItemService.deleteById(selectedItem.getId().longValue());
            if (deleted) {
                setFeedback("Menu item deleted", true);
                clearForm();
                loadMenuItems();
            } else {
                setFeedback("Menu item not found for deletion", false);
            }
        } catch (SQLException exception) {
            LOGGER.error("Failed to delete menu item", exception);
            setFeedback("Failed to delete menu item", false);
        }
    }

    @FXML
    public void onClearForm() {
        clearForm();
        setFeedback("Form cleared", true);
    }

    private void configureTable() {
        idColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<Long>(cell.getValue().getId()));
        nameColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getName()));
        priceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatCents(cell.getValue().getPriceCents())));
        activeColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().isActive() ? "Yes" : "No"));
    }

    private void loadMenuItems() {
        try {
            List<MenuItem> items = menuItemService.findAll();
            menuItems.setAll(items);
            updateSubtitle(items);
        } catch (SQLException exception) {
            LOGGER.error("Failed to load menu items", exception);
            setFeedback("Failed to load menu items", false);
            updateSubtitle(menuItems);
        }
    }

    private void updateSubtitle(List<MenuItem> items) {
        int activeItems = 0;
        for (MenuItem item : items) {
            if (item.isActive()) {
                activeItems++;
            }
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        subtitleLabel.setText("EPIC 3 menu management - active items: " + activeItems + " of " + items.size() + " - " + now);
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

    private void setFeedback(String message, boolean success) {
        feedbackLabel.setText(message);
        if (success) {
            feedbackLabel.setStyle("-fx-text-fill: #2f6b3f;");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: #8f1d21;");
        }
    }
}
