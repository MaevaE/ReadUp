package com.bibliotheque.app.model;
public class OptionLivre {
    private int id;
    private String titre;

    public OptionLivre(int id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return titre;
    }
}
