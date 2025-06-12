package com.bibliotheque.app.controller;

import com.bibliotheque.app.dao.EmpruntFormDao;
import com.bibliotheque.app.model.EmpruntFormModel;
import com.bibliotheque.app.model.OptionUtilisateur;
import com.bibliotheque.app.model.OptionLivre;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class EmpruntFormController {
    @FXML private ComboBox<OptionUtilisateur> comboBoxUtilisateur;
    @FXML private ComboBox<OptionLivre> comboBoxLivre;


   
    @FXML private DatePicker datePickerEmprunt;
    @FXML private DatePicker datePickerRetour;
    @FXML private Button btnValiderEmprunt;

    private int idLivreSelectionne;
    private int idUtilisateurSelectionne;

    @FXML
    public void initialize() {
        btnValiderEmprunt.setOnMousePressed(e -> btnValiderEmprunt.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 170px"));
        btnValiderEmprunt.setOnMouseReleased(e -> btnValiderEmprunt.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 170px"));
         chargerUtilisateurs();
         chargerLivres();
    }

    @FXML
private void handleValiderEmprunt() {
    OptionUtilisateur utilisateur = comboBoxUtilisateur.getValue();
    OptionLivre livre = comboBoxLivre.getValue();

    if (utilisateur == null || livre == null) {
        showAlert("Veuillez sélectionner un utilisateur et un livre.");
        return;
    }

    EmpruntFormModel emprunt = new EmpruntFormModel();
    emprunt.setIdUtilisateur(utilisateur.getId());
    emprunt.setIdLivre(livre.getId());
    emprunt.setDateEmprunt(datePickerEmprunt.getValue());
    emprunt.setDateRetourPrevue(datePickerRetour.getValue());

    EmpruntFormDao dao = new EmpruntFormDao();
    dao.enregistrerEmprunt(emprunt);
    clearForm();

}

     private void clearForm() {
    comboBoxUtilisateur.setValue(null);
    comboBoxLivre.setValue(null);
    datePickerEmprunt.setValue(null);
    datePickerRetour.setValue(null); // CORRECTION


}


    private EmpruntRetourController parentController;

public void setParentController(EmpruntRetourController controller) {
    this.parentController = controller;
}

private void chargerUtilisateurs() {
    comboBoxUtilisateur.getItems().clear();
    comboBoxUtilisateur.getItems().addAll(EmpruntFormDao.getUtilisateursAvecId()); // méthode à créer
}

private void chargerLivres() {
    comboBoxLivre.getItems().clear();
    comboBoxLivre.getItems().addAll(EmpruntFormDao.getLivresDisponiblesAvecId()); // méthode à créer
}

private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Attention");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}



}