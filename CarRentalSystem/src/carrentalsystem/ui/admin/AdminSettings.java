/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrentalsystem.ui.admin;

import carrentalsystem.auth.LoginFrame;
import carrentalsystem.interfaces.IAdminService;
import carrentalsystem.services.AdminService;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.*;
/**
 *
 * @author macbookairm1grey
 */

public class AdminSettings extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminSettings.class.getName());
    private final IAdminService adminService = new AdminService();
    private Map<String, String> currentSettings;

    
    public AdminSettings() {
        initComponents();
        setupNavigation();
        setupIcons();
        loadSettings();
        setupEditableLabels();

        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }
    
    private void loadSettings() {
        try {
            currentSettings = adminService.getSystemSettings();

            // Map DB values to UI labels
            lblNumFreeAccount1.setText(currentSettings.getOrDefault("free_listing_limit", "0"));
            lblNumProAccount1.setText(currentSettings.getOrDefault("pro_listing_limit", "0"));
            lblNumAcceptanceWindow1.setText(currentSettings.getOrDefault("acceptance_window", "0") + "h");
            lblNumCancellationWindow1.setText(currentSettings.getOrDefault("cancellation_window", "0") + "h");

            // Maintenance Mode Styling
            boolean isMaintenance = "ON".equals(currentSettings.get("maintenance_mode"));
            updateMaintenanceUI(isMaintenance);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void setupEditableLabels() {
        // Logic: Click a label to change its value
        addEditListener(lblNumFreeAccount1, "free_listing_limit", "Set Free Listing Limit");
        addEditListener(lblNumProAccount1, "pro_listing_limit", "Set Pro Listing Limit");
        addEditListener(lblNumAcceptanceWindow1, "acceptance_window", "Set Acceptance Window (Hours)");
        addEditListener(lblNumCancellationWindow1, "cancellation_window", "Set Cancellation Window (Hours)");

        // Maintenance Toggle
        pnlMaintenanceMode.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                toggleMaintenance();
            }
        });

        // System Alert Broadcast
        lblSystemAlerts.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSystemAlerts.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String msg = JOptionPane.showInputDialog(null, "Enter System Announcement:");
                if (msg != null && !msg.trim().isEmpty()) {
                    try {
                        adminService.issueSystemAlert(msg);
                        JOptionPane.showMessageDialog(null, "Broadcast sent to all users.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    private void addEditListener(JLabel label, String key, String title) {
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String newVal = JOptionPane.showInputDialog(null, title, label.getText().replaceAll("h", ""));
                if (newVal != null && newVal.matches("\\d+")) {
                    try {
                        adminService.updateSystemSetting(key, newVal);
                        loadSettings(); // Refresh UI
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    private void toggleMaintenance() {
        String current = currentSettings.getOrDefault("maintenance_mode", "OFF");
        String newState = "OFF".equals(current) ? "ON" : "OFF";
        try {
            adminService.updateSystemSetting("maintenance_mode", newState);
            loadSettings();
            JOptionPane.showMessageDialog(this, "Maintenance Mode is now " + newState);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateMaintenanceUI(boolean isOn) {
        if (isOn) {
            pnlMaintenanceMode.setBackground(new Color(150, 50, 50)); // Red-ish
            lblTextLableForMaintenanceMode.setText("ON");
        } else {
            pnlMaintenanceMode.setBackground(new Color(60, 150, 60)); // Green-ish
            lblTextLableForMaintenanceMode.setText("OFF");
        }
    }
    
    private void setupNavigation() {
        btnOverviewButton.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            this.dispose();
        });
        btnListingButton.addActionListener(e -> {
            new AdminListingPanel().setVisible(true);
            this.dispose();
        });
        btnUsersButton.addActionListener(e -> {
            new ApprovalQueuePanel().setVisible(true);
            this.dispose();
        });
        btnBookingsButton.addActionListener(e -> {
            new AdminBookingPanel().setVisible(true);
            this.dispose();
        });
        btnSupportButton.addActionListener(e -> {
            new AdminSupportPanel().setVisible(true);
            this.dispose();
        });
        btnLogoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
    }
    
    private void setupIcons() {
        setIcon(lblOverviewIcon, "/carrentalsystem/ui/admin/PIC/four-squares.png", 35, 35);
        setIcon(lblListingIcon, "/carrentalsystem/ui/admin/PIC/Listing.png", 35, 35);
        setIcon(lblUsersIcon, "/carrentalsystem/ui/admin/PIC/Users.png", 35, 35);
        setIcon(lblBookingsIcon, "/carrentalsystem/ui/admin/PIC/Bookings.png", 35, 35);
        setIcon(lblSupportIcon, "/carrentalsystem/ui/admin/PIC/support.png", 35, 35);
        setIcon(lblSettingsIcon, "/carrentalsystem/ui/admin/PIC/setting (1).png", 35, 35);
        setIcon(lblLogoutIcon, "/carrentalsystem/ui/admin/PIC/logout-white.png", 35, 35);
    }

    private void setIcon(javax.swing.JLabel label, String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Icon load error: " + path);
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
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTopBar = new javax.swing.JPanel();
        lblCarRental = new javax.swing.JLabel();
        lblProfileIcon = new javax.swing.JLabel();
        lblNotifyIcon = new javax.swing.JLabel();
        pnlSideBar = new javax.swing.JPanel();
        lblMain = new javax.swing.JLabel();
        lblAdmin = new javax.swing.JLabel();
        lblOverviewIcon = new javax.swing.JLabel();
        btnOverviewButton = new javax.swing.JButton();
        lblListingIcon = new javax.swing.JLabel();
        btnUsersButton = new javax.swing.JButton();
        lblUsersIcon = new javax.swing.JLabel();
        lblBookingsIcon = new javax.swing.JLabel();
        btnBookingsButton = new javax.swing.JButton();
        lblSettingsIcon = new javax.swing.JLabel();
        btnSettingsButton = new javax.swing.JButton();
        pnlHighlight = new javax.swing.JPanel();
        lblSupportIcon = new javax.swing.JLabel();
        btnSupportButton = new javax.swing.JButton();
        lblLogoutIcon = new javax.swing.JLabel();
        btnLogoutButton = new javax.swing.JButton();
        btnListingButton = new javax.swing.JButton();
        pnlMainPanel = new javax.swing.JPanel();
        lblSystemSettings = new javax.swing.JLabel();
        pnlFirst1 = new javax.swing.JPanel();
        lblListingLimits1 = new javax.swing.JLabel();
        lblFreeAccount1 = new javax.swing.JLabel();
        lblProAccount1 = new javax.swing.JLabel();
        lblNumFreeAccount1 = new javax.swing.JLabel();
        lblNumProAccount1 = new javax.swing.JLabel();
        pnlSecond1 = new javax.swing.JPanel();
        lblBookingRules1 = new javax.swing.JLabel();
        lblAcceptanceWindow1 = new javax.swing.JLabel();
        lblCancellationWindow1 = new javax.swing.JLabel();
        lblNumAcceptanceWindow1 = new javax.swing.JLabel();
        lblNumCancellationWindow1 = new javax.swing.JLabel();
        pnlThird = new javax.swing.JPanel();
        lblSystemAlerts = new javax.swing.JLabel();
        lblMaintenanceMode = new javax.swing.JLabel();
        pnlMaintenanceMode = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // Turn on Anti-Aliasing for smooth edges
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Set the background color (matching the light gray in your reference)
                g2.setColor(new java.awt.Color(215, 210, 205)); 

                // Draw the rounded rectangle. 
                // Using 'getHeight()' for the arc width/height creates the "pill" effect.
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

                g2.dispose();
            }
        };
        lblTextLableForMaintenanceMode = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(3, 33, 33));

        pnlTopBar.setBackground(new java.awt.Color(30, 30, 30));
        pnlTopBar.setPreferredSize(new java.awt.Dimension(1290, 90));
        pnlTopBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCarRental.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lblCarRental.setForeground(new java.awt.Color(255, 255, 255));
        lblCarRental.setText("Rent A Car");
        pnlTopBar.add(lblCarRental, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        lblProfileIcon.setPreferredSize(new java.awt.Dimension(20, 90));
        pnlTopBar.add(lblProfileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 10, 80, 70));

        lblNotifyIcon.setPreferredSize(new java.awt.Dimension(20, 90));
        pnlTopBar.add(lblNotifyIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 20, 90, 60));

        getContentPane().add(pnlTopBar, java.awt.BorderLayout.NORTH);

        pnlSideBar.setBackground(new java.awt.Color(38, 38, 36));
        pnlSideBar.setMinimumSize(new java.awt.Dimension(300, 485));
        pnlSideBar.setPreferredSize(new java.awt.Dimension(353, 700));
        pnlSideBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMain.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblMain.setForeground(new java.awt.Color(255, 255, 255));
        lblMain.setText("MAIN");
        pnlSideBar.add(lblMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        lblAdmin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblAdmin.setForeground(new java.awt.Color(255, 255, 255));
        lblAdmin.setText("ADMIN");
        pnlSideBar.add(lblAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        lblOverviewIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblOverviewIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 35, 35));

        btnOverviewButton.setBackground(new java.awt.Color(38, 38, 36));
        btnOverviewButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnOverviewButton.setForeground(new java.awt.Color(255, 255, 255));
        btnOverviewButton.setText("Overview");
        btnOverviewButton.setBorder(null);
        btnOverviewButton.setBorderPainted(false);
        btnOverviewButton.setContentAreaFilled(false);
        btnOverviewButton.setFocusPainted(false);
        btnOverviewButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOverviewButton.setPreferredSize(new java.awt.Dimension(270, 50));
        pnlSideBar.add(btnOverviewButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        lblListingIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblListingIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 35, 35));

        btnUsersButton.setBackground(new java.awt.Color(48, 48, 46));
        btnUsersButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnUsersButton.setForeground(new java.awt.Color(255, 255, 255));
        btnUsersButton.setText("Users");
        btnUsersButton.setBorder(null);
        btnUsersButton.setBorderPainted(false);
        btnUsersButton.setContentAreaFilled(false);
        btnUsersButton.setFocusPainted(false);
        btnUsersButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUsersButton.setPreferredSize(new java.awt.Dimension(270, 50));
        pnlSideBar.add(btnUsersButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, -1, -1));

        lblUsersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblUsersIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 35, 35));

        lblBookingsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblBookingsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 35, 35));

        btnBookingsButton.setBackground(new java.awt.Color(48, 48, 46));
        btnBookingsButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnBookingsButton.setForeground(new java.awt.Color(255, 255, 255));
        btnBookingsButton.setText("Bookings");
        btnBookingsButton.setBorder(null);
        btnBookingsButton.setBorderPainted(false);
        btnBookingsButton.setContentAreaFilled(false);
        btnBookingsButton.setFocusPainted(false);
        btnBookingsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBookingsButton.setPreferredSize(new java.awt.Dimension(270, 50));
        pnlSideBar.add(btnBookingsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 270, -1));

        lblSettingsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblSettingsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 35, 35));

        btnSettingsButton.setBackground(new java.awt.Color(48, 48, 46));
        btnSettingsButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnSettingsButton.setForeground(new java.awt.Color(255, 255, 255));
        btnSettingsButton.setText("Settings");
        btnSettingsButton.setBorder(null);
        btnSettingsButton.setBorderPainted(false);
        btnSettingsButton.setContentAreaFilled(false);
        btnSettingsButton.setFocusPainted(false);
        btnSettingsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSettingsButton.setPreferredSize(new java.awt.Dimension(270, 50));
        pnlSideBar.add(btnSettingsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, -1, -1));

        pnlHighlight.setBackground(new java.awt.Color(48, 48, 46));

        javax.swing.GroupLayout pnlHighlightLayout = new javax.swing.GroupLayout(pnlHighlight);
        pnlHighlight.setLayout(pnlHighlightLayout);
        pnlHighlightLayout.setHorizontalGroup(
            pnlHighlightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        pnlHighlightLayout.setVerticalGroup(
            pnlHighlightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        pnlSideBar.add(pnlHighlight, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 360, 50));

        lblSupportIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblSupportIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 35, 35));

        btnSupportButton.setBackground(new java.awt.Color(48, 48, 46));
        btnSupportButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnSupportButton.setForeground(new java.awt.Color(255, 255, 255));
        btnSupportButton.setText("Support");
        btnSupportButton.setBorder(null);
        btnSupportButton.setBorderPainted(false);
        btnSupportButton.setContentAreaFilled(false);
        btnSupportButton.setFocusPainted(false);
        btnSupportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSupportButton.setPreferredSize(new java.awt.Dimension(270, 50));
        pnlSideBar.add(btnSupportButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, -1));

        lblLogoutIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblLogoutIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 35, 35));

        btnLogoutButton.setBackground(new java.awt.Color(48, 48, 46));
        btnLogoutButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnLogoutButton.setForeground(new java.awt.Color(255, 255, 255));
        btnLogoutButton.setText("Logout");
        btnLogoutButton.setBorder(null);
        btnLogoutButton.setBorderPainted(false);
        btnLogoutButton.setContentAreaFilled(false);
        btnLogoutButton.setFocusPainted(false);
        btnLogoutButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogoutButton.setPreferredSize(new java.awt.Dimension(270, 50));
        pnlSideBar.add(btnLogoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 440, -1, -1));

        btnListingButton.setBackground(new java.awt.Color(48, 48, 46));
        btnListingButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnListingButton.setForeground(new java.awt.Color(255, 255, 255));
        btnListingButton.setText("Listing");
        btnListingButton.setBorder(null);
        btnListingButton.setBorderPainted(false);
        btnListingButton.setContentAreaFilled(false);
        btnListingButton.setFocusPainted(false);
        btnListingButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnListingButton.setPreferredSize(new java.awt.Dimension(270, 50));
        pnlSideBar.add(btnListingButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, -1, -1));

        getContentPane().add(pnlSideBar, java.awt.BorderLayout.WEST);

        pnlMainPanel.setBackground(new java.awt.Color(48, 48, 46));
        pnlMainPanel.setForeground(new java.awt.Color(255, 255, 255));
        pnlMainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSystemSettings.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lblSystemSettings.setForeground(new java.awt.Color(255, 255, 255));
        lblSystemSettings.setText("System Settings");
        pnlMainPanel.add(lblSystemSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        pnlFirst1.setBackground(new java.awt.Color(38, 38, 36));
        pnlFirst1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblListingLimits1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        lblListingLimits1.setForeground(new java.awt.Color(255, 255, 255));
        lblListingLimits1.setText("Listing limits");
        pnlFirst1.add(lblListingLimits1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 4, -1, -1));

        lblFreeAccount1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblFreeAccount1.setForeground(new java.awt.Color(255, 255, 255));
        lblFreeAccount1.setText("Free account max listings");
        pnlFirst1.add(lblFreeAccount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 44, -1, -1));

        lblProAccount1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblProAccount1.setForeground(new java.awt.Color(255, 255, 255));
        lblProAccount1.setText("Pro account max listings");
        pnlFirst1.add(lblProAccount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 85, -1, -1));

        lblNumFreeAccount1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblNumFreeAccount1.setForeground(new java.awt.Color(255, 255, 255));
        lblNumFreeAccount1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumFreeAccount1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        lblNumFreeAccount1.setPreferredSize(new java.awt.Dimension(50, 27));
        pnlFirst1.add(lblNumFreeAccount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 44, 230, 29));

        lblNumProAccount1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblNumProAccount1.setForeground(new java.awt.Color(255, 255, 255));
        lblNumProAccount1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumProAccount1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        lblNumProAccount1.setPreferredSize(new java.awt.Dimension(200, 27));
        pnlFirst1.add(lblNumProAccount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(739, 85, 221, 29));

        pnlMainPanel.add(pnlFirst1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 970, 150));

        pnlSecond1.setBackground(new java.awt.Color(38, 38, 36));
        pnlSecond1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblBookingRules1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        lblBookingRules1.setForeground(new java.awt.Color(255, 255, 255));
        lblBookingRules1.setText("Booking rules");
        pnlSecond1.add(lblBookingRules1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 4, -1, -1));

        lblAcceptanceWindow1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblAcceptanceWindow1.setForeground(new java.awt.Color(255, 255, 255));
        lblAcceptanceWindow1.setText("Owner acceptance window");
        pnlSecond1.add(lblAcceptanceWindow1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 44, -1, -1));

        lblCancellationWindow1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblCancellationWindow1.setForeground(new java.awt.Color(255, 255, 255));
        lblCancellationWindow1.setText("Cancellation window");
        pnlSecond1.add(lblCancellationWindow1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 85, -1, -1));

        lblNumAcceptanceWindow1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblNumAcceptanceWindow1.setForeground(new java.awt.Color(255, 255, 255));
        lblNumAcceptanceWindow1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumAcceptanceWindow1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        lblNumAcceptanceWindow1.setPreferredSize(new java.awt.Dimension(150, 27));
        pnlSecond1.add(lblNumAcceptanceWindow1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 44, 220, 29));

        lblNumCancellationWindow1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblNumCancellationWindow1.setForeground(new java.awt.Color(255, 255, 255));
        lblNumCancellationWindow1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumCancellationWindow1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        lblNumCancellationWindow1.setPreferredSize(new java.awt.Dimension(200, 27));
        pnlSecond1.add(lblNumCancellationWindow1, new org.netbeans.lib.awtextra.AbsoluteConstraints(739, 85, 221, 29));

        pnlMainPanel.add(pnlSecond1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 970, 150));

        pnlThird.setBackground(new java.awt.Color(38, 38, 36));
        pnlThird.setLayout(new java.awt.GridBagLayout());

        lblSystemAlerts.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        lblSystemAlerts.setForeground(new java.awt.Color(255, 255, 255));
        lblSystemAlerts.setText("System alerts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        pnlThird.add(lblSystemAlerts, gridBagConstraints);

        lblMaintenanceMode.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblMaintenanceMode.setForeground(new java.awt.Color(255, 255, 255));
        lblMaintenanceMode.setText("Maintenance mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 20, 0);
        pnlThird.add(lblMaintenanceMode, gridBagConstraints);

        pnlMaintenanceMode.setOpaque(false);
        pnlMaintenanceMode.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTextLableForMaintenanceMode.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTextLableForMaintenanceMode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlMaintenanceMode.add(lblTextLableForMaintenanceMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 671, 0, 21);
        pnlThird.add(pnlMaintenanceMode, gridBagConstraints);

        pnlMainPanel.add(pnlThird, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 970, 100));

        getContentPane().add(pnlMainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**/
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AdminSettings().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBookingsButton;
    private javax.swing.JButton btnListingButton;
    private javax.swing.JButton btnLogoutButton;
    private javax.swing.JButton btnOverviewButton;
    private javax.swing.JButton btnSettingsButton;
    private javax.swing.JButton btnSupportButton;
    private javax.swing.JButton btnUsersButton;
    private javax.swing.JLabel lblAcceptanceWindow1;
    private javax.swing.JLabel lblAdmin;
    private javax.swing.JLabel lblBookingRules1;
    private javax.swing.JLabel lblBookingsIcon;
    private javax.swing.JLabel lblCancellationWindow1;
    private javax.swing.JLabel lblCarRental;
    private javax.swing.JLabel lblFreeAccount1;
    private javax.swing.JLabel lblListingIcon;
    private javax.swing.JLabel lblListingLimits1;
    private javax.swing.JLabel lblLogoutIcon;
    private javax.swing.JLabel lblMain;
    private javax.swing.JLabel lblMaintenanceMode;
    private javax.swing.JLabel lblNotifyIcon;
    private javax.swing.JLabel lblNumAcceptanceWindow1;
    private javax.swing.JLabel lblNumCancellationWindow1;
    private javax.swing.JLabel lblNumFreeAccount1;
    private javax.swing.JLabel lblNumProAccount1;
    private javax.swing.JLabel lblOverviewIcon;
    private javax.swing.JLabel lblProAccount1;
    private javax.swing.JLabel lblProfileIcon;
    private javax.swing.JLabel lblSettingsIcon;
    private javax.swing.JLabel lblSupportIcon;
    private javax.swing.JLabel lblSystemAlerts;
    private javax.swing.JLabel lblSystemSettings;
    private javax.swing.JLabel lblTextLableForMaintenanceMode;
    private javax.swing.JLabel lblUsersIcon;
    private javax.swing.JPanel pnlFirst1;
    private javax.swing.JPanel pnlHighlight;
    private javax.swing.JPanel pnlMainPanel;
    private javax.swing.JPanel pnlMaintenanceMode;
    private javax.swing.JPanel pnlSecond1;
    private javax.swing.JPanel pnlSideBar;
    private javax.swing.JPanel pnlThird;
    private javax.swing.JPanel pnlTopBar;
    // End of variables declaration//GEN-END:variables
}
