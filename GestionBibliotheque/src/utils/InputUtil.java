package utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utilitaires pour la lecture et la validation sécurisée des saisies utilisateurs dans la console.
 */
public class InputUtil {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lit une chaîne de caractères non vide.
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(ConsoleColor.CYAN + prompt + ConsoleColor.RESET);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(ConsoleColor.RED + "La saisie ne peut pas être vide. Réessayez." + ConsoleColor.RESET);
        }
    }

    /**
     * Lit un entier positif ou nul.
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(ConsoleColor.CYAN + prompt + ConsoleColor.RESET);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= 0) {
                    return value;
                }
                System.out.println(ConsoleColor.RED + "Veuillez entrer un nombre positif ou nul." + ConsoleColor.RESET);
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColor.RED + "Entrée invalide. Veuillez entrer un nombre entier." + ConsoleColor.RESET);
            }
        }
    }

    /**
     * Lit un entier dans une plage spécifique (ex: pour le menu).
     */
    public static int readIntRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(ConsoleColor.RED + "Veuillez saisir un choix entre " + min + " et " + max + "." + ConsoleColor.RESET);
        }
    }

    /**
     * Lit une date au format dd/MM/yyyy.
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(ConsoleColor.CYAN + prompt + " (jj/mm/aaaa) : " + ConsoleColor.RESET);
            String input = scanner.nextLine().trim();
            try {
                return DateUtil.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println(ConsoleColor.RED + "Format de date invalide. Respectez le format jj/mm/aaaa (ex: 25/12/2026)." + ConsoleColor.RESET);
            }
        }
    }

    /**
     * Lit un boolean (O/N).
     */
    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(ConsoleColor.CYAN + prompt + " (O/N) : " + ConsoleColor.RESET);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("O") || input.equals("OUI") || input.equals("Y") || input.equals("YES")) {
                return true;
            } else if (input.equals("N") || input.equals("NON") || input.equals("NO")) {
                return false;
            }
            System.out.println(ConsoleColor.RED + "Veuillez répondre par O (Oui) ou N (Non)." + ConsoleColor.RESET);
        }
    }
}
