package service;

import repository.EmpruntRepository;
import repository.LivreRepository;
import repository.RetourRepository;
import repository.UtilisateurRepository;
import model.*;
import utils.ConsoleColor;
import utils.DateUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * Service orchestrant la logique métier complexe des emprunts et retours.
 */
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RetourRepository retourRepository;

    // Tarif d'amende par jour de retard en Euros (€)
    private static final double TARIF_AMENDE_PAR_JOUR = 1.50;

    public EmpruntService() {
        this.empruntRepository = new EmpruntRepository();
        this.livreRepository = new LivreRepository();
        this.utilisateurRepository = new UtilisateurRepository();
        this.retourRepository = new RetourRepository();
    }

    /**
     * Enregistre un nouvel emprunt avec vérification stricte du stock et de la disponibilité.
     */
    public boolean enregistrerEmprunt(int livreId, int utilisateurId, int dureeJours) {
        Livre livre = livreRepository.rechercherParId(livreId);
        if (livre == null) {
            System.out.println(ConsoleColor.RED + " [ERREUR] Livre introuvable (ID: " + livreId + ")." + ConsoleColor.RESET);
            return false;
        }

        Utilisateur utilisateur = utilisateurRepository.rechercherParId(utilisateurId);
        if (utilisateur == null) {
            System.out.println(ConsoleColor.RED + " [ERREUR] Utilisateur introuvable (ID: " + utilisateurId + ")." + ConsoleColor.RESET);
            return false;
        }

        // RÈGLE MÉTIER 1 & 2 : Empêcher l'emprunt d'un livre indisponible ou stock à 0
        if (!livre.isDisponible() || livre.getQuantite() <= 0) {
            System.out.println(ConsoleColor.RED + " [EMPRUNT IMPOSSIBLE] Le livre '" + livre.getTitre() +
                    "' n'est pas disponible actuellement (Stock: " + livre.getQuantite() + ")." + ConsoleColor.RESET);
            return false;
        }

        LocalDate aujourdhui = LocalDate.now();
        LocalDate datePrevue = aujourdhui.plusDays(dureeJours > 0 ? dureeJours : 14); // 14 jours par défaut

        Emprunt emprunt = new Emprunt(livre, utilisateur, aujourdhui, datePrevue);

        // Décrémenter le stock du livre
        livre.setQuantite(livre.getQuantite() - 1);
        if (livre.getQuantite() == 0) {
            livre.setDisponible(false);
        }

        boolean modificationLivreOk = livreRepository.modifier(livre);
        boolean empruntCreeOk = empruntRepository.ajouter(emprunt);

        if (modificationLivreOk && empruntCreeOk) {
            System.out.println(ConsoleColor.GREEN + " [SUCCÈS] Emprunt enregistré avec succès !" + ConsoleColor.RESET);
            System.out.println(ConsoleColor.CYAN + "Date limite de retour : " + DateUtil.format(datePrevue) + ConsoleColor.RESET);
            return true;
        } else {
            System.out.println(ConsoleColor.RED + " [ÉCHEC] Erreur lors de l'enregistrement de l'emprunt." + ConsoleColor.RESET);
            return false;
        }
    }

    /**
     * Traite le retour d'un livre emprunté et gère le calcul des pénalités/retards.
     */
    public boolean retournerLivre(int empruntId, LocalDate dateRetourEffective) {
        Emprunt emprunt = empruntRepository.rechercherParId(empruntId);
        if (emprunt == null) {
            System.out.println(ConsoleColor.RED + " [ERREUR] Emprunt introuvable (ID: " + empruntId + ")." + ConsoleColor.RESET);
            return false;
        }

        if (emprunt.getStatut() == StatutEmprunt.RETOURNE) {
            System.out.println(ConsoleColor.YELLOW + " [INFORMATION] Cet emprunt a déjà été retourné." + ConsoleColor.RESET);
            return false;
        }

        if (dateRetourEffective == null) {
            dateRetourEffective = LocalDate.now();
        }

        // Calcul des jours de retard
        int joursRetard = DateUtil.calculerRetardEnJours(emprunt.getDateRetourPrevue(), dateRetourEffective);
        double amende = joursRetard * TARIF_AMENDE_PAR_JOUR;

        // Mise à jour de l'emprunt
        emprunt.setDateRetourEffective(dateRetourEffective);
        emprunt.setStatut(StatutEmprunt.RETOURNE);
        empruntRepository.modifier(emprunt);

        // Enregistrement du retour dans la table `retour`
        Retour retour = new Retour(emprunt, dateRetourEffective, joursRetard, amende);
        retourRepository.ajouter(retour);

        // Réincrémentation du stock du livre
        Livre livre = emprunt.getLivre();
        if (livre != null) {
            livre.setQuantite(livre.getQuantite() + 1);
            livre.setDisponible(true);
            livreRepository.modifier(livre);
        }

        // Affichage des informations du retour
        System.out.println(ConsoleColor.GREEN + " [SUCCÈS] Livre '" + (livre != null ? livre.getTitre() : "") + "' retourné avec succès !" + ConsoleColor.RESET);
        if (joursRetard > 0) {
            System.out.println(ConsoleColor.RED_BOLD + " [RETARD DÉTECTÉ] Retard de " + joursRetard + " jour(s)." + ConsoleColor.RESET);
            System.out.println(ConsoleColor.YELLOW_BOLD + " [AMENDE] Montant à régler : " + String.format("%.2f €", amende) + ConsoleColor.RESET);
        } else {
            System.out.println(ConsoleColor.GREEN + " [INFO] Retour effectué dans les délais (Aucune amende)." + ConsoleColor.RESET);
        }

        return true;
    }

    public List<Emprunt> obtenirTousLesEmprunts() {
        return empruntRepository.afficherTous();
    }

    public List<Emprunt> obtenirEmpruntsEnCours() {
        return empruntRepository.rechercherEmpruntsEnCoursOuEnRetard();
    }

    public List<Emprunt> rechercherEmprunts(String motCle) {
        return empruntRepository.rechercherParTitre(motCle);
    }
}
