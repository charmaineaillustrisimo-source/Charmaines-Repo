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
import carrentalsystem.auth.LoginFrame;
import carrentalsystem.interfaces.IAdminService;
import carrentalsystem.services.AdminService;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class ApprovalQueuePanel extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ApprovalQueuePanel.class.getName());
    private final IAdminService adminService = new AdminService();
    private List<carrentalsystem.models.User> currentUsers;
    // ── ADD: Lister verification fields ──────────────────────────────────
    private javax.swing.JTable tableListerVerif;
    private java.util.List<carrentalsystem.models.ListerRequirement> listerReqList = new java.util.ArrayList<>();
    
    public ApprovalQueuePanel() {
        initComponents();
        setupTableStyles();
        setupNavigation();
        setupIcons();
        
        // Initial data load
        loadUsersFromDatabase();
        
        setupTabbedLayout();
        loadListerVerifications();

        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        // 1. Set the background of the header area to match your panel
      tableUsers.getTableHeader().setBackground(new Color(48, 48, 46));
    // 2. Set the text color to white
      tableUsers.getTableHeader().setForeground(Color.BLACK);

   // 3. Optional: Make the font bold so it stands out
       tableUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
    }
    
    private void setupTableStyles() {
        Color panelBg = new Color(48, 48, 46);

        // Style the ScrollPane
        spUser.setBackground(panelBg);
        spUser.getViewport().setBackground(panelBg);
        spUser.setBorder(BorderFactory.createEmptyBorder());

        // Style the Table
        tableUsers.setBackground(panelBg);
        tableUsers.setForeground(Color.WHITE);
        tableUsers.setRowHeight(60);
        tableUsers.setSelectionBackground(new Color(70, 70, 70));
        tableUsers.setShowGrid(false);
        tableUsers.setIntercellSpacing(new Dimension(0, 0));

        // Center all column text and set white foreground
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.WHITE);
                setBackground(isSelected ? new Color(70, 70, 70) : panelBg);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)));
                return c;
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tableUsers.getColumnCount(); i++) {
            tableUsers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Add Click Listener for the "Actions" column
        tableUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tableUsers.rowAtPoint(e.getPoint());
                int col = tableUsers.columnAtPoint(e.getPoint());
                if (col == 4 && row != -1) {
                    showUserOptions(row);
                }
            }
        });
    }
    
    public void loadUsersFromDatabase() {
        DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
        model.setRowCount(0);

        try {
            // Note: Ensure you have added 'getAllUsers' to your AdminService
            currentUsers = adminService.getAllUsers();

            for (carrentalsystem.models.User user : currentUsers) {
                model.addRow(new Object[]{
                    user.getFullName(),
                    user.getEmail(),
                    user.getTier(),
                    "Check Details", // Dynamic listing count logic can go here
                    "MANAGE"
                });
            }
        } catch (SQLException e) {
            System.err.println("Load Users Error: " + e.getMessage());
        }
    }
    
    private void showUserOptions(int row) {
        carrentalsystem.models.User user = currentUsers.get(row);
        // Added "Verify ID" to the options list
        String[] options = {"Verify ID", "Send Warning", "Ban User", "Change Plan", "Cancel"};

        int choice = JOptionPane.showOptionDialog(this,
                "Manage Account: " + user.getFullName(), "User Moderation",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        try {
            switch (choice) {
                case 0: // Verify ID (New Case)
                    carrentalsystem.models.ListerRequirement matchingReq = null;
                    if (listerReqList != null) {
                        for (carrentalsystem.models.ListerRequirement req : listerReqList) {
                            if (req.getUserId() == user.getUserId()) {
                                matchingReq = req;
                                break;
                            }
                        }
                    }

                    // 2. Pass the Requirement object (matchingReq) NOT the User object
                    if (matchingReq != null) {
                        handleDocumentVerification(matchingReq);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "This user has not submitted any verification documents yet.",
                                "No Documents Found", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 1: // Warning (Was case 0)
                    String msg = JOptionPane.showInputDialog(this, "Enter Warning Message:");
                    if (msg != null && !msg.trim().isEmpty()) {
                        adminService.warnUser(user.getUserId(), msg);
                        JOptionPane.showMessageDialog(this, "Warning sent.");
                    }
                    break;
                case 2: // Ban User (Was case 1)
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "This will ban the user and remove them from active status. Confirm?",
                            "Fraud Detection", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        adminService.banUser(user.getUserId());
                    }
                    break;
                case 3: // Change Tier (Was case 2)
                    String[] tiers = {"FREE", "PRO"};
                    String newTier = (String) JOptionPane.showInputDialog(this, "Select new tier:",
                            "Update Tier", JOptionPane.QUESTION_MESSAGE, null, tiers, user.getTier());
                    if (newTier != null) {
                        adminService.changeUserTier(user.getUserId(), newTier);
                    }
                    break;
            }
            loadUsersFromDatabase(); // Refresh table
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Action failed: " + e.getMessage());
        }
    }
    
    private void setupNavigation() {
        // 1. Overview Button
        btnOverviewButton.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            this.dispose();
        });

        // 2. Listing Button
        btnListingButton.addActionListener(e -> {
            new AdminListingPanel().setVisible(true);
            this.dispose();
        });

        // 3. Users Button (Current Panel)
        btnUsersButton.addActionListener(e -> {
            // Just refresh the data instead of opening a new frame if already here
            loadUsersFromDatabase();
        });

        // 4. Bookings Button
        btnBookingsButton.addActionListener(e -> {
            new AdminBookingPanel().setVisible(true);
            this.dispose();
        });

        // 5. Support Button
        btnSupportButton.addActionListener(e -> {
            new AdminSupportPanel().setVisible(true);
            this.dispose();
        });

        // 6. Settings Button
        btnSettingsButton.addActionListener(e -> {
            new AdminSettings().setVisible(true);
            this.dispose();
        });

        // 7. Logout Button with Confirmation
        btnLogoutButton.addActionListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?", "Logout",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                new carrentalsystem.auth.LoginFrame().setVisible(true);
                this.dispose();
            }
        });
    }

    private void setupIcons() {
        setIcon(lblOverviewIcon, "/carrentalsystem/ui/admin/PIC/four-squares.png", 35, 35);
        setIcon(lblListingIcon, "/carrentalsystem/ui/admin/PIC/Listing.png", 35, 35);
        setIcon(lblUsersIcon, "/carrentalsystem/ui/admin/PIC/Users.png", 35, 35);
        setIcon(lblBookingsIcon, "/carrentalsystem/ui/admin/PIC/Bookings.png", 35, 35);
        setIcon(lblLogoutIcon, "/carrentalsystem/ui/admin/PIC/logout-white.png", 35, 35);
        setIcon(lblSupportIcon, "/carrentalsystem/ui/admin/PIC/support.png", 35, 35);
        setIcon(lblSettingsIcon, "/carrentalsystem/ui/admin/PIC/setting (1).png", 35, 35);
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
            System.err.println("Icon error: " + path);
        }
    }
    
    /**
     * Replaces the flat pnlMain layout with a JTabbedPane containing: Tab 1 —
     * User Management (existing spUser table) Tab 2 — Lister Verifications (new
     * tableListerVerif)
     */
    private void setupTabbedLayout() {

        Color darkBg = new Color(38, 38, 36);
        Color tabBg = new Color(48, 48, 46);
        Color white = Color.WHITE;

        // ── Build the Lister Verifications table ──────────────────────────
        tableListerVerif = new javax.swing.JTable(
                new javax.swing.table.DefaultTableModel(
                        new Object[][]{},
                        new String[]{"User", "Email", "Submitted", "Status", "Action"}
                ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        }
        );
        tableListerVerif.setBackground(tabBg);
        tableListerVerif.setForeground(white);
        tableListerVerif.setRowHeight(55);
        tableListerVerif.setShowGrid(false);
        tableListerVerif.setIntercellSpacing(new Dimension(0, 0));
        tableListerVerif.setSelectionBackground(new Color(70, 70, 70));
        tableListerVerif.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // Style headers
        tableListerVerif.getTableHeader().setBackground(darkBg);
        tableListerVerif.getTableHeader().setForeground(white);
        tableListerVerif.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Cell renderer — white text, bottom border
        javax.swing.table.DefaultTableCellRenderer lvRenderer
                = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                c.setForeground(white);
                c.setBackground(sel ? new Color(70, 70, 70) : tabBg);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new Color(80, 80, 80)));
                return c;
            }
        };
        for (int i = 0; i < tableListerVerif.getColumnCount(); i++) {
            tableListerVerif.getColumnModel().getColumn(i).setCellRenderer(lvRenderer);
        }

        // Status column — colored badge
        tableListerVerif.getColumnModel().getColumn(3)
                .setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable t, Object v,
                            boolean sel, boolean foc, int row, int col) {
                        super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                        setHorizontalAlignment(JLabel.CENTER);
                        setOpaque(false);
                        String status = v != null ? v.toString() : "";
                        switch (status) {
                            case "APPROVED":
                                setForeground(new Color(11, 213, 91));
                                break;
                            case "REJECTED":
                                setForeground(new Color(220, 80, 80));
                                break;
                            default:
                                setForeground(new Color(229, 192, 123));
                                break;
                        }
                        return this;
                    }
                });

        // Action column — "REVIEW" button text styled
        tableListerVerif.getColumnModel().getColumn(4)
                .setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable t, Object v,
                            boolean sel, boolean foc, int row, int col) {
                        super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                        setHorizontalAlignment(JLabel.CENTER);
                        setForeground(new Color(100, 180, 255));
                        setBackground(sel ? new Color(70, 70, 70) : tabBg);
                        return this;
                    }
                });

        // Click listener — review on column 4 click
        tableListerVerif.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tableListerVerif.rowAtPoint(e.getPoint());
                int col = tableListerVerif.columnAtPoint(e.getPoint());

                // Column 4 is the Action column
                if (col == 4 && row >= 0 && row < listerReqList.size()) {
                    // FIX: Get the Requirement from listerReqList, NOT User from currentUsers
                    carrentalsystem.models.ListerRequirement selectedReq = listerReqList.get(row);
                    handleDocumentVerification(selectedReq);
                }
            }
        });

        javax.swing.JScrollPane spLister = new javax.swing.JScrollPane(tableListerVerif);
        spLister.setBackground(tabBg);
        spLister.getViewport().setBackground(tabBg);
        spLister.setBorder(BorderFactory.createEmptyBorder());

        // ── Pending badge panel for lister tab ────────────────────────────
        javax.swing.JPanel listerHeader = new javax.swing.JPanel(new BorderLayout(16, 0));
        listerHeader.setBackground(darkBg);
        listerHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        javax.swing.JLabel lblListerTitle = new javax.swing.JLabel("Lister Verification Queue");
        lblListerTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        lblListerTitle.setForeground(white);

        javax.swing.JButton btnRefreshLister = new javax.swing.JButton("↻ Refresh");
        btnRefreshLister.setBackground(new Color(60, 60, 60));
        btnRefreshLister.setForeground(white);
        btnRefreshLister.setFocusPainted(false);
        btnRefreshLister.setBorderPainted(false);
        btnRefreshLister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefreshLister.addActionListener(e -> loadListerVerifications());

        listerHeader.add(lblListerTitle, BorderLayout.WEST);
        listerHeader.add(btnRefreshLister, BorderLayout.EAST);

        javax.swing.JPanel listerTab = new javax.swing.JPanel(new BorderLayout());
        listerTab.setBackground(tabBg);
        listerTab.add(listerHeader, BorderLayout.NORTH);
        listerTab.add(spLister, BorderLayout.CENTER);

        // ── Users tab wrapper ─────────────────────────────────────────────
        javax.swing.JPanel usersTab = new javax.swing.JPanel(new BorderLayout());
        usersTab.setBackground(tabBg);

        javax.swing.JPanel usersHeader = new javax.swing.JPanel(new BorderLayout(16, 0));
        usersHeader.setBackground(darkBg);
        usersHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        javax.swing.JLabel lblUsersTitle = new javax.swing.JLabel("User Management");
        lblUsersTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        lblUsersTitle.setForeground(white);

        javax.swing.JButton btnRefreshUsers = new javax.swing.JButton("↻ Refresh");
        btnRefreshUsers.setBackground(new Color(60, 60, 60));
        btnRefreshUsers.setForeground(white);
        btnRefreshUsers.setFocusPainted(false);
        btnRefreshUsers.setBorderPainted(false);
        btnRefreshUsers.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefreshUsers.addActionListener(e -> loadUsersFromDatabase());

        usersHeader.add(lblUsersTitle, BorderLayout.WEST);
        usersHeader.add(btnRefreshUsers, BorderLayout.EAST);

        // Move the existing spUser into the users tab
        pnlMain.remove(spUser);
        usersTab.add(usersHeader, BorderLayout.NORTH);
        usersTab.add(spUser, BorderLayout.CENTER);

        // ── Build JTabbedPane ─────────────────────────────────────────────
        javax.swing.JTabbedPane tabs = new javax.swing.JTabbedPane();
        tabs.setBackground(darkBg);
        tabs.setForeground(white);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("👥  Users", usersTab);
        tabs.addTab("🔍  Lister Verifications", listerTab);

        // ── Rebuild pnlMain ───────────────────────────────────────────────
        pnlMain.removeAll();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(tabBg);
        pnlMain.add(tabs, BorderLayout.CENTER);
        pnlMain.revalidate();
        pnlMain.repaint();
    }
    
    /**
     * Fetches ALL lister verification submissions (all statuses) and populates
     * the verification table.
     */
    public void loadListerVerifications() {
        try {
            // Load ALL statuses so admin can see full history
            // Ordered: PENDING first, then others
            listerReqList = new java.util.ArrayList<>();
            listerReqList.addAll(adminService.getListerRequirements("PENDING"));
            listerReqList.addAll(adminService.getListerRequirements("APPROVED"));
            listerReqList.addAll(adminService.getListerRequirements("REJECTED"));

            DefaultTableModel model = (DefaultTableModel) tableListerVerif.getModel();
            model.setRowCount(0);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");

            for (carrentalsystem.models.ListerRequirement req : listerReqList) {
                String submittedStr = req.getSubmittedAt() != null
                        ? sdf.format(req.getSubmittedAt()) : "—";
                String actionLabel = "PENDING".equals(req.getStatus())
                        ? "🔍 REVIEW" : "👁 VIEW";
                model.addRow(new Object[]{
                    req.getUserFullName(),
                    req.getUserEmail(),
                    submittedStr,
                    req.getStatus(),
                    actionLabel
                });
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading verifications: " + e.getMessage(),
                    "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void handleDocumentVerification(carrentalsystem.models.ListerRequirement req) {
        if (req == null) {
            return;
        }

        ViewingDocumentsPanel viewPanel = new ViewingDocumentsPanel(req);
        viewPanel.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        viewPanel.setLocationRelativeTo(this);
        viewPanel.setVisible(true);

        // Refresh tables after admin review
        viewPanel.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadListerVerifications();
                loadUsersFromDatabase();
            }
        });
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
        pnlHighlight = new javax.swing.JPanel();
        lblBookingsIcon = new javax.swing.JLabel();
        btnBookingsButton = new javax.swing.JButton();
        lblSupportIcon = new javax.swing.JLabel();
        btnSupportButton = new javax.swing.JButton();
        lblSettingsIcon = new javax.swing.JLabel();
        btnSettingsButton = new javax.swing.JButton();
        lblLogoutIcon = new javax.swing.JLabel();
        btnLogoutButton = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        lblUserManagement = new javax.swing.JLabel();
        spUser = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();

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

        pnlSideBar.add(pnlHighlight, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 360, 50));

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

        getContentPane().add(pnlSideBar, java.awt.BorderLayout.WEST);

        pnlMain.setBackground(new java.awt.Color(48, 48, 46));
        pnlMain.setForeground(new java.awt.Color(255, 255, 255));
        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUserManagement.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lblUserManagement.setForeground(new java.awt.Color(255, 255, 255));
        lblUserManagement.setText("User Management");
        pnlMain.add(lblUserManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        spUser.setBorder(null);
        spUser.setPreferredSize(new java.awt.Dimension(1000, 1000));

        tableUsers.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tableUsers.setForeground(new java.awt.Color(48, 48, 46));
        tableUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User", "Email", "Plan", "Listings", "Actions"
            }
        ));
        tableUsers.setPreferredSize(new java.awt.Dimension(610, 500));
        spUser.setViewportView(tableUsers);

        pnlMain.add(spUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 840, 400));

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

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
        java.awt.EventQueue.invokeLater(() -> new ApprovalQueuePanel().setVisible(true));
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
    private javax.swing.JLabel lblUserManagement;
    private javax.swing.JLabel lblUsersIcon;
    private javax.swing.JPanel pnlHighlight;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlSideBar;
    private javax.swing.JPanel pnlTopBar;
    private javax.swing.JScrollPane spUser;
    private javax.swing.JTable tableUsers;
    // End of variables declaration//GEN-END:variables
}
