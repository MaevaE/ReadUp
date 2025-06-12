package com.bibliotheque.app.controller;

import com.bibliotheque.app.dao.LivresDao;
import com.bibliotheque.app.model.LivresModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.*;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LivresController {

    @FXML private TableView<LivresModel> tableLivre;
    @FXML private TableColumn<LivresModel, String> colTitre;
    @FXML private TableColumn<LivresModel, String> colAuteur;
    @FXML private TableColumn<LivresModel, String> colTheme;
    @FXML private TableColumn<LivresModel, String> colDatePublication;
    @FXML private TableColumn<LivresModel, Integer> colNombreExemplaires;
    @FXML private TextField txtExemplaires;
    @FXML private TableColumn<LivresModel, String> colDisponibilite;

    @FXML private TextField txtTitre;
    @FXML private TextField txtAuteur;
    @FXML private TextField txtTheme;
    @FXML private DatePicker txtDatePublication;
    @FXML private Button btnAjouter;
    @FXML private Button btnSupprimer;
    @FXML private Button btnModifier;
    @FXML private Button btnRefresh;
    @FXML private TextField champRecherche;

    private ObservableList<LivresModel> livresData = FXCollections.observableArrayList();
    private FilteredList<LivresModel> filteredList;
    private LivresDao livresDao = new LivresDao();

    @FXML
    public void initialize() {
        // Style des boutons
        btnAjouter.setOnMousePressed(e -> btnAjouter.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnAjouter.setOnMouseReleased(e -> btnAjouter.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnSupprimer.setOnMousePressed(e -> btnSupprimer.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnSupprimer.setOnMouseReleased(e -> btnSupprimer.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnModifier.setOnMousePressed(e -> btnModifier.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnModifier.setOnMouseReleased(e -> btnModifier.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 80px"));
        btnRefresh.setOnMousePressed(e -> btnRefresh.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 90px"));
        btnRefresh.setOnMouseReleased(e -> btnRefresh.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 90px"));

        // Double clic pour remplir les champs
        tableLivre.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                LivresModel livre = tableLivre.getSelectionModel().getSelectedItem();
                // Dans tableLivre.setOnMouseClicked (remplissage des champs au double clic) :
                if (livre != null) {
                    txtTitre.setText(livre.getTitre());
                    txtAuteur.setText(livre.getAuteur());
                    txtTheme.setText(livre.getTheme());
                    txtDatePublication.setValue(livre.getDatePublication()); // CORRECTION ICI
                    txtExemplaires.setText(String.valueOf(livre.getNombreExemplaires()));

                }

            }
        });

        // Colonnes
        colTitre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitre()));
        colAuteur.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAuteur()));
        colTheme.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTheme()));
        colDatePublication.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDatePublication().toString())
            );
        colDisponibilite.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getDisponibilite())
        );
        colNombreExemplaires.setCellValueFactory(cell ->
            new SimpleIntegerProperty(cell.getValue().getNombreExemplaires()).asObject()
        );




        chargerLivres();

        // Filtrage
        filteredList = new FilteredList<>(livresData, p -> true);
        tableLivre.setItems(filteredList);

        champRecherche.textProperty().addListener((obs, oldVal, newVal) -> filtrerLivres(newVal));
    }

    private void chargerLivres() {
        try {
            List<LivresModel> livres = livresDao.getAllLivres();
            livresData.setAll(livres);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filtrerLivres(String filtre) {
        filteredList.setPredicate(livre -> {
            if (filtre == null || filtre.isEmpty()) return true;
            String filtreLower = filtre.toLowerCase();
            return 
                   livre.getAuteur().toLowerCase().contains(filtreLower) ||
                   livre.getTheme().toLowerCase().contains(filtreLower) ||
                   String.valueOf(livre.getDatePublication().getYear()).contains(filtreLower);
        });
    }

        
    @FXML
    private void handleAjouterLivre(ActionEvent event) {
        LivresModel livre = new LivresModel();
        livre.setTitre(txtTitre.getText());
        livre.setAuteur(txtAuteur.getText());
        livre.setTheme(txtTheme.getText());

        LocalDate date = txtDatePublication.getValue();
        if (date == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une date de publication.");
            return;
        }
        livre.setDatePublication(date);

        int exemplaires;
        try {
            exemplaires = Integer.parseInt(txtExemplaires.getText());
            if (exemplaires < 0) {
                afficherAlerte("Erreur", "Le nombre d'exemplaires ne peut pas être négatif.");
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez entrer un nombre d'exemplaires valide.");
            return;
        }
        livre.setNombreExemplaires(exemplaires);

        try {
            livresDao.ajouterLivre(livre);
            chargerLivres();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



   @FXML
private void handleModifierLivre(ActionEvent event) {
    LivresModel livre = tableLivre.getSelectionModel().getSelectedItem();
    if (livre != null) {
        livre.setTitre(txtTitre.getText());
        livre.setAuteur(txtAuteur.getText());
        livre.setTheme(txtTheme.getText());

        LocalDate date = txtDatePublication.getValue();
        if (date == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une date de publication.");
            return;
        }
        livre.setDatePublication(date);

        int exemplaires;
        try {
            exemplaires = Integer.parseInt(txtExemplaires.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez saisir un nombre d'exemplaires valide.");
            return;
        }
        livre.setNombreExemplaires(exemplaires);

        try {
            livresDao.mettreAJourLivre(livre);
            tableLivre.refresh();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



    @FXML
    private void handleSupprimerLivre(ActionEvent event) {
        LivresModel livre = tableLivre.getSelectionModel().getSelectedItem();
        if (livre != null) {
            try {
                livresDao.supprimerLivre(livre.getId());
                livresData.remove(livre);
                tableLivre.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        chargerLivres();
        filteredList = new FilteredList<>(livresData, p -> true);
        tableLivre.setItems(filteredList);
        clearForm();
    }

   private void clearForm() {
    txtTitre.clear();
    txtAuteur.clear();
    txtTheme.clear();
    txtDatePublication.setValue(null); // CORRECTION
    txtExemplaires.clear();

}


    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
