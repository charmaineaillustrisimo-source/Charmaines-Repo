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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ApprovalQueuePanel extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ApprovalQueuePanel.class.getName());
    // Add this at the top of your class variables
    private java.util.List<carrentalsystem.models.Car> userCarList = new java.util.ArrayList<>();

    
    public ApprovalQueuePanel() {
        initComponents();
        
        loadUsersFromDatabase();
        styleUserTable();
        applyRowStyles();
        
        tableUsers.repaint();
    tableUsers.revalidate();
        
        tableUsers.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // 1. Find which row and column was clicked
        int row = tableUsers.rowAtPoint(e.getPoint());
        int col = tableUsers.columnAtPoint(e.getPoint());

        // 2. Check if the "View" column was clicked (Index 4)
        if (col == 4 && row != -1) {
            try {
                // 3. Get the User's name or email from the selected row to pass to the next form
                String selectedUserName = tableUsers.getValueAt(row, 0).toString();
                String selectedUserEmail = tableUsers.getValueAt(row, 1).toString();

                // 4. Create the new JForm (Replace 'UserDetailsFrame' with your actual class name)
                // If your class doesn't take parameters yet, just use: new UserDetailsFrame()
                ViewButtonPanel viewButton = new ViewButtonPanel();
                
                // 5. Show the new form and close this one
                viewButton.setVisible(true);
                dispose(); 
                
            } catch (Exception ex) {
                System.err.println("Error opening User Details: " + ex.getMessage());
            }
        }
    }
});
        
        //centerTableData();
        loadUsersFromDatabase();
        
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
        
        btnListingButton.addActionListener (e -> {
        try {
            AdminListingPanel listing = new AdminListingPanel();
        listing.setVisible(true);
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
        
        tableUsers.repaint();
        tableUsers.revalidate();
        
        tableUsers.setShowGrid(false);
        tableUsers.setIntercellSpacing(new java.awt.Dimension(0, 0));

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

    public void loadUsersFromDatabase() {
        // Get the table model and clear any existing rows
        DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
        model.setRowCount(0);

        String url = "jdbc:mysql://localhost:3306/car_rental_db?useSSL=false&serverTimezone=UTC";
                //"jdbc:mysql://localhost:3306/car_rental";
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
        
        model.addRow(new Object[]{"John Doe", "john@example.com", "Premium", "3 active", "View"});
    model.addRow(new Object[]{"Jane Smith", "jane@example.com", "Basic", "1 active", "View"});
    model.addRow(new Object[]{"Admin Test", "admin@carrent.com", "Gold", "10 active", "View"});
    
    // Set the color of the highlight when a row is selected
tableUsers.setSelectionBackground(new java.awt.Color(70, 70, 70)); 

// Set the color of the text when a row is selected (e.g., White)
tableUsers.setSelectionForeground(java.awt.Color.WHITE);
tableUsers.setFocusable(false);

// Optional: Ensure the grid and viewport match your dark theme
tableUsers.setGridColor(new java.awt.Color(60, 60, 60));
tableUsers.setShowGrid(true);

         tableUsers.revalidate();
         tableUsers.repaint();
    }

    private void styleUserTable() {
        java.awt.Color panelColor = new java.awt.Color(48, 48, 46);
        
    tableUsers.setBackground(panelColor);
    tableUsers.setFillsViewportHeight(true);
    tableUsers.setRowHeight(50);
    tableUsers.setShowGrid(false); // Remove grid lines
    tableUsers.setBorder(null);
    tableUsers.setIntercellSpacing(new java.awt.Dimension(0, 0));

        spUser.setBackground(panelColor); 
        spUser.getViewport().setBackground(panelColor);
        spUser.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        spUser.setBorder(null);
        spUser.setViewportBorder(null);
        
        applyRowStyles();
        
        tableUsers.setBackground(panelColor);
        tableUsers.setFillsViewportHeight(true);
        
        spUser.getVerticalScrollBar().setBackground(panelColor);
        spUser.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(8, 0)); // Thinner scrollbar
        spUser.getVerticalScrollBar().setBorder(null);
        
        spUser.setCorner(javax.swing.JScrollPane.UPPER_RIGHT_CORNER, new javax.swing.JPanel() {{
        setBackground(panelColor);
        
        tableUsers.setModel(new DefaultTableModel(
    new Object [][] { },
    new String [] { "User", "Email", "Plan", "Listings", "Actions" }
) {
    @Override
    public boolean isCellEditable(int row, int column) {
        // This prevents the "white box" from appearing when you click
        return false; 
    }
});
    }});

    // 4. Apply the invisible scrollbar UI
    makeScrollBarInvisible(spUser);
        
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

//applyActionColumnRenderer();

// 1. Define your standard table background color (Matches User/Email cells)
java.awt.Color transparentBg = new java.awt.Color(48, 48, 46);

// Define the colors
java.awt.Color tableBg = new java.awt.Color(48, 48, 46);
java.awt.Color clickEffectColor = new java.awt.Color(80, 80, 80); // Slightly lighter for the click

tableUsers.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        javax.swing.JLabel label = new javax.swing.JLabel("View");
        label.setOpaque(true);
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setForeground(java.awt.Color.WHITE);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        // 1. MAKE IT SMALLER: The EmptyBorder adds "invisible padding" around the text
        // (top, left, bottom, right) -> Adjust 10 and 30 to make it smaller/larger
        label.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEmptyBorder(10, 30, 10, 30), 
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100), 1)
        ));

        // 2. THE CLICK EFFECT: 
        // hasFocus is true the moment the user clicks the cell
        if (hasFocus) {
            label.setBackground(clickEffectColor);
        } else if (isSelected) {
            label.setBackground(new java.awt.Color(65, 65, 63)); // Slightly different for selected row
        } else {
            label.setBackground(tableBg); // Transparent/Match Table
        }

        return label;
    }
});
    }
    
    private void applyBadgeStyle() {
    // Assuming "Plan" is the 3rd column (index 2)
    tableUsers.getColumnModel().getColumn(2).setCellRenderer(new BadgeRenderer());
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
    
    /*private void applyActionColumnRenderer() {
    tableUsers.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            javax.swing.JButton button = new javax.swing.JButton("View");
            
            // Style it to match your theme
            button.setBackground(new java.awt.Color(60, 60, 60));
            button.setForeground(java.awt.Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

            // If the row is selected, make the button slightly lighter
            if (isSelected) {
                button.setBackground(new java.awt.Color(80, 80, 80));
            }

            return button;
        }
    });
}*/
    
private void applyRowStyles() {
    // Define your colors clearly
    java.awt.Color panelBg = new java.awt.Color(48, 48, 46);
    java.awt.Color separatorColor = new java.awt.Color(80, 80, 80);

    javax.swing.table.DefaultTableCellRenderer permanentRenderer = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            javax.swing.JLabel c = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // THE FIX: This must be OUTSIDE of any 'if' statements
            // This ensures the text is white the moment the program starts.
            c.setForeground(java.awt.Color.WHITE); 
            c.setBackground(isSelected ? new java.awt.Color(70, 70, 70) : panelBg);
            c.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            
            // Add the bottom border line
            c.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, separatorColor));
            
            return c;
        }
    };

    // Apply this to every column
    for (int i = 0; i < tableUsers.getColumnCount(); i++) {
        // We skip column 2 and 4 because you use a BadgeRenderer and ButtonEditor there
        if (i != 2 && i != 4) {
            tableUsers.getColumnModel().getColumn(i).setCellRenderer(permanentRenderer);
        }
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
        spUser.setPreferredSize(new java.awt.Dimension(500, 500));

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
        spUser.setViewportView(tableUsers);

        pnlMain.add(spUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 980, -1));

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
