package com.bibliotheque.app.controller;

import com.bibliotheque.app.dao.UtilisateursDao;
import com.bibliotheque.app.model.UtilisateursModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.transformation.FilteredList;



import java.sql.SQLException;
import java.util.List;

public class UtilisateursController {

    @FXML private TableView<UtilisateursModel> tableUtilisateur;
    @FXML private TableColumn<UtilisateursModel, String> colNom;
    @FXML private TableColumn<UtilisateursModel, String> colPrenom;
    @FXML private TableColumn<UtilisateursModel, String> colEmail;
    @FXML private TableColumn<UtilisateursModel, String> colNumTelephone;
    @FXML private TableColumn<UtilisateursModel, String> colAdresse;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtNumTelephone;
    @FXML private TextField txtAdresse;
    @FXML private Button btnAjouter;
    @FXML private Button btnSupprimer;
    @FXML private Button btnModifier;
    @FXML private Button btnRefresh;

    @FXML
    private TextField champRecherche;
    private ObservableList<UtilisateursModel> utilisateursList;
    private FilteredList<UtilisateursModel> filteredList;

    


    private UtilisateursDao utilisateursDao = new UtilisateursDao();
    private ObservableList<UtilisateursModel> utilisateursData = FXCollections.observableArrayList();

        @FXML
        private void filtrerUtilisateurs(String filtre) {
            filteredList.setPredicate(utilisateur -> {
                if (filtre == null || filtre.isEmpty()) {
                    return true;
                }
                String filtreLower = filtre.toLowerCase();
                return utilisateur.getNom().toLowerCase().contains(filtreLower) ||
                    utilisateur.getPrenom().toLowerCase().contains(filtreLower) ||
                    utilisateur.getEmail().toLowerCase().contains(filtreLower) ||
                    utilisateur.getNumTelephone().toLowerCase().contains(filtreLower) ||
                    utilisateur.getAdresse().toLowerCase().contains(filtreLower);
            });
        }

    @FXML
    public void initialize() {

        btnAjouter.setOnMousePressed(e -> btnAjouter.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnAjouter.setOnMouseReleased(e -> btnAjouter.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnSupprimer.setOnMousePressed(e -> btnSupprimer.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnSupprimer.setOnMouseReleased(e -> btnSupprimer.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnModifier.setOnMousePressed(e -> btnModifier.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnModifier.setOnMouseReleased(e -> btnModifier.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnRefresh.setOnMousePressed(e -> btnRefresh.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 90px"));
        btnRefresh.setOnMouseReleased(e -> btnRefresh.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 90px"));



                tableUtilisateur.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // double clic
                UtilisateursModel utilisateurSelectionne = tableUtilisateur.getSelectionModel().getSelectedItem();
                if (utilisateurSelectionne != null) {
                    // Remplir les champs du formulaire
                    txtNom.setText(utilisateurSelectionne.getNom());
                    txtPrenom.setText(utilisateurSelectionne.getPrenom());
                    txtEmail.setText(utilisateurSelectionne.getEmail());
                    txtNumTelephone.setText(utilisateurSelectionne.getNumTelephone());
                    txtAdresse.setText(utilisateurSelectionne.getAdresse());
                }
            }
        });



        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colNumTelephone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumTelephone()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));

        chargerUtilisateurs();

        // Initialisation de la liste filtrée
    filteredList = new FilteredList<>(utilisateursData, p -> true);
    tableUtilisateur.setItems(filteredList);

    // Écoute les changements dans le champ de recherche
    champRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
        filtrerUtilisateurs(newValue);
    });
    }

    private void chargerUtilisateurs() {
        try {
            List<UtilisateursModel> utilisateurs = utilisateursDao.getAllUtilisateurs();
            utilisateursData.setAll(utilisateurs);
            tableUtilisateur.setItems(utilisateursData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    @FXML
    private void handleAjouterUtilisateur(ActionEvent event) {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String numero_telephone = txtNumTelephone.getText();
        String adresse = txtAdresse.getText();

        if (nom.isEmpty() || prenom.isEmpty()) {
            // afficher une alerte d'erreur ici si tu veux
            return;
        }

        UtilisateursModel utilisateur = new UtilisateursModel();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setNumTelephone(numero_telephone);
        utilisateur.setAdresse(adresse);

        try {
            utilisateursDao.ajouterUtilisateur(utilisateur);
            chargerUtilisateurs();
            txtNom.clear();
            txtPrenom.clear();
            txtEmail.clear();
            txtNumTelephone.clear();
            txtAdresse.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL");
        }
    }

        @FXML
    
    private void handleSupprimerUtilisateur() {
    UtilisateursModel utilisateurSelectionne = tableUtilisateur.getSelectionModel().getSelectedItem();

    if (utilisateurSelectionne != null) {
        int idUtilisateur = utilisateurSelectionne.getId(); 

        try {
            utilisateursDao.supprimerUtilisateur(idUtilisateur);

            // Supprimer dynamiquement de la liste observable source
            utilisateursData.remove(utilisateurSelectionne);
            filteredList = new FilteredList<>(utilisateursData, p -> true);
            tableUtilisateur.setItems(filteredList);
            tableUtilisateur.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Suppression réussie");
            alert.setContentText("Vous avez supprimé l'utilisateur avec succès.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Suppression impossible");
            alert.setContentText("Une erreur est survenue lors de la suppression.");
            alert.showAndWait();
        }
    } else {
        System.out.println("Aucun utilisateur sélectionné !");
    }
}



      @FXML
private void handleModifierUtilisateur(ActionEvent event) {
    UtilisateursModel utilisateurSelectionne = tableUtilisateur.getSelectionModel().getSelectedItem();

    if (utilisateurSelectionne != null) {
        utilisateurSelectionne.setNom(txtNom.getText());
        utilisateurSelectionne.setPrenom(txtPrenom.getText());
        utilisateurSelectionne.setEmail(txtEmail.getText());
        utilisateurSelectionne.setNumTelephone(txtNumTelephone.getText());
        utilisateurSelectionne.setAdresse(txtAdresse.getText());

        try {
            utilisateursDao.mettreAJourUtilisateur(utilisateurSelectionne);
            tableUtilisateur.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Modification réussie");
            alert.setContentText("L'utilisateur a bien été modifié.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


                
        @FXML
        private void handleRefresh(ActionEvent event) {
            try {
                List<UtilisateursModel> utilisateurs = utilisateursDao.getAllUtilisateurs();
                ObservableList<UtilisateursModel> observableUtilisateurs = FXCollections.observableArrayList(utilisateurs);
                tableUtilisateur.setItems(observableUtilisateurs);
                tableUtilisateur.refresh();

                        // Initialisation de la liste filtrée
                filteredList = new FilteredList<>(observableUtilisateurs, p -> true);

                // Appliquer la liste filtrée à la table
                tableUtilisateur.setItems(filteredList);
                tableUtilisateur.refresh();

                // Réinitialiser les champs du formulaire
                txtNom.clear();
                txtPrenom.clear();
                txtEmail.clear();
                txtNumTelephone.clear();
                txtAdresse.clear();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors du rafraîchissement");
                alert.setContentText("Impossible de recharger les données.");
                alert.showAndWait();
            }
        }


}
