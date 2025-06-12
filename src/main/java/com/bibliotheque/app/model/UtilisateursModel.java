package com.bibliotheque.app.model;

public class UtilisateursModel {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String numero_telephone;
    private String adresse;

    public UtilisateursModel() {}

    public UtilisateursModel(int id, String nom) {
    this.id = id;
    this.nom = nom;
}


    public UtilisateursModel(int id, String nom, String prenom, String email, String numero_telephone, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numero_telephone = numero_telephone;
        this.adresse = adresse;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNumTelephone() { return numero_telephone; }
    public void setNumTelephone(String numero_telephone) { this.numero_telephone = numero_telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
public String toString() {
    return nom; // Ou nom + " " + prenom si tu veux afficher les deux
}

}
