package com.example.controller;

import com.example.DAO.LivreDAO;
import com.example.model.Livre;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.concurrent.Task;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// NOUVELLES IMPORTATIONS NÉCESSAIRES POUR LE FILTRAGE
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.util.function.Predicate;


public class LivreController {

    @FXML private TextField titreField;
    @FXML private TextField auteurField;
    @FXML private TextField datePublicationField;
    @FXML private TextField themeField;
    @FXML private TextField categorieField;
    @FXML private TextField statusField;
    @FXML private TextField nombreExemplaireField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    @FXML private TableView<Livre> livreTable;
    @FXML private TableColumn<Livre, Integer> idColumn;
    @FXML private TableColumn<Livre, String> titreColumn;
    @FXML private TableColumn<Livre, String> auteurColumn;
    @FXML private TableColumn<Livre, String> datePublicationColumn;
    @FXML private TableColumn<Livre, String> themeColumn;
    @FXML private TableColumn<Livre, String> categorieColumn;
    @FXML private TableColumn<Livre, String> statusColumn;
        
    private ObservableList<Livre> masterLivreList = FXCollections.observableArrayList();
    private FilteredList<Livre> filteredLivreList; 
    private Livre currentSelectedLivreForEdit; 

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        datePublicationColumn.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        filteredLivreList = new FilteredList<>(masterLivreList, p -> true); 

        SortedList<Livre> sortedLivreList = new SortedList<>(filteredLivreList);

        sortedLivreList.comparatorProperty().bind(livreTable.comparatorProperty());

        livreTable.setItems(sortedLivreList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterLivres(newValue);
        });
        
        loadLivresIntoUI();
    }

    @FXML
    protected void onRefreshButtonClick() {
        messageLabel.setText("Rafraîchissement des livres..."); 
        loadLivresIntoUI(); 
    }

    @FXML
    protected void onAddLivreButtonClick() {
        System.out.println("DEBUG: onAddLivreButtonClick appelé. currentSelectedLivreForEdit: " + currentSelectedLivreForEdit);
        if (currentSelectedLivreForEdit != null) {
            System.out.println("  --> DEBUG: Mode modification pour le livre avec ID: " + currentSelectedLivreForEdit.getId());
        } else {
            System.out.println("  --> DEBUG: Mode ajout de nouveau livre.");
        }

        String titre = titreField.getText().trim();
        String auteur = auteurField.getText().trim();
        String datePublication = datePublicationField.getText().trim();
        String theme = themeField.getText().trim();
        String categorie = categorieField.getText().trim();
        String status = statusField.getText().trim();
        String description = descriptionArea.getText().trim();

        final int finalNombreExemplaire;
        try {
            finalNombreExemplaire = Integer.parseInt(nombreExemplaireField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur de saisie", "Le nombre d'exemplaires doit être un nombre valide.");
            return;
        }

        if (titre.isEmpty() || auteur.isEmpty() || datePublication.isEmpty()) {
            showAlert(AlertType.WARNING, "Saisie incomplète", "Veuillez remplir les champs obligatoires : Titre, Auteur, Date de publication.");
            return;
        }

        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                LivreDAO livreDAO = new LivreDAO(); 

                if (currentSelectedLivreForEdit != null) {
                    currentSelectedLivreForEdit.setTitre(titre);
                    currentSelectedLivreForEdit.setAuteur(auteur);
                    currentSelectedLivreForEdit.setDatePublication(datePublication);
                    currentSelectedLivreForEdit.setTheme(theme);
                    currentSelectedLivreForEdit.setCategorie(categorie);
                    currentSelectedLivreForEdit.setStatus(status);
                    currentSelectedLivreForEdit.setNombreExemplaire(finalNombreExemplaire); 
                    currentSelectedLivreForEdit.setDescription(description);

                    return livreDAO.updateLivre(currentSelectedLivreForEdit); 
                } else {
                    Livre newLivre = new Livre(
                        titre, auteur, datePublication, theme, categorie,
                        status, finalNombreExemplaire, description 
                    );
                    return LivreDAO.addLivre(newLivre); 
                }
            }
        };

        saveTask.setOnSucceeded(event -> {
            boolean success = saveTask.getValue();
            if (success) {
                if (currentSelectedLivreForEdit != null) { 
                    showAlert(AlertType.INFORMATION, "Succès", "Livre '" + titre + "' modifié avec succès !");
                } else {
                    showAlert(AlertType.INFORMATION, "Succès", "Livre '" + titre + "' ajouté avec succès !");
                }
                clearInputFields(); 
                loadLivresIntoUI(); 
                currentSelectedLivreForEdit = null; 
            } else {
                showAlert(AlertType.ERROR, "Échec", "Erreur lors de l'opération sur le livre. La base de données pourrait être inaccessible.");
            }
        });

        saveTask.setOnFailed(event -> {
            showAlert(AlertType.ERROR, "Erreur BD", "Impossible d'effectuer l'opération : " + saveTask.getException().getMessage());
            saveTask.getException().printStackTrace();
            messageLabel.setText("Erreur lors de l'opération sur le livre.");
        });

        new Thread(saveTask).start();
    }

    private void loadLivresIntoUI() {
        messageLabel.setText("Chargement des livres..."); 
        Task<List<Livre>> loadTask = new Task<>() {
            @Override
            protected List<Livre> call() throws Exception {
                return LivreDAO.getAllLivres();
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<Livre> livres = loadTask.getValue();
            masterLivreList.setAll(livres); 

            filterLivres(searchField.getText()); // Applique le filtre actuel si le champ de recherche n'est pas vide

            if (livres.isEmpty()) {
                messageLabel.setText("Aucun livre trouvé dans la base de données.");
            } else {
                messageLabel.setText("Livres chargés avec succès : " + livres.size() + " éléments.");
            }
        });

        loadTask.setOnFailed(event -> {
            showAlert(AlertType.ERROR, "Erreur de chargement", "Impossible de charger les livres : " + loadTask.getException().getMessage());
            loadTask.getException().printStackTrace();
            messageLabel.setText("Erreur lors du chargement des livres."); 
        });

        new Thread(loadTask).start();
    }

    /**
     * Filtre la liste des livres affichée dans le tableau
     * en fonction du texte de recherche.
     * La recherche est effectuée sur le titre, l'auteur, le thème et la catégorie.
     */
    private void filterLivres(String searchText) {
        String lowerCaseFilter = searchText.toLowerCase();

        filteredLivreList.setPredicate(livre -> {
            // Si le texte de recherche est vide, afficher tous les livres.
            if (lowerCaseFilter == null || lowerCaseFilter.isEmpty()) {
                return true;
            }

            // Compare le titre du livre avec le texte de recherche
            if (livre.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                return true; 
            } 
            // Compare l'auteur du livre avec le texte de recherche
            else if (livre.getAuteur().toLowerCase().contains(lowerCaseFilter)) {
                return true; 
            }
            // NOUVEAU : Comparaison avec le thème du livre
            else if (livre.getTheme() != null && livre.getTheme().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            // NOUVEAU : Comparaison avec la catégorie du livre
            else if (livre.getCategorie() != null && livre.getCategorie().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }

            return false; // Aucune correspondance trouvée
        });
    }

    private void clearInputFields() {
        titreField.clear();
        auteurField.clear();
        datePublicationField.clear();
        themeField.clear();
        categorieField.clear();
        statusField.clear();
        nombreExemplaireField.clear();
        descriptionArea.clear();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title); 
        alert.setHeaderText(null); 
        alert.setContentText(message);
        alert.showAndWait(); 
    }

    @FXML
    private void handleDashboard() {
        System.out.println("Clic sur Dashboard");
    }

    @FXML
    private void handleEmprunterRetourner() {
        System.out.println("Clic sur Emprunter/Retourner");
    }

    @FXML
    private void handleInformationsClients() {
        System.out.println("Clic sur Informations Clients");
    }

    @FXML
    private void handleInformationsLivres() {
        System.out.println("Clic sur Informations Livres");
        loadLivresIntoUI();
    }

    @FXML
    private void handleParametres() {
        System.out.println("Clic sur Paramètres");
    }

    @FXML
    private void handleAPropos() {
        System.out.println("Clic sur À Propos");
    }

    @FXML
    private void handleModifierLivre() {
        System.out.println("Clic sur Modifier Livre (depuis la table)");
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();

        if (selectedLivre == null) {
            showAlert(AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un livre à modifier dans le tableau.");
            return;
        }

        this.currentSelectedLivreForEdit = selectedLivre;

        titreField.setText(selectedLivre.getTitre());
        auteurField.setText(selectedLivre.getAuteur());
        datePublicationField.setText(selectedLivre.getDatePublication());
        themeField.setText(selectedLivre.getTheme());
        categorieField.setText(selectedLivre.getCategorie());
        statusField.setText(selectedLivre.getStatus());
        nombreExemplaireField.setText(String.valueOf(selectedLivre.getNombreExemplaire()));
        descriptionArea.setText(selectedLivre.getDescription());

        showAlert(AlertType.INFORMATION, "Modification", "Les informations du livre ont été chargées dans le formulaire. Modifiez les champs nécessaires et cliquez sur le bouton 'Ajouter' pour valider les modifications.");
    }

    @FXML
    private void handleSupprimerLivre() {
        System.out.println("Clic sur Supprimer Livre");
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();

        if (selectedLivre == null) {
            showAlert(AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un livre à supprimer dans le tableau.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le livre : " + selectedLivre.getTitre());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce livre ? Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Boolean> deleteTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    LivreDAO livreDAO = new LivreDAO(); 
                    return livreDAO.deleteLivre(selectedLivre.getId());
                }
            };

            deleteTask.setOnSucceeded(e -> {
                if (deleteTask.getValue()) { 
                    loadLivresIntoUI();
                    clearInputFields();
                    showAlert(AlertType.INFORMATION, "Succès", "Le livre a été supprimé avec succès !");
                } else {
                    showAlert(AlertType.ERROR, "Échec de suppression", "Le livre n'a pas pu être supprimé.");
                }
            });

            deleteTask.setOnFailed(e -> {
                showAlert(AlertType.ERROR, "Erreur de suppression", "Impossible de supprimer le livre : " + deleteTask.getException().getMessage());
                deleteTask.getException().printStackTrace();
            });

            new Thread(deleteTask).start();
        }
    }

    @FXML
    private void handleRechercher() {
        System.out.println("Clic sur Rechercher avec le texte : " + searchField.getText());
        // La logique de filtrage est maintenant gérée par le Listener sur searchField.textProperty()
        // Cette méthode peut rester si un bouton "Rechercher" l'appelle, sinon elle est redondante.
        // Si vous avez un bouton "Rechercher", vous pouvez appeler ici: filterLivres(searchField.getText());
    }
}

/* 
package com.example.controller;

import com.example.DAO.LivreDAO;
import com.example.model.Livre;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.concurrent.Task;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LivreController {

    @FXML private TextField titreField;
    @FXML private TextField auteurField;
    @FXML private TextField datePublicationField;
    @FXML private TextField themeField;
    @FXML private TextField categorieField;
    @FXML private TextField statusField;
    @FXML private TextField nombreExemplaireField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    @FXML private TableView<Livre> livreTable;
    @FXML private TableColumn<Livre, Integer> idColumn;
    @FXML private TableColumn<Livre, String> titreColumn;
    @FXML private TableColumn<Livre, String> auteurColumn;
    @FXML private TableColumn<Livre, String> datePublicationColumn;
    @FXML private TableColumn<Livre, String> themeColumn;
    @FXML private TableColumn<Livre, String> categorieColumn;
    @FXML private TableColumn<Livre, String> statusColumn;
        
    private ObservableList<Livre> masterLivreList = FXCollections.observableArrayList();

    private Livre currentSelectedLivreForEdit;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        datePublicationColumn.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        livreTable.setItems(masterLivreList);

        loadLivresIntoUI();
    }

    @FXML
    protected void onRefreshButtonClick() {
        messageLabel.setText("Rafraîchissement des livres...");
        loadLivresIntoUI();
    }

    @FXML
    protected void onAddLivreButtonClick() {
        String titre = titreField.getText().trim();
        String auteur = auteurField.getText().trim();
        String datePublicationString = datePublicationField.getText().trim();
        String theme = themeField.getText().trim();
        String categorie = categorieField.getText().trim();
        String status = statusField.getText().trim();
        String description = descriptionArea.getText().trim();

       
        int nombreExemplaire = 0; 

    try {
        nombreExemplaire = Integer.parseInt(nombreExemplaireField.getText().trim());
    } catch (NumberFormatException e) {
        showAlert(AlertType.ERROR, "Erreur de saisie", "Le nombre d'exemplaires doit être un nombre valide.");
        return;
    }

    
    if (titre.isEmpty() || auteur.isEmpty() || datePublicationString.isEmpty()) {
        showAlert(AlertType.WARNING, "Saisie incomplète", "Veuillez remplir les champs obligatoires : Titre, Auteur, Date de publication.");
        return;
    }

    Task<Boolean> saveTask = new Task<>() {
        @Override
        protected Boolean call() throws Exception {
            LivreDAO livreDAO = new LivreDAO();

            if (currentSelectedLivreForEdit != null) {
                currentSelectedLivreForEdit.setTitre(titre);
                currentSelectedLivreForEdit.setAuteur(auteur);
                currentSelectedLivreForEdit.setDatePublication(datePublicationString);
                currentSelectedLivreForEdit.setTheme(theme);
                currentSelectedLivreForEdit.setCategorie(categorie);
                currentSelectedLivreForEdit.setStatus(status);
                currentSelectedLivreForEdit.setNombreExemplaire(nombreExemplaire); 
                currentSelectedLivreForEdit.setDescription(description);

                return livreDAO.updateLivre(currentSelectedLivreForEdit);
            } else {
                Livre newLivre = new Livre(
                    titre, auteur, datePublicationString, theme, categorie,
                    status, nombreExemplaire, description 
                );
                return LivreDAO.addLivre(newLivre);
            }
        }
    };

        saveTask.setOnSucceeded(event -> {
            if (saveTask.getValue()) {
                String action = (currentSelectedLivreForEdit != null) ? "modifié" : "ajouté";
                showAlert(AlertType.INFORMATION, "Succès", "Livre '" + titre + "' " + action + " avec succès !");
                clearInputFields();
                loadLivresIntoUI();
                currentSelectedLivreForEdit = null;
            } else {
                showAlert(AlertType.ERROR, "Échec", "Erreur lors de l'opération sur le livre. La base de données pourrait être inaccessible ou le livre existe déjà.");
            }
        });

        saveTask.setOnFailed(event -> {
            showAlert(AlertType.ERROR, "Erreur BD", "Impossible d'effectuer l'opération : " + saveTask.getException().getMessage());
            saveTask.getException().printStackTrace();
            messageLabel.setText("Erreur lors de l'opération sur les livres.");
        });

        new Thread(saveTask).start();
    }

    private void loadLivresIntoUI() {
        messageLabel.setText("Chargement des livres...");
        Task<List<Livre>> loadTask = new Task<>() {
            @Override
            protected List<Livre> call() throws Exception {
                return LivreDAO.getAllLivres();
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<Livre> livres = loadTask.getValue();
            masterLivreList.setAll(livres);

            if (livres.isEmpty()) {
                messageLabel.setText("Aucun livre trouvé dans la base de données.");
            } else {
                messageLabel.setText("Livres chargés avec succès : " + livres.size() + " éléments.");
            }
        });

        loadTask.setOnFailed(event -> {
            showAlert(AlertType.ERROR, "Erreur de chargement", "Impossible de charger les livres : " + loadTask.getException().getMessage());
            loadTask.getException().printStackTrace();
            messageLabel.setText("Erreur lors du chargement des livres.");
        });

        new Thread(loadTask).start();
    }

    private void clearInputFields() {
        titreField.clear();
        auteurField.clear();
        datePublicationField.clear();
        themeField.clear();
        categorieField.clear();
        statusField.clear();
        nombreExemplaireField.clear();
        descriptionArea.clear();
        currentSelectedLivreForEdit = null;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleDashboard() {
        System.out.println("Clic sur Dashboard");
    }

    @FXML
    private void handleEmprunterRetourner() {
        System.out.println("Clic sur Emprunter/Retourner");
    }

    @FXML
    private void handleInformationsClients() {
        System.out.println("Clic sur Informations Clients");
    }

    @FXML
    private void handleInformationsLivres() {
        System.out.println("Clic sur Informations Livres");
        loadLivresIntoUI();
    }

    @FXML
    private void handleParametres() {
        System.out.println("Clic sur Paramètres");
    }

    @FXML
    private void handleAPropos() {
        System.out.println("Clic sur À Propos");
    }

    @FXML
    private void handleModifierLivre() {
        System.out.println("Clic sur Modifier Livre");
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();

        if (selectedLivre == null) {
            showAlert(AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un livre à modifier dans le tableau.");
            return;
        }

        this.currentSelectedLivreForEdit = selectedLivre;

        titreField.setText(selectedLivre.getTitre());
        auteurField.setText(selectedLivre.getAuteur());
        datePublicationField.setText(selectedLivre.getDatePublication());
        themeField.setText(selectedLivre.getTheme());
        categorieField.setText(selectedLivre.getCategorie());
        statusField.setText(selectedLivre.getStatus());
        nombreExemplaireField.setText(String.valueOf(selectedLivre.getNombreExemplaire()));
        descriptionArea.setText(selectedLivre.getDescription());

        showAlert(AlertType.INFORMATION, "Modification", "Les informations du livre ont été chargées dans le formulaire. Modifiez les champs nécessaires et cliquez sur le bouton 'Ajouter' (qui agira comme 'Enregistrer') pour valider.");
    }

    @FXML
    private void handleSupprimerLivre() {
        System.out.println("Clic sur Supprimer Livre");
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();

        if (selectedLivre == null) {
            showAlert(AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un livre à supprimer dans le tableau.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le livre : " + selectedLivre.getTitre());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce livre ? Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> deleteTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    LivreDAO livreDAO = new LivreDAO();
                    livreDAO.deleteLivre(selectedLivre.getId());
                    return null;
                }
            };

            deleteTask.setOnSucceeded(e -> {
                loadLivresIntoUI();
                clearInputFields();
                showAlert(AlertType.INFORMATION, "Succès", "Le livre a été supprimé avec succès !");
            });

            deleteTask.setOnFailed(e -> {
                showAlert(AlertType.ERROR, "Erreur de suppression", "Impossible de supprimer le livre : " + deleteTask.getException().getMessage());
                deleteTask.getException().printStackTrace();
            });

            new Thread(deleteTask).start();
        }
    }

    @FXML
    private void handleRechercher() {
        System.out.println("Clic sur Rechercher avec le texte : " + searchField.getText());
    }
} */