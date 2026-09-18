package service;

import repository.BibliothecaireRepository;
import model.Bibliothecaire;

import java.util.List;

/**
 * Service gérant la logique métier associée aux bibliothécaires.
 */
public class BibliothecaireService {

    private final BibliothecaireRepository bibliothecaireRepository;

    public BibliothecaireService() {
        this.bibliothecaireRepository = new BibliothecaireRepository();
    }

    public boolean ajouterBibliothecaire(Bibliothecaire b) {
        return bibliothecaireRepository.ajouter(b);
    }

    public boolean modifierBibliothecaire(Bibliothecaire b) {
        return bibliothecaireRepository.modifier(b);
    }

    public boolean supprimerBibliothecaire(int id) {
        return bibliothecaireRepository.supprimer(id);
    }

    public Bibliothecaire trouverParId(int id) {
        return bibliothecaireRepository.rechercherParId(id);
    }

    public List<Bibliothecaire> obtenirTous() {
        return bibliothecaireRepository.afficherTous();
    }
}
