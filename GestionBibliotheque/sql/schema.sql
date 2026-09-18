-- ============================================================
-- SCRIPT DE CRÉATION ET D'INITIALISATION DE LA BASE DE DONNÉES
-- Projet : Application de Gestion de Bibliothèque
-- Langage : SQL / MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS `bibliotheque` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `bibliotheque`;

-- Disable foreign key checks for clean recreation
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `retour`;
DROP TABLE IF EXISTS `emprunt`;
DROP TABLE IF EXISTS `bibliothecaire`;
DROP TABLE IF EXISTS `utilisateur`;
DROP TABLE IF EXISTS `livre`;
SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------------------
-- Table 1 : LIVRE
-- ------------------------------------------------------------
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

-- ------------------------------------------------------------
-- Table 2 : UTILISATEUR

CREATE TABLE IF NOT EXISTS `utilisateur` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `nom` VARCHAR(100) NOT NULL,
    `prenom` VARCHAR(100) NOT NULL,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `telephone` VARCHAR(30) NOT NULL,
    `adresse` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Table 3 : BIBLIOTHECAIRE
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothecaire` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `nom` VARCHAR(100) NOT NULL,
    `prenom` VARCHAR(100) NOT NULL,
    `poste` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Table 4 : EMPRUNT
-- ------------------------------------------------------------
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

-- ------------------------------------------------------------
-- Table 5 : RETOUR
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `retour` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `emprunt_id` INT NOT NULL UNIQUE,
    `date_retour` DATE NOT NULL,
    `retard` INT NOT NULL DEFAULT 0,
    `montant_amende` DOUBLE NOT NULL DEFAULT 0.0,
    CONSTRAINT `fk_retour_emprunt` FOREIGN KEY (`emprunt_id`) REFERENCES `emprunt`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- DONNÉES DE DÉMONSTRATION (SEED DATA)
-- ============================================================

-- Livres
INSERT INTO `livre` (`titre`, `auteur`, `categorie`, `isbn`, `annee_publication`, `quantite`, `disponible`) VALUES
('Le Petit Prince', 'Antoine de Saint-Exupéry', 'Conte', '978-2070612758', 1943, 5, TRUE),
('Les Misérables', 'Victor Hugo', 'Roman classique', '978-2253096337', 1862, 3, TRUE),
('L''Étranger', 'Albert Camus', 'Roman philosophique', '978-2070360024', 1942, 2, TRUE),
('Clean Code', 'Robert C. Martin', 'Informatique', '978-0132350884', 2008, 4, TRUE),
('Java: The Complete Reference', 'Herbert Schildt', 'Informatique', '978-1260440232', 2021, 1, TRUE),
('1984', 'George Orwell', 'Science-Fiction', '978-2070368228', 1949, 0, FALSE);

-- Utilisateurs
INSERT INTO `utilisateur` (`nom`, `prenom`, `email`, `telephone`, `adresse`) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', '0601020304', '12 Rue de la Paix, Paris'),
('Martin', 'Sophie', 'sophie.martin@email.com', '0611223344', '45 Avenue des Champs, Lyon'),
('Benali', 'Amine', 'amine.benali@email.com', '0655667788', '8 Boulevard Mohamed V, Casablanca');

-- Bibliothécaires
INSERT INTO `bibliothecaire` (`nom`, `prenom`, `poste`) VALUES
('Leroy', 'Claire', 'Responsable Bibliothèque'),
('Moreau', 'Lucas', 'Assistant Gestionnaire');

-- Emprunts de démonstration
INSERT INTO `emprunt` (`livre_id`, `utilisateur_id`, `date_emprunt`, `date_retour_prevue`, `date_retour_effective`, `statut`) VALUES
(1, 1, '2026-09-01', '2026-09-15', '2026-09-14', 'RETOURNE'),
(6, 2, '2026-09-05', '2026-09-19', NULL, 'EN_COURS'),
(2, 3, '2026-08-20', '2026-09-03', NULL, 'EN_RETARD');

-- Retours de démonstration
INSERT INTO `retour` (`emprunt_id`, `date_retour`, `retard`, `montant_amende`) VALUES
(1, '2026-09-14', 0, 0.0);
