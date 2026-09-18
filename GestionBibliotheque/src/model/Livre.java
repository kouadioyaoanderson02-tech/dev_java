package model;

/**
 * Modèle représentant un livre dans la bibliothèque.
 */
public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private String categorie;
    private String isbn;
    private int anneePublication;
    private int quantite;
    private boolean disponible;

    // Constructeur vide
    public Livre() {
    }

    // Constructeur sans ID (pour l'insertion)
    public Livre(String titre, String auteur, String categorie, String isbn, int anneePublication, int quantite) {
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
        this.quantite = quantite;
        this.disponible = (quantite > 0);
    }

    // Constructeur complet
    public Livre(int id, String titre, String auteur, String categorie, String isbn, int anneePublication, int quantite, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
        this.quantite = quantite;
        this.disponible = disponible;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        this.disponible = (quantite > 0);
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Titre: %-30s | Auteur: %-20s | Catégorie: %-15s | ISBN: %-15s | Année: %d | Stock: %d | Disponible: %s",
                id, titre, auteur, categorie, isbn, anneePublication, quantite, disponible ? "OUI" : "NON");
    }
}
