package com.bibliotheque.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import  com.bibliotheque.app.controller.EmpruntFormController ;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.IOException;

public class EmpruntRetourController {

    @FXML
    private Button btnEmprunter;

    @FXML
    private Button btnRetourner;

    @FXML
    private VBox container;

    @FXML
    private void initialize() {
         
        chargerFormulaire("EmpruntForm.fxml");
        btnEmprunter.setOnMousePressed(e -> btnEmprunter.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 400px"));
        btnEmprunter.setOnMouseReleased(e -> btnEmprunter.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 400px"));
        btnRetourner.setOnMousePressed(e -> btnRetourner.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 400px"));
        btnRetourner.setOnMouseReleased(e -> btnRetourner.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 400px"));
        // Charger l'emprunt par défaut ou rien
    }

    @FXML
    private void handleEmprunter() {
        chargerFormulaire("EmpruntForm.fxml");
    }

    @FXML
    private void handleRetourner() {
        chargerFormulaire("RetourForm.fxml");
    }

    private void chargerFormulaire(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlPath));
            VBox formulaire = loader.load();

            // Donner accès au contrôleur parent si besoin
            Object controller = loader.getController();
            if (controller instanceof EmpruntFormController) {
                ((EmpruntFormController) controller).setParentController(this);
            } else if (controller instanceof RetourFormController) {
                ((RetourFormController) controller).setParentController(this);
            }

            container.getChildren().setAll(formulaire);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode à appeler pour rafraîchir les données (si nécessaire)
    public void rafraichirTableau() {
        System.out.println("Actualisation des données...");
        // Tu peux y intégrer un rafraîchissement de tableau principal
    }
}
