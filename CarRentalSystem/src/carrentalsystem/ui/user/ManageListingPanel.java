/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.user;

/**
 *
 * @author Nicole
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ManageListingPanel extends javax.swing.JFrame {

    private final Color COLOR_BG = new Color(214, 201, 201);
    private final Color COLOR_CARD = new Color(45, 36, 36);
    private final Color COLOR_HEADER = new Color(34, 21, 21);

    public ManageListingPanel() {
        initComponents();
        setupCustomUI();
    }

    private void setupCustomUI() {
        setTitle("Manage Your Listings");
        this.setPreferredSize(new Dimension(1440, 1024));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(COLOR_BG);

        // 1. Navigation Bar
        mainContainer.add(createNavBar(), BorderLayout.NORTH);

        // 2. Content Area
        JPanel listContainer = new JPanel();
        listContainer.setBackground(COLOR_BG);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(new EmptyBorder(40, 200, 40, 200));

        JLabel pageTitle = new JLabel("YOUR LISTS");
        pageTitle.setFont(new Font("Serif", Font.BOLD, 36));
        pageTitle.setForeground(new Color(60, 45, 45));
        pageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        listContainer.add(pageTitle);
        listContainer.add(Box.createRigidArea(new Dimension(0, 50)));

        // Path to your image file
        String imagePath = "TOYOTAVIOS.png"; 

        // Generate Cards
        for (int i = 0; i < 3; i++) {
            listContainer.add(createListingCard("Toyota Vios " + (i + 1), "PHP 2,500/day", "2/13", "Active", imagePath));
            listContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        }

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        mainContainer.add(scrollPane, BorderLayout.CENTER);

        this.setContentPane(mainContainer);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private JPanel createNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(COLOR_HEADER);
        nav.setPreferredSize(new Dimension(1440, 80));
        nav.setBorder(new EmptyBorder(0, 40, 0, 40));

        JLabel leftIcon = new JLabel("● ● ●   ☰");
        leftIcon.setForeground(Color.WHITE);
        leftIcon.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel rightIcon = new JLabel("🔔  👤");
        rightIcon.setForeground(Color.WHITE);
        rightIcon.setFont(new Font("SansSerif", Font.BOLD, 28));

        nav.add(leftIcon, BorderLayout.WEST);
        nav.add(rightIcon, BorderLayout.EAST);
        return nav;
    }

    private JPanel createListingCard(String title, String price, String date, String status, String imgPath) {
        // Main Card Panel
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 60, 60));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(1000, 240));
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(20, 40, 20, 40));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        
        // --- IMAGE SECTION WITH CORRECT RESOURCE LOADING ---
        JPanel carImageContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setClip(roundedRect);
                
                try {
                    // NEW LOGIC: Load from the package instead of a File
                    // This matches your screenshot: carrentalsystem/ui/user/TOYOTAVIOS.png
                    java.net.URL imgURL = getClass().getResource("/carrentalsystem/ui/user/" + imgPath);
                    
                    if (imgURL != null) {
                        BufferedImage img = ImageIO.read(imgURL);
                        g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                    } else {
                        // If path is wrong, show a gray box so it doesn't crash
                        g2.setColor(Color.GRAY);
                        g2.fill(roundedRect);
                        System.out.println("Could not find image: " + imgPath);
                    }
                } catch (Exception e) {
                    g2.setColor(Color.GRAY);
                    g2.fill(roundedRect);
                }
                g2.dispose();
            }
        };
        carImageContainer.setPreferredSize(new Dimension(280, 180));
        carImageContainer.setOpaque(false);
        gbc.gridx = 0;
        card.add(carImageContainer, gbc);

        // --- TEXT SECTION ---
        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        
        JLabel nameLbl = new JLabel(title);
        nameLbl.setFont(new Font("Serif", Font.BOLD, 32));
        nameLbl.setForeground(Color.WHITE);

        JLabel priceLbl = new JLabel(price);
        priceLbl.setFont(new Font("Serif", Font.PLAIN, 22));
        priceLbl.setForeground(Color.WHITE);

        JLabel dateLbl = new JLabel("Listed on " + date);
        dateLbl.setFont(new Font("Serif", Font.PLAIN, 18));
        dateLbl.setForeground(new Color(180, 180, 180));

        JLabel statusLbl = new JLabel(status);
        statusLbl.setFont(new Font("Serif", Font.ITALIC, 18));
        statusLbl.setForeground(new Color(180, 180, 180));

        details.add(nameLbl);
        details.add(Box.createRigidArea(new Dimension(0, 8)));
        details.add(priceLbl);
        details.add(dateLbl);
        details.add(statusLbl);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 50, 0, 0);
        card.add(details, gbc);

        // --- THE "EVERYTHING TAPPABLE" LOGIC ---
        MouseAdapter cardTapListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleSelection(title);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        };

        card.addMouseListener(cardTapListener);
        carImageContainer.addMouseListener(cardTapListener);
        details.addMouseListener(cardTapListener);

        return card;
    }
    
    private void handleSelection(String carName) {
        // This is your debugging best friend
        System.out.println("SUCCESS: Tapped on " + carName);
        
        // Add your logic here, e.g.:
        // if (carName.contains("Toyota")) { ... }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
        pack();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageListingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageListingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageListingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageListingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageListingPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
