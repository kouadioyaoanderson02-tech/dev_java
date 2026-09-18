package model;

import utils.DateUtil;

import java.time.LocalDate;

/**
 * Modèle représentant un emprunt de livre par un utilisateur.
 */
public class Emprunt {
    private int id;
    private Livre livre;
    private Utilisateur utilisateur;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private StatutEmprunt statut;

    public Emprunt() {
    }

    public Emprunt(Livre livre, Utilisateur utilisateur, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.livre = livre;
        this.utilisateur = utilisateur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.statut = StatutEmprunt.EN_COURS;
    }

    public Emprunt(int id, Livre livre, Utilisateur utilisateur, LocalDate dateEmprunt, LocalDate dateRetourPrevue, LocalDate dateRetourEffective, StatutEmprunt statut) {
        this.id = id;
        this.livre = livre;
        this.utilisateur = utilisateur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public StatutEmprunt getStatut() {
        return statut;
    }

    public void setStatut(StatutEmprunt statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        String titreLivre = (livre != null) ? livre.getTitre() : "N/A";
        String nomUtilisateur = (utilisateur != null) ? utilisateur.getNomComplet() : "N/A";
        String dateRetourEff = (dateRetourEffective != null) ? DateUtil.format(dateRetourEffective) : "En cours";

        return String.format("ID: %-3d | Livre: %-25s | Emprunteur: %-20s | Date Emprunt: %-10s | Date Prévue: %-10s | Date Retour: %-10s | Statut: %s",
                id, titreLivre, nomUtilisateur, DateUtil.format(dateEmprunt), DateUtil.format(dateRetourPrevue), dateRetourEff, statut.getLibelle());
    }
}
