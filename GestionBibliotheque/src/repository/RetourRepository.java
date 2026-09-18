package repository;

import database.DatabaseConnection;
import model.Emprunt;
import model.Retour;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository JDBC pour la gestion des Retours de livres.
 */
public class RetourRepository {

    private final EmpruntRepository empruntRepository = new EmpruntRepository();

    public boolean ajouter(Retour retour) {
        String sql = "INSERT INTO retour (emprunt_id, date_retour, retard, montant_amende) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, retour.getEmprunt().getId());
            pstmt.setDate(2, Date.valueOf(retour.getDateRetour()));
            pstmt.setInt(3, retour.getRetard());
            pstmt.setDouble(4, retour.getMontantAmende());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        retour.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'enregistrement du retour: " + e.getMessage());
        }
        return false;
    }

    public List<Retour> afficherTous() {
        List<Retour> retours = new ArrayList<>();
        String sql = "SELECT * FROM retour ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int empruntId = rs.getInt("emprunt_id");
                Emprunt emprunt = empruntRepository.rechercherParId(empruntId);

                Retour retour = new Retour(
                        rs.getInt("id"),
                        emprunt,
                        rs.getDate("date_retour").toLocalDate(),
                        rs.getInt("retard"),
                        rs.getDouble("montant_amende")
                );
                retours.add(retour);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'affichage des retours: " + e.getMessage());
        }
        return retours;
    }

    public double calculerTotalAmendes() {
        String sql = "SELECT SUM(montant_amende) FROM retour";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du calcul du total des amendes: " + e.getMessage());
        }
        return 0.0;
    }
}
