/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.IMessageService;
import carrentalsystem.models.Message;
import java.sql.*;
import java.util.*;

/**
 *
 * @author macbookairm1grey
 */
public class MessageService implements IMessageService{
    @Override
    public List<Message> getConversations(int userId) throws SQLException {
        // Gets the latest message for each unique conversation partner
        String sql
                = "SELECT m.*, "
                + "  u.full_name AS sender_name, "
                + "  CONCAT(c.brand, ' ', c.model) AS car_name "
                + "FROM messages m "
                + "JOIN users u ON u.user_id = CASE "
                + "  WHEN m.sender_id = ? THEN m.receiver_id ELSE m.sender_id END "
                + "LEFT JOIN cars c ON c.car_id = m.car_id "
                + "WHERE m.message_id IN ( "
                + "  SELECT MAX(m2.message_id) FROM messages m2 "
                + "  WHERE m2.sender_id = ? OR m2.receiver_id = ? "
                + "  GROUP BY LEAST(m2.sender_id, m2.receiver_id), "
                + "           GREATEST(m2.sender_id, m2.receiver_id) "
                + ") ORDER BY m.created_at DESC";

        List<Message> list = new ArrayList<>();
        try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMessage(rs));
            }
        }
        return list;
    }

    @Override
    public List<Message> getThread(int userId, int otherUserId, int carId)
            throws SQLException {
        String sql
                = "SELECT m.*, u.full_name AS sender_name "
                + "FROM messages m "
                + "JOIN users u ON u.user_id = m.sender_id "
                + "WHERE ((m.sender_id=? AND m.receiver_id=?) "
                + "    OR (m.sender_id=? AND m.receiver_id=?)) "
                + "ORDER BY m.created_at ASC";

        List<Message> list = new ArrayList<>();
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, otherUserId);
            ps.setInt(3, otherUserId);
            ps.setInt(4, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMessage(rs));
            }
        }
        return list;
    }

    @Override
    public void sendMessage(int senderId, int receiverId, int carId, String content)
            throws SQLException {
        String sql = "INSERT INTO messages "
                + "(sender_id, receiver_id, car_id, content) VALUES (?,?,?,?)";
        try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            if (carId > 0) {
                ps.setInt(3, carId);
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, content);
            ps.executeUpdate();
        }
    }

    @Override
    public void markThreadRead(int receiverId, int senderId) throws SQLException {
        String sql = "UPDATE messages SET is_read = TRUE "
                + "WHERE receiver_id = ? AND sender_id = ?";
        try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, receiverId);
            ps.setInt(2, senderId);
            ps.executeUpdate();
        }
    }

    @Override
    public int countUnread(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM messages "
                + "WHERE receiver_id = ? AND is_read = FALSE";
        try (PreparedStatement ps
                = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // ── private mapper ──────────────────────────────────────
    private Message mapMessage(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setMessageId(rs.getInt("message_id"));
        m.setSenderId(rs.getInt("sender_id"));
        m.setReceiverId(rs.getInt("receiver_id"));
        m.setCarId(rs.getInt("car_id"));
        m.setBookingId(rs.getInt("booking_id"));
        m.setContent(rs.getString("content"));
        m.setRead(rs.getBoolean("is_read"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            m.setSenderName(rs.getString("sender_name"));
        } catch (Exception ignored) {
        }
        try {
            m.setCarName(rs.getString("car_name"));
        } catch (Exception ignored) {
        }
        return m;
    }

}
