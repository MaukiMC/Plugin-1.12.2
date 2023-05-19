package net.mauki.maukiseasonpl.core;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.*;

/**
 * Manager for the SQL-Database
 */
public class LiteSQL {

    /**
     * The connection of the SQL-connection
     */
    private static Connection conn;

    /**
     * Check if there's already a connection to the database
     * @return If there's already a connection to the database
     */
    public static boolean isConnected() {
        try {
            if (conn.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Establish a connection to the database
     */
    public static void connect() {
        conn = null;
        try {
            File file = new File("./database.db");
            if (!file.exists()) {
                file.createNewFile();
            }
            String url = "jdbc:sqlite:" + file.getPath();
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect from the database
     */
    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send an UPDATE SQL-Statement
     * @param sql The UPDATE-Statement you want to execute
     * @return boolean if the statement was successfully
     */
    public static boolean onUpdate(String sql) {
        try {
            return conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send an QUERY SQL-Statement
     * @param sql The QUERY-Statement you want to execute
     * @return The output of the input
     */
    @Nullable
    public static ResultSet onQuery(String sql) {
        try {
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates the tables for the plugin (if they don't exists)
     */
    public static void createTables() {
        onUpdate("CREATE TABLE IF NOT EXISTS connections(uuid VARCHAR, code VARCHAR, discord_id VARCHAR)");
    }

}
