package com.bibliotheque.app.dao;

import com.bibliotheque.app.model.RetourFormModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RetourFormDao {

    public void enregistrerRetour(RetourFormModel retour) {
        String sql = "INSERT INTO retours (id_emprunt, date_retour_effective) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, retour.getIdEmprunt());
            stmt.setDate(2, java.sql.Date.valueOf(retour.getDateRetourEffective()));
            stmt.executeUpdate();

            // Mise à jour du stock +1 pour le livre concerné
            String updateStock = "UPDATE livres SET nombre_exemplaires = nombre_exemplaires + 1 " +
                                 "WHERE id = (SELECT id_livre FROM emprunts WHERE id = ?)";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateStock)) {
                updateStmt.setInt(1, retour.getIdEmprunt());
                updateStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
