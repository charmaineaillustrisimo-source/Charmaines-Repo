/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.core;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author macbookairm1grey
 */
public class Test {
 
    public static void main(String[] args) {
        System.out.println("--- Database Connection Test ---");
        try {
            // Attempt to get a connection using your DBConnection class
            Connection conn = DBConnection.getConnection();
            
            if (conn != null) {
                System.out.println("✅ SUCCESS: Java is connected to XAMPP MySQL!");
                
                // Extra check: Can we actually read the Admin account you just saw?
                String query = "SELECT full_name FROM users WHERE role = 'ADMIN' LIMIT 1";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                if (rs.next()) {
                    System.out.println("✅ DATA CHECK: Found Admin User -> " + rs.getString("full_name"));
                }
                
                conn.close();
                System.out.println("---------------------------------");
                System.out.println("MEMBER 1 SETUP IS 100% COMPLETE!");
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: Connection failed.");
            System.out.println("Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

