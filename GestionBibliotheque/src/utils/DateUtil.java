package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Utilitaires pour la manipulation, le formatage et le calcul de différence entre dates.
 */
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Formate une LocalDate au format français dd/MM/yyyy.
     */
    public static String format(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(FORMATTER);
    }

    /**
     * Convertit une chaîne au format dd/MM/yyyy en LocalDate.
     * @throws DateTimeParseException si le format est invalide
     */
    public static LocalDate parse(String dateStr) throws DateTimeParseException {
        return LocalDate.parse(dateStr.trim(), FORMATTER);
    }

    /**
     * Calcule le nombre de jours de retard entre une date prévue et une date effective/actuelle.
     * @return nombre de jours de retard (0 si pas de retard)
     */
    public static int calculerRetardEnJours(LocalDate datePrevue, LocalDate dateRetour) {
        if (datePrevue == null || dateRetour == null) return 0;
        if (dateRetour.isAfter(datePrevue)) {
            return (int) ChronoUnit.DAYS.between(datePrevue, dateRetour);
        }
        return 0;
    }
}
