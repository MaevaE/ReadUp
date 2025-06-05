package com.example.controller;

// Importations des classes nécessaires pour l'application JavaFX et l'accès aux données
import com.example.DAO.LivreDAO; // Permet d'interagir avec la base de données pour les opérations CRUD sur les livres
import com.example.model.Livre;   // La classe qui représente un livre (votre modèle de données)
import javafx.fxml.FXML;           // Annotation pour lier les éléments de l'interface utilisateur définis dans FXML
import javafx.scene.control.*;     // Importe toutes les classes de contrôle d'interface comme TextField, Label, TableView, etc.
import javafx.collections.FXCollections; // Utilisé pour créer des listes observables (nécessaires pour TableView)
import javafx.collections.ObservableList; // Type de liste dynamique qui notifie les observateurs des changements (comme TableView)
import javafx.scene.control.cell.PropertyValueFactory; // Permet de lier une colonne de TableView à une propriété de l'objet modèle
import javafx.scene.control.Alert; // Classe pour afficher des boîtes de dialogue d'alerte à l'utilisateur
import javafx.scene.control.Alert.AlertType; // Types d'alertes (INFO, ERROR, WARNING, etc.)
import javafx.concurrent.Task;     // Classe pour exécuter des opérations longues en arrière-plan sans bloquer l'interface utilisateur
import java.sql.SQLException;      // Gère les exceptions spécifiques aux bases de données (bien que le DAO les gère principalement)
import java.util.List;             // Utilisé pour manipuler des collections de livres

/**
 * Contrôleur JavaFX pour la gestion des livres.
 * Ce contrôleur gère les interactions de l'utilisateur avec l'interface graphique (ajout,
 * rafraîchissement, et affichage des livres) et communique avec la couche d'accès aux données (DAO)
 * pour effectuer les opérations sur la base de données. Il utilise des tâches (Tasks) pour
 * les opérations de longue durée afin de maintenir l'interface fluide et réactive.
 */
public class LivreController {

    // --- Champs FXML : Déclaration des éléments de l'interface utilisateur liés depuis le fichier FXML ---

    @FXML private TextField titreField;             // Champ de texte pour saisir ou afficher le titre du livre
    @FXML private TextField auteurField;            // Champ de texte pour saisir ou afficher l'auteur du livre
    @FXML private TextField datePublicationField;   // Champ de texte pour saisir ou afficher la date de publication (String dans le modèle)
    @FXML private TextField isbnField;              // Champ de texte pour saisir ou afficher l'ISBN du livre
    @FXML private TextField themeField;             // Champ de texte pour saisir ou afficher le thème du livre
    @FXML private TextField categorieField;         // Champ de texte pour saisir ou afficher la catégorie du livre
    @FXML private TextField statusField;            // Champ de texte pour saisir ou afficher le statut du livre (ex: "Disponible", "Emprunté")
    @FXML private TextField nombreExemplaireField;  // Champ de texte pour saisir ou afficher le nombre d'exemplaires
    @FXML private TextArea descriptionArea;          // Zone de texte pour saisir ou afficher la description du livre
    @FXML private TextField searchField;            // Champ de texte pour la fonctionnalité de recherche (pas encore implémentée)
    @FXML private Label messageLabel;               // Label utilisé pour afficher des messages d'état ou d'information à l'utilisateur

    // --- TableView et ses colonnes pour afficher la liste des livres ---
    @FXML private TableView<Livre> livreTable;                 // Le tableau qui affichera la liste des objets Livre
    @FXML private TableColumn<Livre, Integer> idColumn;        // Colonne pour l'ID du livre (Integer car c'est un nombre)
    @FXML private TableColumn<Livre, String> titreColumn;      // Colonne pour le titre
    @FXML private TableColumn<Livre, String> auteurColumn;     // Colonne pour l'auteur
    @FXML private TableColumn<Livre, String> datePublicationColumn; // Colonne pour la date de publication (String car votre modèle l'est)
    @FXML private TableColumn<Livre, String> themeColumn;      // Colonne pour le thème
    @FXML private TableColumn<Livre, String> categorieColumn;  // Colonne pour la catégorie
    @FXML private TableColumn<Livre, String> statusColumn;     // Colonne pour le statut
    @FXML private TableColumn<Livre, String> isbnColumn;       // Colonne pour l'ISBN

    // Cette liste observable contient les objets Livre qui seront affichés dans la TableView.
    // Toute modification (ajout, suppression, mise à jour) de cette liste se reflétera
    // automatiquement dans l'interface graphique du tableau.
    private ObservableList<Livre> masterLivreList = FXCollections.observableArrayList();

    // --- Méthodes du contrôleur ---

    /**
     * Méthode d'initialisation.
     * Cette méthode est automatiquement appelée par JavaFX une fois que le fichier FXML
     * correspondant au contrôleur a été entièrement chargé et que tous les éléments @FXML
     * ont été injectés. C'est l'endroit idéal pour effectuer des configurations initiales.
     */
    @FXML
    public void initialize() {
        // Configuration des CellValueFactory pour chaque colonne de la TableView.
        // Chaque PropertyValueFactory est liée à une propriété de l'objet Livre (via ses getters).
        // Par exemple, 'titreColumn' affichera la valeur retournée par livre.getTitre().
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        datePublicationColumn.setCellValueFactory(new PropertyValueFactory<>("datePublication")); // Liaison avec getDatePublication() du modèle Livre
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        // Lie l'ObservableList 'masterLivreList' à la TableView.
        // Cela signifie que 'livreTable' affichera toujours le contenu de 'masterLivreList'.
        livreTable.setItems(masterLivreList);

        // Appelle la méthode pour charger les livres depuis la base de données et les afficher au démarrage.
        loadLivresIntoUI();
    }

    /**
     * Gère l'événement de clic sur le bouton "Rafraîchir".
     * Cette méthode est connectée à un bouton "Rafraîchir" dans le FXML (via onAction="#onRefreshButtonClick").
     * Elle met à jour le message d'état et relance le chargement des livres pour rafraîchir le tableau.
     */
    @FXML
    protected void onRefreshButtonClick() {
        messageLabel.setText("Rafraîchissement des livres..."); // Affiche un message de rafraîchissement
        loadLivresIntoUI(); // Recharge la liste des livres depuis la BD
    }

    /**
     * Gère l'événement de clic sur le bouton "Ajouter Livre".
     * Cette méthode est connectée à un bouton "Ajouter" dans le FXML.
     * Elle récupère les données saisies par l'utilisateur, les valide,
     * crée un nouvel objet Livre et tente de l'ajouter à la base de données.
     * L'opération d'ajout se fait dans un thread séparé pour éviter de bloquer l'interface.
     */
    @FXML
    protected void onAddLivreButtonClick() {
        // 1. Récupération et nettoyage des données des champs de saisie.
        // La méthode .trim() supprime les espaces blancs inutiles au début et à la fin.
        String titre = titreField.getText().trim();
        String auteur = auteurField.getText().trim();
        String datePublication = datePublicationField.getText().trim(); // La date est traitée comme un String
        String isbn = isbnField.getText().trim();
        String theme = themeField.getText().trim();
        String categorie = categorieField.getText().trim();
        String status = statusField.getText().trim();
        String description = descriptionArea.getText().trim();

        // 2. Conversion et validation du champ numérique (nombre d'exemplaires).
        // Utilise un bloc try-catch pour gérer les erreurs de format si l'utilisateur n'entre pas un nombre.
        int nombreExemplaire = 0;
        try {
            nombreExemplaire = Integer.parseInt(nombreExemplaireField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur de saisie", "Le nombre d'exemplaires doit être un nombre valide.");
            return; // Arrête l'exécution de la méthode si la conversion échoue
        }

        // 3. Validation des champs obligatoires.
        // Vérifie que les champs essentiels ne sont pas vides.
        if (titre.isEmpty() || auteur.isEmpty() || datePublication.isEmpty() || isbn.isEmpty()) {
            showAlert(AlertType.WARNING, "Saisie incomplète", "Veuillez remplir les champs obligatoires : Titre, Auteur, Date de publication, ISBN.");
            return; // Arrête l'exécution si des champs obligatoires sont vides
        }

        // 4. Création de l'objet Livre.
        // Utilise le constructeur du modèle Livre pour créer une nouvelle instance avec les données saisies.
        // Le constructeur utilisé ici est celui sans l'ID, car l'ID est généré par la base de données.
        Livre nouveauLivre = new Livre(
                titre, auteur, datePublication, theme, categorie,
                status, nombreExemplaire, description, isbn
        );

        // 5. Exécution de l'opération d'ajout en arrière-plan (utilisation de javafx.concurrent.Task).
        // Cela empêche l'interface utilisateur de se figer pendant que l'application interagit avec la base de données.
        Task<Boolean> addTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Cette méthode s'exécute dans un thread séparé.
                // Elle appelle la méthode d'ajout de votre LivreDAO, qui interagit avec la base de données.
                return LivreDAO.addLivre(nouveauLivre); // Retourne true si l'ajout est réussi, false sinon
            }
        };

        // 6. Gestion du succès de l'opération.
        // Cette partie s'exécute automatiquement sur le thread d'application JavaFX une fois que la 'Task' est terminée avec succès.
        addTask.setOnSucceeded(event -> {
            if (addTask.getValue()) { // Si la valeur retournée par la Task est true (succès de l'ajout)
                showAlert(AlertType.INFORMATION, "Succès", "Livre '" + nouveauLivre.getTitre() + "' ajouté avec succès !");
                clearInputFields(); // Efface le contenu des champs de saisie pour une nouvelle entrée
                loadLivresIntoUI(); // Recharge la liste des livres dans le tableau pour afficher le nouveau livre
            } else { // Si la valeur est false (échec de l'ajout)
                showAlert(AlertType.ERROR, "Échec", "Erreur lors de l'ajout du livre. La base de données pourrait être inaccessible ou le livre existe déjà.");
            }
        });

        // 7. Gestion de l'échec de l'opération.
        // Cette partie s'exécute sur le thread d'application JavaFX si la 'Task' échoue (par exemple, une exception SQL).
        addTask.setOnFailed(event -> {
            showAlert(AlertType.ERROR, "Erreur BD", "Impossible d'ajouter le livre : " + addTask.getException().getMessage());
            addTask.getException().printStackTrace(); // Affiche la trace complète de l'erreur dans la console (utile pour le débogage)
            messageLabel.setText("Erreur lors de l'ajout des livres."); // Affiche un message d'erreur
        });

        // 8. Démarrage du thread pour exécuter la 'Task'.
        new Thread(addTask).start(); // Lance la Task dans un nouveau thread en arrière-plan
    }

    /**
     * Charge tous les livres depuis la base de données et met à jour la TableView.
     * Cette opération est également exécutée dans un thread séparé (Task) pour
     * maintenir la réactivité de l'interface utilisateur.
     */
    private void loadLivresIntoUI() {
        messageLabel.setText("Chargement des livres..."); // Affiche un message de chargement
        Task<List<Livre>> loadTask = new Task<>() {
            @Override
            protected List<Livre> call() throws Exception {
                // Cette méthode s'exécute en arrière-plan.
                // Elle récupère tous les livres en appelant la méthode getAllLivres() du DAO.
                return LivreDAO.getAllLivres();
            }
        };

        // Gestion du succès du chargement.
        loadTask.setOnSucceeded(event -> {
            List<Livre> livres = loadTask.getValue(); // Récupère la liste des livres de la Task
            masterLivreList.setAll(livres); // Met à jour l'ObservableList, ce qui rafraîchit automatiquement la TableView

            // Met à jour le message d'état en fonction du résultat du chargement.
            if (livres.isEmpty()) {
                messageLabel.setText("Aucun livre trouvé dans la base de données.");
            } else {
                messageLabel.setText("Livres chargés avec succès : " + livres.size() + " éléments.");
            }
        });

        // Gestion de l'échec du chargement.
        loadTask.setOnFailed(event -> {
            showAlert(AlertType.ERROR, "Erreur de chargement", "Impossible de charger les livres : " + loadTask.getException().getMessage());
            loadTask.getException().printStackTrace(); // Affiche la trace de l'erreur
            messageLabel.setText("Erreur lors du chargement des livres."); // Affiche un message d'erreur
        });

        // Démarrage du thread pour exécuter la 'Task' de chargement.
        new Thread(loadTask).start();
    }

    // --- Méthodes utilitaires ---

    /**
     * Efface le contenu de tous les champs de saisie du formulaire.
     * Utile après un ajout réussi ou pour réinitialiser le formulaire.
     */
    private void clearInputFields() {
        titreField.clear();
        auteurField.clear();
        datePublicationField.clear();
        isbnField.clear();
        themeField.clear();
        categorieField.clear();
        statusField.clear();
        nombreExemplaireField.clear();
        descriptionArea.clear();
    }

    /**
     * Affiche une boîte de dialogue d'alerte à l'utilisateur.
     * @param type Le type de l'alerte (par exemple, AlertType.INFORMATION, AlertType.ERROR).
     * @param title Le titre de la fenêtre d'alerte.
     * @param message Le texte principal affiché dans le corps de l'alerte.
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type); // Crée une nouvelle alerte du type spécifié
        alert.setTitle(title);         // Définit le titre de la fenêtre d'alerte
        alert.setHeaderText(null);     // Supprime le texte d'en-tête (pour un affichage plus simple)
        alert.setContentText(message); // Définit le message principal de l'alerte
        alert.showAndWait();           // Affiche l'alerte et attend que l'utilisateur la ferme
    }

    // --- Fonctions supplémentaires pour la navigation et les actions de la barre latérale ---
    // Ces méthodes sont nécessaires pour les onAction définis dans votre fichier FXML de la barre latérale.

    @FXML
    private void handleDashboard() {
        System.out.println("Clic sur Dashboard");
        // Logique pour naviguer vers la vue du tableau de bord.
        // Cela nécessitera probablement de changer la scène principale de l'application.
    }

    @FXML
    private void handleEmprunterRetourner() {
        System.out.println("Clic sur Emprunter/Retourner");
        // Logique pour naviguer vers la vue d'emprunt/retour.
    }

    @FXML
    private void handleInformationsClients() {
        System.out.println("Clic sur Informations Clients");
        // Logique pour naviguer vers la vue des informations clients.
    }

    @FXML
    private void handleInformationsLivres() {
        System.out.println("Clic sur Informations Livres");
        // Puisque vous êtes déjà sur la page des livres, vous pourriez rafraîchir le tableau.
        loadLivresIntoUI();
    }

    @FXML
    private void handleParametres() {
        System.out.println("Clic sur Paramètres");
        // Logique pour naviguer vers la vue des paramètres.
    }

    @FXML
    private void handleAPropos() {
        System.out.println("Clic sur À Propos");
        // Logique pour naviguer vers la vue "À Propos".
    }

    // --- Fonctions pour les boutons "Modifier" et "Supprimer" (si présents dans votre FXML) ---
    @FXML
    private void handleModifierLivre() {
        System.out.println("Clic sur Modifier Livre");
        // Implémentez ici la logique pour modifier un livre sélectionné.
        // Cela impliquerait de récupérer le livre sélectionné du tableau, de remplir les champs
        // du formulaire avec ses données, et d'avoir un bouton de mise à jour.
    }

    @FXML
    private void handleSupprimerLivre() {
        System.out.println("Clic sur Supprimer Livre");
        // Implémentez ici la logique pour supprimer un livre sélectionné du tableau.
        // N'oubliez pas une boîte de dialogue de confirmation avant la suppression.
    }

    @FXML
    private void handleRechercher() {
        System.out.println("Clic sur Rechercher avec le texte : " + searchField.getText());
        // Implémentez ici la logique de recherche ou de filtrage des livres dans le tableau.
        // Vous pouvez filtrer `masterLivreList` ou appeler une nouvelle méthode DAO.
    }
}