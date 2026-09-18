package repository;

import database.DatabaseConnection;
import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository JDBC pour la gestion de la persistance des Utilisateurs.
 */
public class UtilisateurRepository {

    /**
     * Ajoute un utilisateur dans la BDD.
     */
    public boolean ajouter(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, telephone, adresse) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getEmail());
            pstmt.setString(4, utilisateur.getTelephone());
            pstmt.setString(5, utilisateur.getAdresse());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        utilisateur.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout de l'utilisateur: " + e.getMessage());
        }
        return false;
    }

    /**
     * Modifie un utilisateur.
     */
    public boolean modifier(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, telephone = ?, adresse = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getEmail());
            pstmt.setString(4, utilisateur.getTelephone());
            pstmt.setString(5, utilisateur.getAdresse());
            pstmt.setInt(6, utilisateur.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la modification de l'utilisateur: " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime un utilisateur par son ID.
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de l'utilisateur: " + e.getMessage());
        }
        return false;
    }

    /**
     * Recherche un utilisateur par son ID.
     */
    public Utilisateur rechercherParId(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche par ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Recherche par mot clé (nom, prénom ou email).
     */
    public List<Utilisateur> rechercherParTitre(String motCle) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur WHERE LOWER(nom) LIKE LOWER(?) OR LOWER(prenom) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) ORDER BY nom ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String query = "%" + motCle + "%";
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(mapResultSetToUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche d'utilisateur: " + e.getMessage());
        }
        return utilisateurs;
    }

    /**
     * Retourne tous les utilisateurs.
     */
    public List<Utilisateur> afficherTous() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'affichage des utilisateurs: " + e.getMessage());
        }
        return utilisateurs;
    }

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getString("adresse")
        );
    }
}
