/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.IAdminService;
import carrentalsystem.models.Car;
import carrentalsystem.models.Ticket;
import java.sql.*;
import java.util.*;
/**
 *
 * @author macbookairm1grey
 */
public class AdminService implements IAdminService{
    @Override
    public List<Car> getPendingListings() throws SQLException {
        String sql = "SELECT * FROM cars WHERE status = 'PENDING_APPROVAL' "
                + "ORDER BY is_priority DESC, created_at ASC";
        List<Car> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCar(rs));
            }
        }
        return list;
    }

    @Override
    public void approveListing(int carId) throws SQLException {
        String sql = "CALL approve_listing(?, NULL)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.executeUpdate();
        }
    }

    @Override
    public void rejectListing(int carId, String reason) throws SQLException {
        String sql = "CALL reject_listing(?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setString(2, reason);
            ps.executeUpdate();
        }
    }

    @Override
    public void removeListing(int carId) throws SQLException {
        String sql = "UPDATE cars SET status = 'ARCHIVED' WHERE car_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Ticket> getOpenTickets() throws SQLException {
        String sql = "SELECT t.*, u.full_name AS user_full_name FROM tickets t "
                + "JOIN users u ON t.user_id = u.user_id "
                + "WHERE t.status = 'OPEN' ORDER BY t.created_at ASC";
        List<Ticket> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapTicket(rs));
            }
        }
        return list;
    }

    @Override
    public void replyToTicket(int ticketId, String reply) throws SQLException {
        String sql = "UPDATE tickets SET admin_reply = ? WHERE ticket_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, reply);
            ps.setInt(2, ticketId);
            ps.executeUpdate();
        }
    }

    @Override
    public void closeTicket(int ticketId) throws SQLException {
        String sql = "UPDATE tickets SET status = 'RESOLVED' WHERE ticket_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.executeUpdate();
        }
    }

    @Override
    public void issueSystemAlert(String message) throws SQLException {
        String sql = "CALL issue_system_alert(?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, message);
            ps.executeUpdate();
        }
    }

    @Override
    public void performDBMaintenance() throws SQLException {
        String sql = "CALL clean_old_sessions()";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    @Override
    public int countPendingListings() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cars WHERE status = 'PENDING_APPROVAL'";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int countOpenTickets() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tickets WHERE status = 'OPEN'";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // ── Helpers ───────────────────────────────────────────────
    private Car mapCar(ResultSet rs) throws SQLException {
        Car c = new Car();
        c.setCarId(rs.getInt("car_id"));
        c.setOwnerId(rs.getInt("owner_id"));
        c.setModel(rs.getString("model"));
        c.setBrand(rs.getString("brand"));
        c.setYear(rs.getInt("year"));
        c.setPlateNumber(rs.getString("plate_number"));
        c.setColor(rs.getString("color"));
        c.setSeats(rs.getInt("seats"));
        c.setFuelType(rs.getString("fuel_type"));
        c.setDescription(rs.getString("description"));
        c.setBasePrice(rs.getDouble("base_price"));
        c.setMileageLimit(rs.getInt("mileage_limit"));
        c.setFuelPolicy(rs.getString("fuel_policy"));
        c.setHouseRules(rs.getString("house_rules"));
        c.setImagePath(rs.getString("image_path"));
        c.setViewsCount(rs.getInt("views_count"));
        c.setPriority(rs.getBoolean("is_priority"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }

    private Ticket mapTicket(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setTicketId(rs.getInt("ticket_id"));
        t.setUserId(rs.getInt("user_id"));
        t.setSubject(rs.getString("subject"));
        t.setDescription(rs.getString("description"));
        t.setAdminReply(rs.getString("admin_reply"));
        t.setStatus(rs.getString("status"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            t.setUserFullName(rs.getString("user_full_name"));
        } catch (SQLException ignored) {
        }
        return t;
    }
}
