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
        String sql = "INSERT INTO bookings (car_id, renter_id, start_date, end_date, total_price, image_path, pickup_location, return_location) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getCarId());
            ps.setInt(2, booking.getRenterId());
            ps.setDate(3, booking.getStartDate());
            ps.setDate(4, booking.getEndDate());
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, booking.getImagePath());
            ps.setString(7, booking.getPickupLocation());
            ps.setString(8, booking.getReturnLocation());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int generatedId = keys.getInt(1);  // ← capture it first

                // ── AUTO-MESSAGE: send booking card to owner's inbox ──
                try {
                    String ownerSql = "SELECT owner_id FROM cars WHERE car_id = ?";
                    try (PreparedStatement ownerPs = DBConnection.getConnection()
                            .prepareStatement(ownerSql)) {
                        ownerPs.setInt(1, booking.getCarId());
                        ResultSet ownerRs = ownerPs.executeQuery();
                        if (ownerRs.next()) {
                            int ownerId = ownerRs.getInt("owner_id");
                            int renterId = booking.getRenterId();

                            String msgSql
                                    = "INSERT INTO messages (sender_id, receiver_id, car_id, booking_id, content) "
                                    + "VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement msgPs = DBConnection.getConnection()
                                    .prepareStatement(msgSql)) {
                                msgPs.setInt(1, renterId);
                                msgPs.setInt(2, ownerId);
                                msgPs.setInt(3, booking.getCarId());
                                msgPs.setInt(4, generatedId);
                                msgPs.setString(5, "BOOKING_CARD::" + generatedId);
                                msgPs.executeUpdate();
                                
                                // Notify the owner about the new booking request
                                carrentalsystem.services.NotificationService notifSvc
                                        = new carrentalsystem.services.NotificationService();

                                // Get the car name for the message
                                String carNameSql = "SELECT CONCAT(brand, ' ', model) FROM cars WHERE car_id = ?";
                                String carName = "a car";
                                try (java.sql.PreparedStatement carPs
                                        = carrentalsystem.core.DBConnection.getConnection()
                                                .prepareStatement(carNameSql)) {
                                            carPs.setInt(1, booking.getCarId());
                                            java.sql.ResultSet carRs = carPs.executeQuery();
                                            if (carRs.next()) {
                                                carName = carRs.getString(1);
                                            }
                                        } catch (Exception ignored) {
                                        }

                                        // Notify owner: new booking request
                                        notifSvc.notify(ownerId,
                                                "New booking request for your " + carName + ". Tap to review.",
                                                "RENTAL");

                                        // Notify renter: booking submitted
                                        notifSvc.notify(renterId,
                                                "Your booking request for " + carName + " has been submitted. Waiting for owner approval.",
                                                "RENTAL");
                            }
                            
                            String notifSql = "INSERT INTO notifications (user_id, message, type, is_read) VALUES (?, ?, 'RENTAL', 0)";
                            try (PreparedStatement nPs = DBConnection.getConnection().prepareStatement(notifSql)) {
                                nPs.setInt(1, ownerId);
                                nPs.setString(2, "You have a new booking request for car #" + booking.getCarId());
                                nPs.executeUpdate();
                            }
                        }
                    }
                } catch (Exception e) {
                    // Don't fail the booking if messaging fails
                    System.err.println("[BookingService] Auto-message failed: " + e.getMessage());
                }
                // ── END AUTO-MESSAGE ──────────────────────────────────

                return generatedId;  // ← return it after the message is sent
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
    
    public void updateStatus(int bookingId, String newStatus) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    public carrentalsystem.models.Booking getBookingById(int bookingId) throws SQLException {
        String sql
                = "SELECT b.*, "
                + "  c.brand AS car_brand, c.model AS car_model, "
                + "  c.owner_id "
                + "FROM bookings b "
                + "JOIN cars c ON c.car_id = b.car_id "
                + "WHERE b.booking_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                carrentalsystem.models.Booking b = new carrentalsystem.models.Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setCarId(rs.getInt("car_id"));
                b.setRenterId(rs.getInt("renter_id"));
                b.setOwnerId(rs.getInt("owner_id"));
                b.setStartDate(rs.getDate("start_date"));
                b.setEndDate(rs.getDate("end_date"));
                b.setTotalPrice(rs.getDouble("total_price"));
                b.setStatus(rs.getString("status"));
                b.setCarBrand(rs.getString("car_brand"));
                b.setCarModel(rs.getString("car_model"));
                b.setPickupLocation(rs.getString("pickup_location"));
                b.setReturnLocation(rs.getString("return_location"));

                return b;
            }
        }
        return null;
    }

    @Override
    public List<Booking> getBookingsByRenter(int renterId) throws SQLException {
        String sql = "SELECT b.*, c.brand AS car_brand, c.model AS car_model, "
                + "c.image_path, c.owner_id, u.full_name AS owner_name "
                + "FROM bookings b "
                + "JOIN cars c ON b.car_id = c.car_id "
                + "JOIN users u ON c.owner_id = u.user_id "
                + "WHERE b.renter_id = ? ORDER BY b.created_at DESC";
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, renterId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking b = mapBooking(rs);
                b.setOwnerName(rs.getString("owner_name"));
                list.add(b);
            }
        }
        return list;
    }

    @Override
    public List<Booking> getBookingsByOwner(int ownerId) throws SQLException {
        String sql = "SELECT b.*, c.brand AS car_brand, c.model AS car_model, "
                + "c.image_path, c.owner_id FROM bookings b "
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
            b.setImagePath (rs.getString("image_path"));
        } catch (SQLException ignored) {
        }
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
        try {
            b.setPickupLocation(rs.getString("pickup_location"));
            b.setReturnLocation(rs.getString("return_location"));
        } catch (SQLException ignored) {
        }
        return b;
    }
}
