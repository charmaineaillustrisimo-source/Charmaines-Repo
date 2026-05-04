/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.interfaces.IHistoryService;
import carrentalsystem.models.History;
import carrentalsystem.core.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author macbookairm1grey
 */
public class HistoryService implements IHistoryService {
    @Override
    public void logBrowsingActivity(int userId, int carId) throws SQLException {
        String sql = "INSERT INTO browsing_history (user_id, car_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, carId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<History> getBrowsingHistory(int userId) throws SQLException {
        List<History> historyList = new ArrayList<>();
        String sql = "SELECT h.viewed_at, c.car_id, c.brand, c.model, c.price_per_day, c.image_path "
                + "FROM browsing_history h JOIN cars c ON h.car_id = c.car_id "
                + "WHERE h.user_id = ? ORDER BY h.viewed_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    History h = new History();
                    h.setCarId(rs.getInt("car_id"));
                    h.setBrand(rs.getString("brand"));
                    h.setModel(rs.getString("model"));
                    h.setPrice(rs.getDouble("price_per_day"));
                    h.setImagePath(rs.getString("image_path"));
                    h.setCreatedAt(rs.getTimestamp("viewed_at"));
                    historyList.add(h);
                }
            }
        }
        return historyList;
    }

    @Override
    public List<History> getRentalHistory(int userId) throws SQLException {
        List<History> rentalList = new ArrayList<>();
        String sql = "SELECT b.created_at, c.car_id, c.brand, c.model, c.price_per_day, c.image_path "
                + "FROM bookings b JOIN cars c ON b.car_id = c.car_id "
                + "WHERE b.renter_id = ? AND b.status = 'SUCCESSFUL' ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    History h = new History();
                    h.setCarId(rs.getInt("car_id"));
                    h.setBrand(rs.getString("brand"));
                    h.setModel(rs.getString("model"));
                    h.setPrice(rs.getDouble("price_per_day"));
                    h.setImagePath(rs.getString("image_path"));
                    h.setCreatedAt(rs.getTimestamp("created_at"));
                    rentalList.add(h);
                }
            }
        }
        return rentalList;
    }
}
