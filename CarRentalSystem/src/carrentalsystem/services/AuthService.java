/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.IAuthService;
import carrentalsystem.models.User;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author macbookairm1grey
 */
public class AuthService implements IAuthService{
    @Override
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND status = 'ACTIVE'";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                if (BCrypt.checkpw(password, hashed)) {
                    return mapUser(rs);
                }
            }
        }
        return null; // wrong credentials
    }

    @Override
    public void register(String fullName, String username,
                         String email, String password) throws SQLException {
        // Check if username already taken
        String checkSql = "SELECT user_id FROM users WHERE username = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(checkSql)) {
            ps.setString(1, username);
            if (ps.executeQuery().next()) {
                throw new SQLException("Username already taken. Please choose another.");
            }
        }
        // Check if email already registered
        String checkEmail = "SELECT user_id FROM users WHERE email = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(checkEmail)) {
            ps.setString(1, email);
            if (ps.executeQuery().next()) {
                throw new SQLException("Email already registered. Please log in instead.");
            }
        }
        // Hash password and insert
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO users (username, full_name, email, password) "
                   + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, fullName);
            ps.setString(3, email);
            ps.setString(4, hashed);
            ps.executeUpdate();
        }
    }

    @Override
    public void sendPasswordReset(String email) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            if (!ps.executeQuery().next()) {
                throw new SQLException("No account found with that email.");
            }
        }
        // TODO: integrate email API here
    }

    @Override
    public void banUser(String email) throws SQLException {
        String sql = "UPDATE users SET status = 'BANNED' WHERE email = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }
    
    // ── Helper ───────────────────────────────────────────────
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        
        // Core ID and Credentials
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setFullName(rs.getString("full_name"));
        u.setPassword(rs.getString("password"));

        // Roles and Tiers
        u.setRole(rs.getString("role"));
        u.setTier(rs.getString("user_tier")); // Note: This matches the 'user_tier' column in SQL

        // Status and Verification (New Columns)
        u.setStatus(rs.getString("status"));
        u.setVerified(rs.getBoolean("is_verified"));

        // Timestamps
        u.setLastLogin(rs.getTimestamp("last_login"));
        u.setCreatedAt(rs.getTimestamp("created_at"));

        return u;
    }
}
