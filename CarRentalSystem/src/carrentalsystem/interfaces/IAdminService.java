/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.Car;
import carrentalsystem.models.Ticket;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author macbookairm1grey
 */
public interface IAdminService {
    // ── LISTING MODERATION ─────────────────────────────────────

    /**
     * Return all car listings with status = 'PENDING_APPROVAL'. Called by
     * ApprovalQueuePanel.loadData() to fill the JTable. PRO listings
     * (is_priority=true) appear first.
     */
    List<Car> getPendingListings() throws SQLException;

    /**
     * Approve a listing: sets cars.status = 'ACTIVE'. Also sends a notification
     * to the car owner. Called when admin clicks Approve in ApprovalQueuePanel.
     */
    void approveListing(int carId) throws SQLException;

    /**
     * Reject a listing: sets cars.status = 'REJECTED'. Also sends a
     * notification to the owner with the reason. Called when admin clicks
     * Reject in ApprovalQueuePanel.
     */
    void rejectListing(int carId, String reason) throws SQLException;

    /**
     * Remove an active listing from the feed: sets cars.status = 'ARCHIVED'.
     * Used for listings that violate rules after approval.
     */
    void removeListing(int carId) throws SQLException;

    // ── SUPPORT TICKETS ────────────────────────────────────────
    /**
     * Return all open support tickets. Called by AdminSupportPanel.loadData()
     * to fill the JTable.
     */
    List<Ticket> getOpenTickets() throws SQLException;

    /**
     * Save the admin's reply to a support ticket. Sets tickets.admin_reply and
     * optionally marks as RESOLVED. Called when admin clicks Send Reply in the
     * ticket reply dialog.
     */
    void replyToTicket(int ticketId, String reply) throws SQLException;

    /**
     * Close a resolved ticket: sets tickets.status = 'RESOLVED'. Called from
     * the ticket reply dialog after sending a response.
     */
    void closeTicket(int ticketId) throws SQLException;

    // ── SYSTEM MANAGEMENT ──────────────────────────────────────
    /**
     * Send a notification message to ALL active users at once. Used for system
     * announcements (maintenance, new features). Called from SystemConfigPanel.
     */
    void issueSystemAlert(String message) throws SQLException;

    /**
     * Clean up the sessions table: removes rows older than 24h with
     * current_state = 'OFFLINE' or 'IDLE'. Called from the DB Maintenance
     * button in SystemConfigPanel.
     */
    void performDBMaintenance() throws SQLException;

    // ── KPI COUNTS (for Admin Overview dashboard cards) ─────────
    /**
     * Count of listings with status = 'PENDING_APPROVAL'. For the KPI badge.
     */
    int countPendingListings() throws SQLException;

    /**
     * Count of tickets with status = 'OPEN'. For the KPI badge.
     */
    int countOpenTickets() throws SQLException;

}
