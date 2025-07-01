package com.bibliotheque.app.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.control.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.image.ImageView;



public class MainLayoutController {

    @FXML
    private BorderPane mainPane;
    @FXML
    private StackPane topPane;
    @FXML
    private StackPane contentPane;
        @FXML
    private Button DashboardButton;
     @FXML
    private Button LivreButton;
    @FXML
    private Button ClientButton;
    @FXML
    private Button EmpruntButton;
        @FXML
    private Button AboutButton;
        @FXML
    private Button ParametreButton;
    @FXML
private Label titleLabel;
@FXML
private ImageView welcomeImage;



    @FXML
    //action de click sur les boutons de la home
    public void initialize() {
        setContent("/fxml/Dashboard.fxml");
         titleLabel.setText("Dashboard");
        
                   
        
    // Ajouter un effet au survol (hover)
        DashboardButton.setOnMouseEntered(e -> {DashboardButton.setStyle("-fx-background-color:rgb(24, 60, 122);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        DashboardButton.setOnMouseExited(e -> { DashboardButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        DashboardButton.setOnMousePressed(e -> DashboardButton.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        DashboardButton.setOnMouseReleased(e -> DashboardButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        LivreButton.setOnMouseEntered(e -> {LivreButton.setStyle("-fx-background-color:rgb(24, 60, 122);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        LivreButton.setOnMouseExited(e -> { LivreButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        LivreButton.setOnMousePressed(e -> LivreButton.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        LivreButton.setOnMouseReleased(e -> LivreButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        ClientButton.setOnMousePressed(e -> ClientButton.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        ClientButton.setOnMouseEntered(e -> {ClientButton.setStyle("-fx-background-color:rgb(24, 60, 122);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        ClientButton.setOnMouseExited(e -> { ClientButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        ClientButton.setOnMouseReleased(e -> ClientButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        EmpruntButton.setOnMousePressed(e -> EmpruntButton.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        EmpruntButton.setOnMouseEntered(e -> {EmpruntButton.setStyle("-fx-background-color:rgb(24, 60, 122);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        EmpruntButton.setOnMouseExited(e -> { EmpruntButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        EmpruntButton.setOnMouseReleased(e -> EmpruntButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        AboutButton.setOnMouseEntered(e -> {AboutButton.setStyle("-fx-background-color:rgb(24, 60, 122);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        AboutButton.setOnMouseExited(e -> { AboutButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        AboutButton.setOnMousePressed(e -> AboutButton.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        AboutButton.setOnMouseReleased(e -> AboutButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        ParametreButton.setOnMouseEntered(e -> {ParametreButton.setStyle("-fx-background-color:rgb(24, 60, 122);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        ParametreButton.setOnMouseExited(e -> { ParametreButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);");});
        ParametreButton.setOnMousePressed(e -> ParametreButton.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
        ParametreButton.setOnMouseReleased(e -> ParametreButton.setStyle("-fx-background-color: #3A86FF;-fx-border-radius: 20;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(114, 112, 112, 0.75), 10, 0.5, 0, 2);"));
    }


    // Charge une vue dans contentPane (zone dynamique au centre)
    public void setContent(String fxmlPath) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard() {
        setContent("/fxml/Dashboard.fxml");
         titleLabel.setText("Dashboard");
    }

    @FXML
    private void goToLivres() {
        setContent("/fxml/Livres.fxml");
         titleLabel.setText("Informtion sur les livres");
    }

    @FXML
    private void goToUtilisateurs() {
       setContent("/fxml/Utilisateurs.fxml");
       titleLabel.setText("Informations Clients");
    }

       @FXML
    private void goToAbout() {
       setContent("/fxml/About.fxml");
       titleLabel.setText("A Propos");
    }

        @FXML
    private void goToEmpruntRetour() {
      setContent("/fxml/EmpruntRetour.fxml");
      titleLabel.setText("Emprunter/Retourner");
    }

    
}

