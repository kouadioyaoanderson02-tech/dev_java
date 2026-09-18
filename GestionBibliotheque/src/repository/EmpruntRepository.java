package repository;

import database.DatabaseConnection;
import model.Emprunt;
import model.Livre;
import model.StatutEmprunt;
import model.Utilisateur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository JDBC pour la gestion des Emprunts.
 */
public class EmpruntRepository {

    private final LivreRepository livreRepository = new LivreRepository();
    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository();

    public boolean ajouter(Emprunt emprunt) {
        String sql = "INSERT INTO emprunt (livre_id, utilisateur_id, date_emprunt, date_retour_prevue, date_retour_effective, statut) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, emprunt.getLivre().getId());
            pstmt.setInt(2, emprunt.getUtilisateur().getId());
            pstmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            pstmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
            pstmt.setDate(5, emprunt.getDateRetourEffective() != null ? Date.valueOf(emprunt.getDateRetourEffective()) : null);
            pstmt.setString(6, emprunt.getStatut().name());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        emprunt.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'enregistrement de l'emprunt: " + e.getMessage());
        }
        return false;
    }

    public boolean modifier(Emprunt emprunt) {
        String sql = "UPDATE emprunt SET livre_id = ?, utilisateur_id = ?, date_emprunt = ?, date_retour_prevue = ?, date_retour_effective = ?, statut = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, emprunt.getLivre().getId());
            pstmt.setInt(2, emprunt.getUtilisateur().getId());
            pstmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            pstmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
            pstmt.setDate(5, emprunt.getDateRetourEffective() != null ? Date.valueOf(emprunt.getDateRetourEffective()) : null);
            pstmt.setString(6, emprunt.getStatut().name());
            pstmt.setInt(7, emprunt.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour de l'emprunt: " + e.getMessage());
        }
        return false;
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM emprunt WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de l'emprunt: " + e.getMessage());
        }
        return false;
    }

    public Emprunt rechercherParId(int id) {
        String sql = "SELECT * FROM emprunt WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmprunt(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche de l'emprunt par ID: " + e.getMessage());
        }
        return null;
    }

    public List<Emprunt> rechercherParTitre(String motCle) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = """
            SELECT e.* FROM emprunt e
            JOIN livre l ON e.livre_id = l.id
            JOIN utilisateur u ON e.utilisateur_id = u.id
            WHERE LOWER(l.titre) LIKE LOWER(?) OR LOWER(u.nom) LIKE LOWER(?) OR LOWER(u.prenom) LIKE LOWER(?)
            ORDER BY e.id DESC
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String query = "%" + motCle + "%";
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(mapResultSetToEmprunt(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche d'emprunts: " + e.getMessage());
        }
        return emprunts;
    }

    public List<Emprunt> afficherTous() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'affichage de tous les emprunts: " + e.getMessage());
        }
        return emprunts;
    }

    /**
     * Retourne la liste des emprunts actuellement en cours ou en retard.
     */
    public List<Emprunt> rechercherEmpruntsEnCoursOuEnRetard() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt WHERE statut IN ('EN_COURS', 'EN_RETARD') ORDER BY date_retour_prevue ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche des emprunts en cours: " + e.getMessage());
        }
        return emprunts;
    }

    /**
     * Compte combien d'emprunts sont en cours pour un livre spécifique.
     */
    public int compterEmpruntsActifsPourLivre(int livreId) {
        String sql = "SELECT COUNT(*) FROM emprunt WHERE livre_id = ? AND statut IN ('EN_COURS', 'EN_RETARD')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livreId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du comptage des emprunts pour le livre: " + e.getMessage());
        }
        return 0;
    }

    private Emprunt mapResultSetToEmprunt(ResultSet rs) throws SQLException {
        int livreId = rs.getInt("livre_id");
        int utilisateurId = rs.getInt("utilisateur_id");

        Livre livre = livreRepository.rechercherParId(livreId);
        Utilisateur utilisateur = utilisateurRepository.rechercherParId(utilisateurId);

        Date dateEff = rs.getDate("date_retour_effective");
        LocalDate dateRetourEffective = (dateEff != null) ? dateEff.toLocalDate() : null;

        return new Emprunt(
                rs.getInt("id"),
                livre,
                utilisateur,
                rs.getDate("date_emprunt").toLocalDate(),
                rs.getDate("date_retour_prevue").toLocalDate(),
                dateRetourEffective,
                StatutEmprunt.fromString(rs.getString("statut"))
        );
    }
}
