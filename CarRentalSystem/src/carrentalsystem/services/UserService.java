/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.IUserService;
import carrentalsystem.models.User;
import java.sql.*;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author macbookairm1grey
 */
public class UserService implements IUserService{
    @Override
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        }
        return null;
    }

    @Override
    public void updateProfile(User user) throws SQLException {
        // Only re-hash if it's a new plain-text password
        String password = user.getPassword();
        if (!password.startsWith("$2a$")) {
            password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        }
        String sql = "UPDATE users SET full_name=?, email=?, username=?, password=? "
                + "WHERE user_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getUsername());
            ps.setString(4, password);
            ps.setInt(5, user.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAccount(int userId) throws SQLException {
        // ON DELETE CASCADE handles sessions, bookings, cars
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public void upgradeToPro(int userId) throws SQLException {
        String sql = "UPDATE users SET tier = 'PRO' WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users WHERE role = 'USER' ORDER BY created_at DESC";
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        }
        return list;
    }

    @Override
    public void suspendUser(int userId) throws SQLException {
        String sql = "UPDATE users SET status = 'BANNED' WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    // ── Helper ───────────────────────────────────────────────
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setTier(rs.getString("tier"));
        u.setStatus(rs.getString("status"));
        u.setVerified(rs.getBoolean("is_verified"));
        u.setLastLogin(rs.getTimestamp("last_login"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
