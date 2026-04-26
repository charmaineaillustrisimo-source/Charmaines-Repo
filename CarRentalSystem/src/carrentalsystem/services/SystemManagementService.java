/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.ISystemManagementService;
import carrentalsystem.models.Booking;
import carrentalsystem.models.Car;
import carrentalsystem.models.User;
import java.sql.*;
import java.util.*;
/**
 *
 * @author macbookairm1grey
 */
public class SystemManagementService implements ISystemManagementService{
    // ── OVERVIEW KPI COUNTS ───────────────────────────────────

    /**
     * Total number of USER accounts (not counting ADMIN).
     */
    public int countTotalUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'USER'";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Count of new users registered in the last 7 days.
     */
    public int countNewUsersThisWeek() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users "
                + "WHERE role = 'USER' "
                + "AND created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Count of cars with status = ACTIVE.
     */
    public int countActiveListings() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cars WHERE status = 'ACTIVE'";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Count of new ACTIVE listings added today.
     */
    public int countNewListingsToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cars "
                + "WHERE status = 'ACTIVE' "
                + "AND DATE(created_at) = CURDATE()";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Count of bookings created today.
     */
    public int countBookingsToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings "
                + "WHERE DATE(created_at) = CURDATE()";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Count of bookings with status = SUCCESSFUL today.
     */
    public int countCompletedBookingsToday() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings "
                + "WHERE status = 'SUCCESSFUL' "
                + "AND DATE(created_at) = CURDATE()";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Count of listings with status = PENDING_APPROVAL. For KPI badge.
     */
    public int countPendingApprovals() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cars "
                + "WHERE status = 'PENDING_APPROVAL'";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // ── LISTINGS BY TYPE (for bar chart in Overview) ──────────
    /**
     * Returns count of ACTIVE cars grouped by fuel_type or model keyword. Used
     * for the "Listings by type" bar chart in AdminOverviewPanel. Returns a
     * list of Object[]{String label, Integer count}.
     */
    public List<Object[]> countListingsByType() throws SQLException {
        // Groups by broad type based on common model keywords
        String sql = "SELECT "
                + "  CASE "
                + "    WHEN LOWER(model) LIKE '%van%' "
                + "         OR LOWER(model) LIKE '%hiace%' "
                + "         OR LOWER(model) LIKE '%urvan%' THEN 'Van' "
                + "    WHEN LOWER(model) LIKE '%ranger%' "
                + "         OR LOWER(model) LIKE '%hilux%' "
                + "         OR LOWER(model) LIKE '%navara%' "
                + "         OR LOWER(model) LIKE '%pickup%' THEN 'Pickup' "
                + "    WHEN LOWER(model) LIKE '%suv%' "
                + "         OR LOWER(model) LIKE '%montero%' "
                + "         OR LOWER(model) LIKE '%fortuner%' "
                + "         OR LOWER(model) LIKE '%crv%' "
                + "         OR LOWER(model) LIKE '%cruiser%' THEN 'SUV' "
                + "    ELSE 'Sedan' "
                + "  END AS car_type, "
                + "  COUNT(*) AS total "
                + "FROM cars "
                + "WHERE status = 'ACTIVE' "
                + "GROUP BY car_type "
                + "ORDER BY total DESC";

        List<Object[]> result = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Object[]{
                    rs.getString("car_type"),
                    rs.getInt("total")
                });
            }
        }
        return result;
    }

    // ── ALL BOOKINGS TABLE (Admin Bookings panel) ─────────────
    /**
     * Returns ALL bookings with renter name, owner name, car info, dates. Used
     * by AdminBookingsPanel to fill the JTable.
     */
    public List<Booking> getAllBookings() throws SQLException {
        String sql = "SELECT b.*, "
                + "  r.full_name  AS renter_name, "
                + "  o.full_name  AS owner_name, "
                + "  c.brand      AS car_brand, "
                + "  c.model      AS car_model "
                + "FROM bookings b "
                + "JOIN users r ON b.renter_id = r.user_id "
                + "JOIN cars  c ON b.car_id    = c.car_id "
                + "JOIN users o ON c.owner_id  = o.user_id "
                + "ORDER BY b.created_at DESC";

        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setCarId(rs.getInt("car_id"));
                b.setRenterId(rs.getInt("renter_id"));
                b.setStartDate(rs.getDate("start_date"));
                b.setEndDate(rs.getDate("end_date"));
                b.setTotalPrice(rs.getDouble("total_price"));
                b.setStatus(rs.getString("status"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                b.setCarBrand(rs.getString("car_brand"));
                b.setCarModel(rs.getString("car_model"));
                b.setOwnerId(rs.getInt("owner_id"));
                // Store renter/owner names in extra fields
                b.setRenterName(rs.getString("renter_name"));
                b.setOwnerName(rs.getString("owner_name"));
                list.add(b);
            }
        }
        return list;
    }

    // ── USER MANAGEMENT with listing count ───────────────────
    /**
     * Returns all users WITH their active listing count. Used by
     * UserManagementPanel JTable — shows Name, Email, Plan, Listings. Returns
     * list of Object[]{User, Integer activeListings}.
     */
    public List<Object[]> getAllUsersWithListingCount() throws SQLException {
        String sql = "SELECT u.*, "
                + "  COUNT(c.car_id) AS active_listings "
                + "FROM users u "
                + "LEFT JOIN cars c ON u.user_id = c.owner_id "
                + "  AND c.status = 'ACTIVE' "
                + "WHERE u.role = 'USER' "
                + "GROUP BY u.user_id "
                + "ORDER BY u.created_at DESC";

        List<Object[]> result = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setTier(rs.getString("user_tier"));
                u.setStatus(rs.getString("status"));
                u.setVerified(rs.getBoolean("is_verified"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                int activeListings = rs.getInt("active_listings");
                result.add(new Object[]{u, activeListings});
            }
        }
        return result;
    }

    // ── SESSION CLEANUP ───────────────────────────────────────
    /**
     * Remove OFFLINE/IDLE sessions older than 24 hours.
     */
    public void cleanOldSessions() throws SQLException {
        String sql = "CALL clean_old_sessions()";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("[SystemManagement] Old sessions cleaned.");
        }
    }
    
    // ── SYSTEM ALERTS ─────────────────────────────────────────
    /**
     * Send system-wide alert to all ACTIVE users.
     * Calls stored procedure which inserts notification for every active user.
     */
    @Override
    public void issueSystemAlert(String message) throws SQLException {
        String sql = "CALL issue_system_alert(?)";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql)) {
            ps.setString(1, message);
            ps.executeUpdate();
            System.out.println("[SystemManagement] Alert sent: " + message);
        }
    }
}
