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
import carrentalsystem.models.Booking;
import carrentalsystem.models.Ticket;
import carrentalsystem.interfaces.IAdminService;
import carrentalsystem.services.AdminService;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminDashboard.class.getName());
    // Use the Service Layer to handle all DB logic - No more hardcoded passwords here!
    private final IAdminService adminService = new AdminService();
    private List<Booking> recentBookingsList;
    
    public AdminDashboard() {
    initComponents();
    setupNavigation();
    
    // Icons
        setIcon(lblOverviewIcon, "/carrentalsystem/ui/admin/PIC/four-squares.png", 35, 35);
        setIcon(lblListingIcon, "/carrentalsystem/ui/admin/PIC/Listing.png", 35, 35);
        setIcon(lblUsersIcon, "/carrentalsystem/ui/admin/PIC/Users.png", 35, 35);
        setIcon(lblBookingsIcon1, "/carrentalsystem/ui/admin/PIC/Bookings.png", 35, 35);
        setIcon(lblSupportIcon, "/carrentalsystem/ui/admin/PIC/support.png", 35, 35);
        setIcon(lblSettingsIcon, "/carrentalsystem/ui/admin/PIC/setting (1).png", 35, 35);
        setIcon(lblLogoutIcon, "/carrentalsystem/ui/admin/PIC/logout-white.png", 35, 35);
        setIcon(lblProfileIcon, "/carrentalsystem/ui/admin/PIC/Profile.png", 50, 50);
        setIcon(lblNotifyIcon, "/carrentalsystem/ui/admin/PIC/bell.png", 50, 50);
        
    setupTableStyles();

    // Fetch all data from the database using the Service[cite: 29]
    loadDashboardData();

    this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    
    
}
    
    
    private void setupNavigation() {
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

        btnSettingsButton.addActionListener(e -> {
            new AdminSettings().setVisible(true);
            this.dispose();
        });

        btnLogoutButton.addActionListener(e -> handleLogout());
    }
    
    private void loadDashboardData() {
        try {
            // 1. Fetch KPI Counts from Service layer[cite: 29]
            int users = adminService.countTotalUsers();
            int active = adminService.countActiveListings();
            int bookings = adminService.countBookingsToday();
            int pending = adminService.countPendingListings();
            int newUsers = adminService.countNewUsersThisWeek();
            int newCars = adminService.countNewListingsToday();
            int completedBookings = adminService.countCompletedBookingsToday();

            // 2. Update KPI StatCards (Removing hardcoded growth strings)[cite: 11, 22]
            TotalUsers.setData("Total Users", String.valueOf(users), "+" + newUsers + " this week", new Color(11, 213, 91));
            ActiveListings.setData("Active Listings", String.valueOf(active), "+" + newCars + " today", new Color(11, 213, 91));
            BookingsToday.setData("Bookings Today", String.valueOf(bookings), completedBookings + " completed", Color.WHITE);

            // Dynamically change color if action is needed[cite: 11]
            PendingApprovals.setData("Pending Approvals", String.valueOf(pending),
                    (pending > 0 ? "needs action" : "all clear"),
                    (pending > 0 ? Color.RED : Color.GRAY));
            
            PendingApprovals.setCursor(new Cursor(Cursor.HAND_CURSOR));
            PendingApprovals.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new AdminListingPanel().setVisible(true);
                    dispose();
                }
            });
            
            // 3. Populate "Listings by type" (Fixes the green overlap)[cite: 11, 21]
            pnlCarTypeContainer.removeAll();
            pnlCarTypeContainer.setLayout(new javax.swing.BoxLayout(pnlCarTypeContainer, javax.swing.BoxLayout.Y_AXIS));
            Map<String, Integer> types = adminService.getListingsByType();
            if (active > 0) {
                types.forEach((type, count) -> addDynamicBar(type, count, active));
            }

            // 4. Populate Support Tickets[cite: 11, 29]
            pnlSupportTicket1.removeAll();
            pnlSupportTicket1.setLayout(new javax.swing.BoxLayout(pnlSupportTicket1, javax.swing.BoxLayout.Y_AXIS));
            List<carrentalsystem.models.Ticket> tickets = adminService.getOpenTickets();
            for (carrentalsystem.models.Ticket t : tickets) {
                TicketRow row = new TicketRow(t);

                // ADD THIS: Make the row clickable
                row.setCursor(new Cursor(Cursor.HAND_CURSOR));
                row.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        new AdminSupportPanel().setVisible(true);
                        dispose();
                    }
                });

                pnlSupportTicket1.add(row);
                pnlSupportTicket1.add(javax.swing.Box.createVerticalStrut(10));
            }
            pnlSupportTicket1.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pnlSupportTicket1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new AdminSupportPanel().setVisible(true);
                    dispose();
                }
            });

            // 5. Populate Recent Bookings Table[cite: 14, 22]
            DefaultTableModel model = (DefaultTableModel) tableForBookings.getModel();
            model.setRowCount(0);
            recentBookingsList = adminService.getRecentBookings();
            for (carrentalsystem.models.Booking b : recentBookingsList) {
                model.addRow(new Object[]{
                    b.getRenterName(),
                    b.getCarBrand() + " " + b.getCarModel(),
                    b.getStartDate() + " - " + b.getEndDate(),
                    b.getStatus()
                });
            }

            // Final UI Refresh
            refreshListingBars(active);
            refreshSupportTickets();
            refreshBookingsTable();

        } catch (java.sql.SQLException e) {
            System.err.println("Database Fetch Error: " + e.getMessage());
        }
    }
    
    private void refreshListingBars(int totalActive) throws SQLException {
        pnlCarTypeContainer.removeAll();
        // Set layout to Y_AXIS so they stack vertically like the prototype
        pnlCarTypeContainer.setLayout(new BoxLayout(pnlCarTypeContainer, BoxLayout.Y_AXIS));

        Map<String, Integer> typeCounts = adminService.getListingsByType();
        if (totalActive > 0) {
            typeCounts.forEach((type, count) -> {
                addDynamicBar(type, count, totalActive);
            });
        }
        pnlCarTypeContainer.revalidate();
        pnlCarTypeContainer.repaint();
    }
    
    private void refreshSupportTickets() throws SQLException {
        pnlSupportTicket1.removeAll();
        pnlSupportTicket1.setLayout(new BoxLayout(pnlSupportTicket1, BoxLayout.Y_AXIS));

        List<Ticket> openTickets = adminService.getOpenTickets();

        for (Ticket t : openTickets) {
            // Assuming you created the TicketRow component we discussed
            pnlSupportTicket1.add(new TicketRow(t));
            pnlSupportTicket1.add(Box.createVerticalStrut(10));
        }
        pnlSupportTicket1.revalidate();
        pnlSupportTicket1.repaint();
    }
    
    private void refreshBookingsTable() throws SQLException {
        DefaultTableModel model = (DefaultTableModel) tableForBookings.getModel();
        model.setRowCount(0);

        recentBookingsList = adminService.getRecentBookings();
        
        for (Booking b : recentBookingsList) {
            model.addRow(new Object[]{
                b.getRenterName(),
                b.getCarBrand() + " " + b.getCarModel(),
                b.getStartDate() + " - " + b.getEndDate(),
                b.getStatus()
            });
        }
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
    
    
    
    private void addDynamicBar(String typeName, int count, int totalCars) {
    // 1. Create a wrapper panel (The Row)
    javax.swing.JPanel row = new javax.swing.JPanel(new java.awt.BorderLayout(15, 0));
    row.setOpaque(false);

    // 2. The Name (Left)
    javax.swing.JLabel name = new javax.swing.JLabel(typeName);
    name.setForeground(java.awt.Color.WHITE);
    name.setPreferredSize(new java.awt.Dimension(100, 25));

    // 3. The Bar (Center)
    javax.swing.JPanel bar = new javax.swing.JPanel();
    bar.setBackground(new java.awt.Color(11, 213, 91));
    int maxWidth = 150; 
    int barWidth = (totalCars > 0) ? (int)((double)count / totalCars * maxWidth) : 0;
    bar.setPreferredSize(new java.awt.Dimension(barWidth, 15));

    // 4. THE NUMBER (Right) - Use JLabel here!
    javax.swing.JLabel number = new javax.swing.JLabel(String.valueOf(count));
    number.setForeground(new java.awt.Color(11, 213, 91)); // Matches the bar color
    number.setFont(new java.awt.Font("Segoe UI", 1, 16));
    number.setPreferredSize(new java.awt.Dimension(30, 25));

    // 5. Assemble the Row
    row.add(name, java.awt.BorderLayout.WEST);
    row.add(bar, java.awt.BorderLayout.CENTER);
    row.add(number, java.awt.BorderLayout.EAST); // Puts the number on the right
    
    pnlCarTypeContainer.add(row);
}
    
    
    private void centerTableText() {
    javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Fix cell background and text color
            if (!isSelected) {
                c.setBackground(new java.awt.Color(48, 48, 46));
            } else {
                c.setBackground(new java.awt.Color(60, 60, 60)); // Highlight color
            }
            c.setForeground(java.awt.Color.WHITE);
            
            // FIX THE FONT HERE
            c.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
            
            return c;
        }
    };

    centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

    for (int i = 0; i < tableForBookings.getColumnCount(); i++) {
        tableForBookings.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}
    
    private void centerTableData() {
    javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
    
    // Loop through every column and apply the center alignment
    for (int i = 0; i < tableForBookings.getColumnCount(); i++) {
        tableForBookings.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}
    
    private void applyStatusShaping() {
    // We target specifically the Status column (Index 3 in your model: Renter, Car, Dates, Status)
    tableForBookings.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // 1. Basic Setup
            setOpaque(false); // Make it false so our custom shape shows
            setForeground(java.awt.Color.WHITE);
            setHorizontalAlignment(javax.swing.JLabel.CENTER);
            setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            
            return this;
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            String status = getText().toLowerCase();
            
            // 2. Set color based on text
            if (status.contains("active") || status.contains("completed")) {
                g2.setColor(new java.awt.Color(11, 213, 91)); // Green
            } else if (status.contains("pending")) {
                g2.setColor(new java.awt.Color(255, 193, 7)); // Yellow
            } else {
                g2.setColor(new java.awt.Color(70, 70, 70)); // Default Grey
            }

            // 3. Draw the Rounded Rectangle (Badge style)
            // Adjust the 5, 5, width-10, height-10 to change the padding
            g2.fillRoundRect(10, 8, getWidth() - 20, getHeight() - 16, 20, 20);
            
            g2.dispose();
            super.paintComponent(g);
        }
    });
}
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
    
    private void setupTableStyles() {
        centerTableText(); // Reuses your existing centering logic[cite: 31]
        applyStatusShaping(); // Reuses your existing badge logic[cite: 31]
        tableForBookings.setRowHeight(40);
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
        pnlHighlight = new javax.swing.JPanel();
        btnOverviewButton = new javax.swing.JButton();
        lblListingIcon = new javax.swing.JLabel();
        btnListingButton = new javax.swing.JButton();
        lblUsersIcon = new javax.swing.JLabel();
        btnUsersButton = new javax.swing.JButton();
        lblBookingsIcon1 = new javax.swing.JLabel();
        btnBookingsButton = new javax.swing.JButton();
        lblSupportIcon = new javax.swing.JLabel();
        btnSupportButton = new javax.swing.JButton();
        lblSettingsIcon = new javax.swing.JLabel();
        btnSettingsButton = new javax.swing.JButton();
        lblLogoutIcon = new javax.swing.JLabel();
        btnLogoutButton = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        pnlCardContainer = new javax.swing.JPanel();
        TotalUsers = new carrentalsystem.ui.admin.StatCard();
        ActiveListings = new carrentalsystem.ui.admin.StatCard();
        BookingsToday = new carrentalsystem.ui.admin.StatCard();
        PendingApprovals = new carrentalsystem.ui.admin.StatCard();
        lblOverview = new javax.swing.JLabel();
        pnlCarTypeContainer = new javax.swing.JPanel();
        lblListingsbytype = new javax.swing.JLabel();
        lblRecentBookings = new javax.swing.JLabel();
        pnlBookings = new javax.swing.JPanel();
        spBookings = new javax.swing.JScrollPane();
        tableForBookings = new javax.swing.JTable();
        pnlSupportTicket1 = new javax.swing.JPanel();
        lblOpenSourceTickets1 = new javax.swing.JLabel();

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
        lblOverviewIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/admin/PIC/four-squares.png"))); // NOI18N
        pnlSideBar.add(lblOverviewIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 35, 35));

        pnlHighlight.setBackground(new java.awt.Color(48, 48, 46));

        btnOverviewButton.setBackground(new java.awt.Color(48, 48, 46));
        btnOverviewButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnOverviewButton.setForeground(new java.awt.Color(255, 255, 255));
        btnOverviewButton.setText("Overview");
        btnOverviewButton.setBorder(null);
        btnOverviewButton.setBorderPainted(false);
        btnOverviewButton.setContentAreaFilled(false);
        btnOverviewButton.setFocusPainted(false);
        btnOverviewButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOverviewButton.setPreferredSize(new java.awt.Dimension(270, 50));

        javax.swing.GroupLayout pnlHighlightLayout = new javax.swing.GroupLayout(pnlHighlight);
        pnlHighlight.setLayout(pnlHighlightLayout);
        pnlHighlightLayout.setHorizontalGroup(
            pnlHighlightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHighlightLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(btnOverviewButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHighlightLayout.setVerticalGroup(
            pnlHighlightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHighlightLayout.createSequentialGroup()
                .addComponent(btnOverviewButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlSideBar.add(pnlHighlight, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 360, 50));

        lblListingIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblListingIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/admin/PIC/Listing.png"))); // NOI18N
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
        lblUsersIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/admin/PIC/Users.png"))); // NOI18N
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

        lblBookingsIcon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBookingsIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/admin/PIC/Bookings.png"))); // NOI18N
        pnlSideBar.add(lblBookingsIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 35, 35));

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
        lblSupportIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/admin/PIC/support.png"))); // NOI18N
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
        lblSettingsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/admin/PIC/setting (1).png"))); // NOI18N
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
        lblLogoutIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/admin/PIC/logout-white.png"))); // NOI18N
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

        pnlCardContainer.setBackground(new java.awt.Color(38, 38, 36));
        pnlCardContainer.setOpaque(false);
        pnlCardContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 10));

        TotalUsers.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TotalUsers.setPreferredSize(new java.awt.Dimension(220, 160));
        pnlCardContainer.add(TotalUsers);

        ActiveListings.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ActiveListings.setPreferredSize(new java.awt.Dimension(220, 160));
        pnlCardContainer.add(ActiveListings);

        BookingsToday.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BookingsToday.setPreferredSize(new java.awt.Dimension(220, 160));
        pnlCardContainer.add(BookingsToday);

        PendingApprovals.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PendingApprovals.setPreferredSize(new java.awt.Dimension(220, 160));
        pnlCardContainer.add(PendingApprovals);

        pnlMain.add(pnlCardContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 50, -1, -1));

        lblOverview.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lblOverview.setForeground(new java.awt.Color(255, 255, 255));
        lblOverview.setText("Overview");
        pnlMain.add(lblOverview, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        pnlCarTypeContainer.setBackground(new java.awt.Color(38, 38, 36));
        pnlCarTypeContainer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlCarTypeContainer.setPreferredSize(new java.awt.Dimension(531, 200));
        pnlCarTypeContainer.setLayout(new javax.swing.BoxLayout(pnlCarTypeContainer, javax.swing.BoxLayout.Y_AXIS));

        lblListingsbytype.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblListingsbytype.setForeground(new java.awt.Color(255, 255, 255));
        lblListingsbytype.setText("Listings by type");
        lblListingsbytype.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblListingsbytype.setAlignmentY(0.0F);
        lblListingsbytype.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        pnlCarTypeContainer.add(lblListingsbytype);

        pnlMain.add(pnlCarTypeContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 236, 400, -1));

        lblRecentBookings.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblRecentBookings.setForeground(new java.awt.Color(255, 255, 255));
        lblRecentBookings.setText("Recent Bookings");
        pnlMain.add(lblRecentBookings, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, -1, -1));

        pnlBookings.setBackground(new java.awt.Color(48, 48, 46));
        pnlBookings.setPreferredSize(new java.awt.Dimension(980, 500));
        pnlBookings.setLayout(new java.awt.BorderLayout());

        spBookings.setBackground(new java.awt.Color(46, 46, 48));
        spBookings.setPreferredSize(new java.awt.Dimension(980, 500));

        tableForBookings.setBackground(new java.awt.Color(48, 48, 46));
        tableForBookings.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tableForBookings.setForeground(new java.awt.Color(48, 48, 46));
        tableForBookings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Renter", "Car", "Dates", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableForBookings.setPreferredSize(new java.awt.Dimension(500, 64));
        tableForBookings.setRowHeight(50);
        tableForBookings.setSelectionBackground(new java.awt.Color(60, 60, 60));
        spBookings.setViewportView(tableForBookings);

        pnlBookings.add(spBookings, java.awt.BorderLayout.CENTER);

        pnlMain.add(pnlBookings, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        pnlSupportTicket1.setBackground(new java.awt.Color(38, 38, 36));
        pnlSupportTicket1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlSupportTicket1.setPreferredSize(new java.awt.Dimension(531, 200));
        pnlSupportTicket1.setLayout(new javax.swing.BoxLayout(pnlSupportTicket1, javax.swing.BoxLayout.Y_AXIS));

        lblOpenSourceTickets1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblOpenSourceTickets1.setForeground(new java.awt.Color(255, 255, 255));
        lblOpenSourceTickets1.setText("Open support tickets");
        lblOpenSourceTickets1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblOpenSourceTickets1.setAlignmentY(0.0F);
        lblOpenSourceTickets1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        pnlSupportTicket1.add(lblOpenSourceTickets1);

        pnlMain.add(pnlSupportTicket1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 236, -1, -1));

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
        java.awt.EventQueue.invokeLater(() -> new AdminDashboard().setVisible(true));
        
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private carrentalsystem.ui.admin.StatCard ActiveListings;
    private carrentalsystem.ui.admin.StatCard BookingsToday;
    private carrentalsystem.ui.admin.StatCard PendingApprovals;
    private carrentalsystem.ui.admin.StatCard TotalUsers;
    private javax.swing.JButton btnBookingsButton;
    private javax.swing.JButton btnListingButton;
    private javax.swing.JButton btnLogoutButton;
    private javax.swing.JButton btnOverviewButton;
    private javax.swing.JButton btnSettingsButton;
    private javax.swing.JButton btnSupportButton;
    private javax.swing.JButton btnUsersButton;
    private javax.swing.JLabel lblAdmin;
    private javax.swing.JLabel lblBookingsIcon1;
    private javax.swing.JLabel lblCarRental;
    private javax.swing.JLabel lblListingIcon;
    private javax.swing.JLabel lblListingsbytype;
    private javax.swing.JLabel lblLogoutIcon;
    private javax.swing.JLabel lblMain;
    private javax.swing.JLabel lblNotifyIcon;
    private javax.swing.JLabel lblOpenSourceTickets1;
    private javax.swing.JLabel lblOverview;
    private javax.swing.JLabel lblOverviewIcon;
    private javax.swing.JLabel lblProfileIcon;
    private javax.swing.JLabel lblRecentBookings;
    private javax.swing.JLabel lblSettingsIcon;
    private javax.swing.JLabel lblSupportIcon;
    private javax.swing.JLabel lblUsersIcon;
    private javax.swing.JPanel pnlBookings;
    private javax.swing.JPanel pnlCarTypeContainer;
    private javax.swing.JPanel pnlCardContainer;
    private javax.swing.JPanel pnlHighlight;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlSideBar;
    private javax.swing.JPanel pnlSupportTicket1;
    private javax.swing.JPanel pnlTopBar;
    private javax.swing.JScrollPane spBookings;
    private javax.swing.JTable tableForBookings;
    // End of variables declaration//GEN-END:variables
}
