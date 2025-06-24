package com.bibliotheque.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private StackPane contentPane;

    public void afficherLivres() {
        chargerVue("/fxml/livres.fxml");
    }

    public void afficherEmprunts() {
        chargerVue("/fxml/emprunts.fxml");
    }

    public void afficherRetours() {
        chargerVue("/fxml/retours.fxml");
    }

    @FXML
    public void initialize() {
          
        chargerVue("/fxml/livres.fxml");
    
    }

    private void chargerVue(String cheminFxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(cheminFxml));
            contentPane.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

