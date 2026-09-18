package service;

import repository.EmpruntRepository;
import repository.LivreRepository;
import repository.RetourRepository;
import repository.UtilisateurRepository;
import model.Emprunt;
import model.Livre;
import model.StatutEmprunt;
import utils.ConsoleColor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service dédié au calcul et à la présentation des statistiques globales de la bibliothèque.
 */
public class StatistiqueService {

    private final LivreRepository livreRepository = new LivreRepository();
    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository();
    private final EmpruntRepository empruntRepository = new EmpruntRepository();
    private final RetourRepository retourRepository = new RetourRepository();

    /**
     * Affiche un rapport statistique complet sur la console.
     */
    public void afficherStatistiquesGlobales() {
        List<Livre> livres = livreRepository.afficherTous();
        List<Emprunt> emprunts = empruntRepository.afficherTous();
        int totalUtilisateurs = utilisateurRepository.afficherTous().size();
        double totalAmendes = retourRepository.calculerTotalAmendes();

        int totalTitres = livres.size();
        int totalExemplaires = livres.stream().mapToInt(Livre::getQuantite).sum();
        long livresDisponibles = livres.stream().filter(Livre::isDisponible).count();

        long empruntsEnCours = emprunts.stream().filter(e -> e.getStatut() == StatutEmprunt.EN_COURS).count();
        long empruntsEnRetard = emprunts.stream().filter(e -> e.getStatut() == StatutEmprunt.EN_RETARD).count();
        long empruntsRetournes = emprunts.stream().filter(e -> e.getStatut() == StatutEmprunt.RETOURNE).count();

        System.out.println(ConsoleColor.CYAN_BOLD + "\n==================================================================" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.YELLOW_BOLD + "                  STATISTIQUES DE LA BIBLIOTHÈQUE                 " + ConsoleColor.RESET);
        System.out.println(ConsoleColor.CYAN_BOLD + "==================================================================" + ConsoleColor.RESET);

        System.out.printf("  Total des titres de livres en catalogue : %s%d%s\n", ConsoleColor.GREEN_BOLD, totalTitres, ConsoleColor.RESET);
        System.out.printf("  Total des exemplaires en stock         : %s%d%s\n", ConsoleColor.GREEN_BOLD, totalExemplaires, ConsoleColor.RESET);
        System.out.printf("  Nombre de titres disponibles           : %s%d%s\n", ConsoleColor.GREEN_BOLD, livresDisponibles, ConsoleColor.RESET);
        System.out.println("  ----------------------------------------------------------------");
        System.out.printf("  Nombre d'utilisateurs inscrits         : %s%d%s\n", ConsoleColor.BLUE_BOLD, totalUtilisateurs, ConsoleColor.RESET);
        System.out.println("  ----------------------------------------------------------------");
        System.out.printf("  Emprunts actuellement en cours         : %s%d%s\n", ConsoleColor.YELLOW_BOLD, empruntsEnCours, ConsoleColor.RESET);
        System.out.printf("  Emprunts actuellement en retard       : %s%d%s\n", ConsoleColor.RED_BOLD, empruntsEnRetard, ConsoleColor.RESET);
        System.out.printf("  Total des emprunts retournés           : %s%d%s\n", ConsoleColor.GREEN_BOLD, empruntsRetournes, ConsoleColor.RESET);
        System.out.printf("  Total historique des emprunts          : %s%d%s\n", ConsoleColor.PURPLE_BOLD, emprunts.size(), ConsoleColor.RESET);
        System.out.println("  ----------------------------------------------------------------");
        System.out.printf("  Total des amendes perçues              : %s%.2f €%s\n", ConsoleColor.GREEN_BOLD, totalAmendes, ConsoleColor.RESET);

        // Répartition par catégorie
        Map<String, Long> parCategorie = livres.stream()
                .collect(Collectors.groupingBy(Livre::getCategorie, Collectors.counting()));

        System.out.println(ConsoleColor.CYAN_BOLD + "\n  Répartition des livres par catégorie :" + ConsoleColor.RESET);
        parCategorie.forEach((cat, count) -> {
            System.out.printf("    - %-25s : %d titre(s)\n", cat, count);
        });

        System.out.println(ConsoleColor.CYAN_BOLD + "==================================================================\n" + ConsoleColor.RESET);
    }
}
