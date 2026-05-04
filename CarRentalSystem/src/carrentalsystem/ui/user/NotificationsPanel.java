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
public class NotificationsPanel extends javax.swing.JPanel {

    /**
     * Creates new form MyListings
     */
    private MainDashboard dashboard;
    
    public NotificationsPanel() {
        initComponents();
        this.setOpaque(false);
        this.setOpaque(true); 
        this.setBackground(new java.awt.Color(45, 36, 34)); // Your dark brown/black theme

        // Add a border to define the floating edges
        this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(65, 55, 50), 2));
        spListContainer.getVerticalScrollBar().setUnitIncrement(16);
    }

    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }
    
    private javax.swing.JPanel createNotifRow(carrentalsystem.models.Notification notif) {
        // 1. Create the rounded capsule panel
        javax.swing.JPanel row = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Background Color: Slightly brighter if unread
                if (!notif.isRead()) {
                    g2.setColor(new java.awt.Color(235, 235, 235)); // Lighter gray for "new" feel
                } else {
                    g2.setColor(new java.awt.Color(217, 217, 217)); // Standard gray
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Optional: Draw a small blue dot for unread notifications
                if (!notif.isRead()) {
                    g2.setColor(new java.awt.Color(116, 185, 255));
                    g2.fillOval(getWidth() - 30, 15, 10, 10);
                }

                g2.dispose();
            }
        };

        // Row Properties
        row.setPreferredSize(new java.awt.Dimension(360, 90));
        row.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        row.setOpaque(false);
        row.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Data Extraction
        String type = notif.getType() != null ? notif.getType().toUpperCase() : "SYSTEM";
        String iconPath = "/carrentalsystem/ui/user/Icons/CarNotifIcon.png"; // Default
        String headerText = "SYSTEM ALERT";

        // 2. Dynamic Mapping for Icons and Headers
        switch (type) {
            case "RENTAL":
                iconPath = "/carrentalsystem/ui/user/Icons/CarNotifIcon.png";
                headerText = "CAR RENTAL";
                break;
            case "CHAT":
                iconPath = "/carrentalsystem/ui/user/Icons/Chat.png";
                headerText = "NEW MESSAGE";
                break;
            case "LISTING":
                iconPath = "/carrentalsystem/ui/user/Icons/Analytics.png";
                headerText = "LISTING UPDATE";
                break;
        }

        // 3. UI Components (Labels)
        javax.swing.JLabel lblIcon = new javax.swing.JLabel();
        carrentalsystem.utils.ImageUtil.setInternalIcon(lblIcon, iconPath);
        if (lblIcon.getIcon() != null) {
            java.awt.Image scaled = ((javax.swing.ImageIcon) lblIcon.getIcon())
                    .getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
            lblIcon.setIcon(new javax.swing.ImageIcon(scaled));
        }
        row.add(lblIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 35, 35));

        javax.swing.JLabel lblHeader = new javax.swing.JLabel(headerText);
        lblHeader.setFont(new java.awt.Font("Serif", 1, 14));
        lblHeader.setForeground(new java.awt.Color(140, 140, 140)); // Muted gray header
        row.add(lblHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 18, 200, -1));

        javax.swing.JLabel lblMsg = new javax.swing.JLabel("<html>" + notif.getMessage() + "</html>");
        lblMsg.setFont(new java.awt.Font("Helvetica Neue", 0, 15));
        lblMsg.setForeground(java.awt.Color.BLACK);
        row.add(lblMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 45, 320, -1));

        // Click Interaction (Navigation)
        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (dashboard != null) {
                    java.awt.CardLayout cl = (java.awt.CardLayout) dashboard.getPnlMainContent().getLayout();
                    String type = notif.getType() != null ? notif.getType().toUpperCase() : "SYSTEM";

                    // 1. Navigation Logic
                    switch (type) {
                        case "RENTAL":
                        case "CHAT":
                            // Redirects to the Inbox/Chat wrapper
                            dashboard.getInboxPanel().loadData(); 
                            cl.show(dashboard.getPnlMainContent(), "inboxCard");
                            break;
                        case "SYSTEM":
                        case "LISTING":
                            // Redirects to a dedicated details panel (ensure "detailsCard" is configured for notifs)
                            cl.show(dashboard.getPnlMainContent(), "detailsCard");
                            break;
                    }

                    // 2. Mark as Read and Refresh UI
                    try {
                        carrentalsystem.services.NotificationService service = new carrentalsystem.services.NotificationService();
                        // Update specific notification to is_read = 1
                        service.markAsRead(notif.getNotifId());
                        // Hide the floating panel after selection
                        setVisible(false);

                        // Refresh the badge count on the bell icon
                        dashboard.updateNotificationBadge();
                    } catch (Exception e) {
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
        System.out.println("[DEBUG] Notifications loadData() triggered");

        // 1. Clear the existing items in the container
        pnlListContainer.removeAll();

        try {
            // 2. Get the current user ID to fetch specific alerts
            int currentUserId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();

            // 3. Initialize the service and fetch the list of notifications
            carrentalsystem.services.NotificationService notifService = new carrentalsystem.services.NotificationService();
            List<carrentalsystem.models.Notification> myNotifs = notifService.getNotificationsForUser(currentUserId);

            // 4. Populate the list with the light-gray capsules
            for (carrentalsystem.models.Notification n : myNotifs) {
                // This calls your newly designed row generator
                pnlListContainer.add(createNotifRow(n));
            }

            // 5. Dynamic height adjustment for the ScrollPane
            // Calculation: 90 (Row Height) + 15 (Gap) = 105 per notification
            int totalHeight = (myNotifs.size() * 105) + 50;

            // This ensures the container is tall enough to allow scrolling
            pnlListContainer.setPreferredSize(new java.awt.Dimension(380, totalHeight));

        } catch (Exception e) {
            System.err.println("Error loading notifications: " + e.getMessage());
            e.printStackTrace();
        }

        // 6. Mandatory UI Refresh to display the new components
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

        spListContainer = new javax.swing.JScrollPane();
        pnlListContainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(223, 208, 209));
        setOpaque(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        spListContainer.setBorder(null);
        spListContainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spListContainer.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        spListContainer.setOpaque(false);
        spListContainer.setPreferredSize(new java.awt.Dimension(400, 500));

        pnlListContainer.setBackground(new java.awt.Color(45, 36, 34));
        pnlListContainer.setPreferredSize(new java.awt.Dimension(400, 500));
        pnlListContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 20));
        spListContainer.setViewportView(pnlListContainer);

        add(spListContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 480));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlListContainer;
    private javax.swing.JScrollPane spListContainer;
    // End of variables declaration//GEN-END:variables
}
