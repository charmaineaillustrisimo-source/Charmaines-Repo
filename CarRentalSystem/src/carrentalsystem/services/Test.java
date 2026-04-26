package carrentalsystem.services;

import carrentalsystem.models.User;
import carrentalsystem.services.AuthService;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import carrentalsystem.core.DBConnection;

/**
 * Diagnostic Test for Car Rental System Authentication
 * @author Charmaine A. Illustrisimo
 */
public class Test {

    public static void main(String[] args) {
        String testEmail = "admin@carrental.com";
        String testPass = "admin123";

        System.out.println("=== STARTING AUTHENTICATION DIAGNOSTIC ===");

        // 1. Library Self-Test: Does BCrypt work in isolation?
        System.out.print("[1/3] BCrypt Library Test: ");
        String sampleHash = "$2a$12$R9h/LIPzIVf5nVm1pYBXbe4gnK9Yw9nEclV53R7.Z.SBl2oG.oXmK";
        if (BCrypt.checkpw("admin123", sampleHash)) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED! Check your jbcrypt-0.4.jar");
        }

        try {
            // 2. Database Data Check: What is actually stored?
            System.out.println("[2/3] Database Entry Check:");
            String sql = "SELECT password, status FROM users WHERE email = ?";
            try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
                ps.setString(1, testEmail);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    String dbStatus = rs.getString("status");
                    System.out.println("   -> Password in DB: " + dbPassword);
                    System.out.println("   -> Status in DB:   " + dbStatus);
                    
                    if (!dbPassword.startsWith("$2a$")) {
                        System.out.println("   !! WARNING: Password is NOT hashed in DB !!");
                    }
                } else {
                    System.out.println("   !! ERROR: No user found with email " + testEmail + " !!");
                }
            }

            // 3. Service Logic Test: Does the AuthService.login() work?
            System.out.println("[3/3] AuthService Logic Test:");
            AuthService auth = new AuthService();
            User u = auth.login(testEmail, testPass);

            if (u != null) {
                System.out.println("   -> SUCCESS! Logged in as: " + u.getFullName());
                System.out.println("=== DIAGNOSTIC COMPLETE: SYSTEM READY ===");
            } else {
                System.out.println("   -> FAILURE: AuthService returned null.");
                System.out.println("   Possible cause: Variable mismatch in AuthService or incorrect status.");
            }

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General Error: " + e.getMessage());
        }
    }
}