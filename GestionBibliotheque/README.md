# Application de Gestion de Bibliothèque (Java 25 & MySQL)

Application professionnelle de gestion de bibliothèque développée en Java 25 avec accès aux données via JDBC et MySQL, suivant les meilleures pratiques de Programmation Orientée Objet (POO) et l'architecture MVC.

---

##  Technologies utilisées

- **Java 25** (JDK Temurin 25.0.4)
- **JDBC** (Java Database Connectivity)
- **MySQL** (Driver `mysql-connector-j-9.2.0.jar`)
- **Architecture MVC** (Model-View-Controller) & Repositories
- **Codes Couleurs ANSI** pour l'interface console

---

## 📁 Architecture du Projet

```text
GestionBibliotheque/
├── bin/                        # Fichiers compilés (.class)
├── lib/                        # Fichiers JAR externes (mysql-connector-j-9.2.0.jar)
├── sql/
│   └── schema.sql              # Script SQL DDL/DML de la BDD bibliotheque
├── src/
│   ├── database/               # Connexion JDBC Singleton & Initialisation auto
│   │   ├── DatabaseConnection.java
│   │   └── DatabaseInitializer.java
│   ├── model/                  # Entités Métier (POJO / Beans)
│   │   ├── Livre.java
│   │   ├── Utilisateur.java
│   │   ├── Bibliothecaire.java
│   │   ├── StatutEmprunt.java
│   │   ├── Emprunt.java
│   │   └── Retour.java
│   ├── repository/             # Accès aux données JDBC (CRUD SQL)
│   │   ├── LivreRepository.java
│   │   ├── UtilisateurRepository.java
│   │   ├── BibliothecaireRepository.java
│   │   ├── EmpruntRepository.java
│   │   └── RetourRepository.java
│   ├── service/                # Logique Métier & Règles d'Affaires
│   │   ├── LivreService.java
│   │   ├── UtilisateurService.java
│   │   ├── BibliothecaireService.java
│   │   ├── EmpruntService.java
│   │   └── StatistiqueService.java
│   ├── utils/                  # Saisie console, gestion des dates, couleurs ANSI
│   │   ├── ConsoleColor.java
│   │   ├── DateUtil.java
│   │   └── InputUtil.java
│   └── main/                   # Point d'entrée de l'application (Console CLI)
│       └── Main.java
├── build.bat                   # Script de compilation batch Windows
├── run.bat                     # Script d'exécution batch Windows
└── README.md
```

---

## 🗄️ Base de données MySQL

Le script SQL complet d'initialisation se trouve dans `sql/schema.sql`.

### Entités & Tables :
1. **`livre`** : `id`, `titre`, `auteur`, `categorie`, `isbn`, `annee_publication`, `quantite`, `disponible`
2. **`utilisateur`** : `id`, `nom`, `prenom`, `email`, `telephone`, `adresse`
3. **`bibliothecaire`** : `id`, `nom`, `prenom`, `poste`
4. **`emprunt`** : `id`, `livre_id`, `utilisateur_id`, `date_emprunt`, `date_retour_prevue`, `date_retour_effective`, `statut`
5. **`retour`** : `id`, `emprunt_id`, `date_retour`, `retard`, `montant_amende`

> **Note :** L'application crée et initialise automatiquement la base de données `bibliotheque` ainsi que toutes ses tables et données de démonstration au premier lancement s'ils n'existent pas déjà.

---

## ⚡ Règles Métier Implémentées

1. **Emprunt d'un livre :**
   - Impossibilité d'emprunter un livre si son stock est à 0 (`quantite = 0`).
   - Impossibilité d'emprunter un livre marqué comme indisponible (`disponible = false`).
   - Décrémentation automatique du stock lors de l'emprunt.
2. **Suppression d'un livre :**
   - Interdiction de supprimer un livre qui possède des emprunts actifs (`EN_COURS` ou `EN_RETARD`).
3. **Retour d'un livre & Penalités :**
   - Réincrémentation automatique du stock et mise à jour de la disponibilité.
   - Calcul automatique des jours de retard par rapport à la date de retour prévue.
   - Calcul et enregistrement des amendes (1,50 € / jour de retard).

---

## 🚀 Compilation & Exécution

### 1. Via les fichiers batch :
- Pour compiler : Double-cliquez sur `build.bat` ou lancez-le dans le terminal.
- Pour exécuter : Double-cliquez sur `run.bat`.

### 2. Via ligne de commande (PowerShell / Terminal) :

**Compilation :**
```bash
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }
javac -encoding UTF-8 -d bin -cp "lib/mysql-connector-j-9.2.0.jar" (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)
```

**Exécution :**
```bash
java -cp "bin;lib/mysql-connector-j-9.2.0.jar" main.Main
```
