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

public class ApprovalQueuePanel extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ApprovalQueuePanel.class.getName());
    // Add this at the top of your class variables
    private java.util.List<carrentalsystem.models.Car> userCarList = new java.util.ArrayList<>();

    /**
     * Creates new form AdminDashboard
     */
    public ApprovalQueuePanel() {
        initComponents();
        styleUserTable();
        centerTableData();
        loadUsersFromDatabase();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        tableUsers.repaint();
        tableUsers.revalidate();
        
        tableUsers.setShowGrid(false);
        tableUsers.setIntercellSpacing(new java.awt.Dimension(0, 0));

        //Position Top Bar Icons
        TopBarPanel.add(ProfileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 20, 50, 50));
        TopBarPanel.add(NotifyIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 20, 50, 50));

        // Add this to your constructor after initComponents()
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int width = getWidth();
                // Position Profile 70 pixels from the right edge
                ProfileIcon.setLocation(width - 90, ProfileIcon.getY());
                // Position Notify 140 pixels from the right edge
                NotifyIcon.setLocation(width - 160, NotifyIcon.getY());
            }
        });

        // The "Easy Way" - One line per icon
        setIcon(OverviewIcon, "/carrentalsystem/ui/admin/PIC/four-squares.png", 35, 35);
        setIcon(ListingIcon, "/carrentalsystem/ui/admin/PIC/Listing.png", 35, 35);
        setIcon(UsersIcon, "/carrentalsystem/ui/admin/PIC/Users.png", 35, 35);
        setIcon(BookingsIcon1, "/carrentalsystem/ui/admin/PIC/Bookings.png", 35, 35);
        setIcon(SupportIcon, "/carrentalsystem/ui/admin/PIC/support.png", 35, 35);
        setIcon(SettingsIcon, "/carrentalsystem/ui/admin/PIC/setting (1).png", 35, 35);
        setIcon(LogoutIcon, "/carrentalsystem/ui/admin/PIC/logout-white.png", 35, 35);
        //Top Bar Panel Icon
        setIcon(ProfileIcon, "/carrentalsystem/ui/admin/PIC/Profile.png", 50, 50);
        setIcon(NotifyIcon, "/carrentalsystem/ui/admin/PIC/bell.png", 50, 50);
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

    public void loadUsersFromDatabase() {
        // Get the table model and clear any existing rows
        DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
        model.setRowCount(0);

        String url = "jdbc:mysql://localhost:3306/car_rental";
        String user = "root";
        String password = ""; // Leave empty if you don't have one

        // Query to get user details
        // Note: You might need to JOIN tables if 'Listings' count is in a different table
        String query = "SELECT name, email, plan_type, (SELECT COUNT(*) FROM cars WHERE owner_id = users.id) as listing_count FROM users";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String plan = rs.getString("plan_type");
                int listings = rs.getInt("listing_count");

                // Add a "View" string for the action column
                model.addRow(new Object[]{name, email, plan, listings + " active", "View"});
            }

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private void styleUserTable() {
        java.awt.Color panelColor = new java.awt.Color(48, 48, 46);
        
    tableUsers.setBackground(panelColor);
    tableUsers.setFillsViewportHeight(true);
    tableUsers.setRowHeight(50);
    tableUsers.setShowGrid(false); // Remove grid lines
    tableUsers.setBorder(null);
    tableUsers.setIntercellSpacing(new java.awt.Dimension(0, 0));

        jScrollPane1.setBackground(panelColor); 
        jScrollPane1.getViewport().setBackground(panelColor);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportBorder(null);
        
        addRowSeparators();
        applyRowStyles();
        
        tableUsers.setBackground(panelColor);
        tableUsers.setFillsViewportHeight(true);
        
        jScrollPane1.getVerticalScrollBar().setBackground(panelColor);
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(8, 0)); // Thinner scrollbar
        jScrollPane1.getVerticalScrollBar().setBorder(null);
        
        jScrollPane1.setCorner(javax.swing.JScrollPane.UPPER_RIGHT_CORNER, new javax.swing.JPanel() {{
        setBackground(panelColor);
    }});

    // 4. Apply the invisible scrollbar UI
    makeScrollBarInvisible(jScrollPane1);
        
        applyInvisibleHeaderStyle();

    // 3. Style the Header to match the "Users" image
    javax.swing.table.JTableHeader header = tableUsers.getTableHeader();
    header.setBackground(panelColor);
    header.setForeground(new java.awt.Color(200, 200, 200)); // Slightly dimmed white for headers
    header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
    
    header.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(80, 80, 80)));
    
    // Check that this is exactly column 2
tableUsers.getColumnModel().getColumn(2).setCellRenderer(new BadgeRenderer());
tableUsers.getColumnModel().getColumn(4).setCellEditor(new TableButtonEditor(new JTextField()));
    }
    
    private void applyBadgeStyle() {
    // Assuming "Plan" is the 3rd column (index 2)
    tableUsers.getColumnModel().getColumn(2).setCellRenderer(new BadgeRenderer());
}
    
    private void centerTableText() {
    java.awt.Color bg = new java.awt.Color(48, 48, 46);
    java.awt.Color line = new java.awt.Color(80, 80, 80);

    javax.swing.table.DefaultTableCellRenderer customRenderer = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            // Get the default component (usually a JLabel)
            javax.swing.JLabel c = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // 1. Set Background to match jPanel1
            c.setBackground(isSelected ? table.getSelectionBackground() : bg);
            
            // 2. Set Text Color to White
            c.setForeground(java.awt.Color.WHITE);
            
            // 3. Set Alignment (LEFT looks best for 'User' and 'Email')
            c.setHorizontalAlignment(javax.swing.JLabel.LEFT);
            
            // 4. Add the thin bottom line separator from your reference image
            c.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, line), // Bottom line
                javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0)       // Left padding
            ));

            return c;
        }
    };

    // Apply this to every column in your table
    for (int i = 0; i < tableUsers.getColumnCount(); i++) {
        tableUsers.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
    }
}
    
    private void styleCells() {
    java.awt.Color panelColor = new java.awt.Color(48, 48, 46);
    java.awt.Color separatorColor = new java.awt.Color(80, 80, 80); // The gray line color

    javax.swing.table.DefaultTableCellRenderer renderer = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            javax.swing.JLabel c = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Set background and text
            c.setBackground(isSelected ? new java.awt.Color(60, 60, 60) : panelColor);
            c.setForeground(java.awt.Color.WHITE);
            c.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
            
            // Create the thin bottom line (separator)
            c.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, separatorColor));
            
            // Adjust padding so text isn't touching the line
            c.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                c.getBorder(), 
                javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0)
            ));

            return c;
        }
    };

    // Apply to all columns
    for (int i = 0; i < tableUsers.getColumnCount(); i++) {
        tableUsers.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }
}

    private void centerTableData() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        // Loop through every column and apply the center alignment
        for (int i = 0; i < tableUsers.getColumnCount(); i++) {
            tableUsers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void applyInvisibleHeaderStyle() {
    java.awt.Color panelBg = new java.awt.Color(48, 48, 46);
    
    // Get the header of your table
    javax.swing.table.JTableHeader header = tableUsers.getTableHeader();
    
    // Create a renderer to handle the white text and alignment
    header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // 1. Text Properties: White and Centered
            label.setForeground(java.awt.Color.WHITE);
            label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
            
            // 2. Background: Match the panel exactly
            label.setBackground(panelBg);
            
            // 3. Remove the default 'button' borders of the header
            label.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(80, 80, 80)));
            
            return label;
        }
    });
}
    
    private void makeScrollBarInvisible(javax.swing.JScrollPane scrollPane) {
    java.awt.Color panelBg = new java.awt.Color(48, 48, 46); // Your preferred background
    
    // Set the scrollbar's track color
    scrollPane.getVerticalScrollBar().setBackground(panelBg);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
    
    // Remove the buttons (the up/down arrows) to make it look cleaner
    scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new java.awt.Color(60, 60, 60); // Subtle thumb color
            this.trackColor = panelBg; // Match the panel background
        }

        @Override
        protected javax.swing.JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected javax.swing.JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private javax.swing.JButton createZeroButton() {
            javax.swing.JButton jbutton = new javax.swing.JButton();
            jbutton.setPreferredSize(new java.awt.Dimension(0, 0));
            jbutton.setMinimumSize(new java.awt.Dimension(0, 0));
            jbutton.setMaximumSize(new java.awt.Dimension(0, 0));
            return jbutton;
        }
    });
}
    
    private void addRowSeparators() {
    java.awt.Color panelBg = new java.awt.Color(48, 48, 46); // Your background color
    java.awt.Color separatorColor = new java.awt.Color(80, 80, 80); // Subtle gray for the line

    javax.swing.table.DefaultTableCellRenderer rowRenderer = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            javax.swing.JLabel c = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // 1. Maintain the "Invisible" background and white text
            c.setBackground(isSelected ? new java.awt.Color(60, 60, 60) : panelBg);
            c.setForeground(java.awt.Color.WHITE);
            c.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            
            // 2. The Horizontal Line: MatteBorder(top, left, bottom, right, color)
            // We only set the 'bottom' to 1 pixel.
            c.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, separatorColor));
            
            return c;
        }
    };

    // Apply this renderer to every column
    for (int i = 0; i < tableUsers.getColumnCount(); i++) {
        tableUsers.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
    }
}
    
    private void applyRowStyles() {
    java.awt.Color panelBg = new java.awt.Color(48, 48, 46); // Your background color
    java.awt.Color lineGray = new java.awt.Color(80, 80, 80); // The color of the line

    javax.swing.table.DefaultTableCellRenderer rowRenderer = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            javax.swing.JLabel c = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Background and Text
            c.setBackground(isSelected ? new java.awt.Color(60, 60, 60) : panelBg);
            c.setForeground(java.awt.Color.WHITE);
            c.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            
            // THE FIX: Draw only the bottom border line
            // MatteBorder(top, left, bottom, right, color)
            c.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, lineGray));
            
            return c;
        }
    };

    // Apply this to every column in your table
    for (int i = 0; i < tableUsers.getColumnCount(); i++) {
        tableUsers.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
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

        TopBarPanel = new javax.swing.JPanel();
        CarRental = new javax.swing.JLabel();
        ProfileIcon = new javax.swing.JLabel();
        NotifyIcon = new javax.swing.JLabel();
        sideBarPanel = new javax.swing.JPanel();
        Main1 = new javax.swing.JLabel();
        Admin = new javax.swing.JLabel();
        OverviewIcon = new javax.swing.JLabel();
        OverviewButton = new javax.swing.JButton();
        ListingIcon = new javax.swing.JLabel();
        ListingButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        UsersIcon = new javax.swing.JLabel();
        UsersButton = new javax.swing.JButton();
        BookingsIcon1 = new javax.swing.JLabel();
        BookingsButton = new javax.swing.JButton();
        SupportIcon = new javax.swing.JLabel();
        SupportButton = new javax.swing.JButton();
        SettingsIcon = new javax.swing.JLabel();
        SettingsButton = new javax.swing.JButton();
        LogoutIcon = new javax.swing.JLabel();
        LogoutButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblUserManagement = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(3, 33, 33));

        TopBarPanel.setBackground(new java.awt.Color(30, 30, 30));
        TopBarPanel.setPreferredSize(new java.awt.Dimension(1290, 90));
        TopBarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CarRental.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        CarRental.setForeground(new java.awt.Color(255, 255, 255));
        CarRental.setText("Rent A Car");
        TopBarPanel.add(CarRental, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        ProfileIcon.setPreferredSize(new java.awt.Dimension(20, 90));
        TopBarPanel.add(ProfileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 10, 80, 70));

        NotifyIcon.setPreferredSize(new java.awt.Dimension(20, 90));
        TopBarPanel.add(NotifyIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 20, 90, 60));

        getContentPane().add(TopBarPanel, java.awt.BorderLayout.NORTH);

        sideBarPanel.setBackground(new java.awt.Color(38, 38, 36));
        sideBarPanel.setMinimumSize(new java.awt.Dimension(300, 485));
        sideBarPanel.setPreferredSize(new java.awt.Dimension(353, 700));
        sideBarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Main1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Main1.setForeground(new java.awt.Color(255, 255, 255));
        Main1.setText("MAIN");
        sideBarPanel.add(Main1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        Admin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Admin.setForeground(new java.awt.Color(255, 255, 255));
        Admin.setText("ADMIN");
        sideBarPanel.add(Admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        OverviewIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(OverviewIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 35, 35));

        OverviewButton.setBackground(new java.awt.Color(38, 38, 36));
        OverviewButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        OverviewButton.setForeground(new java.awt.Color(255, 255, 255));
        OverviewButton.setText("Overview");
        OverviewButton.setBorder(null);
        OverviewButton.setBorderPainted(false);
        OverviewButton.setContentAreaFilled(false);
        OverviewButton.setFocusPainted(false);
        OverviewButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        OverviewButton.setPreferredSize(new java.awt.Dimension(270, 50));
        sideBarPanel.add(OverviewButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        ListingIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(ListingIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 35, 35));

        ListingButton.setBackground(new java.awt.Color(48, 48, 46));
        ListingButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ListingButton.setForeground(new java.awt.Color(255, 255, 255));
        ListingButton.setText("Listing");
        ListingButton.setBorder(null);
        ListingButton.setBorderPainted(false);
        ListingButton.setContentAreaFilled(false);
        ListingButton.setFocusPainted(false);
        ListingButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ListingButton.setPreferredSize(new java.awt.Dimension(270, 50));
        sideBarPanel.add(ListingButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, -1, -1));

        jPanel2.setBackground(new java.awt.Color(48, 48, 46));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        sideBarPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 360, 50));

        UsersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(UsersIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 35, 35));

        UsersButton.setBackground(new java.awt.Color(48, 48, 46));
        UsersButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UsersButton.setForeground(new java.awt.Color(255, 255, 255));
        UsersButton.setText("Users");
        UsersButton.setBorder(null);
        UsersButton.setBorderPainted(false);
        UsersButton.setContentAreaFilled(false);
        UsersButton.setFocusPainted(false);
        UsersButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        UsersButton.setPreferredSize(new java.awt.Dimension(270, 50));
        sideBarPanel.add(UsersButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, -1, -1));

        BookingsIcon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(BookingsIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 35, 35));

        BookingsButton.setBackground(new java.awt.Color(48, 48, 46));
        BookingsButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        BookingsButton.setForeground(new java.awt.Color(255, 255, 255));
        BookingsButton.setText("Bookings");
        BookingsButton.setBorder(null);
        BookingsButton.setBorderPainted(false);
        BookingsButton.setContentAreaFilled(false);
        BookingsButton.setFocusPainted(false);
        BookingsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BookingsButton.setPreferredSize(new java.awt.Dimension(270, 50));
        sideBarPanel.add(BookingsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 270, -1));

        SupportIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(SupportIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 35, 35));

        SupportButton.setBackground(new java.awt.Color(48, 48, 46));
        SupportButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        SupportButton.setForeground(new java.awt.Color(255, 255, 255));
        SupportButton.setText("Support");
        SupportButton.setBorder(null);
        SupportButton.setBorderPainted(false);
        SupportButton.setContentAreaFilled(false);
        SupportButton.setFocusPainted(false);
        SupportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SupportButton.setPreferredSize(new java.awt.Dimension(270, 50));
        sideBarPanel.add(SupportButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, -1));

        SettingsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(SettingsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 35, 35));

        SettingsButton.setBackground(new java.awt.Color(48, 48, 46));
        SettingsButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        SettingsButton.setForeground(new java.awt.Color(255, 255, 255));
        SettingsButton.setText("Settings");
        SettingsButton.setBorder(null);
        SettingsButton.setBorderPainted(false);
        SettingsButton.setContentAreaFilled(false);
        SettingsButton.setFocusPainted(false);
        SettingsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SettingsButton.setPreferredSize(new java.awt.Dimension(270, 50));
        sideBarPanel.add(SettingsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, -1, -1));

        LogoutIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(LogoutIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 35, 35));

        LogoutButton.setBackground(new java.awt.Color(48, 48, 46));
        LogoutButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        LogoutButton.setForeground(new java.awt.Color(255, 255, 255));
        LogoutButton.setText("Logout");
        LogoutButton.setBorder(null);
        LogoutButton.setBorderPainted(false);
        LogoutButton.setContentAreaFilled(false);
        LogoutButton.setFocusPainted(false);
        LogoutButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LogoutButton.setPreferredSize(new java.awt.Dimension(270, 50));
        sideBarPanel.add(LogoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 440, -1, -1));

        getContentPane().add(sideBarPanel, java.awt.BorderLayout.WEST);

        jPanel1.setBackground(new java.awt.Color(48, 48, 46));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUserManagement.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lblUserManagement.setForeground(new java.awt.Color(255, 255, 255));
        lblUserManagement.setText("User Management");
        jPanel1.add(lblUserManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jScrollPane1.setBorder(null);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 500));

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
        tableUsers.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tableUsers);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 980, -1));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

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
    private javax.swing.JLabel Admin;
    private javax.swing.JButton BookingsButton;
    private javax.swing.JLabel BookingsIcon1;
    private javax.swing.JLabel CarRental;
    private javax.swing.JButton ListingButton;
    private javax.swing.JLabel ListingIcon;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JLabel LogoutIcon;
    private javax.swing.JLabel Main1;
    private javax.swing.JLabel NotifyIcon;
    private javax.swing.JButton OverviewButton;
    private javax.swing.JLabel OverviewIcon;
    private javax.swing.JLabel ProfileIcon;
    private javax.swing.JButton SettingsButton;
    private javax.swing.JLabel SettingsIcon;
    private javax.swing.JButton SupportButton;
    private javax.swing.JLabel SupportIcon;
    private javax.swing.JPanel TopBarPanel;
    private javax.swing.JButton UsersButton;
    private javax.swing.JLabel UsersIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblUserManagement;
    private javax.swing.JPanel sideBarPanel;
    private javax.swing.JTable tableUsers;
    // End of variables declaration//GEN-END:variables
}
