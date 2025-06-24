package com.bibliotheque.app.dao;

import com.bibliotheque.app.model.EmpruntFormModel;
import com.bibliotheque.app.model.OptionUtilisateur;
import com.bibliotheque.app.model.OptionLivre;
import com.bibliotheque.app.model.EmpruntSelection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpruntFormDao {
    

    public void enregistrerEmprunt(EmpruntFormModel emprunt) {
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO emprunts (id_utilisateur, id_livre, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, emprunt.getIdUtilisateur());
            stmt.setInt(2, emprunt.getIdLivre());
            stmt.setDate(3, java.sql.Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(4, java.sql.Date.valueOf(emprunt.getDateRetourPrevue()));
            System.out.println("ID utilisateur: " + emprunt.getIdUtilisateur());
System.out.println("ID livre: " + emprunt.getIdLivre());
System.out.println("ID utilisateur: " + emprunt.getIdUtilisateur());
System.out.println("Date début: " + emprunt.getDateEmprunt());
System.out.println("Date fin: " + emprunt.getDateRetourPrevue());

            stmt.executeUpdate();

            // Mise à jour du stock du livre
            String updateStock = "UPDATE livres SET nombre_exemplaires = nombre_exemplaires - 1 WHERE id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(updateStock);
            stmt2.setInt(1, emprunt.getIdLivre());
            stmt2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


        public List<EmpruntSelection> getEmpruntsNonRendus() {
    List<EmpruntSelection> emprunts = new ArrayList<>();
    String sql = """
        SELECT e.id, u.nom, u.prenom, l.titre, e.date_emprunt
        FROM emprunts e
        JOIN utilisateurs u ON e.id_utilisateur = u.id
        JOIN livres l ON e.id_livre = l.id
        WHERE e.id NOT IN (SELECT id_emprunt FROM retours)
    """;

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String affichage = rs.getString("nom") + " " + rs.getString("prenom") +
                               " - \"" + rs.getString("titre") +
                               "\" (" + rs.getDate("date_emprunt") + ")";
            emprunts.add(new EmpruntSelection(id, affichage));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return emprunts;
}


public static List<String> getNomsUtilisateurs() {
    List<String> utilisateurs = new ArrayList<>();
    String sql = "SELECT prenom, nom FROM utilisateurs";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String nomComplet = rs.getString("prenom") + " " + rs.getString("nom");
            utilisateurs.add(nomComplet);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return utilisateurs;
}

public static List<String> getTitresLivresDisponibles() {
    List<String> titres = new ArrayList<>();
    String sql = "SELECT titre FROM livres WHERE nombre_exemplaires > 0";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            titres.add(rs.getString("titre"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return titres;
}

public static List<OptionUtilisateur> getUtilisateursAvecId() {
    List<OptionUtilisateur> list = new ArrayList<>();
    try (Connection conn = Database.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT id, nom FROM utilisateurs")) {
        while (rs.next()) {
            list.add(new OptionUtilisateur(rs.getInt("id"), rs.getString("nom")));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

public static List<OptionLivre> getLivresDisponiblesAvecId() {
    List<OptionLivre> list = new ArrayList<>();
    try (Connection conn = Database.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT id, titre FROM livres WHERE nombre_exemplaires > 0")) {
        while (rs.next()) {
            list.add(new OptionLivre(rs.getInt("id"), rs.getString("titre")));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

public boolean estEncoreEmprunte(int idLivre) {
    String sql = """
        SELECT COUNT(*) FROM emprunts 
        WHERE id_livre = ? AND id NOT IN (SELECT id_emprunt FROM retours)
    """;
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idLivre);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // encore emprunté
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return true;
}



    }

