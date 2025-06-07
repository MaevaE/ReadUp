package com.example.DAO;

import com.example.model.Livre;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final Statement DatabaseConnection = null;

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Récupère tous les livres de la base de données.
     * @return Une liste d'objets Livre.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public static List<Livre> getAllLivres() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        // Assurez-vous que les noms des colonnes ici correspondent EXACTEMENT à votre base de données.
        // La colonne 'date_publication' est maintenant lue comme un String.
        String query = "SELECT id, titre, auteur, date_publication, theme, categorie, statut, nombre_exemplaires, description FROM livres";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                livres.add(new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("date_publication"), // Lire comme String
                    rs.getString("theme"),
                    rs.getString("categorie"),
                    rs.getString("statut"), // Maintenu comme String
                    rs.getInt("nombre_exemplaires"),
                    rs.getString("description")   

                ));
            }
        }
        return livres;
    }

    /**
     * Ajoute un nouveau livre à la base de données.
     * @param livre L'objet Livre à ajouter (l'ID sera auto-généré par la BD).
     * @return true si l'ajout est réussi, false sinon.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public static boolean addLivre(Livre livre) throws SQLException {
        // La requête INSERT est mise à jour pour inclure toutes les colonnes du modèle Livre (sauf l'ID qui est auto-incrémenté)
        // Vérifiez que cette liste de colonnes correspond EXACTEMENT à votre table dans la BD.
        String query = "INSERT INTO livres (titre, auteur, date_publication, theme, categorie, statut, nombre_exemplaires, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getDatePublication()); // Utilisation de String pour la date
            pstmt.setString(4, livre.getTheme());
            pstmt.setString(5, livre.getCategorie());
            pstmt.setString(6, livre.getStatus());
            pstmt.setInt(7, livre.getNombreExemplaire());
            pstmt.setString(8, livre.getDescription());
         
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Si l'insertion a réussi, récupérer l'ID auto-généré et l'assigner à l'objet Livre
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livre.setId(generatedKeys.getInt(1)); // Met à jour l'ID de l'objet Livre
                    }
                }
            }
            return rowsAffected > 0;
        }
    }

    public boolean updateLivre(Livre livre) {
        // CORRIGÉ : Les noms de colonnes SQL doivent correspondre à ceux de votre base de données.
        // Utilisation de snake_case pour les noms de colonnes SQL.
        String sql = "UPDATE livres SET titre = ?, auteur = ?, date_publication = ?, theme = ?, categorie = ?, statut = ?, nombre_exemplaires = ?, description = ? WHERE id = ?";
        try (Connection conn = getConnection(); // Utilise la méthode getConnection() interne
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getDatePublication());
            stmt.setString(4, livre.getTheme());
            stmt.setString(5, livre.getCategorie());
            stmt.setString(6, livre.getStatus());
            stmt.setInt(7, livre.getNombreExemplaire());
            stmt.setString(8, livre.getDescription());
            stmt.setInt(9, livre.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du livre: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteLivre(int id) {
        String sql = "DELETE FROM livres WHERE id = ?";
        try (Connection conn = getConnection(); // Utilise la méthode getConnection() interne
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre avec l'ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}