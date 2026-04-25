/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.IBookingService;
import carrentalsystem.models.Booking;
import java.sql.*;
import java.util.*;
/**
 *
 * @author macbookairm1grey
 */
public class BookingService implements IBookingService{
    @Override
    public int submitRequest(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (car_id, renter_id, start_date, end_date, total_price) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getCarId());
            ps.setInt(2, booking.getRenterId());
            ps.setDate(3, booking.getStartDate());
            ps.setDate(4, booking.getEndDate());
            ps.setDouble(5, booking.getTotalPrice());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public void confirmBooking(int bookingId) throws SQLException {
        String sql = "UPDATE bookings SET status = 'CONFIRMED' WHERE booking_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }
    }

    @Override
    public void completeTransaction(int bookingId) throws SQLException {
        // Stored procedure also creates receipt + sends notification
        String sql = "CALL complete_booking(?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }
    }

    @Override
    public void cancelBooking(int bookingId, String reason) throws SQLException {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Booking> getBookingsByRenter(int renterId) throws SQLException {
        String sql = "SELECT b.*, c.brand AS car_brand, c.model AS car_model, "
                + "c.owner_id FROM bookings b "
                + "JOIN cars c ON b.car_id = c.car_id "
                + "WHERE b.renter_id = ? ORDER BY b.created_at DESC";
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, renterId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapBooking(rs));
            }
        }
        return list;
    }

    @Override
    public List<Booking> getBookingsByOwner(int ownerId) throws SQLException {
        String sql = "SELECT b.*, c.brand AS car_brand, c.model AS car_model, "
                + "c.owner_id FROM bookings b "
                + "JOIN cars c ON b.car_id = c.car_id "
                + "WHERE c.owner_id = ? ORDER BY b.created_at DESC";
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapBooking(rs));
            }
        }
        return list;
    }

    // ── Helper ───────────────────────────────────────────────
    private Booking mapBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setCarId(rs.getInt("car_id"));
        b.setRenterId(rs.getInt("renter_id"));
        b.setStartDate(rs.getDate("start_date"));
        b.setEndDate(rs.getDate("end_date"));
        b.setTotalPrice(rs.getDouble("total_price"));
        b.setStatus(rs.getString("status"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            b.setCarBrand(rs.getString("car_brand"));
        } catch (SQLException ignored) {
        }
        try {
            b.setCarModel(rs.getString("car_model"));
        } catch (SQLException ignored) {
        }
        try {
            b.setOwnerId(rs.getInt("owner_id"));
        } catch (SQLException ignored) {
        }
        return b;
    }
}
