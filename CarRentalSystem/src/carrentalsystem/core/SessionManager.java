/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.core;
import carrentalsystem.models.User;
import java.sql.*;
/**
 *
 * @author macbookairm1grey
 */
public class SessionManager {
    private static User currentUser = null;
    private static int currentSessionId = -1;

    public static void startSession(User user) throws SQLException {
        currentUser = user;
        String sql = "INSERT INTO sessions (user_id, current_state) VALUES (?,'ONLINE')";
try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                currentSessionId = rs.getInt(1);
            }
        }
// Update last_login
String upd = "UPDATE users SET last_login=NOW() WHERE user_id=?";
        try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(upd)) {
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
        }
    }

    public static void setIdle() throws SQLException {
        String sql = "UPDATE sessions SET current_state='IDLE' WHERE session_id =  ?";
try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, currentSessionId);
            ps.executeUpdate();
        }
    }

    public static void setOnline() throws SQLException {
        String sql = "UPDATE sessions SET current_state ='ONLINE',last_activity = NOW() WHERE session_id =  ?";
try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, currentSessionId);
            ps.executeUpdate();
        }
    }

    public static void endSession() throws SQLException {
        if (currentSessionId == -1) {
            return;
        }
        String sql = "UPDATE sessions SET current_state='OFFLINE' WHERE session_id =  ?";
try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, currentSessionId);
            ps.executeUpdate();
        }
        currentUser = null;
        currentSessionId = -1;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getSessionId() {
        return currentSessionId;
    }
}
