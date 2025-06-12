package com.bibliotheque.app.dao;

import com.bibliotheque.app.model.LivresModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LivresDao {

    public List<LivresModel> getAllLivres() throws SQLException {
        List<LivresModel> livresList = new ArrayList<>();

        String sql = "SELECT * FROM livres";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LivresModel livre = new LivresModel();
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

    public void ajouterLivre(LivresModel livre) throws SQLException {
        String sql = "INSERT INTO livres (titre, auteur, theme, date_publication, nombre_exemplaires, disponible) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getTheme());
            pstmt.setDate(4, Date.valueOf(livre.getDatePublication()));
            pstmt.setInt(5, livre.getNombreExemplaires());
            pstmt.setBoolean(6, livre.isDisponible());

            pstmt.executeUpdate();
        }
    }

    public void supprimerLivre(int id) throws SQLException {
        String sql = "DELETE FROM livres WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Livre supprimé avec succès.");
        }
    }

    public void mettreAJourLivre(LivresModel livre) throws SQLException {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, theme = ?, date_publication = ?, nombre_exemplaires = ?, disponible = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getTheme());
            stmt.setDate(4, Date.valueOf(livre.getDatePublication()));
            stmt.setInt(5, livre.getNombreExemplaires());
            stmt.setBoolean(6, livre.isDisponible());
            stmt.setInt(7, livre.getId());

            stmt.executeUpdate();
        }
    }
}
