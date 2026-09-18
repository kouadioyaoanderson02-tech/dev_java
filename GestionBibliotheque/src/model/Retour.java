package model;

import utils.DateUtil;

import java.time.LocalDate;

/**
 * Modèle représentant le retour d'un livre emprunté, incluant l'historique du retard et des amendes.
 */
public class Retour {
    private int id;
    private Emprunt emprunt;
    private LocalDate dateRetour;
    private int retard;
    private double montantAmende;

    public Retour() {
    }

    public Retour(Emprunt emprunt, LocalDate dateRetour, int retard, double montantAmende) {
        this.emprunt = emprunt;
        this.dateRetour = dateRetour;
        this.retard = retard;
        this.montantAmende = montantAmende;
    }

    public Retour(int id, Emprunt emprunt, LocalDate dateRetour, int retard, double montantAmende) {
        this.id = id;
        this.emprunt = emprunt;
        this.dateRetour = dateRetour;
        this.retard = retard;
        this.montantAmende = montantAmende;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Emprunt getEmprunt() {
        return emprunt;
    }

    public void setEmprunt(Emprunt emprunt) {
        this.emprunt = emprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }

    public int getRetard() {
        return retard;
    }

    public void setRetard(int retard) {
        this.retard = retard;
    }

    public double getMontantAmende() {
        return montantAmende;
    }

    public void setMontantAmende(double montantAmende) {
        this.montantAmende = montantAmende;
    }

    @Override
    public String toString() {
        int empruntId = (emprunt != null) ? emprunt.getId() : 0;
        String titreLivre = (emprunt != null && emprunt.getLivre() != null) ? emprunt.getLivre().getTitre() : "N/A";
        String nomUser = (emprunt != null && emprunt.getUtilisateur() != null) ? emprunt.getUtilisateur().getNomComplet() : "N/A";

        return String.format("ID Retour: %-3d | Emprunt #%-3d | Livre: %-25s | Client: %-20s | Date Retour: %-10s | Retard: %d jours | Amende: %.2f €",
                id, empruntId, titreLivre, nomUser, DateUtil.format(dateRetour), retard, montantAmende);
    }
}
