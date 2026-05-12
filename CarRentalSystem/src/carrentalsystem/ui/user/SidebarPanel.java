/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package carrentalsystem.ui.user;

import java.awt.CardLayout;
import carrentalsystem.interfaces.NavigationListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.SwingUtilities;

/**
 *
 * @author macbookairm1grey
 */
public class SidebarPanel extends javax.swing.JPanel {

    /**
     * Creates new form SidebarPanel
     */
    private NavigationListener navListener;
    private JButton activeBtn = null;
    private javax.swing.JPanel pnlMainContent;
    private final MainDashboard dashboard;
    private Runnable analyticsAction;

    // Theme Colors
    private final Color HOVER_COLOR = new Color(65, 56, 54);
    private final Color ACTIVE_COLOR = new Color(85, 75, 70);
    private final Color LOGOUT_RED = new Color(180, 40, 40); 
    private final Color LOGIN_BLUE = new Color(100, 200, 255); 
    private final Color DIMMED_TEXT = new Color(140, 140, 140); 
    private final Color BRIGHT_WHITE = Color.WHITE;

    public SidebarPanel(javax.swing.JPanel mainContent, MainDashboard dashboard) {
        initComponents();
        this.pnlMainContent = mainContent;
        this.dashboard = dashboard;

        setActiveButton(btnHome);
        refresh();
    }

    public void setNavigationListener(NavigationListener listener) {
        this.navListener = listener;
    }

    private void setActiveButton(JButton btn) {
        // Reset previous button
        if (activeBtn != null) {
            activeBtn.setContentAreaFilled(false);
            activeBtn.setOpaque(false);
        }

        // Set new active button
        activeBtn = btn;
        activeBtn.setOpaque(true);
        activeBtn.setContentAreaFilled(true);
        activeBtn.setBackground(ACTIVE_COLOR);
        activeBtn.setForeground(BRIGHT_WHITE);
        this.repaint();
    }

    /**
     * Centralized method to handle both the UI highlight and the Page switch.
     */
    private void handleNavigation(JButton btn, String cardName) {
        setActiveButton(btn);
        if (pnlMainContent != null) {
            ((CardLayout) pnlMainContent.getLayout()).show(pnlMainContent, cardName);
        }
        if (navListener != null) navListener.onNavigate(cardName);
        applyModeRestriction();
    }

    // Helper for hover effects
    private void applyHover(JButton btn, boolean entering) {
        if (btn == activeBtn) {
            return; // Don't change background if it's currently active
        }
        if (entering) {
            btn.setContentAreaFilled(true);
            btn.setBackground(HOVER_COLOR);
            btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else {
            btn.setContentAreaFilled(false);
        }
    }

    public void setAnalyticsAction(Runnable action) {
        this.analyticsAction = action;
    }

    /**
     * Hides/shows buttons based on RENTER or LISTER mode. Does nothing if no
     * user is logged in (guest sees a collapsed sidebar).
     */
    public void applyModeRestriction() {
        carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();

        if (user == null) {
            setAllRestrictedButtonsColor(DIMMED_TEXT);
            // We keep them ENABLED so clicks trigger the Login prompt
            return;
        } else {
            setAllRestrictedButtonsColor(BRIGHT_WHITE);
            String type = (user.getUserType() != null) ? user.getUserType() : "NONE";

            if ("BOTH".equalsIgnoreCase(type)) {
                // All bright
            } else if ("RENTER".equalsIgnoreCase(type)) {
                // Dim Lister buttons but keep them clickable for the prompt
                btnMyLists.setForeground(DIMMED_TEXT);
                btnAnalytics.setForeground(DIMMED_TEXT);
                btnAddList.setForeground(DIMMED_TEXT);
            } else if ("LISTER".equalsIgnoreCase(type)) {
                // Dim Renter buttons
                btnRents.setForeground(DIMMED_TEXT);
            }
        }

        if (activeBtn != null) {
            activeBtn.setForeground(BRIGHT_WHITE);
        }
    }

    private void setAllRestrictedButtonsColor(Color color) {
        btnMyLists.setForeground(color);
        btnRents.setForeground(color);
        btnInbox.setForeground(color);
        btnAnalytics.setForeground(color);
        btnReservations.setForeground(color);
        btnAddList.setForeground(color);
        btnSettings.setForeground(color);
    }

// These helper methods wrap your specific button and label names
    private void setRenterEnabled(boolean enabled) {
        btnRents.setEnabled(enabled);
        lblRents.setEnabled(enabled);
    }

    private void setListerEnabled(boolean enabled) {
        btnMyLists.setEnabled(enabled);
        lblMyLIsts.setEnabled(enabled);
        btnAnalytics.setEnabled(enabled);
        lblAnalytics.setEnabled(enabled);
        btnAddList.setEnabled(enabled);
    }

    private void handleRoleAccess(String targetRole, Runnable onSuccess) {
        if (!dashboard.requireLogin()) {
            return;
        }

        carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();
        String current = (user != null) ? user.getUserType() : null;
        String status = (user != null) ? user.getListerStatus() : "NONE";

        // 1. If trying to access Lister features and application is PENDING
        if (targetRole.equalsIgnoreCase("LISTER") && "PENDING".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(dashboard,
                    "Your application is currently under review. Access will be granted once approved.",
                    "Under Review", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2. Already has access
        if ("BOTH".equalsIgnoreCase(current) || targetRole.equalsIgnoreCase(current)) {
            onSuccess.run();
            return;
        }

        // 3. Needs Registration
        int choice = JOptionPane.showConfirmDialog(dashboard,
                "To access " + targetRole + " features, you must complete verification. Proceed?",
                "Registration Required", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            carrentalsystem.utils.LoginFlowHelper.showRoleSelectionAndProceed(dashboard, () -> {
                applyModeRestriction(); // Refresh visuals
                onSuccess.run();
            }, dashboard, targetRole);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHome = new javax.swing.JLabel();
        btnHome = new javax.swing.JButton();
        lblMyLIsts = new javax.swing.JLabel();
        btnMyLists = new javax.swing.JButton();
        lblRents = new javax.swing.JLabel();
        btnRents = new javax.swing.JButton();
        lblInbox = new javax.swing.JLabel();
        btnInbox = new javax.swing.JButton();
        lblAnalytics = new javax.swing.JLabel();
        btnAnalytics = new javax.swing.JButton();
        lblReservations = new javax.swing.JLabel();
        btnReservations = new javax.swing.JButton();
        btnAddList = new javax.swing.JButton();
        lblSettings = new javax.swing.JLabel();
        btnSettings = new javax.swing.JButton();
        lblLogout = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        sptLine1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(45, 36, 34));
        setPreferredSize(new java.awt.Dimension(500, 1024));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Home.png"))); // NOI18N
        lblHome.setPreferredSize(new java.awt.Dimension(50, 50));
        add(lblHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, -1, -1));

        btnHome.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setText("Home");
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHome.setPreferredSize(new java.awt.Dimension(200, 50));
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHomeMouseExited(evt);
            }
        });
        btnHome.addActionListener(this::btnHomeActionPerformed);
        add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, -1));

        lblMyLIsts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Manage Listing.png"))); // NOI18N
        lblMyLIsts.setPreferredSize(new java.awt.Dimension(50, 50));
        add(lblMyLIsts, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, -1));

        btnMyLists.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnMyLists.setForeground(new java.awt.Color(255, 255, 255));
        btnMyLists.setText("My Listings");
        btnMyLists.setBorderPainted(false);
        btnMyLists.setContentAreaFilled(false);
        btnMyLists.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMyLists.setPreferredSize(new java.awt.Dimension(200, 50));
        btnMyLists.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMyListsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMyListsMouseExited(evt);
            }
        });
        btnMyLists.addActionListener(this::btnMyListsActionPerformed);
        add(btnMyLists, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, -1, -1));

        lblRents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/MyRentals.png"))); // NOI18N
        lblRents.setMaximumSize(new java.awt.Dimension(560, 67));
        lblRents.setPreferredSize(new java.awt.Dimension(60, 60));
        add(lblRents, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 50, 70));

        btnRents.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnRents.setForeground(new java.awt.Color(255, 255, 255));
        btnRents.setText("My Rentals");
        btnRents.setBorderPainted(false);
        btnRents.setContentAreaFilled(false);
        btnRents.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRents.setPreferredSize(new java.awt.Dimension(200, 50));
        btnRents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRentsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRentsMouseExited(evt);
            }
        });
        btnRents.addActionListener(this::btnRentsActionPerformed);
        add(btnRents, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, -1, -1));

        lblInbox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Inbox.png"))); // NOI18N
        add(lblInbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, -1, -1));

        btnInbox.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnInbox.setForeground(new java.awt.Color(255, 255, 255));
        btnInbox.setText("Inbox");
        btnInbox.setBorderPainted(false);
        btnInbox.setContentAreaFilled(false);
        btnInbox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInbox.setPreferredSize(new java.awt.Dimension(200, 50));
        btnInbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnInboxMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnInboxMouseExited(evt);
            }
        });
        btnInbox.addActionListener(this::btnInboxActionPerformed);
        add(btnInbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, -1, -1));

        lblAnalytics.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Analytics.png"))); // NOI18N
        add(lblAnalytics, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, -1));

        btnAnalytics.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnAnalytics.setForeground(new java.awt.Color(255, 255, 255));
        btnAnalytics.setText("Analytics");
        btnAnalytics.setBorderPainted(false);
        btnAnalytics.setContentAreaFilled(false);
        btnAnalytics.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAnalytics.setPreferredSize(new java.awt.Dimension(200, 50));
        btnAnalytics.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAnalyticsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAnalyticsMouseExited(evt);
            }
        });
        btnAnalytics.addActionListener(this::btnAnalyticsActionPerformed);
        add(btnAnalytics, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 380, -1, -1));

        lblReservations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Reservations.png"))); // NOI18N
        lblReservations.setPreferredSize(new java.awt.Dimension(50, 50));
        add(lblReservations, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 460, -1, 60));

        btnReservations.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnReservations.setForeground(new java.awt.Color(255, 255, 255));
        btnReservations.setText("Reservations");
        btnReservations.setBorderPainted(false);
        btnReservations.setContentAreaFilled(false);
        btnReservations.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReservations.setPreferredSize(new java.awt.Dimension(200, 50));
        btnReservations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReservationsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReservationsMouseExited(evt);
            }
        });
        btnReservations.addActionListener(this::btnReservationsActionPerformed);
        add(btnReservations, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 460, -1, -1));

        btnAddList.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnAddList.setForeground(new java.awt.Color(255, 255, 255));
        btnAddList.setText("Add List");
        btnAddList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnAddList.setContentAreaFilled(false);
        btnAddList.setPreferredSize(new java.awt.Dimension(200, 50));
        btnAddList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddListMouseExited(evt);
            }
        });
        btnAddList.addActionListener(this::btnAddListActionPerformed);
        add(btnAddList, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 550, 250, -1));

        lblSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Settings.png"))); // NOI18N
        add(lblSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 660, -1, -1));

        btnSettings.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnSettings.setForeground(new java.awt.Color(255, 255, 255));
        btnSettings.setText("Settings");
        btnSettings.setBorderPainted(false);
        btnSettings.setContentAreaFilled(false);
        btnSettings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSettings.setPreferredSize(new java.awt.Dimension(200, 50));
        btnSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSettingsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSettingsMouseExited(evt);
            }
        });
        btnSettings.addActionListener(this::btnSettingsActionPerformed);
        add(btnSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 660, -1, -1));

        lblLogout.setForeground(new java.awt.Color(255, 255, 255));
        lblLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Logout.png"))); // NOI18N
        lblLogout.setPreferredSize(new java.awt.Dimension(50, 50));
        add(lblLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 730, -1, -1));

        btnLogout.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Logout");
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.setPreferredSize(new java.awt.Dimension(200, 50));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoutMouseExited(evt);
            }
        });
        btnLogout.addActionListener(this::btnLogoutActionPerformed);
        add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 730, -1, -1));

        sptLine1.setPreferredSize(new java.awt.Dimension(300, 10));
        add(sptLine1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 630, -1, 170));
    }// </editor-fold>//GEN-END:initComponents

    private void btnMyListsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMyListsActionPerformed
        handleRoleAccess("LISTER", () -> {
            handleNavigation(btnMyLists, "myListingsCard");
            if (dashboard != null && dashboard.getMyListings1() != null) {
                dashboard.getMyListings1().loadData();
            }
        });
    }//GEN-LAST:event_btnMyListsActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        handleNavigation(btnHome, "discovery");
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnInboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInboxActionPerformed
        // TODO add your handling code here:
        if (!dashboard.requireLogin()) {
            return;
        }
        handleNavigation(btnInbox, "inboxCard");
        if (dashboard != null && dashboard.getInboxPanel() != null) {
            dashboard.getInboxPanel().loadData();
        }
    }//GEN-LAST:event_btnInboxActionPerformed

    private void btnRentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentsActionPerformed
        handleRoleAccess("RENTER", () -> {
            handleNavigation(btnRents, "myRentalsCard");
            if (dashboard != null && dashboard.getMyRentalsPanel() != null) {
                dashboard.getMyRentalsPanel().loadData();
            }
        });
    }//GEN-LAST:event_btnRentsActionPerformed

    private void btnAnalyticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalyticsActionPerformed
        handleRoleAccess("LISTER", () -> {
            carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();
            // PRO CHECK
            if (user != null && !"PRO".equalsIgnoreCase(user.getTier())) {
                JOptionPane.showMessageDialog(this, "Analytics is a PRO feature. Upgrade to access analytics dashboard.", "PRO Feature", JOptionPane.INFORMATION_MESSAGE);
                if (dashboard != null) {
                    dashboard.getPnlProAlertWrapper().setVisible(true);
                }
                return;
            }
            handleNavigation(btnAnalytics, "analyticsCard");
            dashboard.showAnalytics();
        });
    }//GEN-LAST:event_btnAnalyticsActionPerformed

    private void btnReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservationsActionPerformed
        // TODO add your handling code here:
        if (!dashboard.requireLogin()) {
            return;
        }
        handleNavigation(btnReservations, "calendarCard");
        if (dashboard != null)
            dashboard.showRentalCalendar();
    }//GEN-LAST:event_btnReservationsActionPerformed

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        // TODO add your handling code here:
        applyHover(btnHome, true);
    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseExited
        // TODO add your handling code here:
        applyHover(btnHome, false);
    }//GEN-LAST:event_btnHomeMouseExited

    private void btnMyListsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMyListsMouseEntered
        // TODO add your handling code here:
        applyHover(btnMyLists, true);
    }//GEN-LAST:event_btnMyListsMouseEntered

    private void btnMyListsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMyListsMouseExited
        // TODO add your handling code here:
        applyHover(btnMyLists, false);
    }//GEN-LAST:event_btnMyListsMouseExited

    private void btnInboxMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInboxMouseEntered
        // TODO add your handling code here:
        applyHover(btnInbox, true);
    }//GEN-LAST:event_btnInboxMouseEntered

    private void btnInboxMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInboxMouseExited
        // TODO add your handling code here:
        applyHover(btnInbox, false);
    }//GEN-LAST:event_btnInboxMouseExited

    private void btnRentsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRentsMouseEntered
        // TODO add your handling code here:
        applyHover(btnRents, true);
    }//GEN-LAST:event_btnRentsMouseEntered

    private void btnRentsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRentsMouseExited
        // TODO add your handling code here:
        applyHover(btnRents, false);
    }//GEN-LAST:event_btnRentsMouseExited

    private void btnAnalyticsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnalyticsMouseEntered
        // TODO add your handling code here:
        applyHover(btnAnalytics, true);
    }//GEN-LAST:event_btnAnalyticsMouseEntered

    private void btnAnalyticsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnalyticsMouseExited
        // TODO add your handling code here:
        applyHover(btnAnalytics, false);
    }//GEN-LAST:event_btnAnalyticsMouseExited

    private void btnReservationsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReservationsMouseEntered
        // TODO add your handling code here:
        applyHover(btnReservations, true);
    }//GEN-LAST:event_btnReservationsMouseEntered

    private void btnReservationsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReservationsMouseExited
        // TODO add your handling code here:
        applyHover(btnReservations, false);
    }//GEN-LAST:event_btnReservationsMouseExited

    private void btnAddListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddListMouseEntered
        // TODO add your handling code here:
        applyHover(btnAddList, true);
    }//GEN-LAST:event_btnAddListMouseEntered

    private void btnAddListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddListMouseExited
        // TODO add your handling code here:
        applyHover(btnAddList, false);
    }//GEN-LAST:event_btnAddListMouseExited

    private void btnAddListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddListActionPerformed
        handleRoleAccess("LISTER", () -> {
            carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();

            // PRO Users have unlimited access
            if ("FREE".equalsIgnoreCase(user.getTier())) {
                try {
                    // 1. Fetch current dynamic limit from database
                    Map<String, String> settings = new carrentalsystem.services.AdminService().getSystemSettings();
                    int adminLimit = Integer.parseInt(settings.getOrDefault("free_listing_limit", "5"));

                    // 2. Count current user listings
                    int currentCount = new carrentalsystem.services.CarService().countUserListings(user.getUserId());

                    if (currentCount >= adminLimit) {
                        JOptionPane.showMessageDialog(this,
                                "Free users are limited to " + adminLimit + " listings.\nUpgrade to PRO for unlimited listings!",
                                "Limit Reached", JOptionPane.WARNING_MESSAGE);
                        if (dashboard != null) {
                            dashboard.getPnlProAlertWrapper().setVisible(true);
                        }
                        return;
                    }
                } catch (SQLException | NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }

            handleNavigation(btnAddList, "addListing");
            dashboard.getPnlAddList1().prepareAdd();
        });

    }//GEN-LAST:event_btnAddListActionPerformed

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        // TODO add your handling code here:
        applyHover(btnLogout, true);
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        // TODO add your handling code here:
        applyHover(btnLogout, false);
    }//GEN-LAST:event_btnLogoutMouseExited

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // If no user is logged in, the button acts as "Login"
        if (carrentalsystem.core.SessionManager.getCurrentUser() == null) {
            dashboard.showLogin();
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(dashboard, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                carrentalsystem.core.SessionManager.endSession();
                dashboard.refreshAfterLogout();
                refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnSettingsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettingsMouseEntered
        // TODO add your handling code here:
        applyHover(btnSettings, true);
    }//GEN-LAST:event_btnSettingsMouseEntered

    private void btnSettingsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettingsMouseExited
        // TODO add your handling code here:
        applyHover(btnSettings, false);
    }//GEN-LAST:event_btnSettingsMouseExited

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        // TODO add your handling code here:
        if (!dashboard.requireLogin()) {
            return;
        }
        handleNavigation(btnSettings, "settingsCard");
    }//GEN-LAST:event_btnSettingsActionPerformed

    /**
     * Call after login/logout/role-change to refresh sidebar state.
     */
     public void refresh() {
        carrentalsystem.models.User u = carrentalsystem.core.SessionManager.getCurrentUser();
        btnLogout.setText(u == null ? "Login / Sign Up" : "Logout");
        btnLogout.setForeground(u == null ? new Color(100, 200, 255) : Color.WHITE);
        applyModeRestriction();
        this.revalidate();
        this.repaint();
    }

    

    

    

    // Helper to choose files
    private java.io.File chooseFile() {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        return (fc.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) ? fc.getSelectedFile() : null;
    }

    private boolean saveRequirementsToDatabase(carrentalsystem.models.ListerRequirement req) {
        String sql = "INSERT INTO lister_requirements (user_id, valid_id_path, lto_document_path, selfie_photo_path, status) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = carrentalsystem.core.DBConnection.getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, req.getUserId());
            ps.setString(2, req.getValidIdPath());
            ps.setString(3, req.getLtoDocumentPath());
            ps.setString(4, req.getSelfiePhotoPath());
            ps.setString(5, "PENDING");
            ps.executeUpdate();

            // Also update the User's lister_status in the users table
            String userSql = "UPDATE users SET lister_status = 'PENDING' WHERE user_id = ?";
            try (java.sql.PreparedStatement ps2 = conn.prepareStatement(userSql)) {
                ps2.setInt(1, req.getUserId());
                ps2.executeUpdate();
            }

            // Update local session
            carrentalsystem.core.SessionManager.getCurrentUser().setListerStatus("PENDING");
            return true;
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(dashboard, "Database Error: " + e.getMessage());
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddList;
    private javax.swing.JButton btnAnalytics;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnInbox;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMyLists;
    private javax.swing.JButton btnRents;
    private javax.swing.JButton btnReservations;
    private javax.swing.JButton btnSettings;
    private javax.swing.JLabel lblAnalytics;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblInbox;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblMyLIsts;
    private javax.swing.JLabel lblRents;
    private javax.swing.JLabel lblReservations;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JSeparator sptLine1;
    // End of variables declaration//GEN-END:variables
}
