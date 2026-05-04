/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.user;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author tomac
 */
public class SettingsBrowsingPanel extends javax.swing.JPanel {

    /**
     * Creates new form SettingsBrowsingPanel
     */
    public SettingsBrowsingPanel() {
        initComponents();

        this.removeAll();
        this.setLayout(new BorderLayout());

// Force the content background to use BorderLayout so the scrollpane fills it
        panelContentBg.setLayout(new BorderLayout());

        // THEME COLORS
        Color darkBg = new Color(45, 35, 35);
        Color lightPink = new Color(223, 208, 209);
        Color cardColor = new Color(220, 205, 205);

        this.setLayout(new BorderLayout());
        this.setBackground(darkBg);

        // 1. FIXED HEADER AREA
        JPanel fixedHeader = new JPanel(new BorderLayout());
        fixedHeader.setOpaque(false);

        JPanel headerPinkBar = new JPanel(new GridBagLayout());
        headerPinkBar.setBackground(lightPink);
        headerPinkBar.setPreferredSize(new Dimension(1000, 110));

        btnActLog.setText("Activity Log");
        btnActLog.setFont(new Font("Serif", Font.BOLD, 28));
        btnActLog.setPreferredSize(new Dimension(300, 60));
        btnActLog.setForeground(Color.WHITE);
        btnActLog.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand icon on hover

// Disable default rendering to allow custom dark color
        btnActLog.setContentAreaFilled(false);
        btnActLog.setOpaque(false);
        btnActLog.setBorderPainted(false);
        btnActLog.setFocusPainted(false);

        btnActLog.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Darkens slightly when clicked
                if (((AbstractButton) c).getModel().isPressed()) {
                    g2.setColor(darkBg.darker());
                } else {
                    g2.setColor(darkBg);
                }

                // Draws a rounded rectangle for the button body
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);
                g2.dispose();
                super.paint(g, c);
            }
        });
        headerPinkBar.add(btnActLog);

        JPanel filterSection = new JPanel(new GridBagLayout());
        filterSection.setBackground(darkBg);
        GridBagConstraints gbc = new GridBagConstraints();

        lblFilter.setText("Filter by Activity");
        lblFilter.setFont(new Font("Serif", Font.PLAIN, 36));
        lblFilter.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 10, 0);
        filterSection.add(lblFilter, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        styleFilterBtn(btnBrowsing, "Browsing");
        styleFilterBtn(btnRentals, "Rentals");
        gbc.insets = new Insets(0, 40, 20, 40);
        filterSection.add(btnBrowsing, gbc);
        gbc.gridx = 1;
        filterSection.add(btnRentals, gbc);

        fixedHeader.add(headerPinkBar, BorderLayout.NORTH);
        fixedHeader.add(filterSection, BorderLayout.SOUTH);
        this.add(fixedHeader, BorderLayout.NORTH);
        
        

        // 2. THE SCROLLABLE PANEL (This panel grows with content)
        panelBackground.setLayout(new BoxLayout(panelBackground, BoxLayout.Y_AXIS));
        panelBackground.setBackground(darkBg);

        // Scaling the car images to be larger (320x200)
        ImageIcon carImg = getScaledIcon("/carrentalsystem/ui/user/Icon/Car.png", 320, 200);

        // 3. JSCROLLPANE SETUP
        scrollPanelActivity.setViewportView(panelBackground);
        scrollPanelActivity.setBorder(null);
        scrollPanelActivity.setOpaque(false);
        scrollPanelActivity.getViewport().setOpaque(false);

        scrollPanelActivity.getVerticalScrollBar().setUnitIncrement(25); // Faster scroll for bigger cards
        scrollPanelActivity.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show for better UI

        this.add(scrollPanelActivity, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

// Updated setupCard for bigger dimensions
    private void setupCard(JPanel card, JLabel icon, JLabel title, JLabel price, JLabel history, JLabel status, ImageIcon img, Color color) {
        card.removeAll();
        card.setLayout(new BorderLayout(40, 0)); // More horizontal gap
        card.setBackground(color);

        // BIGGER DIMENSIONS HERE
        card.setMaximumSize(new Dimension(950, 250));
        card.setPreferredSize(new Dimension(950, 250));

        icon.setIcon(img);
        icon.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        styleLabel(title, 32, true);  // Bigger text
        styleLabel(price, 22, true);  // Bigger text
        styleLabel(history, 18, false);
        styleLabel(status, 18, false);

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(title);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(price);
        infoPanel.add(history);
        infoPanel.add(status);
        infoPanel.add(Box.createVerticalGlue());

        card.add(icon, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 50, 0, 50), // Sideways padding
                new RoundedBorder(50, color) // More rounded corners for bigger cards
        ));
    }

    private void styleFilterBtn(JButton btn, String text) {
        btn.setText(text);
        btn.setFont(new Font("Serif", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Changes cursor to hand

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);

        // Adds the hover effect logic
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Change to a lighter pink/grey on hover
                btn.setForeground(new Color(223, 208, 209));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Back to white when mouse leaves
                btn.setForeground(Color.WHITE);
            }
        });
    }

    private void styleLabel(JLabel lbl, int size, boolean bold) {
        lbl.setFont(new Font("Serif", bold ? Font.BOLD : Font.PLAIN, size));
        lbl.setForeground(new Color(45, 35, 35));
    }

    private ImageIcon getScaledIcon(String path, int w, int h) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL == null) {
                System.err.println("[DEBUG] Image not found at: " + path);
                return null; // Returning null here prevents the URL crash
            }
            Image img = new ImageIcon(imgURL).getImage();
            return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
    }

    class RoundedBorder implements Border {

        private int r;
        private Color c;

        RoundedBorder(int r, Color c) {
            this.r = r;
            this.c = c;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(15, 25, 15, 25);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(this.c);
            g2.fillRoundRect(x, y, w - 1, h - 1, r, r);
            g2.dispose();
        }
    }
    
    private JPanel createHistoryCapsule(carrentalsystem.models.History h) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(new Color(220, 205, 205)); // cardColor[cite: 22, 24]
        card.setMaximumSize(new Dimension(950, 200));
        card.setPreferredSize(new Dimension(950, 200));
        card.setOpaque(true);

        // Scaling the car image[cite: 20, 24]
        JLabel lblIcon = new JLabel();
        if (h.getImagePath() != null && !h.getImagePath().isEmpty()) {
            carrentalsystem.utils.ImageUtil.applyScaledImage(lblIcon, h.getImagePath(), 280, 180);
        }

        // Combined brand + model info[cite: 24]
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(h.getBrand() + " " + h.getModel());
        title.setFont(new Font("Serif", Font.BOLD, 28));

        JLabel price = new JLabel("PHP " + String.format("%.2f", h.getPrice()) + "/day");

        // Formatting the timestamp from your model[cite: 22]
        String dateStr = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm").format(h.getCreatedAt());
        JLabel time = new JLabel("Activity: " + dateStr);

        info.add(Box.createVerticalGlue());
        info.add(title);
        info.add(price);
        info.add(time);
        info.add(Box.createVerticalGlue());

        card.add(lblIcon, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        return card;
    }
    
    public void refreshHistory() {
        panelBackground.removeAll(); // Clears the empty container
        int userId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();

        new Thread(() -> {
            try {
                // Use your new HistoryService to fetch data[cite: 21, 23]
                carrentalsystem.interfaces.IHistoryService service = new carrentalsystem.services.HistoryService();
                java.util.List<carrentalsystem.models.History> list = service.getBrowsingHistory(userId);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    for (carrentalsystem.models.History h : list) {
                        panelBackground.add(createHistoryCapsule(h));
                        panelBackground.add(Box.createVerticalStrut(20)); // Add spacing between capsules
                    }
                    panelBackground.revalidate();
                    panelBackground.repaint();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        btnActLog = new javax.swing.JButton();
        panelContentBg = new javax.swing.JPanel();
        lblFilter = new javax.swing.JLabel();
        btnBrowsing = new javax.swing.JButton();
        btnRentals = new javax.swing.JButton();
        scrollPanelActivity = new javax.swing.JScrollPane();
        panelBackground = new javax.swing.JPanel();

        setBackground(new java.awt.Color(45, 35, 35));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeader.setBackground(new java.awt.Color(223, 208, 209));

        btnActLog.setBackground(new java.awt.Color(45, 35, 35));
        btnActLog.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        btnActLog.setForeground(new java.awt.Color(255, 255, 255));
        btnActLog.setText("Actvity Log");
        btnActLog.setBorderPainted(false);
        btnActLog.setFocusPainted(false);

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(413, 413, 413)
                .addComponent(btnActLog)
                .addContainerGap(378, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(btnActLog)
                .addContainerGap())
        );

        add(panelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 110));

        panelContentBg.setBackground(new java.awt.Color(45, 35, 35));

        lblFilter.setBackground(new java.awt.Color(45, 35, 35));
        lblFilter.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblFilter.setForeground(new java.awt.Color(255, 255, 255));
        lblFilter.setText("Filter by Activity");
        lblFilter.setOpaque(true);

        btnBrowsing.setBackground(new java.awt.Color(45, 35, 35));
        btnBrowsing.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        btnBrowsing.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowsing.setText("Browsing");
        btnBrowsing.setBorderPainted(false);
        btnBrowsing.setContentAreaFilled(false);
        btnBrowsing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowsingActionPerformed(evt);
            }
        });

        btnRentals.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        btnRentals.setForeground(new java.awt.Color(255, 255, 255));
        btnRentals.setText("Rentals");
        btnRentals.setBorderPainted(false);
        btnRentals.setContentAreaFilled(false);
        btnRentals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentalsActionPerformed(evt);
            }
        });

        scrollPanelActivity.setBackground(new java.awt.Color(45, 35, 35));
        scrollPanelActivity.setOpaque(false);

        panelBackground.setBackground(new java.awt.Color(45, 35, 35));
        panelBackground.setLayout(new javax.swing.BoxLayout(panelBackground, javax.swing.BoxLayout.Y_AXIS));
        scrollPanelActivity.setViewportView(panelBackground);

        javax.swing.GroupLayout panelContentBgLayout = new javax.swing.GroupLayout(panelContentBg);
        panelContentBg.setLayout(panelContentBgLayout);
        panelContentBgLayout.setHorizontalGroup(
            panelContentBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContentBgLayout.createSequentialGroup()
                .addGroup(panelContentBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelContentBgLayout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(lblFilter))
                    .addGroup(panelContentBgLayout.createSequentialGroup()
                        .addGap(287, 287, 287)
                        .addComponent(btnBrowsing)
                        .addGap(108, 108, 108)
                        .addComponent(btnRentals)))
                .addContainerGap(409, Short.MAX_VALUE))
            .addComponent(scrollPanelActivity)
        );
        panelContentBgLayout.setVerticalGroup(
            panelContentBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContentBgLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblFilter)
                .addGap(28, 28, 28)
                .addGroup(panelContentBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowsing)
                    .addComponent(btnRentals))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPanelActivity, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(panelContentBg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 1000, 690));
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowsingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowsingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowsingActionPerformed

    private void btnRentalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentalsActionPerformed
        // TODO add your handling code here:
        java.awt.Container parent = getParent();
        if (parent != null && parent.getLayout() instanceof java.awt.CardLayout) {
            ((java.awt.CardLayout) parent.getLayout()).show(parent, "SUB_RENTALS");
        }
    }//GEN-LAST:event_btnRentalsActionPerformed

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Standard fallback
        }

        JFrame frame = new JFrame("Activity Log Preview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add your panel to the frame
        frame.add(new SettingsBrowsingPanel());

        // Adjust the window size to fit the components and center it
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActLog;
    private javax.swing.JButton btnBrowsing;
    private javax.swing.JButton btnRentals;
    private javax.swing.JLabel lblFilter;
    private javax.swing.JPanel panelBackground;
    private javax.swing.JPanel panelContentBg;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JScrollPane scrollPanelActivity;
    // End of variables declaration//GEN-END:variables
}
