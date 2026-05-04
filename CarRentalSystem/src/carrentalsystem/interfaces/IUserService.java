/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.User;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author macbookairm1grey
 */
public interface IUserService {
    // ── RENTER / OWNER ACTIONS ─────────────────────────────────

    /**
     * Load the full user profile from the users table. Called by
     * ProfilePanel.loadData() to populate the form fields.
     */
    User getUserById(int userId) throws SQLException;

    /**
     * Save updated name, email, and/or password back to the users table. Called
     * when the user clicks UPDATE in ProfilePanel (edit mode).
     */
    void updateProfile(User user) throws SQLException;

    /**
     * Hard-delete the user's own account. Called after the DELETE confirmation
     * dialog in ProfilePanel. After this, SessionManager.endSession() must be
     * called to log out.
     */
    void deleteAccount(int userId) throws SQLException;

    /**
     * Upgrade a FREE account to PRO. Called when the user clicks Upgrade to Pro
     * in ProfilePanel. Sets users.tier = 'PRO' and enables unlimited listings +
     * analytics.
     */
    boolean upgradeToPro(int userId) throws SQLException;

    // ── ADMIN-SIDE ACTIONS (called from AdminService or UserManagementPanel) ──
    /**
     * Return ALL users in the system for the admin user management table.
     * Called by UserManagementPanel.loadData().
     */
    List<User> getAllUsers() throws SQLException;

    /**
     * Suspend a user — sets users.status = 'BANNED'. The user cannot log in
     * while banned. Their listings are hidden. Called by admin from
     * UserManagementPanel.
     */
    void suspendUser(int userId) throws SQLException;

    /**
     * Hard-delete a user account from the admin panel. This removes the user
     * row and cascades to their listings and bookings. Called by admin from
     * UserManagementPanel after confirmation.
     */
    void deleteUser(int userId) throws SQLException;


}
