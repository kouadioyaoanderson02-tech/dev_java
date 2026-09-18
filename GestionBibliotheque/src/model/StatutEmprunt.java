package model;

/**
 * Enumération représentant les différents statuts d'un emprunt.
 */
public enum StatutEmprunt {
    EN_COURS("En cours"),
    RETOURNE("Retourné"),
    EN_RETARD("En retard");

    private final String libelle;

    StatutEmprunt(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static StatutEmprunt fromString(String text) {
        if (text == null) return EN_COURS;
        for (StatutEmprunt statut : StatutEmprunt.values()) {
            if (statut.name().equalsIgnoreCase(text.trim()) || statut.libelle.equalsIgnoreCase(text.trim())) {
                return statut;
            }
        }
        return EN_COURS;
    }
}
