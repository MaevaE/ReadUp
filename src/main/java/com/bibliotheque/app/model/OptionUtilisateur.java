package com.bibliotheque.app.model;
public class OptionUtilisateur {
    private int id;
    private String nom;

    public OptionUtilisateur(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return nom;
    }
}
