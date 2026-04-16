/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author macbookairm1grey
 */
public class DatabaseConnection {

    // Database Credentials for XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    private static Connection connection = null;

    // Private constructor to prevent creating multiple instances
    private DatabaseConnection() {}

    /**
     * Gets the current database connection.
     * @return java.sql.Connection
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Ensure the MySQL Driver is loaded from your Libraries
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Establish the bridge
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database Connected Successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Ensure the JAR file is in your Libraries.");
        } catch (SQLException e) {
            System.err.println("SQL Connection Error: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the connection when the app shuts down.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database Connection Closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}