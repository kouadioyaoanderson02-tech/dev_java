package database;

import utils.ConsoleColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton de gestion de la connexion JDBC à la base de données MySQL.
 */
public class DatabaseConnection {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "bibliotheque";
    private static final String PARAMS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    private static final String BASE_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + PARAMS;
    private static final String FULL_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + PARAMS;

    // Utilisateur et mots de passe possibles pour une compatibilité maximale (ex: root sans mdp ou root/root)
    private static String dbUser = "root";
    private static String dbPassword = "";

    private static Connection connection = null;

    static {
        try {
            // Charger le driver MySQL Connector/J
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(ConsoleColor.RED + "Driver JDBC MySQL introuvable dans le classpath." + ConsoleColor.RESET);
        }
    }

    private DatabaseConnection() {
        // Constructeur privé pour pattern Singleton
    }

    /**
     * Obtient la connexion vers la base de données 'bibliotheque'.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = createConnection(FULL_URL);
        }
        return connection;
    }

    /**
     * Obtient une connexion vers le serveur MySQL (sans spécifier de base) pour l'initialisation.
     */
    public static Connection getBaseServerConnection() throws SQLException {
        return createConnection(BASE_URL);
    }

    /**
     * Tente la connexion avec mot de passe vide puis 'root' en cas d'échec.
     */
    private static Connection createConnection(String url) throws SQLException {
        try {
            return DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (SQLException e) {
            // Si le mot de passe vide échoue, essayer "root"
            if ("".equals(dbPassword)) {
                try {
                    dbPassword = "root";
                    return DriverManager.getConnection(url, dbUser, dbPassword);
                } catch (SQLException ex) {
                    dbPassword = ""; // reset
                    throw ex;
                }
            }
            throw e;
        }
    }

    /**
     * Ferme proprement la connexion si elle est ouverte.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(ConsoleColor.RED + "Erreur lors de la fermeture de la connexion BDD: " + e.getMessage() + ConsoleColor.RESET);
            }
        }
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static void setCredentials(String user, String password) {
        dbUser = user;
        dbPassword = password;
        connection = null;
    }
}
