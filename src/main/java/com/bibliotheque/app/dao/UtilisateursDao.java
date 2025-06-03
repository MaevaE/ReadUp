package com.bibliotheque.app.dao;

import com.bibliotheque.app.model.UtilisateursModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateursDao {

    public List<UtilisateursModel> getAllUtilisateurs() throws SQLException {
    List<UtilisateursModel> utilisateursList = new ArrayList<>();

    String sql = "SELECT * FROM utilisateurs";

    try (Connection conn = Database.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            UtilisateursModel utilisateur = new UtilisateursModel();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setNumTelephone(rs.getString("numero_telephone"));
            utilisateur.setAdresse(rs.getString("adresse"));
            utilisateursList.add(utilisateur);
        }
    }
    return utilisateursList;
}


    public void ajouterUtilisateur(UtilisateursModel utilisateurs) throws SQLException {
        String sql = "INSERT INTO utilisateurs (nom, email,prenom,numero_telephone,adresse) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateurs.getNom());
            pstmt.setString(2, utilisateurs.getEmail());
            pstmt.setString(3, utilisateurs.getPrenom());
            pstmt.setString(4, utilisateurs.getNumTelephone());
            pstmt.setString(5, utilisateurs.getAdresse());
            pstmt.executeUpdate();
        }
    }

    public void supprimerUtilisateur(int id) throws SQLException{
    String sql = "DELETE FROM utilisateurs WHERE id = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        stmt.executeUpdate();
        System.out.println("Utilisateur supprimé avec succès.");
        
    } 
}

    public void mettreAJourUtilisateur(UtilisateursModel utilisateur) throws SQLException {
    String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, numero_telephone = ?, adresse = ?  WHERE id = ?";
    try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, utilisateur.getNom());
        stmt.setString(2, utilisateur.getPrenom());
        stmt.setString(3, utilisateur.getEmail());
        stmt.setString(4, utilisateur.getNumTelephone());
        stmt.setString(5, utilisateur.getAdresse());
         stmt.setInt(6, utilisateur.getId());
        stmt.executeUpdate();
    }
}



  
}
