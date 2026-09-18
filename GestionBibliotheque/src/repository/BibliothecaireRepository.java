package repository;

import database.DatabaseConnection;
import model.Bibliothecaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository JDBC pour la gestion des Bibliothécaires.
 */
public class BibliothecaireRepository {

    public boolean ajouter(Bibliothecaire b) {
        String sql = "INSERT INTO bibliothecaire (nom, prenom, poste) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, b.getNom());
            pstmt.setString(2, b.getPrenom());
            pstmt.setString(3, b.getPoste());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        b.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout du bibliothécaire: " + e.getMessage());
        }
        return false;
    }

    public boolean modifier(Bibliothecaire b) {
        String sql = "UPDATE bibliothecaire SET nom = ?, prenom = ?, poste = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, b.getNom());
            pstmt.setString(2, b.getPrenom());
            pstmt.setString(3, b.getPoste());
            pstmt.setInt(4, b.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la modification du bibliothécaire: " + e.getMessage());
        }
        return false;
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM bibliothecaire WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression du bibliothécaire: " + e.getMessage());
        }
        return false;
    }

    public Bibliothecaire rechercherParId(int id) {
        String sql = "SELECT * FROM bibliothecaire WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBibliothecaire(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche par ID: " + e.getMessage());
        }
        return null;
    }

    public List<Bibliothecaire> rechercherParTitre(String motCle) {
        List<Bibliothecaire> liste = new ArrayList<>();
        String sql = "SELECT * FROM bibliothecaire WHERE LOWER(nom) LIKE LOWER(?) OR LOWER(prenom) LIKE LOWER(?) OR LOWER(poste) LIKE LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String query = "%" + motCle + "%";
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapResultSetToBibliothecaire(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche du bibliothécaire: " + e.getMessage());
        }
        return liste;
    }

    public List<Bibliothecaire> afficherTous() {
        List<Bibliothecaire> liste = new ArrayList<>();
        String sql = "SELECT * FROM bibliothecaire ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(mapResultSetToBibliothecaire(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'affichage des bibliothécaires: " + e.getMessage());
        }
        return liste;
    }

    private Bibliothecaire mapResultSetToBibliothecaire(ResultSet rs) throws SQLException {
        return new Bibliothecaire(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("poste")
        );
    }
}
