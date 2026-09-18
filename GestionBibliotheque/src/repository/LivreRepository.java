package repository;

import database.DatabaseConnection;
import model.Livre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository JDBC pour la gestion de la persistance des Livres.
 */
public class LivreRepository {

    /**
     * Ajoute un nouveau livre dans la BDD.
     */
    public boolean ajouter(Livre livre) {
        String sql = "INSERT INTO livre (titre, auteur, categorie, isbn, annee_publication, quantite, disponible) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getCategorie());
            pstmt.setString(4, livre.getIsbn());
            pstmt.setInt(5, livre.getAnneePublication());
            pstmt.setInt(6, livre.getQuantite());
            pstmt.setBoolean(7, livre.getQuantite() > 0);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        livre.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout du livre: " + e.getMessage());
        }
        return false;
    }

    /**
     * Modifie un livre existant.
     */
    public boolean modifier(Livre livre) {
        String sql = "UPDATE livre SET titre = ?, auteur = ?, categorie = ?, isbn = ?, annee_publication = ?, quantite = ?, disponible = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getCategorie());
            pstmt.setString(4, livre.getIsbn());
            pstmt.setInt(5, livre.getAnneePublication());
            pstmt.setInt(6, livre.getQuantite());
            pstmt.setBoolean(7, livre.getQuantite() > 0);
            pstmt.setInt(8, livre.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la modification du livre: " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime un livre par son ID.
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM livre WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression du livre: " + e.getMessage());
        }
        return false;
    }

    /**
     * Recherche un livre par son ID.
     */
    public Livre rechercherParId(int id) {
        String sql = "SELECT * FROM livre WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLivre(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche par ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Recherche des livres par mot-clé dans le titre.
     */
    public List<Livre> rechercherParTitre(String titre) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre WHERE LOWER(titre) LIKE LOWER(?) ORDER BY titre ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + titre + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToLivre(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche par titre: " + e.getMessage());
        }
        return livres;
    }

    /**
     * Retourne la liste de tous les livres.
     */
    public List<Livre> afficherTous() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des livres: " + e.getMessage());
        }
        return livres;
    }

    /**
     * Retourne la liste des livres disponibles (quantité > 0 et disponible = true).
     */
    public List<Livre> rechercherDisponibles() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre WHERE quantite > 0 AND disponible = TRUE ORDER BY titre ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche des livres disponibles: " + e.getMessage());
        }
        return livres;
    }

    /**
     * Mappe un ResultSet SQL en un objet Livre.
     */
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        return new Livre(
                rs.getInt("id"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getString("categorie"),
                rs.getString("isbn"),
                rs.getInt("annee_publication"),
                rs.getInt("quantite"),
                rs.getBoolean("disponible")
        );
    }
}
