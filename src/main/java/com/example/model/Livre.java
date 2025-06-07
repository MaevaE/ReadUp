package com.example.model;

public class Livre {
    private int id; // Ajout de l'ID du livre
    private String titre;
    private String auteur;
    private String datePublication; // Reste un String, comme dans votre modèle
    private String theme;
    private String categorie;
    private String status;
    private int nombreExemplaire;
    private String description;

    public Livre() {
        // Constructeur par défaut
    }

    // Constructeur pour la création d'un nouveau livre (sans ID, car auto-généré par la BD)
    public Livre(String titre, String auteur, String datePublication, String theme, String categorie, String status, int nombreExemplaire, String description) {
        this.titre = titre;
        this.auteur = auteur;
        this.datePublication = datePublication;
        this.theme = theme;
        this.categorie = categorie;
        this.status = status;
        this.nombreExemplaire = nombreExemplaire;
        this.description = description;
    }

    // Constructeur pour récupérer un livre depuis la base de données (avec ID)
    public Livre(int id, String titre, String auteur, String datePublication, String theme, String categorie, String status, int nombreExemplaire, String description) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.datePublication = datePublication;
        this.theme = theme;
        this.categorie = categorie;
        this.status = status;
        this.nombreExemplaire = nombreExemplaire;
        this.description = description;
    }

    // --- Getters et Setters pour chaque propriété ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; } // Setter pour l'ID

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public String getDatePublication() { return datePublication; } // Reste String
    public void setDatePublication(String datePublication) { this.datePublication = datePublication; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getNombreExemplaire() { return nombreExemplaire; }
    public void setNombreExemplaire(int nombreExemplaire) { this.nombreExemplaire = nombreExemplaire; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Optionnel : une méthode toString() pour faciliter le débogage
    @Override
    public String toString() {
        return "Livre{" +
               "id=" + id +
               ", titre='" + titre + '\'' +
               ", auteur='" + auteur + '\'' +
               ", datePublication='" + datePublication + '\'' +
               ", theme='" + theme + '\'' +
               ", categorie='" + categorie + '\'' +
               ", status='" + status + '\'' +
               ", nombreExemplaire=" + nombreExemplaire +
               ", description='" + description + '\'' +
            
               '}';
    }
}