/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrentalsystem.ui.admin;

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
public class AdminDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminDashboard.class.getName());
    // Add this at the top of your class variables
private java.util.List<carrentalsystem.models.Car> userCarList = new java.util.ArrayList<>();
    /**
     * Creates new form AdminDashboard
     */
    public AdminDashboard() {
    initComponents();
    this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    TopBarPanel.add(ProfileIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 20, 50, 50));
    TopBarPanel.add(NotifyIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 20, 50, 50));
    
    javax.swing.table.JTableHeader header = tableForBookings.getTableHeader();
    header.setBackground(new java.awt.Color(48, 48, 46));
    header.setForeground(java.awt.Color.WHITE);
    
    tableForBookings.getTableHeader().setBackground(new java.awt.Color(48, 48, 46)); // Match your panel color
    tableForBookings.getTableHeader().setForeground(java.awt.Color.WHITE); // Header text color
    tableForBookings.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18)); // Change header font
    tableForBookings.getTableHeader().setBorder(null);
    
    ((javax.swing.table.DefaultTableCellRenderer)header.getDefaultRenderer())
    .setHorizontalAlignment(javax.swing.JLabel.CENTER);
    header.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 60)));
    
    jScrollPane1.getViewport().setBackground(new java.awt.Color(48, 48, 46));
    jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    
    loadRecentBookings();
    
    
    // Add this to your constructor after initComponents()
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
    public void componentResized(java.awt.event.ComponentEvent evt) {
        int width = getWidth();
        // Position Profile 70 pixels from the right edge
        ProfileIcon.setLocation(width - 90, ProfileIcon.getY());
        // Position Notify 140 pixels from the right edge
        NotifyIcon.setLocation(width - 160, NotifyIcon.getY());
        
    TotalUsers.setData("Total Users", "", "", new java.awt.Color(11, 213, 91));
    ActiveListings.setData("Active Listings", "", "", new java.awt.Color(11, 213, 91));
    BookingsToday.setData("Bookings Today", "", "", java.awt.Color.WHITE);
    PendingApprovals.setData("Pending Approvals", "", "", java.awt.Color.ORANGE, java.awt.Color.RED);
    }
});
    
    // The "Easy Way" - One line per icon
    setIcon(OverviewIcon, "/carrentalsystem/ui/admin/PIC/four-squares.png", 35, 35);
    setIcon(ListingIcon, "/carrentalsystem/ui/admin/PIC/Listing.png", 35, 35);
    setIcon(UsersIcon, "/carrentalsystem/ui/admin/PIC/Users.png", 35, 35);
    setIcon(BookingsIcon1, "/carrentalsystem/ui/admin/PIC/Bookings.png", 35, 35);
    setIcon(SupportIcon, "/carrentalsystem/ui/admin/PIC/support.png", 35, 35);
    setIcon(SettingsIcon, "/carrentalsystem/ui/admin/PIC/setting (1).png", 35, 35);
    setIcon(LogoutIcon, "/carrentalsystem/ui/admin/PIC/logout.png", 35, 35);
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
    
    private void refreshListingStats() {
    // 1. Clear the old labels so they don't double up
    carTypeContainer.removeAll();

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
    carTypeContainer.revalidate();
    carTypeContainer.repaint();
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
    carTypeContainer.add(typeLabel);
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
    
    carTypeContainer.add(row);
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
    // Create a renderer that centers text and sets the dark background
    javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Set the background to match your dashboard
            if (!isSelected) {
                c.setBackground(new java.awt.Color(48, 48, 46));
            }
            c.setForeground(java.awt.Color.WHITE);
            return c;
        }
    };

    // Apply centering to the renderer
    centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

    // Apply this renderer to every column in your table
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
    
    /*private void addTicketToUI(String initials, String name, String issue, java.awt.Color themeColor) {
    // 1. Create the Row Panel
    javax.swing.JPanel row = new javax.swing.JPanel(new java.awt.BorderLayout(15, 0));
    row.setOpaque(false);
    row.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

    // 2. THE CIRCLE (Using a JLabel with a Custom Border)
    javax.swing.JLabel circle = new javax.swing.JLabel(initials);
    circle.setForeground(themeColor);
    circle.setFont(new java.awt.Font("Segoe UI", 1, 14));
    circle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    
    // This custom border forces a perfect circle shape
    circle.setBorder(new javax.swing.border.LineBorder(themeColor, 2, true) {
        @Override
        public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getLineColor());
            // Draw an oval that fits the square size to make a circle
            g2.drawOval(x + 1, y + 1, width - 3, height - 3);
        }
    });
    circle.setPreferredSize(new java.awt.Dimension(40, 40));

    // 3. THE TEXT (User Info)
    javax.swing.JLabel text = new javax.swing.JLabel("<html><b>" + name + "</b><br><font size='3' color='#AAAAAA'>" + issue + "</font></html>");
    text.setForeground(java.awt.Color.WHITE);

    // 4. Assemble and Add to Container
    row.add(circle, java.awt.BorderLayout.WEST);
    row.add(text, java.awt.BorderLayout.CENTER);

    supportTicketPanel.add(row);
}*/
    
    
    
    
    

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
        Overview = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        ListingIcon = new javax.swing.JLabel();
        Listing = new javax.swing.JLabel();
        UsersIcon = new javax.swing.JLabel();
        Users = new javax.swing.JLabel();
        BookingsIcon1 = new javax.swing.JLabel();
        Bookings1 = new javax.swing.JLabel();
        SupportIcon = new javax.swing.JLabel();
        Support = new javax.swing.JLabel();
        SettingsIcon = new javax.swing.JLabel();
        Settings = new javax.swing.JLabel();
        LogoutIcon = new javax.swing.JLabel();
        Logout = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cardContainer = new javax.swing.JPanel();
        TotalUsers = new carrentalsystem.ui.admin.StatCard();
        ActiveListings = new carrentalsystem.ui.admin.StatCard();
        BookingsToday = new carrentalsystem.ui.admin.StatCard();
        PendingApprovals = new carrentalsystem.ui.admin.StatCard();
        lblOverview = new javax.swing.JLabel();
        carTypeChart = new carrentalsystem.ui.admin.ListingChart();
        ListingsByType = new javax.swing.JLabel();
        carTypeContainer = new javax.swing.JPanel();
        supportTicketPanel = new javax.swing.JPanel();
        openSourceTicketslbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableForBookings = new javax.swing.JTable();

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

        Overview.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Overview.setForeground(new java.awt.Color(255, 255, 255));
        Overview.setText("Overview");
        sideBarPanel.add(Overview, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, -1, -1));

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

        sideBarPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 360, 50));

        ListingIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(ListingIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 35, 35));

        Listing.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Listing.setForeground(new java.awt.Color(255, 255, 255));
        Listing.setText("Listing");
        sideBarPanel.add(Listing, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, -1, -1));

        UsersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(UsersIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 35, 35));

        Users.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Users.setForeground(new java.awt.Color(255, 255, 255));
        Users.setText("Users");
        sideBarPanel.add(Users, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, -1, -1));

        BookingsIcon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(BookingsIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 35, 35));

        Bookings1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Bookings1.setForeground(new java.awt.Color(255, 255, 255));
        Bookings1.setText("Bookings");
        sideBarPanel.add(Bookings1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, -1, -1));

        SupportIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(SupportIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 35, 35));

        Support.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Support.setForeground(new java.awt.Color(255, 255, 255));
        Support.setText("Support");
        sideBarPanel.add(Support, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, -1, -1));

        SettingsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(SettingsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 35, 35));

        Settings.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Settings.setForeground(new java.awt.Color(255, 255, 255));
        Settings.setText("Settings");
        sideBarPanel.add(Settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, -1, -1));

        LogoutIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sideBarPanel.add(LogoutIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 35, 35));

        Logout.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Logout.setForeground(new java.awt.Color(255, 0, 0));
        Logout.setText("Logout");
        sideBarPanel.add(Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, -1, -1));

        getContentPane().add(sideBarPanel, java.awt.BorderLayout.WEST);

        jPanel1.setBackground(new java.awt.Color(48, 48, 46));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cardContainer.setBackground(new java.awt.Color(38, 38, 36));
        cardContainer.setOpaque(false);
        cardContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 10));

        TotalUsers.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TotalUsers.setPreferredSize(new java.awt.Dimension(220, 160));
        cardContainer.add(TotalUsers);

        ActiveListings.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ActiveListings.setPreferredSize(new java.awt.Dimension(220, 160));
        cardContainer.add(ActiveListings);

        BookingsToday.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BookingsToday.setPreferredSize(new java.awt.Dimension(220, 160));
        cardContainer.add(BookingsToday);

        PendingApprovals.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PendingApprovals.setPreferredSize(new java.awt.Dimension(220, 160));
        cardContainer.add(PendingApprovals);

        jPanel1.add(cardContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 50, -1, -1));

        lblOverview.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lblOverview.setForeground(new java.awt.Color(255, 255, 255));
        lblOverview.setText("Overview");
        jPanel1.add(lblOverview, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        carTypeChart.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ListingsByType.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ListingsByType.setForeground(new java.awt.Color(255, 255, 255));
        ListingsByType.setText("Listings by type");

        carTypeContainer.setBackground(new java.awt.Color(38, 38, 36));
        carTypeContainer.setLayout(new java.awt.GridLayout(0, 1, 20, 0));

        javax.swing.GroupLayout carTypeChartLayout = new javax.swing.GroupLayout(carTypeChart);
        carTypeChart.setLayout(carTypeChartLayout);
        carTypeChartLayout.setHorizontalGroup(
            carTypeChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(carTypeChartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(carTypeChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(carTypeContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(carTypeChartLayout.createSequentialGroup()
                        .addComponent(ListingsByType)
                        .addGap(0, 208, Short.MAX_VALUE)))
                .addContainerGap())
        );
        carTypeChartLayout.setVerticalGroup(
            carTypeChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(carTypeChartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ListingsByType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(carTypeContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(carTypeChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 235, -1, -1));

        supportTicketPanel.setBackground(new java.awt.Color(38, 38, 36));
        supportTicketPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        supportTicketPanel.setPreferredSize(new java.awt.Dimension(531, 200));

        openSourceTicketslbl.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        openSourceTicketslbl.setForeground(new java.awt.Color(255, 255, 255));
        openSourceTicketslbl.setText("Open support tickets");

        javax.swing.GroupLayout supportTicketPanelLayout = new javax.swing.GroupLayout(supportTicketPanel);
        supportTicketPanel.setLayout(supportTicketPanelLayout);
        supportTicketPanelLayout.setHorizontalGroup(
            supportTicketPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supportTicketPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(openSourceTicketslbl)
                .addContainerGap(292, Short.MAX_VALUE))
        );
        supportTicketPanelLayout.setVerticalGroup(
            supportTicketPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supportTicketPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(openSourceTicketslbl)
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jPanel1.add(supportTicketPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 236, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Recent Bookings");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, -1, -1));

        jPanel3.setBackground(new java.awt.Color(48, 48, 46));
        jPanel3.setPreferredSize(new java.awt.Dimension(950, 150));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(46, 46, 48));

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
        tableForBookings.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tableForBookings);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JLabel Admin;
    private javax.swing.JLabel Bookings1;
    private javax.swing.JLabel BookingsIcon1;
    private carrentalsystem.ui.admin.StatCard BookingsToday;
    private javax.swing.JLabel CarRental;
    private javax.swing.JLabel Listing;
    private javax.swing.JLabel ListingIcon;
    private javax.swing.JLabel ListingsByType;
    private javax.swing.JLabel Logout;
    private javax.swing.JLabel LogoutIcon;
    private javax.swing.JLabel Main1;
    private javax.swing.JLabel NotifyIcon;
    private javax.swing.JLabel Overview;
    private javax.swing.JLabel OverviewIcon;
    private carrentalsystem.ui.admin.StatCard PendingApprovals;
    private javax.swing.JLabel ProfileIcon;
    private javax.swing.JLabel Settings;
    private javax.swing.JLabel SettingsIcon;
    private javax.swing.JLabel Support;
    private javax.swing.JLabel SupportIcon;
    private javax.swing.JPanel TopBarPanel;
    private carrentalsystem.ui.admin.StatCard TotalUsers;
    private javax.swing.JLabel Users;
    private javax.swing.JLabel UsersIcon;
    private carrentalsystem.ui.admin.ListingChart carTypeChart;
    private javax.swing.JPanel carTypeContainer;
    private javax.swing.JPanel cardContainer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOverview;
    private javax.swing.JLabel openSourceTicketslbl;
    private javax.swing.JPanel sideBarPanel;
    private javax.swing.JPanel supportTicketPanel;
    private javax.swing.JTable tableForBookings;
    // End of variables declaration//GEN-END:variables
}
