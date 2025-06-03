package com.bibliotheque.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label livresDispoLabel;

    @FXML
    private Label utilisateursLabel;

    @FXML
    private Label empruntsLabel;
    

    public void initialize() {
        // Exemple de valeurs dynamiques (à remplacer par des requêtes à ta base de données)
        livresDispoLabel.setText("120");
        utilisateursLabel.setText("45");
        empruntsLabel.setText("30");

        
    }
}
