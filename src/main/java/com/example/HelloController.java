package com.example;
import com.example.DAO.LivreDAO; 
import com.example.model.Livre;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.SQLException;
import java.util.List;

public class HelloController {

   /*  @FXML private TextField titreField;
    @FXML private TextField auteurField;
    @FXML private TextField datePublicationField;
    @FXML private TextField themeField;
    @FXML private TextField categorieField;
    @FXML private TextField statusField;
    @FXML private TextField nombreExemplaireField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField searchField;

    @FXML private TableView<Livre> livreTable;
    @FXML private TableColumn<Livre, String> titreColumn;
    @FXML private TableColumn<Livre, String> auteurColumn;
    @FXML private TableColumn<Livre, String> datePublicationColumn;
    @FXML private TableColumn<Livre, String> themeColumn;
    @FXML private TableColumn<Livre, String> categorieColumn;
    @FXML private TableColumn<Livre, String> statusColumn;

    private ObservableList<Livre> listeLivres = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (titreColumn != null) {
            titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        }
        if (auteurColumn != null) {
            auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        }
        if (datePublicationColumn != null) {
            datePublicationColumn.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        }
        if (themeColumn != null) {
            themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        }
        if (categorieColumn != null) {
            categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        }
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        }

        if (livreTable != null) {
            livreTable.setItems(listeLivres);
            livreTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showLivreDetails(newValue));
        }

        listeLivres.add(new Livre("Cendrillon", "Zidane", "1954-07-29", "Compte", "Roman", "Disponible", 5, "Une grande aventure ."));
        listeLivres.add(new Livre("Tom", "Maeva", "1949-06-08", "Comedie", "Roman", "Emprunté", 2, "Un classique ."));
        listeLivres.add(new Livre("Orgueil et Préjugés", "Jane Austen", "1813-01-28", "Romance", "Roman", "Disponible", 3, "Un roman classique sur la société anglaise du 19ème siècle."));
    }

    @FXML
    private void handleAjouterLivre() {
        if (validateInput()) {
            Livre nouveauLivre = new Livre(
                titreField.getText(),
                auteurField.getText(),
                datePublicationField.getText(),
                themeField.getText(),
                categorieField.getText(),
                statusField.getText(),
                Integer.parseInt(nombreExemplaireField.getText()),
                descriptionArea.getText()
            );

            listeLivres.add(nouveauLivre);
            showAlert(AlertType.INFORMATION, "Succès", "Livre ajouté", "Le livre a été ajouté avec succès.");
            clearFields();
        }
    }

    @FXML
    private void handleSupprimerLivre() {
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();
        if (selectedLivre != null) {
            listeLivres.remove(selectedLivre);
            showAlert(AlertType.INFORMATION, "Succès", "Livre supprimé", "Le livre sélectionné a été supprimé.");
            clearFields();
        } else {
            showAlert(AlertType.WARNING, "Aucune sélection", "Aucun livre sélectionné", "Veuillez sélectionner un livre à supprimer dans la table.");
        }
    }

    @FXML
    private void handleModifierLivre() {
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();
        if (selectedLivre != null) {
            if (validateInput()) {
                selectedLivre.setTitre(titreField.getText());
                selectedLivre.setAuteur(auteurField.getText());
                selectedLivre.setDatePublication(datePublicationField.getText());
                selectedLivre.setTheme(themeField.getText());
                selectedLivre.setCategorie(categorieField.getText());
                selectedLivre.setStatus(statusField.getText());
                selectedLivre.setNombreExemplaire(Integer.parseInt(nombreExemplaireField.getText()));
                selectedLivre.setDescription(descriptionArea.getText());

                livreTable.refresh();
                showAlert(AlertType.INFORMATION, "Succès", "Livre modifié", "Les informations du livre ont été mises à jour.");
            }
        } else {
            showAlert(AlertType.WARNING, "Aucune sélection", "Aucun livre sélectionné", "Veuillez sélectionner un livre à modifier dans la table.");
        }
    }

    @FXML
    private void handleRechercher() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Livre> filteredList = FXCollections.observableArrayList();

        if (searchText.isEmpty()) {
            livreTable.setItems(listeLivres);
            return;
        }

        for (Livre livre : listeLivres) {
            if (livre.getTitre().toLowerCase().contains(searchText) ||
                livre.getAuteur().toLowerCase().contains(searchText) ||
                livre.getTheme().toLowerCase().contains(searchText) ||
                livre.getCategorie().toLowerCase().contains(searchText)) {
                filteredList.add(livre);
            }
        }
        livreTable.setItems(filteredList);
    }

    private void clearFields() {
        titreField.clear();
        auteurField.clear();
        datePublicationField.clear();
        themeField.clear();
        categorieField.clear();
        statusField.clear();
        nombreExemplaireField.clear();
        descriptionArea.clear();
        livreTable.getSelectionModel().clearSelection();
    }

    private void showLivreDetails(Livre livre) {
        if (livre != null) {
            titreField.setText(livre.getTitre());
            auteurField.setText(livre.getAuteur());
            datePublicationField.setText(livre.getDatePublication());
            themeField.setText(livre.getTheme());
            categorieField.setText(livre.getCategorie());
            statusField.setText(livre.getStatus());
            nombreExemplaireField.setText(String.valueOf(livre.getNombreExemplaire()));
            descriptionArea.setText(livre.getDescription());
        } else {
            clearFields();
        }
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (titreField.getText() == null || titreField.getText().isEmpty()) {
            errorMessage += "Le titre est vide !\n";
        }
        if (auteurField.getText() == null || auteurField.getText().isEmpty()) {
            errorMessage += "L'auteur est vide !\n";
        }
        if (nombreExemplaireField.getText() == null || nombreExemplaireField.getText().isEmpty()) {
            errorMessage += "Le nombre d'exemplaires est vide !\n";
        } else {
            try {
                int nbEx = Integer.parseInt(nombreExemplaireField.getText());
                if (nbEx < 0) {
                    errorMessage += "Le nombre d'exemplaires doit être positif !\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Le nombre d'exemplaires doit être un nombre valide !\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(AlertType.ERROR, "Champs invalides", "Veuillez corriger les erreurs", errorMessage);
            return false;
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private void handleDashboard() { showAlert(AlertType.INFORMATION, "Navigation", "Dashboard", "Action: Aller au Dashboard."); }
    @FXML private void handleEmprunterRetourner() { showAlert(AlertType.INFORMATION, "Navigation", "Emprunter/Retourner", "Action: Aller à la gestion des emprunts."); }
    @FXML private void handleInformationsClients() { showAlert(AlertType.INFORMATION, "Navigation", "Informations Clients", "Action: Aller aux informations clients."); }
    @FXML private void handleInformationsLivres() { showAlert(AlertType.INFORMATION, "Navigation", "Informations Livres", "Action: Vous êtes déjà sur cette page."); }
    @FXML private void handleParametres() { showAlert(AlertType.INFORMATION, "Navigation", "Paramètres", "Action: Aller aux paramètres."); }
    @FXML private void handleAPropos() { showAlert(AlertType.INFORMATION, "Navigation", "A propos", "Action: Afficher les informations 'À propos'."); }
*/
}
