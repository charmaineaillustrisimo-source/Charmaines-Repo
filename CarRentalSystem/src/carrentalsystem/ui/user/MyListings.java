/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package carrentalsystem.ui.user;
import carrentalsystem.models.Car;
import carrentalsystem.services.CarService;
import carrentalsystem.core.SessionManager;
import java.util.*;
import carrentalsystem.ui.user.MainDashboard;
/**
 *
 * @author macbookairm1grey
 */
public class MyListings extends javax.swing.JPanel {

    /**
     * Creates new form MyListings
     */
    private MainDashboard dashboard;
    
    public MyListings() {
        initComponents();
        spListContainer.getVerticalScrollBar().setUnitIncrement(16);
    }

    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }
    
    private javax.swing.JPanel createListingRow(carrentalsystem.models.Car car) {
        javax.swing.JPanel row = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                // 35 is the corner radius to match your modern aesthetic
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // 2. Initial Setup[cite: 10]
        row.setPreferredSize(new java.awt.Dimension(800, 180));
        row.setBackground(new java.awt.Color(45, 36, 34)); // Dark Brown[cite: 10]
        row.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        row.setOpaque(false); // CRITICAL: Makes the gray corners disappear
        row.setBorder(new carrentalsystem.utils.RoundedBorder(35, new java.awt.Color(65, 55, 50), 1));
        
        // 1. Car Image
        javax.swing.JLabel img = new javax.swing.JLabel();
        carrentalsystem.utils.ImageUtil.applyScaledImage(img, car.getImagePath(), 200, 140);
        row.add(img, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 200, 140));

        // 2. Car Name (Brand + Model)
        javax.swing.JLabel name = new javax.swing.JLabel(car.getBrand() + " " + car.getModel());
        name.setForeground(java.awt.Color.WHITE);
        name.setFont(new java.awt.Font("Helvetica Neue", 1, 22));
        row.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 25, 400, -1));

        // 3. Price Label
        javax.swing.JLabel price = new javax.swing.JLabel("PHP " + String.format("%,.0f", car.getBasePrice()) + "/day");
        price.setForeground(new java.awt.Color(200, 200, 200));
        price.setFont(new java.awt.Font("Helvetica Neue", 0, 18));
        row.add(price, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 400, -1));

        // 4. Date Created Label
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd");
        String dateStr = (car.getCreatedAt() != null) ? sdf.format(car.getCreatedAt()) : "N/A";
        javax.swing.JLabel date = new javax.swing.JLabel("Listed on " + dateStr);
        date.setForeground(new java.awt.Color(150, 150, 150));
        date.setFont(new java.awt.Font("Helvetica Neue", 0, 14));
        row.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 95, 400, -1));

        // 5. Status Badge (The Colored Pill)
        String displayStatus = car.getStatus().replace("_", " ");
        javax.swing.JLabel status = new javax.swing.JLabel(displayStatus) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        status.setFont(new java.awt.Font("Helvetica Neue", 1, 12));

        // Status Color Logic
        if ("ACTIVE".equalsIgnoreCase(car.getStatus())) {
            status.setBackground(new java.awt.Color(186, 219, 172)); // Soft Green
            status.setForeground(new java.awt.Color(45, 80, 45));
        } else if ("PENDING_APPROVAL".contains(car.getStatus().toUpperCase())) {
            status.setBackground(new java.awt.Color(255, 243, 176)); // Soft Yellow
            status.setForeground(new java.awt.Color(85, 75, 20));
        } else if ("REJECTED".contains(car.getStatus().toUpperCase())){
            status.setBackground(new java.awt.Color(255, 186, 186)); // Soft Red
            status.setForeground(new java.awt.Color(120, 40, 40));
        } else if ("ARCHIVED".contains(car.getStatus().toUpperCase())){
            status.setBackground(new java.awt.Color(172, 205, 219));
            status.setForeground(new java.awt.Color(40, 70, 120));
        } else {
            status.setBackground(new java.awt.Color(230, 230, 230));
            status.setForeground(java.awt.Color.DARK_GRAY);
        }
        row.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 160, 30));

        // 6. Action Icons (Edit/Delete)
        javax.swing.JLabel btnEdit = new javax.swing.JLabel(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Edit.png")));
        javax.swing.JLabel btnDelete = new javax.swing.JLabel(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Delete.png")));
        row.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 45, 30, 30));
        row.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 105, 30, 30));
        
        // Inside createListingRow(carrentalsystem.models.Car car)
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Feedback for user
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Find the MainDashboard and call the bridge method
                if (dashboard != null) {
                    dashboard.openEditListing(car); // Pass the current car object
                }
            }
        });
        
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                        MyListings.this,
                        "Are you sure you want to remove this listing? \n" + car.getBrand() + " " + car.getModel(),
                        "Confirm Delete",
                        javax.swing.JOptionPane.YES_NO_OPTION,
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    try {
                        carrentalsystem.services.CarService carService = new carrentalsystem.services.CarService();
                        carService.archiveCar(car.getCarId());

                        javax.swing.JOptionPane.showMessageDialog(MyListings.this, "Listing removed successfully.");
                        loadData(); // Re-fetch data to remove the row from UI

                    } catch (java.sql.SQLException e) {
                        javax.swing.JOptionPane.showMessageDialog(MyListings.this,
                                "Error deleting listing: " + e.getMessage(),
                                "Database Error",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }
        });

        return row;
    }

    public void loadData() {
        System.out.println("[DEBUG] loadData() triggered");
        // 1. Clear existing rows so they don't double up
        pnlListContainer.removeAll();
        pnlListContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 20));
        
        try {
            // 2. Fetch cars from DB for the logged-in user
            int currentUserId = SessionManager.getCurrentUser().getUserId();
            CarService carService = new CarService();
            List<Car> userCars = carService.getCarsByOwner(currentUserId);
            
            System.out.println("[DEBUG] Cars found in DB: " + userCars.size());

            // Create a capsule for every car in the list
            for (carrentalsystem.models.Car car : userCars) {
                javax.swing.JPanel row = createListingRow(car);
                pnlListContainer.add(row);
            }
            
            int totalHeight = (userCars.size() * 200) + 50;
            pnlListContainer.setPreferredSize(new java.awt.Dimension(840, totalHeight));

        } catch (Exception e) {
            System.err.println("Error loading user listings: " + e.getMessage());
        }

        // 4. CRITICAL: Refresh UI to show the new components
        pnlListContainer.revalidate();
        pnlListContainer.repaint();
        spListContainer.revalidate();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        spListContainer = new javax.swing.JScrollPane();
        pnlListContainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(223, 208, 209));
        setOpaque(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 30)); // NOI18N
        lblTitle.setText("My Lists");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));

        spListContainer.setBorder(null);
        spListContainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spListContainer.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        spListContainer.setOpaque(false);
        spListContainer.setPreferredSize(new java.awt.Dimension(840, 500));

        pnlListContainer.setBackground(new java.awt.Color(223, 208, 209));
        pnlListContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 20));
        spListContainer.setViewportView(pnlListContainer);

        add(spListContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 840, 500));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlListContainer;
    private javax.swing.JScrollPane spListContainer;
    // End of variables declaration//GEN-END:variables
}
