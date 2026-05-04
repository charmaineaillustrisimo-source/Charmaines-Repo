/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.core;
import java.sql.*;
/**
 *
 * @author macbookairm1grey
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // change this
    private static final String PASS = ""; // change this
    //private static Connection conn = null;
    public static Connection getConnection() throws SQLException {
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        // Always return a NEW connection so threads don't crash each other
        return DriverManager.getConnection(URL, USER, PASS);
    }
    /*public static void close() {
        try { if (conn != null && !conn.isClosed()) conn.close(); }
        catch (SQLException e) { e.printStackTrace(); }
    }*/
}
