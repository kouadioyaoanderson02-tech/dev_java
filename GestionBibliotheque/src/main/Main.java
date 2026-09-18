package main;

import database.DatabaseInitializer;
import model.Livre;
import model.Utilisateur;
import model.Emprunt;
import service.*;
import utils.ConsoleColor;
import utils.InputUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe principale (Point d'entrée / Vue & Contrôleur de l'application).
 */
public class Main {

    private static final LivreService livreService = new LivreService();
    private static final UtilisateurService utilisateurService = new UtilisateurService();
    private static final EmpruntService empruntService = new EmpruntService();
    private static final StatistiqueService statistiqueService = new StatistiqueService();

    public static void main(String[] args) {
        // En-tête de démarrage
        afficherEntete();

        // Initialisation automatique de la base de données MySQL et des tables
        DatabaseInitializer.initializeDatabase();

        boolean quitter = false;
        while (!quitter) {
            afficherMenu();
            int choix = InputUtil.readIntRange("Votre choix (1-13) : ", 1, 13);
            System.out.println();

            switch (choix) {
                case 1 -> ajouterLivre();
                case 2 -> modifierLivre();
                case 3 -> supprimerLivre();
                case 4 -> afficherLesLivres();
                case 5 -> rechercherLivre();
                case 6 -> ajouterUtilisateur();
                case 7 -> afficherLesUtilisateurs();
                case 8 -> enregistrerEmprunt();
                case 9 -> retournerLivre();
                case 10 -> voirLivresDisponibles();
                case 11 -> voirLivresEmpruntes();
                case 12 -> statistiqueService.afficherStatistiquesGlobales();
                case 13 -> {
                    quitter = true;
                    System.out.println(ConsoleColor.GREEN_BOLD + "Merci d'avoir utilisé l'application de Gestion de Bibliothèque. Au revoir !" + ConsoleColor.RESET);
                }
            }
            if (!quitter) {
                System.out.println();
            }
        }
    }

    private static void afficherEntete() {
        System.out.println(ConsoleColor.CYAN_BOLD + "==================================================================" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BLUE_BOLD + "          APPLICATION DE GESTION DE BIBLIOTHÈQUE (JAVA 25)        " + ConsoleColor.RESET);
        System.out.println(ConsoleColor.CYAN_BOLD + "==================================================================" + ConsoleColor.RESET);
    }

    private static void afficherMenu() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "===== GESTION DE BIBLIOTHÈQUE =====" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.WHITE_BOLD + " 1." + ConsoleColor.RESET + " Ajouter un livre");
        System.out.println(ConsoleColor.WHITE_BOLD + " 2." + ConsoleColor.RESET + " Modifier un livre");
        System.out.println(ConsoleColor.WHITE_BOLD + " 3." + ConsoleColor.RESET + " Supprimer un livre");
        System.out.println(ConsoleColor.WHITE_BOLD + " 4." + ConsoleColor.RESET + " Afficher les livres");
        System.out.println(ConsoleColor.WHITE_BOLD + " 5." + ConsoleColor.RESET + " Rechercher un livre");
        System.out.println(ConsoleColor.WHITE_BOLD + " 6." + ConsoleColor.RESET + " Ajouter un utilisateur");
        System.out.println(ConsoleColor.WHITE_BOLD + " 7." + ConsoleColor.RESET + " Afficher les utilisateurs");
        System.out.println(ConsoleColor.WHITE_BOLD + " 8." + ConsoleColor.RESET + " Enregistrer un emprunt");
        System.out.println(ConsoleColor.WHITE_BOLD + " 9." + ConsoleColor.RESET + " Retourner un livre");
        System.out.println(ConsoleColor.WHITE_BOLD + "10." + ConsoleColor.RESET + " Voir les livres disponibles");
        System.out.println(ConsoleColor.WHITE_BOLD + "11." + ConsoleColor.RESET + " Voir les livres empruntés");
        System.out.println(ConsoleColor.WHITE_BOLD + "12." + ConsoleColor.RESET + " Statistiques");
        System.out.println(ConsoleColor.WHITE_BOLD + "13." + ConsoleColor.RESET + " Quitter");
        System.out.println(ConsoleColor.CYAN + "------------------------------------" + ConsoleColor.RESET);
    }

    // 1. Ajouter un livre
    private static void ajouterLivre() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- AJOUTER UN LIVRE ---" + ConsoleColor.RESET);
        String titre = InputUtil.readString("Titre du livre : ");
        String auteur = InputUtil.readString("Auteur : ");
        String categorie = InputUtil.readString("Catégorie : ");
        String isbn = InputUtil.readString("Code ISBN : ");
        int annee = InputUtil.readInt("Année de publication : ");
        int quantite = InputUtil.readInt("Quantité d'exemplaires : ");

        Livre livre = new Livre(titre, auteur, categorie, isbn, annee, quantite);
        if (livreService.ajouterLivre(livre)) {
            System.out.println(ConsoleColor.GREEN + " [SUCCÈS] Livre ajouté avec l'ID " + livre.getId() + ConsoleColor.RESET);
        }
    }

    // 2. Modifier un livre
    private static void modifierLivre() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- MODIFIER UN LIVRE ---" + ConsoleColor.RESET);
        int id = InputUtil.readInt("ID du livre à modifier : ");
        Livre livre = livreService.trouverParId(id);

        if (livre == null) {
            System.out.println(ConsoleColor.RED + "Livre introuvable avec l'ID " + id + ConsoleColor.RESET);
            return;
        }

        System.out.println(ConsoleColor.CYAN + "Livre actuel : " + livre + ConsoleColor.RESET);
        String titre = InputUtil.readString("Nouveau titre (ou conserver) : ");
        String auteur = InputUtil.readString("Nouveau auteur (ou conserver) : ");
        String categorie = InputUtil.readString("Nouvelle catégorie : ");
        String isbn = InputUtil.readString("Nouveau code ISBN : ");
        int annee = InputUtil.readInt("Nouvelle année de publication : ");
        int quantite = InputUtil.readInt("Nouvelle quantité en stock : ");

        livre.setTitre(titre);
        livre.setAuteur(auteur);
        livre.setCategorie(categorie);
        livre.setIsbn(isbn);
        livre.setAnneePublication(annee);
        livre.setQuantite(quantite);

        if (livreService.modifierLivre(livre)) {
            System.out.println(ConsoleColor.GREEN + " [SUCCÈS] Livre mis à jour avec succès !" + ConsoleColor.RESET);
        }
    }

    // 3. Supprimer un livre
    private static void supprimerLivre() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- SUPPRIMER UN LIVRE ---" + ConsoleColor.RESET);
        int id = InputUtil.readInt("ID du livre à supprimer : ");
        if (livreService.supprimerLivre(id)) {
            System.out.println(ConsoleColor.GREEN + " [SUCCÈS] Livre supprimé de la base de données." + ConsoleColor.RESET);
        }
    }

    // 4. Afficher les livres
    private static void afficherLesLivres() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- LISTE DE TOUS LES LIVRES ---" + ConsoleColor.RESET);
        List<Livre> livres = livreService.obtenirTousLesLivres();
        if (livres.isEmpty()) {
            System.out.println(ConsoleColor.PURPLE + "Aucun livre enregistré pour le moment." + ConsoleColor.RESET);
        } else {
            for (Livre l : livres) {
                System.out.println(l);
            }
        }
    }

    // 5. Rechercher un livre
    private static void rechercherLivre() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- RECHERCHER UN LIVRE ---" + ConsoleColor.RESET);
        String titre = InputUtil.readString("Entrez un titre ou un mot-clé : ");
        List<Livre> resultats = livreService.rechercherParTitre(titre);

        if (resultats.isEmpty()) {
            System.out.println(ConsoleColor.PURPLE + "Aucun livre ne correspond à la recherche '" + titre + "'." + ConsoleColor.RESET);
        } else {
            System.out.println(ConsoleColor.GREEN + resultats.size() + " livre(s) trouvé(s) :" + ConsoleColor.RESET);
            for (Livre l : resultats) {
                System.out.println(l);
            }
        }
    }

    // 6. Ajouter un utilisateur
    private static void ajouterUtilisateur() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- AJOUTER UN UTILISATEUR ---" + ConsoleColor.RESET);
        String nom = InputUtil.readString("Nom : ");
        String prenom = InputUtil.readString("Prénom : ");
        String email = InputUtil.readString("Adresse email : ");
        String telephone = InputUtil.readString("Numéro de téléphone : ");
        String adresse = InputUtil.readString("Adresse postale : ");

        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, telephone, adresse);
        if (utilisateurService.ajouterUtilisateur(utilisateur)) {
            System.out.println(ConsoleColor.GREEN + " [SUCCÈS] Utilisateur ajouté avec l'ID " + utilisateur.getId() + ConsoleColor.RESET);
        }
    }

    // 7. Afficher les utilisateurs
    private static void afficherLesUtilisateurs() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- LISTE DES UTILISATEURS ---" + ConsoleColor.RESET);
        List<Utilisateur> utilisateurs = utilisateurService.obtenirTousLesUtilisateurs();
        if (utilisateurs.isEmpty()) {
            System.out.println(ConsoleColor.PURPLE + "Aucun utilisateur inscrit." + ConsoleColor.RESET);
        } else {
            for (Utilisateur u : utilisateurs) {
                System.out.println(u);
            }
        }
    }

    // 8. Enregistrer un emprunt
    private static void enregistrerEmprunt() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- ENREGISTRER UN EMPRUNT ---" + ConsoleColor.RESET);
        int livreId = InputUtil.readInt("ID du livre à emprunter : ");
        int utilisateurId = InputUtil.readInt("ID de l'utilisateur emprunteur : ");
        int duree = InputUtil.readInt("Durée de l'emprunt en jours (ex: 14) : ");

        empruntService.enregistrerEmprunt(livreId, utilisateurId, duree);
    }

    // 9. Retourner un livre
    private static void retournerLivre() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- RETOURNER UN LIVRE ---" + ConsoleColor.RESET);
        int empruntId = InputUtil.readInt("ID de l'emprunt à retourner : ");
        boolean customDate = InputUtil.readBoolean("Souhaitez-vous spécifier une date de retour particulière ?");

        LocalDate dateRetour = LocalDate.now();
        if (customDate) {
            dateRetour = InputUtil.readDate("Saisissez la date de retour réelle");
        }

        empruntService.retournerLivre(empruntId, dateRetour);
    }

    // 10. Voir les livres disponibles
    private static void voirLivresDisponibles() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- LIVRES DISPONIBLES À L'EMPRUNT ---" + ConsoleColor.RESET);
        List<Livre> disponibles = livreService.obtenirLivresDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println(ConsoleColor.RED + "Aucun livre disponible en stock actuellement." + ConsoleColor.RESET);
        } else {
            for (Livre l : disponibles) {
                System.out.println(l);
            }
        }
    }

    // 11. Voir les livres empruntés
    private static void voirLivresEmpruntes() {
        System.out.println(ConsoleColor.YELLOW_BOLD + "--- LISTE DES LIVRES ACTUELLEMENT EMPRUNTÉS ---" + ConsoleColor.RESET);
        List<Emprunt> empruntsEnCours = empruntService.obtenirEmpruntsEnCours();
        if (empruntsEnCours.isEmpty()) {
            System.out.println(ConsoleColor.GREEN + "Aucun emprunt en cours actuellement." + ConsoleColor.RESET);
        } else {
            for (Emprunt e : empruntsEnCours) {
                System.out.println(e);
            }
        }
    }
}
