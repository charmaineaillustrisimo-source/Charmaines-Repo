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
    private final Color DEFAULT_COLOR = new Color(45, 36, 34);

    public SidebarPanel(javax.swing.JPanel mainContent, MainDashboard dashboard) {
        initComponents();
        // Show "Login" label on the logout button if no user is logged in
        if (carrentalsystem.core.SessionManager.getCurrentUser() == null) {
            btnLogout.setText("Login / Sign Up");
            btnLogout.setForeground(new java.awt.Color(100, 200, 255)); // light blue
        }
        this.pnlMainContent = mainContent;
        this.dashboard = dashboard;

        setActiveButton(btnHome);
        // At the end of the SidebarPanel constructor, ADD:
        applyModeRestrictions();
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
        this.repaint();
    }
    
    /**
     * Centralized method to handle both the UI highlight and the Page switch.
     */
    private void handleNavigation(JButton btn, String cardName) {
        setActiveButton(btn);

        // 1. Swap the Card in the local pnlMainContent
        if (pnlMainContent != null) {
            CardLayout cl = (CardLayout) pnlMainContent.getLayout();
            cl.show(pnlMainContent, cardName);
        }

        // 2. Notify the Dashboard through the interface (if needed for other logic)
        if (navListener != null) {
            navListener.onNavigate(cardName);
        }
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
    private void applyModeRestrictions() {
        // All buttons are always fully visible.
        // Access is controlled at click-time via requireLogin() and showRoleUpgradeFlow()
        // in each button's ActionPerformed handler — no visual dimming needed here.
        btnMyLists.setVisible(true);
        lblMyLIsts.setVisible(true);
        btnAddList.setVisible(true);
        btnAnalytics.setVisible(true);
        lblAnalytics.setVisible(true);
        btnRents.setVisible(true);
        lblRents.setVisible(true);

        btnMyLists.setForeground(java.awt.Color.WHITE);
        btnAddList.setForeground(java.awt.Color.WHITE);
        btnAnalytics.setForeground(java.awt.Color.WHITE);
        btnRents.setForeground(java.awt.Color.WHITE);

        lblMyLIsts.setEnabled(true);
        lblAnalytics.setEnabled(true);
        lblRents.setEnabled(true);
    }
    
   
    
    public void updateSidebarPermissions() {
        carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();

        if (user == null) {
            return;
        }

        String role = user.getRole();
        String type = user.getUserType();

        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

        // We keep these visible now, but we will control the logic in the ActionEvents
        btnAnalytics.setVisible(isAdmin || !"RENTER".equals(type));
        btnReservations.setVisible(isAdmin);

        // Always visible, but restricted by logic
        btnAddList.setVisible(true);
        btnMyLists.setVisible(true);
        btnRents.setVisible(true);
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
        carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();

        // 1. Guard: Not logged in
        if (user == null) {
            dashboard.showLoginOrSignupPrompt(null);
            return;
        }

        // 2. Guard: Already a Lister or Both?
        String type = user.getUserType() != null ? user.getUserType() : "";
        if (type.equalsIgnoreCase("LISTER") || type.equalsIgnoreCase("BOTH")) {
            // Direct Access - Skip all prompts
            handleNavigation(btnMyLists, "myListingsCard");
            if (dashboard.getMyListings1() != null) {
                dashboard.getMyListings1().loadData();
            }
            return;
        }

        // 3. Fallback: Run Onboarding (Only for RENTERs who want to become LISTERs)
        carrentalsystem.utils.LoginFlowHelper.showRoleSelectionAndProceed(dashboard, () -> {
            handleNavigation(btnMyLists, "myListingsCard");
        }, SwingUtilities.getWindowAncestor(this));
    }//GEN-LAST:event_btnMyListsActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        handleNavigation(btnHome, "discovery");
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnInboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInboxActionPerformed
        // TODO add your handling code here:
        if (!dashboard.requireLogin()) return; 
        handleNavigation(btnInbox, "inboxCard");
        if (dashboard != null && dashboard.getInboxPanel() != null) {
            dashboard.getInboxPanel().loadData(); // This triggers the DB fetch
        }
    }//GEN-LAST:event_btnInboxActionPerformed

    private void btnRentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentsActionPerformed
        if (!dashboard.requireLogin()) {
            return;
        }
        if (carrentalsystem.core.SessionManager.isListerMode()) {
            showRoleUpgradeFlow("My Rentals", "RENTER");
            return;
        }
        handleNavigation(btnRents, "myRentalsCard");
        if (dashboard != null && dashboard.getMyRentalsPanel() != null) {
            dashboard.getMyRentalsPanel().loadData();
        }
    }//GEN-LAST:event_btnRentsActionPerformed

    private void btnAnalyticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalyticsActionPerformed
        carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();

        if (user == null) {
            dashboard.showLoginOrSignupPrompt(null);
            return;
        }

        // GUARD: Check if already a Lister or Both
        String type = user.getUserType() != null ? user.getUserType() : "";
        if (type.equalsIgnoreCase("LISTER") || type.equalsIgnoreCase("BOTH")) {
            handleNavigation(btnAnalytics, "analyticsCard");
            dashboard.showAnalytics();
            return;
        }

        // REDIRECT: Use the centralized helper for onboarding
        carrentalsystem.utils.LoginFlowHelper.showRoleSelectionAndProceed(dashboard, () -> {
            handleNavigation(btnAnalytics, "analyticsCard");
            dashboard.showAnalytics();
        }, SwingUtilities.getWindowAncestor(this));
    }//GEN-LAST:event_btnAnalyticsActionPerformed

    private void btnReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservationsActionPerformed
        // TODO add your handling code here:
        if (!dashboard.requireLogin()) return; 
        handleNavigation(btnReservations, "calendarCard");
        if (dashboard != null) {
            dashboard.showRentalCalendar();
        }
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
        carrentalsystem.models.User user = carrentalsystem.core.SessionManager.getCurrentUser();

        // 1. Guard: Login Check
        if (user == null) {
            dashboard.showLoginOrSignupPrompt(null);
            return;
        }

        // 2. PRO Tier Limit Check (Only for FREE users)
        if ("FREE".equalsIgnoreCase(user.getTier())) {
            try {
                int currentLists = new carrentalsystem.services.CarService().countUserListings(user.getUserId());
                if (currentLists >= 5) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "You have reached the limit of 5 listings for Free users.\nUpgrade to PRO for unlimited listings!",
                            "Limit Reached",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    if (dashboard != null) {
                        dashboard.getPnlProAlertWrapper().setVisible(true);
                        dashboard.getLayeredPane().setComponentZOrder(dashboard.getPnlProAlertWrapper(), 0);
                    }
                    return; // Stop them from proceeding
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }

        // 3. Role Guard: Already a Lister or Both?
        String type = user.getUserType() != null ? user.getUserType() : "";
        if (type.equalsIgnoreCase("LISTER") || type.equalsIgnoreCase("BOTH")) {
            handleNavigation(btnAddList, "addListing");
            dashboard.getPnlAddList1().prepareAdd();
            return;
        }

        // 4. Fallback: Run Onboarding (Only if they are just a RENTER)
        carrentalsystem.utils.LoginFlowHelper.showRoleSelectionAndProceed(dashboard, () -> {
            handleNavigation(btnAddList, "addListing");
            dashboard.getPnlAddList1().prepareAdd();
        }, SwingUtilities.getWindowAncestor(this));

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

        // Otherwise, handle Logout
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this, "Are you sure you want to log out?", "Logout",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                carrentalsystem.core.SessionManager.endSession();
                dashboard.refreshAfterLogout();
            } catch (java.sql.SQLException ex) {
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
        if (!dashboard.requireLogin()) return; 
        handleNavigation(btnSettings, "settingsCard");
    }//GEN-LAST:event_btnSettingsActionPerformed
    
    /**
     * Call after login/logout/role-change to refresh sidebar state.
     */
    public void refresh() {
        carrentalsystem.models.User u = carrentalsystem.core.SessionManager.getCurrentUser();
        btnLogout.setText(u == null ? "Login / Sign Up" : "Logout");
        btnLogout.setForeground(u == null
                ? new java.awt.Color(100, 200, 255)
                : java.awt.Color.WHITE);
        applyModeRestrictions();
        repaint();
    }
    
    // ── ROLE UPGRADE FLOW ─────────────────────────────────────────────────
    /**
     * Called when a user clicks a button locked by their current role.
     * featureName = e.g. "My Listings" requiredRole = "LISTER" or "RENTER"
     */
    private void showRoleUpgradeFlow(String featureName, String requiredRole) {
        carrentalsystem.models.User u = carrentalsystem.core.SessionManager.getCurrentUser();
        if (u == null) {
            return;
        }

        int want = javax.swing.JOptionPane.showConfirmDialog(dashboard,
                "<html><b>" + featureName + "</b> requires <b>Car Lister</b> access.<br><br>"
                + "To list cars, you must upload your Government ID and Vehicle Documents for approval.<br>"
                + "Would you like to start the verification process now?</html>",
                "Verification Required",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (want == javax.swing.JOptionPane.YES_OPTION) {
            if (showProfessionalVerificationDialog()) {
                javax.swing.JOptionPane.showMessageDialog(dashboard,
                        "Submission Successful! Please wait for an Admin to review your documents.");
                applyModeRestrictions(); // Re-dims/refreshes the UI
            }
        }
    }

    /**
     * Scrollable Terms & Conditions dialog. Returns true if user accepted.
     */
    private boolean showTermsAndConditions(String roleLabel) {
        String terms
                = "TERMS AND CONDITIONS — " + roleLabel.toUpperCase() + "\n\n"
                + "1. GENERAL\n"
                + "   By using Rent A Car you agree to these terms.\n\n"
                + "2. RENTER RESPONSIBILITIES\n"
                + "   \u2022 You must be at least 18 years old with a valid driver's license.\n"
                + "   \u2022 You are responsible for the vehicle during the rental period.\n"
                + "   \u2022 Any damage beyond normal wear must be reported immediately.\n"
                + "   \u2022 Subletting the rented vehicle to others is strictly prohibited.\n\n"
                + "3. LISTER RESPONSIBILITIES\n"
                + "   \u2022 You must be the registered owner of any vehicle you list.\n"
                + "   \u2022 All vehicle information must be accurate and up-to-date.\n"
                + "   \u2022 Your vehicle must be roadworthy and properly insured.\n"
                + "   \u2022 You must respond to booking requests within 24 hours.\n"
                + "   \u2022 Listings are subject to admin approval before going live.\n\n"
                + "4. PAYMENTS\n"
                + "   \u2022 All transactions are in Philippine Peso (PHP).\n"
                + "   \u2022 Free users may list up to 5 vehicles.\n"
                + "   \u2022 PRO users get unlimited listings and priority placement.\n\n"
                + "5. PROHIBITED CONDUCT\n"
                + "   \u2022 Providing false information is grounds for account suspension.\n"
                + "   \u2022 Fraudulent bookings result in permanent banning.\n\n"
                + "6. LIABILITY\n"
                + "   \u2022 Rent A Car is a marketplace and is not liable for disputes\n"
                + "     between renters and listers.\n\n"
                + "By clicking OK you confirm you have read and agree to the above.";

        javax.swing.JTextArea ta = new javax.swing.JTextArea(terms);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 13));
        ta.setMargin(new java.awt.Insets(10, 10, 10, 10));

        javax.swing.JScrollPane sp = new javax.swing.JScrollPane(ta);
        sp.setPreferredSize(new java.awt.Dimension(520, 320));

        int r = javax.swing.JOptionPane.showConfirmDialog(
                dashboard, sp,
                "Terms & Conditions — " + roleLabel,
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (r != javax.swing.JOptionPane.OK_OPTION) {
            javax.swing.JOptionPane.showMessageDialog(dashboard,
                    "You must accept the Terms & Conditions to proceed.",
                    "Declined", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Lister requirements checklist — all boxes must be ticked.
     */
    private boolean showProfessionalVerificationDialog() {
        javax.swing.JPanel pnl = new javax.swing.JPanel(new java.awt.GridLayout(0, 1, 5, 10));
        pnl.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));

        pnl.add(new javax.swing.JLabel("<html><b>Lister Verification</b><br>Please upload clear photos of the following:</html>"));

        // File path storage
        final String[] paths = new String[3]; // [0] ID, [1] LTO, [2] Selfie

        // Create buttons for file selection
        javax.swing.JButton btnID = new javax.swing.JButton("Upload Government ID");
        javax.swing.JButton btnLTO = new javax.swing.JButton("Upload LTO OR/CR Documents");
        javax.swing.JButton btnSelfie = new javax.swing.JButton("Upload Selfie with ID");

        // Action Listeners for buttons (using JFileChooser)
        btnID.addActionListener(e -> {
            java.io.File f = chooseFile();
            if (f != null) {
                paths[0] = f.getAbsolutePath();
                btnID.setText("✅ ID: " + f.getName());
            }
        });
        btnLTO.addActionListener(e -> {
            java.io.File f = chooseFile();
            if (f != null) {
                paths[1] = f.getAbsolutePath();
                btnLTO.setText("✅ LTO: " + f.getName());
            }
        });
        btnSelfie.addActionListener(e -> {
            java.io.File f = chooseFile();
            if (f != null) {
                paths[2] = f.getAbsolutePath();
                btnSelfie.setText("✅ Selfie: " + f.getName());
            }
        });

        pnl.add(btnID);
        pnl.add(btnLTO);
        pnl.add(btnSelfie);

        int result = javax.swing.JOptionPane.showConfirmDialog(dashboard, pnl,
                "Professional Verification Required", javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (result == javax.swing.JOptionPane.OK_OPTION) {
            if (paths[0] == null || paths[1] == null || paths[2] == null) {
                javax.swing.JOptionPane.showMessageDialog(dashboard, "All documents are required.");
                return false;
            }

            // ── USE YOUR MODEL HERE ──
            carrentalsystem.models.ListerRequirement req = new carrentalsystem.models.ListerRequirement();
            req.setUserId(carrentalsystem.core.SessionManager.getCurrentUser().getUserId());
            req.setValidIdPath(paths[0]);
            req.setLtoDocumentPath(paths[1]);
            req.setSelfiePhotoPath(paths[2]);
            req.setStatus("PENDING");

            return saveRequirementsToDatabase(req);
        }
        return false;
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
