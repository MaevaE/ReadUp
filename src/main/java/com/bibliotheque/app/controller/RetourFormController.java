package com.bibliotheque.app.controller;

import com.bibliotheque.app.dao.EmpruntFormDao;
import com.bibliotheque.app.dao.RetourFormDao;
import com.bibliotheque.app.model.EmpruntSelection;
import com.bibliotheque.app.model.RetourFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class RetourFormController {

    @FXML
    private ComboBox<EmpruntSelection> comboBoxEmprunt;

    @FXML
    private DatePicker dateRetourEffective;

    @FXML
    private Button btnValiderRetour;

    private EmpruntRetourController parentController;

    public void setParentController(EmpruntRetourController controller) {
        this.parentController = controller;
    }

    @FXML
    private void initialize() {
        btnValiderRetour.setOnMousePressed(e -> btnValiderRetour.setStyle("-fx-background-color:rgb(16, 42, 87);-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 170px"));
        btnValiderRetour.setOnMouseReleased(e -> btnValiderRetour.setStyle("-fx-background-color: #3A86FF;-fx-text-fill: white;-fx-font-size: 14px; -fx-pref-width: 170px"));
        EmpruntFormDao dao = new EmpruntFormDao();
        comboBoxEmprunt.getItems().addAll(dao.getEmpruntsNonRendus());
    }

    @FXML
private void handleValiderRetour() {
    EmpruntSelection selected = comboBoxEmprunt.getSelectionModel().getSelectedItem();

    if (selected == null || dateRetourEffective.getValue() == null) {
        System.err.println("Veuillez sélectionner un emprunt et une date.");
        return;
    }

    RetourFormModel retour = new RetourFormModel();
    retour.setIdEmprunt(selected.getId());
    retour.setDateRetourEffective(dateRetourEffective.getValue());

    RetourFormDao retourDao = new RetourFormDao();
    retourDao.enregistrerRetour(retour);

    // Supprime l'emprunt retourné du ComboBox
    comboBoxEmprunt.getItems().remove(selected);

    // Rafraîchit la table dans le contrôleur parent
    parentController.rafraichirTableau();
    clearForm();
    

}
   private void clearForm() {
    comboBoxEmprunt.setValue(null);
    dateRetourEffective.setValue(null);


}

}
