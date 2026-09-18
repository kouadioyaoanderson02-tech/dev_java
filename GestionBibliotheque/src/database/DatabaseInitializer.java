package database;

import utils.ConsoleColor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Initialiseur automatique de la base de données et des tables MySQL.
 */
public class DatabaseInitializer {

    /**
     * Initialise la base de données 'bibliotheque' et crée les tables + données initiales si nécessaire.
     */
    public static void initializeDatabase() {
        System.out.println(ConsoleColor.CYAN + "Vérification et initialisation de la base de données MySQL..." + ConsoleColor.RESET);

        try (Connection serverConn = DatabaseConnection.getBaseServerConnection();
             Statement stmt = serverConn.createStatement()) {

            // 1. Créer la base de données si elle n'existe pas
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `bibliotheque` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println(ConsoleColor.GREEN + "[OK] Base de données 'bibliotheque' prête." + ConsoleColor.RESET);

        } catch (Exception e) {
            System.err.println(ConsoleColor.RED + "Impossible de se connecter au serveur MySQL sur localhost:3306." + ConsoleColor.RESET);
            System.err.println(ConsoleColor.YELLOW + "Assurez-vous que le service MySQL (ex: WampServer/XAMPP/MySQL Service) est démarré." + ConsoleColor.RESET);
            System.err.println(ConsoleColor.RED + "Erreur: " + e.getMessage() + ConsoleColor.RESET);
            return;
        }

        // 2. Créer les tables et données si besoin via la connexion directe à 'bibliotheque'
        try (Connection dbConn = DatabaseConnection.getConnection();
             Statement stmt = dbConn.createStatement()) {

            // Table Livre
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS `livre` (
                    `id` INT AUTO_INCREMENT PRIMARY KEY,
                    `titre` VARCHAR(255) NOT NULL,
                    `auteur` VARCHAR(255) NOT NULL,
                    `categorie` VARCHAR(100) NOT NULL,
                    `isbn` VARCHAR(20) NOT NULL UNIQUE,
                    `annee_publication` INT NOT NULL,
                    `quantite` INT NOT NULL DEFAULT 1,
                    `disponible` BOOLEAN NOT NULL DEFAULT TRUE,
                    CONSTRAINT `chk_livre_quantite` CHECK (`quantite` >= 0)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """);

            // Table Utilisateur
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS `utilisateur` (
                    `id` INT AUTO_INCREMENT PRIMARY KEY,
                    `nom` VARCHAR(100) NOT NULL,
                    `prenom` VARCHAR(100) NOT NULL,
                    `email` VARCHAR(150) NOT NULL UNIQUE,
                    `telephone` VARCHAR(30) NOT NULL,
                    `adresse` VARCHAR(255) NOT NULL
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """);

            // Table Bibliothecaire
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS `bibliothecaire` (
                    `id` INT AUTO_INCREMENT PRIMARY KEY,
                    `nom` VARCHAR(100) NOT NULL,
                    `prenom` VARCHAR(100) NOT NULL,
                    `poste` VARCHAR(100) NOT NULL
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """);

            // Table Emprunt
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS `emprunt` (
                    `id` INT AUTO_INCREMENT PRIMARY KEY,
                    `livre_id` INT NOT NULL,
                    `utilisateur_id` INT NOT NULL,
                    `date_emprunt` DATE NOT NULL,
                    `date_retour_prevue` DATE NOT NULL,
                    `date_retour_effective` DATE NULL,
                    `statut` VARCHAR(20) NOT NULL DEFAULT 'EN_COURS',
                    CONSTRAINT `fk_emprunt_livre` FOREIGN KEY (`livre_id`) REFERENCES `livre`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
                    CONSTRAINT `fk_emprunt_utilisateur` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """);

            // Table Retour
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS `retour` (
                    `id` INT AUTO_INCREMENT PRIMARY KEY,
                    `emprunt_id` INT NOT NULL UNIQUE,
                    `date_retour` DATE NOT NULL,
                    `retard` INT NOT NULL DEFAULT 0,
                    `montant_amende` DOUBLE NOT NULL DEFAULT 0.0,
                    CONSTRAINT `fk_retour_emprunt` FOREIGN KEY (`emprunt_id`) REFERENCES `emprunt`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """);

            System.out.println(ConsoleColor.GREEN + "[OK] Structure des tables vérifiée avec succès." + ConsoleColor.RESET);

        } catch (Exception e) {
            System.err.println(ConsoleColor.RED + "Erreur lors de l'initialisation des tables: " + e.getMessage() + ConsoleColor.RESET);
        }
    }
}
