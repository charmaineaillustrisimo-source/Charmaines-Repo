/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.IAdminService;
import carrentalsystem.models.Booking;
import carrentalsystem.models.Car;
import carrentalsystem.models.ListerRequirement;
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
        String sql = "CALL approve_listing(?)"; // Only ONE question mark
        try (PreparedStatement ps = carrentalsystem.core.DBConnection.getConnection().prepareStatement(sql)) {
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
        
        try {
            String fetchSql = "SELECT owner_id, brand, model FROM cars WHERE car_id = ?";
            try (PreparedStatement fps = DBConnection.getConnection().prepareStatement(fetchSql)) {
                fps.setInt(1, carId);
                ResultSet rs = fps.executeQuery();
                if (rs.next()) {
                    int ownerId = rs.getInt("owner_id");
                    String carName = rs.getString("brand") + " " + rs.getString("model");

                    String notifSql = "INSERT INTO notifications (user_id, title, message, type, reference_id, is_read) "
                            + "VALUES (?, 'Listing Rejected', ?, 'LISTING', ?, 0)";
                    try (PreparedStatement nps = DBConnection.getConnection().prepareStatement(notifSql)) {
                        nps.setInt(1, ownerId);
                        nps.setString(2, "Your listing '" + carName + "' was rejected. Reason: " + reason);
                        nps.setInt(3, carId);
                        nps.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
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
        String sql = "UPDATE tickets SET admin_reply = ?, status = 'RESOLVED' WHERE ticket_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, reply);
            ps.setInt(2, ticketId);
            ps.executeUpdate();
        }
        
        sendTicketNotification(ticketId, reply);
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
    
    @Override
    public int countBookingsToday() throws SQLException {
        // Query counts bookings created within the current date[cite: 14]
        String sql = "SELECT COUNT(*) FROM bookings WHERE DATE(created_at) = CURDATE()";
        try (PreparedStatement ps = carrentalsystem.core.DBConnection.getConnection().prepareStatement(sql)) {
            java.sql.ResultSet rs = ps.executeQuery();
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
        c.setBrand(rs.getString("brand"));
        c.setModel(rs.getString("model"));
        c.setType(rs.getString("type"));
        c.setSeats(rs.getInt("seats"));
        c.setFuelType(rs.getString("fuel_type"));
        c.setTransmission(rs.getString("transmission"));
        c.setCondition(rs.getString("car_condition")); // Matches the renamed DB column
        c.setDescription(rs.getString("description"));
        c.setBasePrice(rs.getDouble("base_price"));
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

    @Override
    public int countTotalUsers() throws SQLException {
        // Counts all registered users in the database[cite: 20]
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int countActiveListings() throws SQLException {
        // Counts only cars that have been approved and are live[cite: 15]
        String sql = "SELECT COUNT(*) FROM cars WHERE status = 'ACTIVE'";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public List<Booking> getRecentBookings() throws SQLException {
        // Fetches the 10 most recent bookings with associated names for the UI[cite: 11, 14]
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name AS renter_name, c.brand, c.model "
                + "FROM bookings b "
                + "JOIN users u ON b.renter_id = u.user_id "
                + "JOIN cars c ON b.car_id = c.car_id "
                + "ORDER BY b.created_at DESC LIMIT 10";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setRenterName(rs.getString("renter_name")); // Uses the JOIN result
                b.setCarBrand(rs.getString("brand"));
                b.setCarModel(rs.getString("model"));
                b.setStartDate(rs.getDate("start_date"));
                b.setEndDate(rs.getDate("end_date"));
                b.setStatus(rs.getString("status"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(b);
            }
        }
        return list;
    }
    
    @Override
    public Map<String, Integer> getListingsByType() throws SQLException {
        String sql = "SELECT type, COUNT(*) as count FROM cars GROUP BY type";
        Map<String, Integer> counts = new HashMap<>();

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                int count = rs.getInt("count");
                counts.put(type, count);
            }
        }
        return counts;
    }
    
    @Override
    public int countNewUsersThisWeek() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
        try (PreparedStatement ps = carrentalsystem.core.DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    @Override
    public int countNewListingsToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cars WHERE DATE(created_at) = CURDATE() AND status = 'ACTIVE'";
        try (PreparedStatement ps = carrentalsystem.core.DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    @Override
    public int countCompletedBookingsToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE DATE(created_at) = CURDATE() AND status = 'SUCCESSFUL'";
        try (PreparedStatement ps = carrentalsystem.core.DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    @Override
    public void flagListing(int carId, String warningMessage) throws SQLException {
        String fetchSql = "SELECT owner_id, brand, model FROM cars WHERE car_id = ?";
        try (PreparedStatement fps = DBConnection.getConnection().prepareStatement(fetchSql)) {
            fps.setInt(1, carId);
            ResultSet rs = fps.executeQuery();
            if (rs.next()) {
                int ownerId = rs.getInt("owner_id");
                String carName = rs.getString("brand") + " " + rs.getString("model");

                String notifSql = "INSERT INTO notifications (user_id, title, message, type, reference_id, is_read) "
                        + "VALUES (?, 'Listing Warning', ?, 'LISTING', ?, 0)";
                try (PreparedStatement nps = DBConnection.getConnection().prepareStatement(notifSql)) {
                    nps.setInt(1, ownerId);
                    nps.setString(2, "Warning for '" + carName + "': " + warningMessage);
                    nps.setInt(3, carId);
                    nps.executeUpdate();
                }
            }
        }
    }
    
    @Override
    public void warnUser(int userId, String message) throws SQLException {
        // Sends a direct system warning notification[cite: 12]
        String sql = "INSERT INTO notifications (user_id, title, message, type, is_read) "
                + "VALUES (?, 'Account Warning', ?, 'SYSTEM', 0)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
        }
    }

    @Override
    public void banUser(int userId) throws SQLException {
        // Deactivates account so they cannot log in
        String sql = "UPDATE users SET status = 'BANNED' WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
    
    @Override
    public void changeUserTier(int userId, String tier) throws SQLException {
        // Updates the user's tier to 'FREE' or 'PRO'[cite: 26]
        String sql = "UPDATE users SET user_tier = ? WHERE user_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, tier);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }

        // Notify the user about the tier change[cite: 18, 26]
        String notifSql = "INSERT INTO notifications (user_id, title, message, type, is_read) "
                + "VALUES (?, 'Tier Updated', ?, 'SYSTEM', 0)";
        try (PreparedStatement nps = DBConnection.getConnection().prepareStatement(notifSql)) {
            nps.setInt(1, userId);
            nps.setString(2, "Your account has been moved to the " + tier + " tier.");
            nps.executeUpdate();
        }
    }
    
    @Override
    public List<carrentalsystem.models.User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";

        List<carrentalsystem.models.User> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                carrentalsystem.models.User u = new carrentalsystem.models.User();
                u.setUserId(rs.getInt("user_id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setTier(rs.getString("user_tier")); // Matches your DB column[cite: 26]
                list.add(u);

            }
        }
        return list;

    }
    
    @Override
    public List<carrentalsystem.models.Booking> getAllBookings() throws SQLException {
        List<carrentalsystem.models.Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, "
                + "u_renter.full_name AS renter_name, "
                + "u_owner.full_name AS owner_name, "
                + "c.brand, c.model "
                + "FROM bookings b "
                + "JOIN users u_renter ON b.renter_id = u_renter.user_id "
                + "JOIN cars c ON b.car_id = c.car_id "
                + "JOIN users u_owner ON c.owner_id = u_owner.user_id "
                + "ORDER BY b.created_at DESC";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                carrentalsystem.models.Booking b = new carrentalsystem.models.Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setRenterName(rs.getString("renter_name"));
                b.setOwnerName(rs.getString("owner_name")); // Ensure your Booking model has this field
                b.setCarBrand(rs.getString("brand"));
                b.setCarModel(rs.getString("model"));
                b.setStartDate(rs.getDate("start_date"));
                b.setEndDate(rs.getDate("end_date"));
                b.setStatus(rs.getString("status"));
                list.add(b);
            }
        }
        return list;
    }
    
    @Override
    public List<carrentalsystem.models.Ticket> getAllTicketsWithUserInfo() throws SQLException {
        List<carrentalsystem.models.Ticket> list = new ArrayList<>();
        // Removed priority, added JOIN to get full_name from users table
        String sql = "SELECT t.ticket_id, t.user_id, t.subject, t.description, "
                + "t.admin_reply, t.status, t.created_at, u.full_name "
                + "FROM tickets t "
                + "JOIN users u ON t.user_id = u.user_id "
                + "ORDER BY t.created_at DESC";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                carrentalsystem.models.Ticket t = new carrentalsystem.models.Ticket();
                t.setTicketId(rs.getInt("ticket_id"));
                t.setUserId(rs.getInt("user_id"));
                t.setUserFullName(rs.getString("full_name")); // Helper for UI
                t.setSubject(rs.getString("subject"));
                t.setDescription(rs.getString("description"));
                t.setAdminReply(rs.getString("admin_reply")); // Matches your DB column[cite: 14]
                t.setStatus(rs.getString("status"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(t);
            }
        }
        return list;
    }

    

    private void sendTicketNotification(int ticketId, String reply) throws SQLException {
        String fetchUser = "SELECT user_id, subject FROM tickets WHERE ticket_id = ?";
        try (PreparedStatement fps = DBConnection.getConnection().prepareStatement(fetchUser)) {
            fps.setInt(1, ticketId);
            ResultSet rs = fps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String subject = rs.getString("subject");

                String notifSql = "INSERT INTO notifications (user_id, title, message, type, reference_id, is_read) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement nps = DBConnection.getConnection().prepareStatement(notifSql)) {
                    nps.setInt(1, userId);
                    nps.setString(2, "Ticket Answered");
                    nps.setString(3, "Reply: " + reply);
                    nps.setString(4, "SUPPORT");
                    nps.setInt(5, ticketId);
                    nps.setInt(6, 0); // is_read = false
                    nps.executeUpdate();
                }
            }
        }
    }
    
    @Override
    public Map<String, String> getSystemSettings() throws SQLException {
        Map<String, String> settings = new HashMap<>();
        String sql = "SELECT * FROM system_settings";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                settings.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }
        }
        return settings;
    }

    @Override
    public void updateSystemSetting(String key, String value) throws SQLException {
        String sql = "INSERT INTO system_settings (setting_key, setting_value) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE setting_value = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.executeUpdate();
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════
    // LISTER VERIFICATION REVIEW
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    public List<ListerRequirement> getListerRequirements(String status) throws SQLException {
        String sql = "SELECT lr.*, u.full_name, u.email FROM lister_requirements lr JOIN users u ON lr.user_id = u.user_id WHERE lr.status = ? ORDER BY lr.submitted_at ASC";
        List<ListerRequirement> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ListerRequirement req = new ListerRequirement();
                req.setRequirementId(rs.getInt("requirement_id"));
                req.setUserId(rs.getInt("user_id"));
                req.setUserFullName(rs.getString("full_name"));
                req.setUserEmail(rs.getString("email"));
                req.setLtoDocumentPath(rs.getString("lto_document_path"));
                req.setSelfiePhotoPath(rs.getString("selfie_photo_path"));
                req.setValidIdPath(rs.getString("valid_id_path"));
                req.setStatus(rs.getString("status"));
                req.setSubmittedAt(rs.getTimestamp("submitted_at"));
                list.add(req);
            }
        }
        return list;
    }

    @Override
    public void approveListerVerification(int requirementId, int userId) throws SQLException {
        String updateReq = "UPDATE lister_requirements SET status = 'APPROVED', reviewed_at = CURRENT_TIMESTAMP WHERE requirement_id = ?";
        String updateUser = "UPDATE users SET lister_status = 'APPROVED', user_type = 'BOTH' WHERE user_id = ?";
        // Note: The order must match your table: user_id, title, message, type, reference_id, is_read
        String notifSql = "INSERT INTO notifications (user_id, title, message, type, reference_id, is_read) VALUES (?, ?, ?, 'SYSTEM', ?, 0)";

        Connection conn = carrentalsystem.core.DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            System.out.println("[DEBUG] Transaction Started for User ID: " + userId);

            // 1. Update Requirement
            try (PreparedStatement ps1 = conn.prepareStatement(updateReq)) {
                ps1.setInt(1, requirementId);
                ps1.executeUpdate();
                System.out.println("[DEBUG] Step 1: Lister Requirement set to APPROVED");
            }

            // 2. Update User
            try (PreparedStatement ps2 = conn.prepareStatement(updateUser)) {
                ps2.setInt(1, userId);
                ps2.executeUpdate();
                System.out.println("[DEBUG] Step 2: User type set to BOTH and status to APPROVED");
            }

            // 3. Send Notification
            try (PreparedStatement ps3 = conn.prepareStatement(notifSql)) {
                ps3.setInt(1, userId);
                ps3.setString(2, "Verification Success");
                ps3.setString(3, "Your lister verification is complete. You are now a verified Renter and Lister!");
                ps3.setInt(4, requirementId);
                ps3.executeUpdate();
                System.out.println("[DEBUG] Step 3: Notification sent to User");
            }

            conn.commit();
            System.out.println("[DEBUG] Transaction Committed Successfully!");
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            System.err.println("[ERROR] Transaction Failed! Rolling back changes.");
            System.err.println("[ERROR] SQL State: " + e.getSQLState());
            System.err.println("[ERROR] Error Code: " + e.getErrorCode());
            System.err.println("[ERROR] Message: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void rejectListerVerification(int requirementId, int userId, String reason) throws SQLException {
        String updateReq = "UPDATE lister_requirements SET status = 'REJECTED', admin_note = ?, reviewed_at = CURRENT_TIMESTAMP WHERE requirement_id = ?";
        String updateUser = "UPDATE users SET lister_status = 'REJECTED' WHERE user_id = ?";
        String notifSql = "INSERT INTO notifications (user_id, title, message, type, reference_id, is_read) VALUES (?, ?, ?, 'SYSTEM', ?, 0)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(updateReq); PreparedStatement ps2 = conn.prepareStatement(updateUser); PreparedStatement ps3 = conn.prepareStatement(notifSql)) {

                ps1.setString(1, reason);
                ps1.setInt(2, requirementId);
                ps1.executeUpdate();

                ps2.setInt(1, userId);
                ps2.executeUpdate();

                ps3.setInt(1, userId);
                ps3.setString(2, "Verification Rejected");
                ps3.setString(3, "Reason: " + reason);
                ps3.setInt(4, requirementId);
                ps3.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public int countPendingListerVerifications() throws java.sql.SQLException {
        String sql
                = "SELECT COUNT(*) FROM lister_requirements WHERE status = 'PENDING'";
        try (java.sql.Connection conn = carrentalsystem.core.DBConnection.getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            java.sql.ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
