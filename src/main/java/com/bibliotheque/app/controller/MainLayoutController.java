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
    //action de click sur les boutons de la home
    public void initialize() {


                   
        
        DashboardButton.setOnMousePressed(e -> DashboardButton.setStyle("-fx-background-color:rgb(16, 42, 87);"));
        DashboardButton.setOnMouseReleased(e -> DashboardButton.setStyle("-fx-background-color: #3A86FF;"));
        LivreButton.setOnMousePressed(e -> LivreButton.setStyle("-fx-background-color:rgb(16, 42, 87);"));
        LivreButton.setOnMouseReleased(e -> LivreButton.setStyle("-fx-background-color: #3A86FF;"));
        ClientButton.setOnMousePressed(e -> ClientButton.setStyle("-fx-background-color:rgb(16, 42, 87);"));
        ClientButton.setOnMouseReleased(e -> ClientButton.setStyle("-fx-background-color: #3A86FF;"));
        EmpruntButton.setOnMousePressed(e -> EmpruntButton.setStyle("-fx-background-color:rgb(16, 42, 87);"));
        EmpruntButton.setOnMouseReleased(e -> EmpruntButton.setStyle("-fx-background-color: #3A86FF;"));
        AboutButton.setOnMousePressed(e -> AboutButton.setStyle("-fx-background-color:rgb(16, 42, 87);"));
        AboutButton.setOnMouseReleased(e -> AboutButton.setStyle("-fx-background-color: #3A86FF;"));
        ParametreButton.setOnMousePressed(e -> ParametreButton.setStyle("-fx-background-color:rgb(16, 42, 87);"));
        ParametreButton.setOnMouseReleased(e -> ParametreButton.setStyle("-fx-background-color: #3A86FF;"));
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
        //setContent("/fxml/Livres.fxml");
        //titleLabel.setText("Emprunter / Retourner");
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

    
}

