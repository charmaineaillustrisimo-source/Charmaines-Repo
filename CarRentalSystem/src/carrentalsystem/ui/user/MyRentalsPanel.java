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
import java.awt.CardLayout;
/**
 *
 * @author macbookairm1grey
 */
public class MyRentalsPanel extends javax.swing.JPanel {

    /**
     * Creates new form MyListings
     */
    private MainDashboard dashboard;
    
    public MyRentalsPanel() {
        initComponents();
        this.setOpaque(false);
        spListContainer.getVerticalScrollBar().setUnitIncrement(16);
    }

    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }
    
    private javax.swing.JPanel createRentalRow(carrentalsystem.models.Booking booking) {
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

        row.setPreferredSize(new java.awt.Dimension(800, 180));
        row.setBackground(new java.awt.Color(45, 36, 34));
        row.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        row.setOpaque(false);
        row.setBorder(new carrentalsystem.utils.RoundedBorder(35, new java.awt.Color(65, 55, 50), 1));

        // 1. Car Image (Use booking getter)
        javax.swing.JLabel img = new javax.swing.JLabel();
        carrentalsystem.utils.ImageUtil.applyScaledImage(img, booking.getImagePath(), 200, 140);
        row.add(img, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 200, 140));

        // 2. Car Name (Brand + Model from booking)
        javax.swing.JLabel name = new javax.swing.JLabel(booking.getCarBrand() + " " + booking.getCarModel());
        name.setForeground(java.awt.Color.WHITE);
        name.setFont(new java.awt.Font("Helvetica Neue", 1, 22));
        row.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 25, 400, -1));

        // 3. Rent Dates (Use your database start_date and end_date)
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        String dateRange = sdf.format(booking.getStartDate()) + " - " + sdf.format(booking.getEndDate());
        javax.swing.JLabel rentDates = new javax.swing.JLabel("Rent Dates: " + dateRange);
        rentDates.setForeground(new java.awt.Color(200, 200, 200));
        rentDates.setFont(new java.awt.Font("Helvetica Neue", 0, 16));
        row.add(rentDates, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 400, -1));

        // 4. Total Price (Using the utility we built)
        String totalStr = carrentalsystem.utils.PriceCalculator.formatCurrency(booking.getTotalPrice());
        javax.swing.JLabel totalPrice = new javax.swing.JLabel(totalStr + " Total");
        totalPrice.setForeground(java.awt.Color.WHITE);
        totalPrice.setFont(new java.awt.Font("Helvetica Neue", 1, 18));
        row.add(totalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 95, 400, -1));

        // 5. Status Badge (Dynamic Badge Logic)
        javax.swing.JLabel statusBadge = createStatusBadge(booking.getStatus());
        row.add(statusBadge, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 160, 30));

        // 6. Action Icons (Edit/Delete)
        javax.swing.JLabel btnChat = new javax.swing.JLabel(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Chat.png")));
        javax.swing.JLabel btnCancellation = new javax.swing.JLabel(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Cancellation.png")));
        row.add(btnChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 45, 30, 30));
        row.add(btnCancellation, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 105, 30, 30));
        
        // Inside createListingRow(carrentalsystem.models.Car car)
        btnChat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Feedback for user
        btnChat.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Get the InboxPanel
                InboxPanel inbox = dashboard.getInboxPanel();
                String ownerName = booking.getOwnerName() != null ? booking.getOwnerName() : "Owner";
                inbox.openThread(booking.getOwnerId(), booking.getCarId(), ownerName);
                
                java.awt.CardLayout cl = (CardLayout) dashboard.getPnlMainContent().getLayout();
                cl.show(dashboard.getPnlMainContent(), "inboxCard");
                
            }
        });
        
        btnCancellation.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancellation.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                        MyRentalsPanel.this,
                        "Are you sure you want to cancel this bookiing for the " + booking.getCarBrand() + "?",
                        "Confirm Cancellation",
                        javax.swing.JOptionPane.YES_NO_OPTION,
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    try {
                        carrentalsystem.services.BookingService bookingService = new carrentalsystem.services.BookingService();
                        bookingService.cancelBooking(booking.getBookingId(), "Cancelled by Renter");
                        
                        try {
                            carrentalsystem.services.NotificationService notifSvc
                                    = new carrentalsystem.services.NotificationService();
                            String carName = booking.getCarBrand() + " " + booking.getCarModel();

                            // Tell the owner their booking was cancelled by the renter
                            notifSvc.notify(
                                    booking.getOwnerId(),
                                    "The renter has cancelled their booking for your " + carName + ".",
                                    "RENTAL"
                            );

                            // Also confirm to the renter themselves
                            int renterId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();
                            notifSvc.notify(
                                    renterId,
                                    "You have successfully cancelled your booking for " + carName + ".",
                                    "RENTAL"
                            );

                            // Refresh bell badge
                            if (dashboard != null) {
                                dashboard.updateNotificationBadge();
                            }
                        } catch (Exception notifEx) {
                            System.err.println("[MyRentalsPanel] Notification failed: " + notifEx.getMessage());
                        }
                        
                        javax.swing.JOptionPane.showMessageDialog(MyRentalsPanel.this, "Booking cancelled successfully.");
                        loadData(); // Re-fetch data to remove the row from UI

                    } catch (java.sql.SQLException e) {
                        javax.swing.JOptionPane.showMessageDialog(MyRentalsPanel.this,
                                "Error cancelling: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });

        return row;
    }
    
    /**
     * Helper to style the status badge based on the database ENUM values.
     */
    private javax.swing.JLabel createStatusBadge(String statusText) {
        javax.swing.JLabel label = new javax.swing.JLabel(statusText) {
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
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setFont(new java.awt.Font("Helvetica Neue", 1, 12));
        label.setOpaque(false);

        switch (statusText.toUpperCase()) {
            case "CONFIRMED":
            case "SUCCESSFUL":
                label.setBackground(new java.awt.Color(186, 219, 172)); // Green
                label.setForeground(new java.awt.Color(45, 80, 45));
                break;
            case "PENDING":
                label.setBackground(new java.awt.Color(255, 243, 176)); // Yellow
                label.setForeground(new java.awt.Color(85, 75, 20));
                break;
            case "REJECTED":
            case "CANCELLED":
                label.setBackground(new java.awt.Color(255, 186, 186)); // Red
                label.setForeground(new java.awt.Color(120, 40, 40));
                break;
            default:
                label.setBackground(java.awt.Color.LIGHT_GRAY);
                label.setForeground(java.awt.Color.BLACK);
        }
        return label;
    }
    
    public void loadData() {
        System.out.println("[DEBUG] loadData() triggered");
        pnlListContainer.removeAll();
        try {
            int currentUserId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();

            // Fetch from BookingService instead of CarService
            carrentalsystem.services.BookingService bookingService = new carrentalsystem.services.BookingService();
            List<carrentalsystem.models.Booking> myRentals = bookingService.getBookingsByRenter(currentUserId);

            for (carrentalsystem.models.Booking b : myRentals) {
                pnlListContainer.add(createRentalRow(b));

            }

            // Dynamic height adjustment
            int totalHeight = (myRentals.size() * 200) + 50;
            pnlListContainer.setPreferredSize(new java.awt.Dimension(840, totalHeight));

        } catch (Exception e) {
            e.printStackTrace();
        }
        pnlListContainer.revalidate();
        pnlListContainer.repaint();
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
        lblTitle.setText("My Rentals");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));

        spListContainer.setBorder(null);
        spListContainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spListContainer.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        spListContainer.setOpaque(false);
        spListContainer.setPreferredSize(new java.awt.Dimension(840, 500));

        pnlListContainer.setBackground(new java.awt.Color(223, 208, 209));
        pnlListContainer.setPreferredSize(new java.awt.Dimension(840, 500));
        pnlListContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 20));
        spListContainer.setViewportView(pnlListContainer);

        add(spListContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 840, 500));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlListContainer;
    private javax.swing.JScrollPane spListContainer;
    // End of variables declaration//GEN-END:variables
}
