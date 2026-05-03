/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrentalsystem.ui.admin;

import carrentalsystem.auth.LoginFrame;
/**
 *
 * @author macbookairm1grey
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.Map;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminListingPanel extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminListingPanel.class.getName());
    // Add this at the top of your class variables
    private java.util.List<carrentalsystem.models.Car> userCarList = new java.util.ArrayList<>();

    /**
     * Creates new form AdminDashboard
     */
    public AdminListingPanel() {
        initComponents();
        
        Listing listingView = new Listing();
        
        pnlMainContent.removeAll();
        pnlMainContent.add(listingView, java.awt.BorderLayout.CENTER);
        
        pnlMainContent.revalidate();
        pnlMainContent.repaint();
        
        //Button Events
        btnOverviewButton.addActionListener(e -> {
            try {
            AdminDashboard admin = new AdminDashboard();
        admin.setVisible(true);
        this.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });
        
    btnUsersButton.addActionListener(e -> {
            try {
            ApprovalQueuePanel approval = new ApprovalQueuePanel();
        approval.setVisible(true);
        this.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });
    
    btnBookingsButton.addActionListener (e -> {
        try {
            AdminBookingPanel booking = new AdminBookingPanel();
        booking.setVisible(true);
        this.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
        }
    });
    
      btnSupportButton.addActionListener (e -> {
        try {
            AdminSupportPanel support = new AdminSupportPanel();
        support.setVisible(true);
        this.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
        }
    });
    
    btnSettingsButton.addActionListener (e -> {
        try {
            AdminSettings settings = new AdminSettings();
        settings.setVisible(true);
        this.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
        }
    });
    
    String message = "<html>" +
                     "<b style='font-size: 14pt; color: white;'>Are you sure you want to log out?</b><br/>" +
                     "<span style='color: white;'>Logging out will end your current session.</span><br/>" +
                     "<span style='color: white;'>All unsaved work will be lost.</span>" +
                     "</html>";

    // Set Up the Action Listener
    btnLogoutButton.addActionListener(e -> {
    javax.swing.JDialog logoutDialog = new javax.swing.JDialog(this, true);
    logoutDialog.setUndecorated(true);
    
    // 1. Create the Main Panel with Padding
    javax.swing.JPanel pnlLogout = new javax.swing.JPanel();
    pnlLogout.setBackground(new java.awt.Color(49, 49, 47));
    pnlLogout.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
    // Use BoxLayout to stack elements vertically and center them
    pnlLogout.setLayout(new javax.swing.BoxLayout(pnlLogout, javax.swing.BoxLayout.Y_AXIS));

    // 2. Title Label (Centered)
    javax.swing.JLabel lblTitle = new javax.swing.JLabel("Are you sure you want to log out?");
    lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
    lblTitle.setForeground(java.awt.Color.WHITE);
    lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // Centers the component
    lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Centers the text inside

    // 3. Subtext Label (Centered with HTML for wrapping)
    javax.swing.JLabel lblSub = new javax.swing.JLabel("<html><div style='text-align: center;'>"
            + "Logging out will end your current session.<br>"
            + "All unsaved work will be lost.</div></html>");
    lblSub.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    lblSub.setForeground(new java.awt.Color(200, 200, 200));
    lblSub.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    // 4. Button Container (FlowLayout to keep buttons side-by-side)
    javax.swing.JPanel pnlButtons = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 0));
    pnlButtons.setOpaque(false); // Keeps the 49, 49, 47 background visible

    javax.swing.JButton btnConfirm = new javax.swing.JButton("Logout");
    btnConfirm.setBackground(new java.awt.Color(120, 30, 30));
    btnConfirm.setForeground(java.awt.Color.WHITE);
    btnConfirm.setPreferredSize(new java.awt.Dimension(100, 35));
    btnConfirm.setFocusPainted(false);

    javax.swing.JButton btnCancel = new javax.swing.JButton("Cancel");
    btnCancel.setBackground(new java.awt.Color(70, 70, 70));
    btnCancel.setForeground(java.awt.Color.WHITE);
    btnCancel.setPreferredSize(new java.awt.Dimension(100, 35));
    btnCancel.setFocusPainted(false);

    pnlButtons.add(btnConfirm);
    pnlButtons.add(btnCancel);

    // 5. Add components with Spacing (Rigid Areas)
    pnlLogout.add(lblTitle);
    pnlLogout.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 15))); // Gap after title
    pnlLogout.add(lblSub);
    pnlLogout.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 25))); // Gap before buttons
    pnlLogout.add(pnlButtons);

    // 6. Action Logic
    btnConfirm.addActionListener(ev -> {
        logoutDialog.dispose();
        new carrentalsystem.auth.LoginFrame().setVisible(true);
        this.dispose();
    });
    btnCancel.addActionListener(ev -> logoutDialog.dispose());

    // 7. Dialog Final Setup
    logoutDialog.add(pnlLogout);
    logoutDialog.pack(); // Adjusts size automatically based on content
    logoutDialog.setLocationRelativeTo(this); 
    logoutDialog.setVisible(true);
});
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        SeparatorCellRenderer separatorRenderer = new SeparatorCellRenderer();

        //Position Top Bar Icons
        pnlTopBar.add(lblProfileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 20, 50, 50));
        pnlTopBar.add(lblNotifyIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 20, 50, 50));
        
        

        // Add this to your constructor after initComponents()
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int width = getWidth();
                // Position Profile 70 pixels from the right edge
                lblProfileIcon.setLocation(width - 90, lblProfileIcon.getY());
                // Position Notify 140 pixels from the right edge
                lblNotifyIcon.setLocation(width - 160, lblNotifyIcon.getY());
            }
        });

        // The "Easy Way" - One line per icon
        setIcon(lblOverviewIcon, "/carrentalsystem/ui/admin/PIC/four-squares.png", 35, 35);
        setIcon(lblListingIcon, "/carrentalsystem/ui/admin/PIC/Listing.png", 35, 35);
        setIcon(lblUsersIcon, "/carrentalsystem/ui/admin/PIC/Users.png", 35, 35);
        setIcon(lblBookingsIcon, "/carrentalsystem/ui/admin/PIC/Bookings.png", 35, 35);
        setIcon(lblSupportIcon, "/carrentalsystem/ui/admin/PIC/support.png", 35, 35);
        setIcon(lblSettingsIcon, "/carrentalsystem/ui/admin/PIC/setting (1).png", 35, 35);
        setIcon(lblLogoutIcon, "/carrentalsystem/ui/admin/PIC/logout-white.png", 35, 35);
        //Top Bar Panel Icon
        setIcon(lblProfileIcon, "/carrentalsystem/ui/admin/PIC/Profile.png", 50, 50);
        setIcon(lblNotifyIcon, "/carrentalsystem/ui/admin/PIC/bell.png", 50, 50);

    }

    private void setIcon(javax.swing.JLabel label, String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                // Now uses the width and height parameters you pass in
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Could not load image: " + path);
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
        btnListingButton = new javax.swing.JButton();
        lblUsersIcon = new javax.swing.JLabel();
        btnUsersButton = new javax.swing.JButton();
        lblBookingsIcon = new javax.swing.JLabel();
        pnlHighlight = new javax.swing.JPanel();
        lblSupportIcon = new javax.swing.JLabel();
        btnSupportButton = new javax.swing.JButton();
        lblSettingsIcon = new javax.swing.JLabel();
        btnSettingsButton = new javax.swing.JButton();
        lblLogoutIcon = new javax.swing.JLabel();
        btnLogoutButton = new javax.swing.JButton();
        btnBookingsButton = new javax.swing.JButton();
        pnlMainContent = new javax.swing.JPanel();

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

        lblUsersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblUsersIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 35, 35));

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

        lblBookingsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnlSideBar.add(lblBookingsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 35, 35));

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

        pnlSideBar.add(pnlHighlight, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 360, 50));

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
        pnlSideBar.add(btnBookingsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, -70, 1155, 700));

        getContentPane().add(pnlSideBar, java.awt.BorderLayout.WEST);

        pnlMainContent.setBackground(new java.awt.Color(48, 48, 46));
        pnlMainContent.setForeground(new java.awt.Color(255, 255, 255));
        pnlMainContent.setLayout(new java.awt.BorderLayout());
        getContentPane().add(pnlMainContent, java.awt.BorderLayout.CENTER);

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
        java.awt.EventQueue.invokeLater(() -> new AdminListingPanel().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBookingsButton;
    private javax.swing.JButton btnListingButton;
    private javax.swing.JButton btnLogoutButton;
    private javax.swing.JButton btnOverviewButton;
    private javax.swing.JButton btnSettingsButton;
    private javax.swing.JButton btnSupportButton;
    private javax.swing.JButton btnUsersButton;
    private javax.swing.JLabel lblAdmin;
    private javax.swing.JLabel lblBookingsIcon;
    private javax.swing.JLabel lblCarRental;
    private javax.swing.JLabel lblListingIcon;
    private javax.swing.JLabel lblLogoutIcon;
    private javax.swing.JLabel lblMain;
    private javax.swing.JLabel lblNotifyIcon;
    private javax.swing.JLabel lblOverviewIcon;
    private javax.swing.JLabel lblProfileIcon;
    private javax.swing.JLabel lblSettingsIcon;
    private javax.swing.JLabel lblSupportIcon;
    private javax.swing.JLabel lblUsersIcon;
    private javax.swing.JPanel pnlHighlight;
    private javax.swing.JPanel pnlMainContent;
    private javax.swing.JPanel pnlSideBar;
    private javax.swing.JPanel pnlTopBar;
    // End of variables declaration//GEN-END:variables
}
