package com.bibliotheque.app.dao;

import com.bibliotheque.app.model.livreM;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class livreD {

    public List<livreM> getAllLivres() throws SQLException {
        List<livreM> livresList = new ArrayList<>();

        String sql = "SELECT * FROM livres";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livreM livre = new livreM();
                livre.setId(rs.getInt("id"));
                livre.setTitre(rs.getString("titre"));
                livre.setAuteur(rs.getString("auteur"));
                livre.setTheme(rs.getString("theme"));
                livre.setDatePublication(rs.getDate("date_publication").toLocalDate());
                livre.setNombreExemplaires(rs.getInt("nombre_exemplaires"));
                livre.setDisponible(rs.getBoolean("disponible"));
                livresList.add(livre);
            }
        }

        return livresList;
    }
    //  public void compter() throws SQLException {
    //     int nbr ;
    //     String sql = "COUNT * FROM Livres";

    //     try (Connection conn = Database.getConnection();
    //          Statement stmt = conn.createStatement();
    //          ResultSet rs = stmt.executeQuery(sql)) {

    //         while (rs.next()) {
    //           System.out.println("0");
    //         }
    //     }
    //     return rs;
    // }
}