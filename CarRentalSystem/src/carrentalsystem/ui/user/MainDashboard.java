/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrentalsystem.ui.user;

import carrentalsystem.models.Car;
import carrentalsystem.services.CarService;
import javax.swing.JFrame;
import carrentalsystem.ui.user.*;
import java.awt.CardLayout;
import javax.swing.JOptionPane;
import java.util.*;
import carrentalsystem.ui.user.AddListPanel;
import java.awt.Component;

/**
 *
 * @author macbookairm1grey
 */
public class MainDashboard extends javax.swing.JFrame {
    
    private SidebarPanel sideMenu;
    private HeaderPanel headerPanel;
    private MyListings myListings1;
    private CarDetailsPanel carDetailsPanel1;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainDashboard.class.getName());
    
    private AddListPanel pnlAddList1;
    private BookingPanel bookingPanel1;
    private MyRentalsPanel myRentalsPanel1;
    private NotificationsPanel notificationsPanel;
    private InboxPanel inboxPanel1;
    private ProfilePanel profilePanel1;
    
    /**
     * Creates new form MainDashboard
     */
    public MainDashboard() {
        initComponents();
        // Set-up Style
        setSize(1440,1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //Call methods
        setupCustomComponents();
        refreshCarFeed();
        updateNotificationBadge();
        
        
        
        
    }
    
    private void setupCustomComponents() {
        // Scroll Pane
        spDiscoverFeed.getVerticalScrollBar().setUnitIncrement(16);

        // Sidebar Panel
        sideMenu = new SidebarPanel(pnlMainContent, this);
        this.getLayeredPane().add(sideMenu, javax.swing.JLayeredPane.POPUP_LAYER);
        sideMenu.setBounds(0, 65, 250, getHeight()); 
        sideMenu.setVisible(false);
        
        // Header Panel
        headerPanel = new HeaderPanel();
        this.getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        headerPanel.setBurgerAction(() -> {
            boolean shouldShow = !sideMenu.isVisible();
            sideMenu.setVisible(shouldShow);

            if (shouldShow) {
                int headerHeight = headerPanel.getHeight();
                sideMenu.setBounds(0, headerHeight, 400, getHeight() - headerHeight);
                getLayeredPane().setComponentZOrder(sideMenu, 0);
                getLayeredPane().revalidate();
                getLayeredPane().repaint();
            }
        });
        
        headerPanel.setNotificationsAction(() -> {
            boolean isShown = notificationsPanel.isVisible();

            if (!isShown) {
                // Refresh the database data before showing[cite: 3]
                notificationsPanel.loadData();

                int xPos = this.getWidth() - notificationsPanel.getWidth() - 40;
                int yPos = headerPanel.getHeight();
                notificationsPanel.setLocation(xPos, yPos);

                notificationsPanel.setVisible(true);

                // Bring to front in the Z-order[cite: 2]
                getLayeredPane().setComponentZOrder(notificationsPanel, 0);
            } else {
                notificationsPanel.setVisible(false);
            }

            getLayeredPane().revalidate();
            getLayeredPane().repaint();
        });
        
        headerPanel.setProfileAction(() -> {
            profilePanel1.loadUserData(); // Call the backend loader (Step 2)
            CardLayout cl = (CardLayout) pnlMainContent.getLayout();
            cl.show(pnlMainContent, "profileCard"); //[cite: 27]
        });

        // Set Window properties
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        // Car Details Panel
        carDetailsPanel1 = new CarDetailsPanel();
        carDetailsPanel1.setDashboard(this);
        pnlDetailsWrapper.setLayout(new java.awt.GridBagLayout());
        pnlDetailsWrapper.setOpaque(false);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;

        pnlDetailsWrapper.add(carDetailsPanel1, gbc);
        
        pnlCarFeed.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 20));

        for (java.awt.Component comp : pnlAddListWrapper.getComponents()) {
            if (comp instanceof carrentalsystem.ui.user.AddListPanel) {
                this.pnlAddList1 = (carrentalsystem.ui.user.AddListPanel) comp;
                this.pnlAddList1.setDashboard(this);
            }
        }
        
        //My Listings Panel
        myListings1 = new carrentalsystem.ui.user.MyListings();
        myListings1.setDashboard(this);
        pnlMyListingWrapper.add(myListings1, gbc);
        
        // Booking Panel
        bookingPanel1 = new carrentalsystem.ui.user.BookingPanel();
        bookingPanel1.setDashboard(this);
        
        java.awt.GridBagConstraints gbcBook = new java.awt.GridBagConstraints();
        gbcBook.gridx = 0;
        gbcBook.gridy = 0;
        gbcBook.fill = java.awt.GridBagConstraints.BOTH;
        gbcBook.weightx = 1.0;
        gbcBook.weighty = 1.0;

        pnlBookingWrapper.setLayout(new java.awt.GridBagLayout());
        pnlBookingWrapper.setOpaque(true);
        
        pnlBookingWrapper = new javax.swing.JPanel(new java.awt.GridBagLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                try {
                    java.awt.Image img = new javax.swing.ImageIcon(
                            getClass().getResource("/carrentalsystem/ui/user/Icons/backgroundbookingform.jpeg")
                    ).getImage();
                    g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g2.setColor(new java.awt.Color(223, 208, 209));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        pnlBookingWrapper.setOpaque(false);
        pnlBookingWrapper.add(bookingPanel1, gbcBook);
        pnlMainContent.add(pnlBookingWrapper, "bookingCard");


        
        // My Rentals Panel
        myRentalsPanel1 = new carrentalsystem.ui.user.MyRentalsPanel();
        myRentalsPanel1.setDashboard(this);

        pnlMyRentalsWrapper.setLayout(new java.awt.GridBagLayout());
        pnlMyRentalsWrapper.setOpaque(false);

        java.awt.GridBagConstraints gbcRentals = new java.awt.GridBagConstraints();
        gbcRentals.gridx = 0;
        gbcRentals.gridy = 0;
        gbcRentals.anchor = java.awt.GridBagConstraints.CENTER; // Centers the panel[cite: 18]

        pnlMyRentalsWrapper.removeAll();
        pnlMyRentalsWrapper.add(myRentalsPanel1, gbcRentals);

        pnlMainContent.add(pnlMyRentalsWrapper, "myRentalsCard");
        
        // Notifications Panel
        this.notificationsPanel = new carrentalsystem.ui.user.NotificationsPanel();
        this.notificationsPanel.setDashboard(this);
        this.getLayeredPane().add(notificationsPanel, javax.swing.JLayeredPane.POPUP_LAYER);
        notificationsPanel.setBounds(1000, 65, 400, 500);
        notificationsPanel.setVisible(false);
        
        // Inbox Panel
        inboxPanel1 = new InboxPanel();
        inboxPanel1.setDashboard(this);
        pnlInboxWrapper.setLayout(new java.awt.GridBagLayout()); // Use GridBag for centering
        pnlInboxWrapper.setOpaque(false);

        java.awt.GridBagConstraints gbcInbox = new java.awt.GridBagConstraints();
        gbcInbox.gridx = 0;
        gbcInbox.gridy = 0;
        gbcInbox.fill = java.awt.GridBagConstraints.BOTH;
        gbcInbox.weightx = 1.0;
        gbcInbox.weighty = 1.0;

        pnlInboxWrapper.add(inboxPanel1, gbcInbox);
        
        // Profile Panel
        profilePanel1 = new carrentalsystem.ui.user.ProfilePanel();
        profilePanel1.setDashboard(this);
        
        pnlProfileWrapper.setLayout(new java.awt.GridBagLayout());
        pnlProfileWrapper.setOpaque(false);
        
        java.awt.GridBagConstraints gbcProfile = new java.awt.GridBagConstraints();
        gbcProfile.gridx = 0;
        gbcProfile.gridy = 0;
        gbcProfile.fill = java.awt.GridBagConstraints.BOTH;
        gbcProfile.weightx = 1.0;
        gbcProfile.weighty = 1.0;

        pnlProfileWrapper.add(profilePanel1, gbcProfile);
        pnlMainContent.add(pnlProfileWrapper, "profileCard"); 

        
    }
    
    /**
     * Fetches cars from database and adds them to the feed panel.
     */
    public void refreshCarFeed() {
        if (carrentalsystem.core.SessionManager.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(this,
                    "No user sesion active.",
                    "Current User",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        pnlCarFeed.removeAll();
        pnlCarFeed.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 25));
        pnlCarFeed.setPreferredSize(new java.awt.Dimension(1080, 2000));

        try {
            CarService carService = new CarService();
            List<Car> availableCars = carService.getAvailableCars(""); // Empty string for all

            for (Car car : availableCars) {
                addCarToFeed(car);
            }
        } catch (Exception e) {
            System.err.println("Feed Error: " + e.getMessage());
        }

        pnlCarFeed.revalidate();
        pnlCarFeed.repaint();
    }
    
    
    /**
     * Call this method to automatically add a new car card to the discovery
     * feed.
     *
     * @param newCar The car object containing data from AddListPanel
     */
    public void addCarToFeed(carrentalsystem.models.Car car) {
        // Get current user
        carrentalsystem.models.User currentOwner = carrentalsystem.core.SessionManager.getInstance().getCurrentUser();

        // Create the card
        CarCardPanel card = new CarCardPanel();
        card.setCarData(car, null);

        // Events
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showCarDetails(car);
            }
        });
        // Add to feed
        pnlCarFeed.add(card);
        
           
    }
    
    // Inside MainDashboard.java source code
    public MyListings getMyListings1() {
        return myListings1; 
    }
    
    /**
     * Switches the view to the CarDetailsPanel and populates it with data.
     */
    public void showCarDetails(carrentalsystem.models.Car car) {
        // Passes data to the instance carDetailsPanel1 created by the GUI builder
        carDetailsPanel1.setCarDetails(car);

        // Switch the card layout
        java.awt.CardLayout cl = (java.awt.CardLayout) pnlMainContent.getLayout();
        cl.show(pnlMainContent, "detailsCard");
    }
    
    public void openEditListing(carrentalsystem.models.Car car) {
        // 1. Switch the CardLayout to the Add List panel
        java.awt.CardLayout cl = (java.awt.CardLayout) pnlMainContent.getLayout();
        cl.show(pnlMainContent, "addListing");
        if (pnlAddList1 != null) {
            pnlAddList1.prepareEdit(car);
        } else {
            System.err.println("[DEBUG] pnlAddList1 is null! Check setupCustomComponents loop.");
        }
    }
    
    
    public carrentalsystem.ui.user.AddListPanel getPnlAddList1() {
        return pnlAddList1;
    }
    
    public carrentalsystem.ui.user.BookingPanel getBookingPanel1() {
        return bookingPanel1;
    }
    
    public javax.swing.JPanel getPnlMainContent() {
        return pnlMainContent;
    }
    
    public carrentalsystem.ui.user.MyRentalsPanel getMyRentalsPanel() {
        return myRentalsPanel1; // Ensure this matches your variable name
    }
    
    public carrentalsystem.ui.user.NotificationsPanel getNotificationsPanel() {
        return notificationsPanel;
    }
    
    public InboxPanel getInboxPanel() {
        return inboxPanel1;
    }
    
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
    
    public void updateNotificationBadge() {
        try {
            int userId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();
            carrentalsystem.services.NotificationService service = new carrentalsystem.services.NotificationService();

            int count = service.countUnread(userId);
            headerPanel.setUnreadCount(count); // This triggers the repaint![cite: 6]
        } catch (Exception e) {
            System.err.println("Badge Update Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void startNotificationPoller() {
        // Check for new messages every 5 seconds
        javax.swing.Timer timer = new javax.swing.Timer(5000, e -> {
            if (inboxPanel1 != null && inboxPanel1.isVisible()) {
                inboxPanel1.loadData(); // Refresh the conversation list
            }
            updateNotificationBadge(); // Refresh the bell icon badge
        });
        timer.start();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMainContent = new javax.swing.JPanel();
        pnlDiscovery = new javax.swing.JPanel();
        spDiscoverFeed = new javax.swing.JScrollPane();
        pnlCarFeed = new javax.swing.JPanel();
        pnlAddListWrapper = new javax.swing.JPanel();
        // 1. Initialize the card
        AddListPanel addListingCard = new AddListPanel();

        // 2. Set the size YOU specified
        java.awt.Dimension cardSize = new java.awt.Dimension(1100, 700);
        addListingCard.setPreferredSize(cardSize);
        addListingCard.setMinimumSize(cardSize);
        addListingCard.setMaximumSize(cardSize);

        // 3. Configure GridBagConstraints to prevent stretching
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // These two lines help center it
        gbc.weighty = 1.0; // in the available space
        gbc.fill = java.awt.GridBagConstraints.NONE; // CRITICAL: Do not stretch
        gbc.anchor = java.awt.GridBagConstraints.CENTER;

        // 4. Add to wrapper
        pnlAddListWrapper.removeAll(); // Clear any broken design-time components
        pnlAddListWrapper.add(addListingCard, gbc);
        pnlDetailsWrapper = new javax.swing.JPanel();
        pnlMyListingWrapper = new javax.swing.JPanel();
        pnlBookingWrapper = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                // 80, 80 gives it that smooth, modern look
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        pnlMyRentalsWrapper = new javax.swing.JPanel();
        pnlNotificationsWrapper = new javax.swing.JPanel();
        pnlInboxWrapper = new javax.swing.JPanel();
        pnlProfileWrapper = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rent A Car - Cebu, Philippines");
        setBackground(new java.awt.Color(45, 36, 34));
        setPreferredSize(new java.awt.Dimension(1440, 1024));

        pnlMainContent.setBackground(new java.awt.Color(223, 208, 209));
        pnlMainContent.setLayout(new java.awt.CardLayout());

        pnlDiscovery.setBackground(new java.awt.Color(223, 208, 209));
        pnlDiscovery.setOpaque(false);
        pnlDiscovery.setLayout(new java.awt.GridBagLayout());

        spDiscoverFeed.setBackground(new java.awt.Color(223, 208, 209));
        spDiscoverFeed.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        spDiscoverFeed.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        spDiscoverFeed.setMinimumSize(new java.awt.Dimension(1100, 700));
        spDiscoverFeed.setOpaque(false);
        spDiscoverFeed.setPreferredSize(new java.awt.Dimension(1100, 700));

        pnlCarFeed.setBackground(new java.awt.Color(223, 208, 209));
        spDiscoverFeed.setViewportView(pnlCarFeed);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlDiscovery.add(spDiscoverFeed, gridBagConstraints);

        pnlMainContent.add(pnlDiscovery, "discovery");

        pnlAddListWrapper.setBackground(new java.awt.Color(223, 208, 209));
        pnlAddListWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
        pnlAddListWrapper.setOpaque(false);
        pnlAddListWrapper.setLayout(new java.awt.GridBagLayout());
        pnlMainContent.add(pnlAddListWrapper, "addListing");

        pnlDetailsWrapper.setPreferredSize(new java.awt.Dimension(1100, 700));
        pnlDetailsWrapper.setLayout(new java.awt.GridBagLayout());
        pnlMainContent.add(pnlDetailsWrapper, "detailsCard");

        pnlMyListingWrapper.setBackground(new java.awt.Color(223, 208, 209));
        pnlMyListingWrapper.setOpaque(false);
        pnlMyListingWrapper.setLayout(new java.awt.GridBagLayout());
        pnlMainContent.add(pnlMyListingWrapper, "myListingsCard");
        pnlMainContent.add(pnlBookingWrapper, "bookingCard");

        pnlMyRentalsWrapper.setOpaque(false);
        pnlMainContent.add(pnlMyRentalsWrapper, "myRentalsCard");
        pnlMainContent.add(pnlNotificationsWrapper, "notificationsCard");
        pnlMainContent.add(pnlInboxWrapper, "inboxCard");
        pnlMainContent.add(pnlProfileWrapper, "profileCard");

        getContentPane().add(pnlMainContent, java.awt.BorderLayout.CENTER);

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
        java.awt.EventQueue.invokeLater(() -> new MainDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlAddListWrapper;
    private javax.swing.JPanel pnlBookingWrapper;
    private javax.swing.JPanel pnlCarFeed;
    private javax.swing.JPanel pnlDetailsWrapper;
    private javax.swing.JPanel pnlDiscovery;
    private javax.swing.JPanel pnlInboxWrapper;
    private javax.swing.JPanel pnlMainContent;
    private javax.swing.JPanel pnlMyListingWrapper;
    private javax.swing.JPanel pnlMyRentalsWrapper;
    private javax.swing.JPanel pnlNotificationsWrapper;
    private javax.swing.JPanel pnlProfileWrapper;
    private javax.swing.JScrollPane spDiscoverFeed;
    // End of variables declaration//GEN-END:variables
}
