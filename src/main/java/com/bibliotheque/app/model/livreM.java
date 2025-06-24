package com.bibliotheque.app.model;

import java.time.LocalDate;

public class livreM {
    private int id;
    private String titre;
    private String auteur;
    private String theme;
    private LocalDate datePublication;
    private int nombreExemplaires;
    private boolean disponible;

    public livreM() {}

    public livreM(int id, String titre) {
    this.id = id;
    this.titre = titre;
}


    public livreM(int id, String titre, String auteur, String theme, LocalDate datePublication, int nombreExemplaires, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.theme = theme;
        this.datePublication = datePublication;
        this.nombreExemplaires = nombreExemplaires;
        this.disponible = disponible;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public LocalDate getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }

    public int getNombreExemplaires() { return nombreExemplaires; }
    public void setNombreExemplaires(int nombreExemplaires) { this.nombreExemplaires = nombreExemplaires; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getDisponibilite() {
    return nombreExemplaires > 0 ? "Disponible" : "Indisponible";
}


@Override
public String toString() {
    return titre;
}

}
