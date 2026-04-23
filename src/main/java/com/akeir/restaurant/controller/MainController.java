package com.akeir.restaurant.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label subtitleLabel;

    @FXML
    public void initialize() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        subtitleLabel.setText("EPIC 1 bootstrap ready - " + now);
    }
}
