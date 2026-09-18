package utils;

/**
 * Utilitaires de codes couleur ANSI pour embellir l'affichage dans la console.
 */
public class ConsoleColor {
    // Réinitialisation
    public static final String RESET = "\033[0m";

    // Couleurs de texte simples
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    // Couleurs en gras
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String WHITE_BOLD = "\033[1;37m";

    // Arrière-plans
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String RED_BACKGROUND = "\033[41m";
}
