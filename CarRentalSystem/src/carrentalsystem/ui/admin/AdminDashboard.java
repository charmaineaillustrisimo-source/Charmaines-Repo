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
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class AdminDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminDashboard.class.getName());
    // Add this at the top of your class variables
private java.util.List<carrentalsystem.models.Car> userCarList = new java.util.ArrayList<>();
    /**
     * Creates new form AdminDashboard
     */
    public AdminDashboard() {
    initComponents();
    
    //Button Events
    btnListingButton.addActionListener (e -> {
        try {
            AdminListingPanel listing = new AdminListingPanel();
        listing.setVisible(true);
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
    
    //Position Top Bar Icons
    pnlTopBar.add(lblProfileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 20, 50, 50));
    pnlTopBar.add(lblNotifyIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 20, 50, 50));
    
    tableForBookings.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(new java.awt.Color(48, 48, 46)); // Background color
            setForeground(java.awt.Color.WHITE);           // Text color
            setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
            setHorizontalAlignment(javax.swing.JLabel.CENTER);
            setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(60, 60, 60)));
            return this;
        }
    });
    
    spBookings.getViewport().setBackground(new java.awt.Color(48, 48, 46));
    spBookings.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    
    centerTableText();
    applyStatusShaping();
    tableForBookings.setRowHeight(40);
    loadRecentBookings();
    
    
    // Add this to your constructor after initComponents()
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
    public void componentResized(java.awt.event.ComponentEvent evt) {
        int width = getWidth();
        // Position Profile 70 pixels from the right edge
        lblProfileIcon.setLocation(width - 90, lblProfileIcon.getY());
        // Position Notify 140 pixels from the right edge
        lblNotifyIcon.setLocation(width - 160, lblNotifyIcon.getY());
        
    TotalUsers.setData("Total Users", "", "", new java.awt.Color(11, 213, 91));
    ActiveListings.setData("Active Listings", "", "", new java.awt.Color(11, 213, 91));
    BookingsToday.setData("Bookings Today", "", "", java.awt.Color.WHITE);
    PendingApprovals.setData("Pending Approvals", "", "", java.awt.Color.ORANGE, java.awt.Color.RED);
    }
});
    
    // The "Easy Way" - One line per icon
    setIcon(lblOverviewIcon, "/carrentalsystem/ui/admin/PIC/four-squares.png", 35, 35);
    setIcon(lblListingIcon, "/carrentalsystem/ui/admin/PIC/Listing.png", 35, 35);
    setIcon(lblUsersIcon, "/carrentalsystem/ui/admin/PIC/Users.png", 35, 35);
    setIcon(lblBookingsIcon1, "/carrentalsystem/ui/admin/PIC/Bookings.png", 35, 35);
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
    
    private void refreshListingStats() {
    // 1. Clear the old labels so they don't double up
    pnlCarTypeContainer.removeAll();

    // 2. Count the types from your user list
    Map<String, Integer> counts = new HashMap<>();
    for (carrentalsystem.models.Car car : userCarList) {
        String type = car.getType();
        counts.put(type, counts.getOrDefault(type, 0) + 1);
    }

    // 3. For every type the user entered, create a label
    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
        addTypeLabel(entry.getKey(), entry.getValue());
    }

    // 4. Refresh the UI to show the new labels
    pnlCarTypeContainer.revalidate();
    pnlCarTypeContainer.repaint();
}
    
    private void addTypeLabel(String typeName, int count) {
    // Create a new label for each type
    javax.swing.JLabel typeLabel = new javax.swing.JLabel();
    
    // Format the text: "Sedan (5)"
    typeLabel.setText(typeName + " (" + count + ")");
    
    // Style it to match your theme
    typeLabel.setFont(new java.awt.Font("Segoe UI", 1, 18));
    typeLabel.setForeground(java.awt.Color.WHITE);
    typeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    
    // Add it to the grid panel
    pnlCarTypeContainer.add(typeLabel);
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
    
    public void loadRecentBookings() {
    // Get the table model and clear it
    DefaultTableModel model = (DefaultTableModel) tableForBookings.getModel();
    model.setRowCount(0); 

    try {
        // 1. Establish Connection
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "password");
        Statement st = conn.createStatement();
        
        // 2. Execute Query
        String query = "SELECT renter_name, car_name, booking_period, status FROM bookings";
        ResultSet rs = st.executeQuery(query);

        // 3. Loop through results and add rows
        while(rs.next()) {
            String renter = rs.getString("renter_name");
            String car = rs.getString("car_name");
            String dates = rs.getString("booking_period");
            String status = rs.getString("status");
            
            // Add data to the table row
            model.addRow(new Object[]{renter, car, dates, status});
        }
        
        conn.close();
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
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
    
    public void updateListingStats() {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "")) {
        
        // 1. Clear the panel so we don't duplicate bars on refresh
        pnlCarTypeContainer.removeAll();

        // 2. Get total count for percentage math
        ResultSet rsTotal = conn.createStatement().executeQuery("SELECT COUNT(*) FROM cars");
        int totalCars = rsTotal.next() ? rsTotal.getInt(1) : 0;
        if (totalCars == 0) return;

        // 3. Query dynamic types
        String query = "SELECT type, COUNT(*) as count FROM cars GROUP BY type";
        ResultSet rs = conn.createStatement().executeQuery(query);

        while(rs.next()) {
            String typeName = rs.getString("type");
            int count = rs.getInt("count");
            int percentage = (int)((double)count / totalCars * 100);

            // 4. Create UI components on the fly
            javax.swing.JPanel row = new javax.swing.JPanel(new java.awt.BorderLayout(10, 0));
            row.setOpaque(false); // Keeps your dashboard background visible

            javax.swing.JLabel lblName = new javax.swing.JLabel(typeName);
            lblName.setForeground(java.awt.Color.WHITE);
            lblName.setPreferredSize(new java.awt.Dimension(80, 20));

            javax.swing.JProgressBar bar = new javax.swing.JProgressBar(0, 100);
            bar.setValue(percentage);
            bar.setForeground(new java.awt.Color(52, 152, 219));
            bar.setBackground(new java.awt.Color(60, 60, 60));
            bar.setBorderPainted(false);
            bar.setStringPainted(false);

            javax.swing.JLabel lblCount = new javax.swing.JLabel(String.valueOf(count));
            lblCount.setForeground(java.awt.Color.GRAY);

            // 5. Assemble the row
            row.add(lblName, java.awt.BorderLayout.WEST);
            row.add(bar, java.awt.BorderLayout.CENTER);
            row.add(lblCount, java.awt.BorderLayout.EAST);

            // 6. Add to container
            pnlCarTypeContainer.add(row);
            pnlCarTypeContainer.add(javax.swing.Box.createVerticalStrut(10)); // Gap between bars
        }

        // 7. Tell Swing to redraw the new components
        pnlCarTypeContainer.revalidate();
        pnlCarTypeContainer.repaint();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    public void fetchTickets() {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "")) {
        pnlCarTypeContainer.removeAll(); // Clear placeholder labels
        
        String query = "SELECT user_initials, subject, user_name, time_ago, priority FROM support_tickets";
        ResultSet rs = conn.createStatement().executeQuery(query);

        while(rs.next()) {
            TicketRow row = new TicketRow();
            row.setTicketInfo(
                rs.getString("user_initials"),
                rs.getString("subject"),
                rs.getString("user_name") + " * " + rs.getString("time_ago"),
                rs.getString("priority")
            );
            pnlCarTypeContainer.add(row);
        }
        
        pnlCarTypeContainer.revalidate();
        pnlCarTypeContainer.repaint();
    } catch (Exception e) {
        e.printStackTrace();
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

        lblBookingsIcon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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
        pnlCarTypeContainer.setLayout(new javax.swing.BoxLayout(pnlCarTypeContainer, javax.swing.BoxLayout.LINE_AXIS));

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
        pnlBookings.setPreferredSize(new java.awt.Dimension(950, 150));
        pnlBookings.setLayout(new java.awt.BorderLayout());

        spBookings.setBackground(new java.awt.Color(46, 46, 48));

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
        tableForBookings.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tableForBookings.setPreferredSize(new java.awt.Dimension(500, 64));
        tableForBookings.setSelectionBackground(new java.awt.Color(60, 60, 60));
        tableForBookings.setShowHorizontalLines(false);
        tableForBookings.setShowVerticalLines(false);
        spBookings.setViewportView(tableForBookings);

        pnlBookings.add(spBookings, java.awt.BorderLayout.CENTER);

        pnlMain.add(pnlBookings, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        pnlSupportTicket1.setBackground(new java.awt.Color(38, 38, 36));
        pnlSupportTicket1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlSupportTicket1.setPreferredSize(new java.awt.Dimension(531, 200));
        pnlSupportTicket1.setLayout(new javax.swing.BoxLayout(pnlSupportTicket1, javax.swing.BoxLayout.LINE_AXIS));

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
