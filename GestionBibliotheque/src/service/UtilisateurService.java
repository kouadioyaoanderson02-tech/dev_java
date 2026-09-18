package service;

import repository.UtilisateurRepository;
import model.Utilisateur;
import utils.ConsoleColor;

import java.util.List;

/**
 * Service gérant la logique métier associée aux utilisateurs.
 */
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService() {
        this.utilisateurRepository = new UtilisateurRepository();
    }

    public boolean ajouterUtilisateur(Utilisateur utilisateur) {
        if (utilisateur.getEmail() == null || !utilisateur.getEmail().contains("@")) {
            System.out.println(ConsoleColor.RED + "Adresse email invalide." + ConsoleColor.RESET);
            return false;
        }
        return utilisateurRepository.ajouter(utilisateur);
    }

    public boolean modifierUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.modifier(utilisateur);
    }

    public boolean supprimerUtilisateur(int id) {
        return utilisateurRepository.supprimer(id);
    }

    public Utilisateur trouverParId(int id) {
        return utilisateurRepository.rechercherParId(id);
    }

    public List<Utilisateur> rechercherParMotCle(String motCle) {
        return utilisateurRepository.rechercherParTitre(motCle);
    }

    public List<Utilisateur> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.afficherTous();
    }
}
