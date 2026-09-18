package model;

/**
 * Modèle représentant un membre du personnel (Bibliothécaire).
 */
public class Bibliothecaire {
    private int id;
    private String nom;
    private String prenom;
    private String poste;

    public Bibliothecaire() {
    }

    public Bibliothecaire(String nom, String prenom, String poste) {
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
    }

    public Bibliothecaire(int id, String nom, String prenom, String poste) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Nom: %-25s | Poste: %s", id, getNomComplet(), poste);
    }
}
