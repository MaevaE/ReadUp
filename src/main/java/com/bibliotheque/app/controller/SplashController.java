package com.Readup.app.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SplashController {

    @FXML
    private ProgressIndicator progressIndicator;

    public void initialize() {
        // Durée du splash screen (ex: 3 secondes)
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            ouvrirFenetrePrincipale();
            fermerSplash();
        });
        pause.play();
    }

    private void ouvrirFenetrePrincipale() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/livre.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Liste des Livres");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fermerSplash() {
        Stage stage = (Stage) progressIndicator.getScene().getWindow();
        stage.close();
    }
}
