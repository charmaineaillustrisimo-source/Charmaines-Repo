/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author macbookairm1grey
 */
public class MySQLIntegrationTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Car Rental: Database Integration Test ===");
        
        // 1. Test the Connection
        // This will now work because the package name matches DatabaseConnection.java
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("STOP: Connection failed. Is XAMPP running?");
            return;
        }

        try {
            // 2. Ask for user input in the console
            System.out.print("Enter a unique username to register: ");
            String inputUsername = scanner.nextLine();
            
            System.out.print("Enter your full name: ");
            String inputFullName = scanner.nextLine();

            // 3. Save to MySQL
            String insertSQL = "INSERT INTO users (username, password, email, full_name, user_tier) VALUES (?, ?, ?, ?, 'FREE')";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, inputUsername);
                pstmt.setString(2, "testPass123"); // Placeholder password
                pstmt.setString(3, inputUsername + "@test.com"); // Generating a dummy email
                pstmt.setString(4, inputFullName);
                
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("\nSUCCESS: " + inputFullName + " has been saved to the 'users' table!");
                }
            }

            // 4. Retrieve from MySQL to verify
            System.out.println("\n--- Verifying Database Contents ---");
            String selectSQL = "SELECT user_id, username, full_name, user_tier FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setString(1, inputUsername);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("Retrieved from DB -> ID: " + rs.getInt("user_id"));
                    System.out.println("Retrieved from DB -> Username: " + rs.getString("username"));
                    System.out.println("Retrieved from DB -> Tier: " + rs.getString("user_tier"));
                }
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("ERROR: That username already exists in MySQL. Try a different one!");
            } else {
                System.out.println("SQL Error: " + e.getMessage());
            }
        } finally {
            System.out.println("\n=== Test Finished ===");
        }
    }
}
