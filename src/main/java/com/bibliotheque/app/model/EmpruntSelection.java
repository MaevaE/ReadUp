package com.bibliotheque.app.model;

public class EmpruntSelection {
    private int id;
    private String affichage;

    public EmpruntSelection(int id, String affichage) {
        this.id = id;
        this.affichage = affichage;
    }

    public int getId() {
        return id;
    }

    public String getAffichage() {
        return affichage;
    }

    @Override
    public String toString() {
        return affichage;
    }
}
