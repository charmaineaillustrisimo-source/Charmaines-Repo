/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.INotificationService;
import carrentalsystem.models.Notification;
import java.sql.*;
import java.util.*;
/**
 *
 * @author macbookairm1grey
 */
public class NotificationService implements INotificationService{
    @Override
    public void send(int userId, String message, String type) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message, type) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.setString(3, type);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Notification> getUnread(int userId) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = FALSE "
                + "ORDER BY created_at DESC";
        List<Notification> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapNotif(rs));
            }
        }
        return list;
    }

    @Override
    public void markAllRead(int userId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public int countUnread(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications "
                + "WHERE user_id = ? AND is_read = FALSE";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // ── Helper ───────────────────────────────────────────────
    private Notification mapNotif(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setNotifId(rs.getInt("notif_id"));
        n.setUserId(rs.getInt("user_id"));
        n.setMessage(rs.getString("message"));
        n.setType(rs.getString("type"));
        n.setRead(rs.getBoolean("is_read"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        return n;
    }
}
