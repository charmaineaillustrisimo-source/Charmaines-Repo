/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.utils;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 *
 * @author macbookairm1grey
 */
public class LoginFlowHelper {
    // ─────────────────────────────────────────────────────────────────────────
    // MAIN ENTRY POINT — called by LoginFrame after successful login
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * @param dashboard The existing MainDashboard window to refresh after
     * login.
     * @param afterAction What to run after the full flow completes (e.g., show
     * the car the user clicked before login). Pass null if there is no specific
     * after-action.
     * @param parentFrame The window to use as the dialog parent (LoginFrame).
     */
    public static void showRoleSelectionAndProceed(carrentalsystem.ui.user.MainDashboard dashboard, Runnable afterAction, Component parentFrame, String targetRole) {

        carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();
        if (user == null) {
            return;
        }

        String currentType = (user.getUserType() == null || user.getUserType().equalsIgnoreCase("null"))
                ? "NONE" : user.getUserType().toUpperCase();

        // 1. If user is already BOTH, or already has the role they are looking for, just finish.
        if ("BOTH".equals(currentType) || (targetRole != null && targetRole.equalsIgnoreCase(currentType))) {
            dashboard.loadData();
            if (afterAction != null) {
                afterAction.run();
            }
            return;
        }

        // 2. Determine if we show the selection dialog or jump straight to a specific role verification
        if (targetRole != null) {
            if (targetRole.equalsIgnoreCase("LISTER")) {
                handleListerUpgrade(dashboard, afterAction, parentFrame, user, currentType);
            } else if (targetRole.equalsIgnoreCase("RENTER")) {
                handleRenterUpgrade(dashboard, afterAction, parentFrame, user, currentType);
            }
        } else {
            // Show general selection dialog (original flow)
            showGeneralSelection(dashboard, afterAction, parentFrame, user, currentType);
        }
    }

    private static void handleListerUpgrade(carrentalsystem.ui.user.MainDashboard dashboard, Runnable afterAction, Component parentFrame, carrentalsystem.models.User user, String currentType) {
        if (!showListerTerms(parentFrame)) {
            return;
        }

        // Verification docs
        if (!"APPROVED".equals(user.getListerStatus()) && !"PENDING".equals(user.getListerStatus())) {
            if (!showListerRequirementsDialog(parentFrame, user)) {
                JOptionPane.showMessageDialog(parentFrame, "Verification is required to list cars.");
                return;
            }
        }

        try {
            // LOGIC: If they are already a RENTER, they become BOTH
            String newRole = "RENTER".equals(currentType) ? "BOTH" : "LISTER";
            saveUserType(user.getUserId(), newRole);
            dashboard.refreshAfterLogin();
            if (afterAction != null) {
                afterAction.run();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
        }
    }

    private static void handleRenterUpgrade(carrentalsystem.ui.user.MainDashboard dashboard, Runnable afterAction, Component parentFrame, carrentalsystem.models.User user, String currentType) {
        if (!showRenterTerms(parentFrame)) {
            return;
        }

        try {
            String newRole = "LISTER".equals(currentType) ? "BOTH" : "RENTER";
            saveUserType(user.getUserId(), newRole);
            dashboard.refreshAfterLogin();
            if (afterAction != null) {
                afterAction.run();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
        }
    }

    private static void showGeneralSelection(carrentalsystem.ui.user.MainDashboard dashboard, Runnable afterAction, Component parentFrame, carrentalsystem.models.User user, String currentType) {
        String firstName = user.getFullName().split(" ")[0];
        Object[] options = {"🚗  Rent a Car", "📋  List My Car for Rent"};

        int choice = JOptionPane.showOptionDialog(parentFrame,
                "<html><center><b style='font-size:14px'>Welcome, " + firstName + "!</b><br>What would you like to do?</center></html>",
                "Select Path", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            handleRenterUpgrade(dashboard, afterAction, parentFrame, user, currentType);
        } else if (choice == 1) {
            handleListerUpgrade(dashboard, afterAction, parentFrame, user, currentType);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TERMS DIALOGS
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Returns true if user clicks ACCEPT on Renter Terms.
     */
    public static boolean showRenterTerms(Component parent) {
        String terms
                = "RENTER TERMS & CONDITIONS\n"
                + "═══════════════════════════════════════════════\n\n"
                + "By renting a car through RentACar, you agree to:\n\n"
                + "1. ELIGIBILITY\n"
                + "   • You must be at least 18 years old.\n"
                + "   • You must hold a valid driver's license for the\n"
                + "     entire rental period.\n\n"
                + "2. BOOKING & PAYMENT\n"
                + "   • All bookings are subject to owner approval.\n"
                + "   • Payment is made directly to the car owner (cash).\n"
                + "   • RentACar does NOT process or hold payments.\n\n"
                + "3. VEHICLE USE\n"
                + "   • Use the vehicle ONLY for lawful purposes.\n"
                + "   • Smoking, off-road use, and subletting are\n"
                + "     strictly prohibited.\n"
                + "   • You are responsible for fuel unless otherwise\n"
                + "     agreed with the owner.\n\n"
                + "4. DAMAGE & LIABILITY\n"
                + "   • You are fully liable for any damage caused\n"
                + "     during the rental period.\n"
                + "   • Report accidents to the owner IMMEDIATELY.\n"
                + "   • Leaving an accident scene voids all protections.\n\n"
                + "5. RETURN POLICY\n"
                + "   • Return the vehicle on time and in the same\n"
                + "     condition as received.\n"
                + "   • Late returns may incur additional charges set\n"
                + "     by the owner.\n\n"
                + "6. CANCELLATION\n"
                + "   • PENDING bookings may be cancelled at any time.\n"
                + "   • CONFIRMED bookings cancelled within 24 hours\n"
                + "     may incur a cancellation fee.\n\n"
                + "RentACar acts only as a marketplace and is NOT liable\n"
                + "for disputes between renters and car owners.\n\n"
                + "Click OK to ACCEPT these Terms & Conditions.\n"
                + "Click Cancel to decline.\n";

        return showTermsDialog(parent, terms, "Renter Terms & Conditions");
    }

    /**
     * Returns true if user clicks ACCEPT on Lister Terms.
     */
    public static boolean showListerTerms(Component parent) {
        String terms
                = "LISTER (CAR OWNER) TERMS & CONDITIONS\n"
                + "═══════════════════════════════════════════════\n\n"
                + "By listing your vehicle on RentACar, you agree to:\n\n"
                + "1. ELIGIBILITY\n"
                + "   • You must be the registered owner OR authorized\n"
                + "     agent of the listed vehicle.\n"
                + "   • The vehicle must have valid LTO registration\n"
                + "     (OR/CR) for the entire listing period.\n"
                + "   • You must submit a valid government-issued ID,\n"
                + "     a clear selfie photo, and your OR/CR for\n"
                + "     verification before listing.\n\n"
                + "2. LISTING ACCURACY\n"
                + "   • All information (photos, price, specs, condition)\n"
                + "     must be truthful and up to date.\n"
                + "   • Misleading listings will be removed without notice.\n\n"
                + "3. BOOKING MANAGEMENT\n"
                + "   • You must respond to booking requests within 24 hours.\n"
                + "   • Repeated non-response will suspend your listings.\n"
                + "   • You may reject any booking without providing a reason.\n\n"
                + "4. VEHICLE CONDITION\n"
                + "   • Vehicle must be clean, fueled, and ready at pickup.\n"
                + "   • Pre-existing damage must be documented and disclosed\n"
                + "     to the renter before handover.\n\n"
                + "5. PROHIBITED LISTINGS\n"
                + "   • Vehicles with expired OR/CR CANNOT be listed.\n"
                + "   • Listing stolen or encumbered vehicles is strictly\n"
                + "     prohibited and will result in a permanent ban.\n\n"
                + "6. FREE vs. PRO ACCOUNT\n"
                + "   • FREE: Maximum 5 active listings.\n"
                + "   • PRO: Unlimited listings + analytics dashboard\n"
                + "     + priority placement in the feed.\n\n"
                + "RentACar reserves the right to remove any listing\n"
                + "that violates these terms without prior notice.\n\n"
                + "Click OK to ACCEPT these Terms & Conditions.\n"
                + "Click Cancel to decline.\n";

        return showTermsDialog(parent, terms, "Lister Terms & Conditions");
    }

    /**
     * Helper — builds the scrollable terms dialog.
     */
    private static boolean showTermsDialog(Component parent, String text, String title) {
        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        ta.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new java.awt.Dimension(540, 340));
        return JOptionPane.showConfirmDialog(parent, sp, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }
    
    

    // ─────────────────────────────────────────────────────────────────────────
    // LISTER REQUIREMENTS DIALOG
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Prompts the user to upload their 3 required lister documents. Returns
     * true if all documents were uploaded and saved to the DB.
     */
    public static boolean showListerRequirementsDialog(Component parent, carrentalsystem.models.User user) {

        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ── Introduction ─────────────────────────────────────────────────
        javax.swing.JLabel intro = new javax.swing.JLabel(
                "<html><b style='font-size:13px'>Lister Verification Requirements</b><br><br>"
                + "Upload all 3 documents below. They will be reviewed by our<br>"
                + "admin team within 24-48 hours before your listings go live.<br><br></html>");
        intro.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(intro);

        // ── Document 1: LTO / OR-CR ───────────────────────────────────────
        final String[] ltoPath = {null};
        javax.swing.JButton btnLTO = makeUploadButton(
                "1.  LTO Certificate / OR-CR  (vehicle registration photo)");
        btnLTO.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLTO.addActionListener(e -> {
            java.io.File f = chooseImageFile(parent);
            if (f != null) {
                ltoPath[0] = f.getAbsolutePath();
                btnLTO.setText("✅  " + f.getName());
                btnLTO.setForeground(new java.awt.Color(0, 120, 0));
            }
        });
        panel.add(btnLTO);
        panel.add(javax.swing.Box.createVerticalStrut(10));

        // ── Document 2: Selfie Photo ──────────────────────────────────────
        final String[] selfPath = {null};
        javax.swing.JButton btnSelfie = makeUploadButton(
                "2.  Clear Photo of Yourself  (selfie / portrait)");
        btnSelfie.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSelfie.addActionListener(e -> {
            java.io.File f = chooseImageFile(parent);
            if (f != null) {
                selfPath[0] = f.getAbsolutePath();
                btnSelfie.setText("✅  " + f.getName());
                btnSelfie.setForeground(new java.awt.Color(0, 120, 0));
            }
        });
        panel.add(btnSelfie);
        panel.add(javax.swing.Box.createVerticalStrut(10));

        // ── Document 3: Valid Government ID ──────────────────────────────
        final String[] idPath = {null};
        javax.swing.JButton btnID = makeUploadButton(
                "3.  Valid Government-Issued ID  (front side)");
        btnID.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnID.addActionListener(e -> {
            java.io.File f = chooseImageFile(parent);
            if (f != null) {
                idPath[0] = f.getAbsolutePath();
                btnID.setText("✅  " + f.getName());
                btnID.setForeground(new java.awt.Color(0, 120, 0));
            }
        });
        panel.add(btnID);
        panel.add(javax.swing.Box.createVerticalStrut(14));

        // ── Note ─────────────────────────────────────────────────────────
        javax.swing.JLabel note = new javax.swing.JLabel(
                "<html><i style='color:gray'>Documents are kept confidential and used only for"
                + " identity verification.</i></html>");
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(note);

        // ── Show dialog ───────────────────────────────────────────────────
        int result = JOptionPane.showConfirmDialog(
                parent, panel,
                "Submit Lister Verification Documents",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        // ── Validate all 3 uploaded ───────────────────────────────────────
        if (ltoPath[0] == null || selfPath[0] == null || idPath[0] == null) {
            JOptionPane.showMessageDialog(parent,
                    "Please upload ALL 3 required documents to proceed.",
                    "Incomplete Requirements",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // ── Save to DB ────────────────────────────────────────────────────
        try {
            saveListerRequirements(user.getUserId(), ltoPath[0], selfPath[0], idPath[0]);
            JOptionPane.showMessageDialog(parent,
                    "Documents submitted successfully!\n"
                    + "You can list cars now. Admin will verify your account\n"
                    + "within 24-48 hours.",
                    "Submitted!",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error saving documents: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleDeclined(Component parent) {
        try {
            carrentalsystem.core.SessionManager.endSession();
        } catch (SQLException ex) {
            System.getLogger(LoginFlowHelper.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        JOptionPane.showMessageDialog(parent,
                "You must accept the Terms & Conditions to use this feature.\n"
                + "You have been logged out.",
                "Declined",
                JOptionPane.WARNING_MESSAGE);
    }

    private static javax.swing.JButton makeUploadButton(String text) {
        javax.swing.JButton btn = new javax.swing.JButton("📁  " + text);
        btn.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));
        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        return btn;
    }

    private static java.io.File chooseImageFile(Component parent) {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogTitle("Select Image File");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files (JPG, PNG, JPEG)", "jpg", "jpeg", "png"));
        int r = fc.showOpenDialog(parent);
        return (r == javax.swing.JFileChooser.APPROVE_OPTION) ? fc.getSelectedFile() : null;
    }

    /**
     * Inserts a new row into lister_requirements and updates
     * users.lister_status.
     */
    private static boolean saveListerRequirements(int userId, String ltoPath, String selfPath, String idPath) throws java.sql.SQLException {

        String insertSql
                = "INSERT INTO lister_requirements "
                + "(user_id, lto_document_path, selfie_photo_path, valid_id_path, status) "
                + "VALUES (?, ?, ?, ?, 'PENDING')";

        String updateUserSql
                = "UPDATE users SET lister_status = 'PENDING' WHERE user_id = ?";

        try (java.sql.Connection conn
                = carrentalsystem.core.DBConnection.getConnection(); java.sql.PreparedStatement ps1 = conn.prepareStatement(insertSql); java.sql.PreparedStatement ps2 = conn.prepareStatement(updateUserSql)) {

            ps1.setInt(1, userId);
            ps1.setString(2, ltoPath);
            ps1.setString(3, selfPath);
            ps1.setString(4, idPath);
            ps1.executeUpdate();

            ps2.setInt(1, userId);
            ps2.executeUpdate();

            // Also update the in-memory session user
            carrentalsystem.core.SessionManager.getCurrentUser()
                    .setListerStatus("PENDING");
        }
        return true;
    }
    
    private static void saveUserType(int userId, String type) throws SQLException {
        String sql = "UPDATE users SET user_type = ? WHERE user_id = ?";
        try (java.sql.Connection conn = carrentalsystem.core.DBConnection.getConnection(); 
                java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setInt(2, userId);
            ps.executeUpdate();
            carrentalsystem.models.User currentUser = carrentalsystem.core.SessionManager.getCurrentUser();
            if (currentUser != null) currentUser.setUserType(type);
        }
    }
}
