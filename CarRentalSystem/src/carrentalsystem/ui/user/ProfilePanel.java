/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.user;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import carrentalsystem.models.User;
import carrentalsystem.core.SessionManager;
import carrentalsystem.utils.ImageUtil;

/**
 *
 * @author tomac
 */
public class ProfilePanel extends javax.swing.JPanel {

    private final Color BACKGROUND_COLOR = new Color(225, 228, 232);
    private final Color CARD_COLOR = new Color(248, 249, 250);
    private final Color ACCENT_GREEN = new Color(92, 145, 105);
    private final Color TEXT_DARK = new Color(50, 50, 50);
    private final Color BORDER_LIGHT = new Color(210, 210, 210);

    // Connection
    private MainDashboard dashboard;
    private CardLayout cardManager;
    private JPanel cardsContainer;

    /**
     * Creates new form ProfilePanel
     */
    public ProfilePanel() {
        initComponents();
        // Styling
        applyModernStyling();
        setupInternalLayouts();
        
        makeSidebarButton(lblSettings);
        makeSidebarButton(lblSup);
    
        CardLayout cl = (CardLayout) pnlCardsContainer.getLayout();
        cl.show(pnlCardsContainer, "cardSettings");

        this.revalidate();
        this.repaint();
    }

    private void setupInternalLayouts() {
        int contentWidth = 450; // Spacious width for fields

        // --- Account Card ---
        javax.swing.GroupLayout accLayout = new javax.swing.GroupLayout(panelAccountCard);
        panelAccountCard.setLayout(accLayout);

        accLayout.setHorizontalGroup(
                accLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(accLayout.createSequentialGroup()
                                .addGap(30, 30, 30) // Left internal padding
                                .addGroup(accLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblYourAcc)
                                        .addGroup(accLayout.createSequentialGroup()
                                                .addComponent(lblIconProfile)
                                                .addGap(20, 20, 20)
                                                .addComponent(lblName))
                                        .addComponent(lblNameAcc)
                                        .addComponent(txtName, contentWidth, contentWidth, contentWidth)
                                        .addComponent(lbleEmail)
                                        .addComponent(txtEmail, contentWidth, contentWidth, contentWidth)
                                        .addComponent(lblPassword)
                                        .addComponent(txtPassword, contentWidth, contentWidth, contentWidth)
                                        .addComponent(lblLocation)
                                        .addComponent(txtCity, contentWidth, contentWidth, contentWidth)
                                        .addComponent(txtProvince, contentWidth, contentWidth, contentWidth)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accLayout.createSequentialGroup()
                                                .addComponent(btnSwitchAcc, 200, 200, 200)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnDelete, 120, 120, 120)))
                                .addGap(30, 30, 30)) // Right internal padding
        );

        accLayout.setVerticalGroup(
                accLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(accLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(lblYourAcc)
                                .addGap(25, 25, 25)
                                .addGroup(accLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(lblIconProfile)
                                        .addComponent(lblName))
                                .addGap(25, 25, 25)
                                .addComponent(lblNameAcc).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtName, 45, 45, 45)
                                .addGap(15, 15, 15)
                                .addComponent(lbleEmail).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtEmail, 45, 45, 45)
                                .addGap(15, 15, 15)
                                .addComponent(lblPassword).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtPassword, 45, 45, 45)
                                .addGap(15, 15, 15)
                                .addComponent(lblLocation).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtCity, 45, 45, 45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtProvince, 45, 45, 45)
                                .addGap(40, 40, 40)
                                .addGroup(accLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSwitchAcc, 45, 45, 45)
                                        .addComponent(btnDelete, 45, 45, 45))
                                .addGap(35, 35, 35))
        );

        // --- Support Card ---
        javax.swing.GroupLayout suppLayout = new javax.swing.GroupLayout(panelSupportCard);
        panelSupportCard.setLayout(suppLayout);

        suppLayout.setHorizontalGroup(
                suppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(suppLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(suppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblSupport)
                                        .addComponent(lblIssue)
                                        .addComponent(comboSelectCategory, contentWidth, contentWidth, contentWidth)
                                        .addComponent(lblDescription)
                                        .addComponent(jScrollPane1, contentWidth, contentWidth, contentWidth)
                                        .addComponent(btnSubmit, javax.swing.GroupLayout.Alignment.TRAILING, 160, 160, 160))
                                .addGap(30, 30, 30))
        );

        suppLayout.setVerticalGroup(
                suppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(suppLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(lblSupport)
                                .addGap(30, 30, 30)
                                .addComponent(lblIssue)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboSelectCategory, 45, 45, 45)
                                .addGap(25, 25, 25)
                                .addComponent(lblDescription)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, 330, 330, 330) // Matches vertical flow of account card
                                .addGap(40, 40, 40)
                                .addComponent(btnSubmit, 45, 45, 45)
                                .addGap(35, 35, 35))
        );
    }

    private void makeSidebarButton(javax.swing.JLabel label) {
        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Change to a lighter color on hover
                label.setForeground(new java.awt.Color(0, 102, 204));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Reset to original dark color
                label.setForeground(new java.awt.Color(51, 51, 51));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (pnlCardsContainer != null && pnlCardsContainer.getLayout() instanceof CardLayout) {
                    CardLayout cl = (CardLayout) pnlCardsContainer.getLayout();
                if (label.getText().contains("Settings")) {
                        cl.show(pnlCardsContainer, "cardSettings");
                } else if (label.getText().contains("Support")) {
                        cl.show(pnlCardsContainer, "cardSupport");
                }
            } else {
                System.err.println("Error: pnlCardsContainer not found or layout is wrong.");
                }
            }
        });
    }

    private void applyModernStyling() {
        this.setBackground(BACKGROUND_COLOR);

        // 1. Style the Cards - Must be Opaque to show white background
        styleModernCard(panelAccountCard);
        styleModernCard(panelSupportCard);

        // 2. Style Input Fields
        styleModernField(txtName);
        styleModernField(txtEmail);
        styleModernField(txtPassword);
        styleModernField(txtCity);
        styleModernField(txtProvince);

        // 3. Style Buttons
        btnSubmit.setBackground(ACCENT_GREEN);
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setContentAreaFilled(true); // Ensure background is painted
        btnSubmit.setOpaque(true);           // Required for some Look and Feels

        // IMPORTANT: Set fill to FALSE here. 
        // We use the button's background property for the color.
        btnSubmit.setBorder(new RoundedBorder(15, ACCENT_GREEN, false));

        btnDelete.setBackground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorder(new RoundedBorder(10, BORDER_LIGHT, false));

        txtareaType.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jScrollPane1.setBorder(new RoundedBorder(15, BORDER_LIGHT, false));
        jScrollPane1.setBackground(Color.WHITE);
        jScrollPane1.getViewport().setBackground(Color.WHITE);

        // Set the Submit button size clearly
        btnSubmit.setPreferredSize(new Dimension(140, 45));
        
        try {
            lblSettings.setIcon(new ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Settings1.png")));
            lblSup.setIcon(new ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Support.png")));
            lblUpgrade.setIcon(new ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Upgrade.png")));
        } catch (Exception e) {
            System.err.println("Icon missing: " + e.getMessage());
        }
    }

    private void styleModernCard(JPanel p) {
        p.setOpaque(false);
        // Increase the internal empty border (last 4 numbers) to push content away from edges
        p.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(40, Color.WHITE, true),
                BorderFactory.createEmptyBorder(40, 35, 40, 35)
        ));
    }

    private void styleModernField(JTextField f) {
        f.setBackground(Color.WHITE);
        f.setForeground(TEXT_DARK);
        f.setFont(new Font("SansSerif", Font.PLAIN, 15));
        // Compound border: Outer rounded line + Inner padding
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, BORDER_LIGHT, false),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
    }

    // CUSTOM BORDER CLASS - This handles the "Card" look
    class RoundedBorder extends AbstractBorder {

        private int radius;
        private Color color;
        private boolean fill;

        RoundedBorder(int radius, Color color, boolean fill) {
            this.radius = radius;
            this.color = color;
            this.fill = fill;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Only draw the OUTLINE here. 
            // Do NOT fill inside the paintBorder method for a button.
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2.dispose();
        }
    }

    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public void loadUserData() {
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            return;
        }

        // Profile image
        String path = user.getProfileImagePath();
        if (path != null && !path.isEmpty()) {
            ImageUtil.applyScaledImage(lblIconProfile, path, 100, 100);
        } else {
            lblIconProfile.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource("/carrentalsystem/ui/user/Icons/DefaultProfile.png")));
        }

        // Text fields
        txtName.setText(user.getFullName());
        txtEmail.setText(user.getEmail());
        lblName.setText(user.getFullName().split(" ")[0]);

        // Location — now uncommented
        txtCity.setText(user.getCity() != null ? user.getCity() : "");
        txtProvince.setText(user.getProvince() != null ? user.getProvince() : "");

        // Update header icon
        if (dashboard != null) {
            dashboard.getHeaderPanel().updateProfileIcon(user.getProfileImagePath());
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
        java.awt.GridBagConstraints gridBagConstraints;

        panelSidebar = new javax.swing.JPanel();
        lblSettings = new javax.swing.JLabel();
        lblSup = new javax.swing.JLabel();
        lblAboutUs = new javax.swing.JLabel();
        pnlCardsContainer = new javax.swing.JPanel();
        panelSupportCard = new javax.swing.JPanel();
        lblSupport = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        lblIssue = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtareaType = new javax.swing.JTextArea();
        comboSelectCategory = new javax.swing.JComboBox<>();
        panelAccountCard = new javax.swing.JPanel();
        lblYourAcc = new javax.swing.JLabel();
        lblIconProfile = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblUpgrade = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblNameAcc = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lbleEmail = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblPassword = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnDelete.setBackground(java.awt.Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(210, 210, 210), 1, true));
        lblLocation = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        txtProvince = new javax.swing.JTextField();
        btnSwitchAcc = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSidebar.setPreferredSize(new java.awt.Dimension(200, 200));
        panelSidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSettings.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Settings1.png"))); // NOI18N
        lblSettings.setText("   Settings");
        panelSidebar.add(lblSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 164, 63));

        lblSup.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblSup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Support.png"))); // NOI18N
        lblSup.setText("   Support");
        panelSidebar.add(lblSup, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 164, 64));

        add(panelSidebar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        lblAboutUs.setText("<html><center>About Us<br>© 2026 IT. All Rights Reserved.</center></html>");
        lblAboutUs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add(lblAboutUs, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 670, -1, -1));

        pnlCardsContainer.setMinimumSize(new java.awt.Dimension(550, 650));
        pnlCardsContainer.setPreferredSize(new java.awt.Dimension(500, 600));
        pnlCardsContainer.setLayout(new java.awt.CardLayout());

        panelSupportCard.setBackground(new java.awt.Color(255, 255, 255));
        panelSupportCard.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        panelSupportCard.setMinimumSize(new java.awt.Dimension(500, 600));
        panelSupportCard.setPreferredSize(new java.awt.Dimension(500, 600));

        lblSupport.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblSupport.setText("Support");

        btnSubmit.setBackground(new java.awt.Color(0, 102, 51));
        btnSubmit.setText("Submit");

        lblIssue.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblIssue.setText("Isuue");

        lblDescription.setText("Descriprion");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        txtareaType.setColumns(20);
        txtareaType.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        txtareaType.setLineWrap(true);
        txtareaType.setRows(5);
        txtareaType.setText("Type something here...");
        txtareaType.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtareaType);

        comboSelectCategory.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        comboSelectCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout panelSupportCardLayout = new javax.swing.GroupLayout(panelSupportCard);
        panelSupportCard.setLayout(panelSupportCardLayout);
        panelSupportCardLayout.setHorizontalGroup(
            panelSupportCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSupportCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSupportCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSupport)
                    .addComponent(lblIssue)
                    .addComponent(lblDescription)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                    .addComponent(comboSelectCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        panelSupportCardLayout.setVerticalGroup(
            panelSupportCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSupportCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSupport)
                .addGap(22, 22, 22)
                .addComponent(lblIssue)
                .addGap(18, 18, 18)
                .addComponent(comboSelectCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(lblDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        pnlCardsContainer.add(panelSupportCard, "cardSupport");

        panelAccountCard.setBackground(new java.awt.Color(255, 255, 255));
        panelAccountCard.setMinimumSize(new java.awt.Dimension(500, 600));
        panelAccountCard.setPreferredSize(new java.awt.Dimension(500, 600));
        panelAccountCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblYourAcc.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblYourAcc.setText("Your Account");
        panelAccountCard.add(lblYourAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        lblIconProfile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/DefaultAvatar.png"))); // NOI18N
        panelAccountCard.add(lblIconProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 98, 84));

        lblName.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblName.setText("Charmaine");
        panelAccountCard.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, -1, -1));

        lblUpgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Upgrade.png"))); // NOI18N
        panelAccountCard.add(lblUpgrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, 36, 39));

        txtName.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtName.setText("Charmaine Illustrisimo");
        panelAccountCard.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 455, 40));

        lblNameAcc.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblNameAcc.setText("Name");
        panelAccountCard.add(lblNameAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        txtEmail.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtEmail.setText("charmainillustrisimo@gmail.com");
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        panelAccountCard.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 455, 36));

        lbleEmail.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lbleEmail.setText("Email");
        panelAccountCard.add(lbleEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, 13));

        txtPassword.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtPassword.setText("jPasswordField1");
        panelAccountCard.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 455, 37));

        lblPassword.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblPassword.setText("Password");
        panelAccountCard.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        panelAccountCard.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 550, 130, 42));

        lblLocation.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblLocation.setText("Location");
        panelAccountCard.add(lblLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, -1, -1));

        txtCity.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtCity.setText("City");
        panelAccountCard.add(txtCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 455, 41));

        txtProvince.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtProvince.setText("Province");
        panelAccountCard.add(txtProvince, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 455, 41));

        btnSwitchAcc.setText("Switch Account");
        btnSwitchAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSwitchAccActionPerformed(evt);
            }
        });
        panelAccountCard.add(btnSwitchAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 130, 42));

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        panelAccountCard.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 550, 130, 42));

        pnlCardsContainer.add(panelAccountCard, "cardSettings");

        add(pnlCardsContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 32, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete your account?\nThis cannot be undone.",
                "Delete Account",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        new Thread(() -> {
            try {
                int userId = SessionManager.getCurrentUser().getUserId();
                carrentalsystem.services.UserService svc
                        = new carrentalsystem.services.UserService();
                svc.deleteAccount(userId);
                SessionManager.endSession();

                javax.swing.SwingUtilities.invokeLater(() -> {
                    // Go back to login screen
                    new carrentalsystem.auth.LoginFrame().setVisible(true);
                    javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
                });
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(()
                        -> javax.swing.JOptionPane.showMessageDialog(this,
                                "Error deleting account: " + e.getMessage()));
            }
        }).start();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void btnSwitchAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSwitchAccActionPerformed
        // TODO add your handling code here:
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Are you sure you want to switch accounts? You will be logged out.",
                "Switch Account",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // 2. End the current session
            try {
                carrentalsystem.core.SessionManager.endSession();

                // 3. Return to the Login page
                javax.swing.SwingUtilities.invokeLater(() -> {
                    new carrentalsystem.auth.LoginFrame().setVisible(true);

                    // Close the current Dashboard window
                    java.awt.Window ancestor = javax.swing.SwingUtilities.getWindowAncestor(this);
                    if (ancestor != null) {
                        ancestor.dispose();
                    }
                });
            } catch (java.sql.SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error during logout: " + ex.getMessage(),
                        "Logout Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnSwitchAccActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            return;
        }

        // 1. Gather data from fields
        user.setFullName(txtName.getText().trim());
        user.setEmail(txtEmail.getText().trim());
        user.setCity(txtCity.getText().trim());
        user.setProvince(txtProvince.getText().trim());

        String pwdInput = new String(txtPassword.getPassword()).trim();
        if (!pwdInput.isEmpty() && !pwdInput.equals("jPasswordField1")) {
            user.setPassword(pwdInput);
        }

        // 2. Run Database Update in a Background Thread[cite: 38]
        new Thread(() -> {
            try {
                carrentalsystem.services.UserService svc = new carrentalsystem.services.UserService();
                svc.updateProfile(user);

                // Refresh the session with fresh DB data[cite: 38]
                SessionManager.setCurrentUser(svc.getUserById(user.getUserId()));

                javax.swing.SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(this, "Profile Details Updated!");
                    loadUserData(); // Refresh the labels
                });
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(()
                        -> JOptionPane.showMessageDialog(this, "Update Failed: " + e.getMessage()));
            }
        }).start();
    }//GEN-LAST:event_btnUpdateActionPerformed

    public static void main(String[] args) {
        // Set the look and feel to your system's style (Windows/Mac)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        // Create the window
        javax.swing.JFrame frame = new javax.swing.JFrame("Profile Page Test");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        // Add your panel to the window
        frame.add(new ProfilePanel());

        // Set window size and center it
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnSwitchAcc;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> comboSelectCategory;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAboutUs;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblIconProfile;
    private javax.swing.JLabel lblIssue;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNameAcc;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblSup;
    private javax.swing.JLabel lblSupport;
    private javax.swing.JLabel lblUpgrade;
    private javax.swing.JLabel lblYourAcc;
    private javax.swing.JLabel lbleEmail;
    private javax.swing.JPanel panelAccountCard;
    private javax.swing.JPanel panelSidebar;
    private javax.swing.JPanel panelSupportCard;
    private javax.swing.JPanel pnlCardsContainer;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtProvince;
    private javax.swing.JTextArea txtareaType;
    // End of variables declaration//GEN-END:variables
}
