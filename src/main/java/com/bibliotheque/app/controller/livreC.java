package com.bibliotheque.app.controller;

import com.bibliotheque.app.dao.livreD;
import java.sql.SQLIntegrityConstraintViolationException;

import com.bibliotheque.app.model.livreM;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.*;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class livreC {

    @FXML private TableView<livreM> tableLivres;
    @FXML private TableColumn<livreM, String> colTitres;
    @FXML private TableColumn<livreM, String> colAuteurs;
    @FXML private TableColumn<livreM, String> colThemes;
    @FXML private TableColumn<livreM, String> colDatePublications;
    @FXML private TableColumn<livreM, Integer> colNombreExemplairess;
    @FXML private TextField txtExemplairess;
    @FXML private TableColumn<livreM, String> colDisponibilites;
      
    @FXML private DatePicker txtDatePublication;

  
   

    private ObservableList<livreM> livresData = FXCollections.observableArrayList();
    private FilteredList<livreM> filteredList;
    private livreD livresDao = new livreD();

    @FXML
    public void initialize() {
       
        // Colonnes
        colTitres.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitre()));
        colAuteurs.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAuteur()));
        colThemes.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTheme()));
        colDatePublications.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDatePublication().toString())
            );
        colDisponibilites.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getDisponibilite())
        );
        colNombreExemplairess.setCellValueFactory(cell ->
            new SimpleIntegerProperty(cell.getValue().getNombreExemplaires()).asObject()
        );

        // public void count(){
           
        //    return livresDao.compter()
        // }

        chargerLivres();
        // Filtrage
        filteredList = new FilteredList<>(livresData, p -> true);
        tableLivres.setItems(filteredList);

    }
     private void chargerLivres() {
        try {
            List<livreM> livres = livresDao.getAllLivres();
            livresData.setAll(livres);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}