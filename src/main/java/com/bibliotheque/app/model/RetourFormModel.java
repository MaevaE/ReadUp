package com.bibliotheque.app.model;

import java.time.LocalDate;

public class RetourFormModel {
    private int idEmprunt;
    private LocalDate dateRetourEffective;

    public RetourFormModel() {}

    public RetourFormModel(int idEmprunt, LocalDate dateRetourEffective) {
        this.idEmprunt = idEmprunt;
        this.dateRetourEffective = dateRetourEffective;
    }

    public int getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(int idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }
}
