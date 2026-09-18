package service;

import repository.EmpruntRepository;
import repository.LivreRepository;
import model.Livre;
import utils.ConsoleColor;

import java.util.List;

/**
 * Service gérant la logique métier associée aux livres.
 */
public class LivreService {

    private final LivreRepository livreRepository;
    private final EmpruntRepository empruntRepository;

    public LivreService() {
        this.livreRepository = new LivreRepository();
        this.empruntRepository = new EmpruntRepository();
    }

    public boolean ajouterLivre(Livre livre) {
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            System.out.println(ConsoleColor.RED + "Le titre du livre ne peut pas être vide." + ConsoleColor.RESET);
            return false;
        }
        if (livre.getQuantite() < 0) {
            System.out.println(ConsoleColor.RED + "La quantité du livre ne peut pas être négative." + ConsoleColor.RESET);
            return false;
        }
        return livreRepository.ajouter(livre);
    }

    public boolean modifierLivre(Livre livre) {
        Livre existant = livreRepository.rechercherParId(livre.getId());
        if (existant == null) {
            System.out.println(ConsoleColor.RED + "Livre introuvable avec l'ID : " + livre.getId() + ConsoleColor.RESET);
            return false;
        }
        return livreRepository.modifier(livre);
    }

    /**
     * Supprime un livre à condition qu'il ne soit pas actuellement emprunté (exigence métier).
     */
    public boolean supprimerLivre(int livreId) {
        Livre livre = livreRepository.rechercherParId(livreId);
        if (livre == null) {
            System.out.println(ConsoleColor.RED + "Aucun livre trouvé avec l'ID : " + livreId + ConsoleColor.RESET);
            return false;
        }

        // RÈGLE MÉTIER : Empêcher la suppression d'un livre encore emprunté
        int empruntsActifs = empruntRepository.compterEmpruntsActifsPourLivre(livreId);
        if (empruntsActifs > 0) {
            System.out.println(ConsoleColor.RED + " [ACTION BLOQUÉE] Impossible de supprimer le livre '" + livre.getTitre() +
                    "' car " + empruntsActifs + " emprunt(s) est/sont actuellement en cours !" + ConsoleColor.RESET);
            return false;
        }

        return livreRepository.supprimer(livreId);
    }

    public Livre trouverParId(int id) {
        return livreRepository.rechercherParId(id);
    }

    public List<Livre> rechercherParTitre(String titre) {
        return livreRepository.rechercherParTitre(titre);
    }

    public List<Livre> obtenirTousLesLivres() {
        return livreRepository.afficherTous();
    }

    public List<Livre> obtenirLivresDisponibles() {
        return livreRepository.rechercherDisponibles();
    }
}
