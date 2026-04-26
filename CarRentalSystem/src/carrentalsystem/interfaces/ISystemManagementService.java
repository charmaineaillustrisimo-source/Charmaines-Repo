/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.Booking;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author macbookairm1grey
 */
public interface ISystemManagementService {
    
    // ── OVERVIEW KPI COUNTS ───────────────────────────────────
    /**
     * Total USER accounts in the system.
     */
    int countTotalUsers() throws SQLException;

    /**
     * New users registered in the last 7 days.
     */
    int countNewUsersThisWeek() throws SQLException;

    /**
     * Cars with status = ACTIVE.
     */
    int countActiveListings() throws SQLException;

    /**
     * New ACTIVE listings added today.
     */
    int countNewListingsToday() throws SQLException;

    /**
     * Bookings created today.
     */
    int countBookingsToday() throws SQLException;

    /**
     * Bookings with status = SUCCESSFUL today.
     */
    int countCompletedBookingsToday() throws SQLException;

    /**
     * Listings with status = PENDING_APPROVAL. For KPI badge.
     */
    int countPendingApprovals() throws SQLException;

    // ── LISTINGS BY TYPE ──────────────────────────────────────
    /**
     * Count of ACTIVE cars grouped by type (Sedan, SUV, Van, Pickup). Used for
     * the bar chart in AdminOverviewPanel. Returns list of Object[]{String
     * label, Integer count}.
     */
    List<Object[]> countListingsByType() throws SQLException;

    // ── ALL BOOKINGS TABLE ────────────────────────────────────
    /**
     * All bookings with renter name, owner name, car info. Used by
     * AdminBookingsPanel JTable.
     */
    List<Booking> getAllBookings() throws SQLException;

    // ── USER MANAGEMENT ───────────────────────────────────────
    /**
     * All users with their active listing count. Used by UserManagementPanel
     * JTable. Returns list of Object[]{User, Integer activeListings}.
     */
    List<Object[]> getAllUsersWithListingCount() throws SQLException;

    // ── SYSTEM OPERATIONS ─────────────────────────────────────
    /**
     * Remove OFFLINE/IDLE sessions older than 24 hours.
     */
    void cleanOldSessions() throws SQLException;

    /**
     * Send system-wide alert to all ACTIVE users.
     */
    void issueSystemAlert(String message) throws SQLException;
}
