package net.mauki.maukiseasonpl.core;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.*;

public class LiteSQL {

    //Create variables
    private static Connection conn;

    //isConected boolean (for if)
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

    //Create connect method
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


    //Create disconnect method
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


    //Create Update-Method
    public static void onUpdate(String sql) {
        try {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Create Query-Method
    @Nullable
    public static ResultSet onQuery(String sql) {
        try {
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTables() {
        onUpdate("CREATE TABLE IF NOT EXISTS connections(uuid VARCHAR, code VARCHAR, discord_id VARCHAR)");
    }

}
