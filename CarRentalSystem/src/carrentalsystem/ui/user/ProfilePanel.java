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
    private boolean isEditing = false;

    /**
     * Creates new form ProfilePanel
     */
    public ProfilePanel() {
        initComponents();
        // Styling
        applyModernStyling();

        makeSidebarButton(lblSettings);
        makeSidebarButton(lblSup);

        CardLayout cl = (CardLayout) pnlCardsContainer.getLayout();
        cl.show(pnlCardsContainer, "cardSettings");

        this.revalidate();
        this.repaint();
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
        User sessionUser = SessionManager.getCurrentUser();
        if (sessionUser == null) {
            return;
        }
        // Text bg static
        txtName.setText(sessionUser.getFullName());
        txtEmail.setText(sessionUser.getEmail());
        lblName.setText(sessionUser.getFullName().split(" ")[0]);
        
        txtPassword.setText("jPasswordField1");
        txtPassword.setEditable(false);
        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210), 1, true));
        
        // Functional Check for City
        new Thread(() -> {
            try {
                carrentalsystem.services.UserService svc = new carrentalsystem.services.UserService();
                // Fetch the FULL user object from the DB
                User user = svc.getUserById(sessionUser.getUserId());

                if (user != null) {
                    // Update session so other panels have the new location too
                    SessionManager.setCurrentUser(user);

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        // Update Text Fields
                        txtName.setText(user.getFullName());
                        txtEmail.setText(user.getEmail());
                        lblName.setText(user.getFullName().split(" ")[0]);

                        // FUNCTIONAL LOCATION CHECK[cite: 25]
                        if (user.getCity() == null || user.getCity().trim().isEmpty()) {
                            txtCity.setText("City");
                            txtCity.setForeground(java.awt.Color.GRAY);
                        } else {
                            txtCity.setText(user.getCity());
                            txtCity.setForeground(TEXT_DARK);
                        }

                        if (user.getProvince() == null || user.getProvince().trim().isEmpty()) {
                            txtProvince.setText("Province");
                            txtProvince.setForeground(java.awt.Color.GRAY);
                        } else {
                            txtProvince.setText(user.getProvince());
                            txtProvince.setForeground(TEXT_DARK);
                        }

                        // Profile image
                        int size = 80;
                        String path = user.getProfileImagePath();
                        if (path != null && !path.isEmpty()) {
                            JLabel tempCircle = ImageUtil.cropCircle(path, size);
                            lblIconProfile.setIcon(tempCircle.getIcon());
                        } else {
                            lblIconProfile.setIcon(ImageUtil.loadIcon("/carrentalsystem/ui/user/Icons/DefaultAvatar.png", size, size));
                        }

                        // Update header icon
                        if (dashboard != null) {
                            dashboard.getHeaderPanel().updateProfileIcon(user.getProfileImagePath());
                        }

                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void toggleEditMode() {
        isEditing = !isEditing;

        // Toggle field editability
        txtName.setEditable(isEditing);
        txtEmail.setEditable(isEditing);
        txtCity.setEditable(isEditing);
        txtProvince.setEditable(isEditing);
        txtPassword.setEditable(isEditing);

        // Visual border feedback — blue border when editable, grey when view-only
        javax.swing.border.Border editBorder = javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(100, 149, 237), 2, true);
        javax.swing.border.Border viewBorder = javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(210, 210, 210), 1, true);

        javax.swing.border.Border active = isEditing ? editBorder : viewBorder;
        txtName.setBorder(active);
        txtEmail.setBorder(active);
        txtCity.setBorder(active);
        txtProvince.setBorder(active);
        txtPassword.setBorder(active);

        // Show/hide Update button based on mode
        btnUpdate.setVisible(isEditing);

        // Change edit icon to indicate current state
        String iconPath = isEditing
                ? "/carrentalsystem/ui/user/Icons/EditProfile.png" // pencil = editing
                : "/carrentalsystem/ui/user/Icons/EditProfile.png";  // same icon, just toggle
        // If you have a "save/checkmark" icon, use it here instead

        if (isEditing) {
            // Clear the dummy "jPasswordField1" text so the user can type a new one
            txtPassword.setText("");
            txtName.requestFocus();
        } else {
            // Reset the dummy text when exiting edit mode without saving
            txtPassword.setText("jPasswordField1");
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
        lblEdit = new javax.swing.JLabel();
        lblUpgrade = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblNameAcc = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lbleEmail = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblPassword = new javax.swing.JLabel();
        lblLocation = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        txtProvince = new javax.swing.JTextField();
        btnSwitchAcc = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblAboutUs = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        panelSidebar.setOpaque(false);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.ipady = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(100, 70, 0, 0);
        add(panelSidebar, gridBagConstraints);

        pnlCardsContainer.setMinimumSize(new java.awt.Dimension(650, 750));
        pnlCardsContainer.setOpaque(false);
        pnlCardsContainer.setPreferredSize(new java.awt.Dimension(650, 750));
        pnlCardsContainer.setLayout(new java.awt.CardLayout());

        panelSupportCard.setBackground(new java.awt.Color(255, 255, 255));
        panelSupportCard.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        panelSupportCard.setMinimumSize(new java.awt.Dimension(500, 600));
        panelSupportCard.setPreferredSize(new java.awt.Dimension(500, 600));

        lblSupport.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblSupport.setText("Support");

        btnSubmit.setBackground(new java.awt.Color(0, 102, 51));
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

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
        txtareaType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtareaTypeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtareaTypeFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(txtareaType);

        comboSelectCategory.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        comboSelectCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category", "Bug Issue", "Report a user", "Report payment issue", "Report listing issue", "Help" }));

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
                .addContainerGap(246, Short.MAX_VALUE))
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
                .addContainerGap(236, Short.MAX_VALUE))
        );

        pnlCardsContainer.add(panelSupportCard, "cardSupport");

        panelAccountCard.setBackground(new java.awt.Color(255, 255, 255));
        panelAccountCard.setMinimumSize(new java.awt.Dimension(500, 600));
        panelAccountCard.setPreferredSize(new java.awt.Dimension(450, 600));
        panelAccountCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblYourAcc.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblYourAcc.setText("Your Account");
        panelAccountCard.add(lblYourAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        lblIconProfile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/DefaultAvatar.png"))); // NOI18N
        lblIconProfile.setToolTipText("Click to change photo");
        lblIconProfile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblIconProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIconProfileMouseClicked(evt);
            }
        });
        panelAccountCard.add(lblIconProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 98, 84));

        lblName.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblName.setText("Charmaine Illustrisimo");
        panelAccountCard.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, -1, -1));

        lblEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/EditProfile.png"))); // NOI18N
        lblEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditMouseClicked(evt);
            }
        });
        panelAccountCard.add(lblEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 36, 39));

        lblUpgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Upgrade.png"))); // NOI18N
        lblUpgrade.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblUpgrade.setToolTipText("Upgrade to PRO");
        lblUpgrade.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUpgradeMouseClicked(evt);
            }
        });
        lblUpgrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUpgradeMouseClicked(evt);
            }
        });
        panelAccountCard.add(lblUpgrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, 36, 39));

        txtName.setEditable(false);
        txtName.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtName.setText("Charmaine Illustrisimo");
        txtName.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1)));
        txtName.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        panelAccountCard.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 530, 40));

        lblNameAcc.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblNameAcc.setText("Name");
        panelAccountCard.add(lblNameAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, -1));

        txtEmail.setEditable(false);
        txtEmail.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtEmail.setText("charmaineaillustrisimo@gmail.com");
        txtEmail.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1)));
        txtEmail.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtEmail.setPreferredSize(new java.awt.Dimension(257, 25));
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        panelAccountCard.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 530, 40));

        lbleEmail.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lbleEmail.setText("Email");
        panelAccountCard.add(lbleEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, -1, 13));

        txtPassword.setEditable(false);
        txtPassword.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtPassword.setText("jPasswordField1");
        txtPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1)));
        panelAccountCard.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 360, 530, 37));

        lblPassword.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblPassword.setText("Password");
        panelAccountCard.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        lblLocation.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblLocation.setText("Location");
        panelAccountCard.add(lblLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, -1, -1));

        txtCity.setEditable(false);
        txtCity.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtCity.setText("City");
        txtCity.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1)));
        txtCity.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtCity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCityFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCityFocusLost(evt);
            }
        });
        txtCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCityActionPerformed(evt);
            }
        });
        panelAccountCard.add(txtCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 430, 530, 41));

        txtProvince.setEditable(false);
        txtProvince.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtProvince.setText("Province");
        txtProvince.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1)));
        txtProvince.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtProvince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtProvinceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProvinceFocusLost(evt);
            }
        });
        panelAccountCard.add(txtProvince, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 480, 530, 41));

        btnSwitchAcc.setText("Switch Account");
        btnSwitchAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSwitchAccActionPerformed(evt);
            }
        });
        panelAccountCard.add(btnSwitchAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 550, 130, 42));

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        panelAccountCard.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 550, 130, 42));

        btnDelete.setBackground(new java.awt.Color(255, 100, 100));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        panelAccountCard.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 550, 130, 42));

        pnlCardsContainer.add(panelAccountCard, "cardSettings");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(32, 42, 0, 42);
        add(pnlCardsContainer, gridBagConstraints);

        lblAboutUs.setText("<html><center>About Us<br>© 2026 IT. All Rights Reserved.</center></html>");
        lblAboutUs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 123;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(38, 110, 40, 0);
        add(lblAboutUs, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void btnSwitchAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSwitchAccActionPerformed
        // TODO add your handling code here:
        carrentalsystem.models.User currentUser
                = carrentalsystem.core.SessionManager.getCurrentUser();
        if (currentUser == null) {
            return;
        }

        // ── STEP 1: Fetch all other active users ──────────────
        java.util.List<carrentalsystem.models.User> users;
        try {
            carrentalsystem.services.UserService svc
                    = new carrentalsystem.services.UserService();
            users = svc.getAllActiveUsers(currentUser.getUserId());
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Could not load accounts: " + e.getMessage());
            return;
        }

        if (users.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No other accounts found.",
                    "Switch Account",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // ── STEP 2: Show account picker ───────────────────────
        // Build display strings: "Full Name (email)"
        String[] displayNames = users.stream()
                .map(u -> u.getFullName() + "  (" + u.getEmail() + ")")
                .toArray(String[]::new);

        String selected = (String) javax.swing.JOptionPane.showInputDialog(
                this,
                "Select an account to switch to:",
                "Switch Account",
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                displayNames,
                displayNames[0]
        );

        if (selected == null) {
            return; // User cancelled
        }
        // Find the chosen User object by matching display string
        carrentalsystem.models.User chosenUser = null;
        for (int i = 0; i < displayNames.length; i++) {
            if (displayNames[i].equals(selected)) {
                chosenUser = users.get(i);
                break;
            }
        }
        if (chosenUser == null) {
            return;
        }

        final carrentalsystem.models.User targetUser = chosenUser;

        // ── STEP 3: Ask for password ──────────────────────────
        javax.swing.JPasswordField pwdField = new javax.swing.JPasswordField(20);
        int pwdResult = javax.swing.JOptionPane.showConfirmDialog(
                this,
                new Object[]{
                    "Enter password for " + targetUser.getFullName() + ":",
                    pwdField
                },
                "Verify Password",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE
        );

        if (pwdResult != javax.swing.JOptionPane.OK_OPTION) {
            return;
        }

        String enteredPassword = new String(pwdField.getPassword()).trim();
        if (enteredPassword.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Password cannot be empty.");
            return;
        }

        // ── STEP 4: Verify password with BCrypt ──────────────
        boolean passwordOk = org.mindrot.jbcrypt.BCrypt.checkpw(
                enteredPassword, targetUser.getPassword());

        if (!passwordOk) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Incorrect password. Switch cancelled.",
                    "Authentication Failed",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ── STEP 5: Switch the session ────────────────────────
        new Thread(() -> {
            try {
                // End the current session
                carrentalsystem.core.SessionManager.endSession();

                // Start a new session for the chosen user
                carrentalsystem.core.SessionManager.startSession(targetUser);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    // Reload dashboard with the new user's data
                    if (dashboard != null) {
                        dashboard.reloadForUser();
                    } else {
                        // Fallback: open a fresh LoginFrame
                        new carrentalsystem.auth.LoginFrame().setVisible(true);
                        java.awt.Window ancestor
                                = javax.swing.SwingUtilities.getWindowAncestor(this);
                        if (ancestor != null) {
                            ancestor.dispose();
                        }
                    }
                });
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(()
                        -> javax.swing.JOptionPane.showMessageDialog(this,
                                "Error switching account: " + e.getMessage()));
            }
        }).start();
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
        if (!pwdInput.isEmpty()) {
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
                    javax.swing.JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                    loadUserData();
                    isEditing = true;  
                    toggleEditMode();  
                    if (dashboard != null) {
                        carrentalsystem.models.User refreshed
                                = carrentalsystem.core.SessionManager.getCurrentUser();
                        if (refreshed != null) {
                            dashboard.getHeaderPanel().updateProfileIcon(
                                    refreshed.getProfileImagePath());
                        }
                    }
                });
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(()
                        -> JOptionPane.showMessageDialog(this, "Update Failed: " + e.getMessage()));
            }
        }).start();
    }//GEN-LAST:event_btnUpdateActionPerformed

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
                int userId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();
                carrentalsystem.services.UserService svc
                        = new carrentalsystem.services.UserService();
                svc.deleteAccount(userId);
                carrentalsystem.core.SessionManager.endSession();

                javax.swing.SwingUtilities.invokeLater(() -> {
                    new carrentalsystem.auth.LoginFrame().setVisible(true);
                    java.awt.Window ancestor
                            = javax.swing.SwingUtilities.getWindowAncestor(this);
                    if (ancestor != null) {
                        ancestor.dispose();
                    }
                });
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(()
                        -> javax.swing.JOptionPane.showMessageDialog(this,
                                "Error deleting account: " + e.getMessage()));
            }
        }).start();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void lblEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditMouseClicked
        // TODO add your handling code here:
        toggleEditMode();
    }//GEN-LAST:event_lblEditMouseClicked

    private void lblIconProfileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIconProfileMouseClicked
        // TODO add your handling code here:
        // Only allow image change when in edit mode
        if (!isEditing) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Click the ✏ edit icon first to enable editing.",
                    "Edit Mode Required",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Open file chooser
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setDialogTitle("Choose Profile Picture");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files (JPG, PNG)", "jpg", "jpeg", "png"));
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(this);
        if (result != javax.swing.JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File selectedFile = chooser.getSelectedFile();
        if (selectedFile == null || !selectedFile.exists()) {
            return;
        }

        // Copy the image to the project's upload directory
        String savedPath = carrentalsystem.utils.ImageUtil.copyImage(selectedFile);
        if (savedPath == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Could not save image. Please try again.");
            return;
        }

        // Update the profile icon in ProfilePanel immediately
        ImageUtil.applyCircleImage(lblIconProfile, savedPath, 80);

        // Update the header icon immediately
        if (dashboard != null) {
            dashboard.getHeaderPanel().updateProfileIcon(savedPath);
        }

        // Save the path to the current user's model object
        // (will be persisted to DB when user clicks Update)
        carrentalsystem.models.User user
                = carrentalsystem.core.SessionManager.getCurrentUser();
        if (user != null) {
            user.setProfileImagePath(savedPath);
        }
    }//GEN-LAST:event_lblIconProfileMouseClicked

    private void lblUpgradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUpgradeMouseClicked
        // TODO add your handling code here:
        // Check if the user is already PRO to avoid unnecessary popups[cite: 26]
        carrentalsystem.models.User currentUser = carrentalsystem.core.SessionManager.getCurrentUser();

        if (currentUser != null && "PRO".equalsIgnoreCase(currentUser.getTier())) {
            javax.swing.JOptionPane.showMessageDialog(this, "You are already a PRO user!");
        } else {
            // Show the upgrade wrapper you created in the MainDashboard
            if (dashboard != null) {
                dashboard.getPnlProAlertWrapper().setVisible(true);
                dashboard.getLayeredPane().setComponentZOrder(dashboard.getPnlProAlertWrapper(), 0);
            }
        }
    }//GEN-LAST:event_lblUpgradeMouseClicked

    private void txtareaTypeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtareaTypeFocusGained
        // TODO add your handling code here:
        if (txtareaType.getText().equals("Type something here...")) {
            txtareaType.setText("");
            txtareaType.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtareaTypeFocusGained

    private void txtareaTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtareaTypeFocusLost
        // TODO add your handling code here:
        if (txtareaType.getText().trim().isEmpty()) {
            txtareaType.setText("Type something here...");
            txtareaType.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txtareaTypeFocusLost

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        String category = (String) comboSelectCategory.getSelectedItem();
        String description = txtareaType.getText().trim();
        int userId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();

        // Validations
        if (category.equals("Select Category")) {
            JOptionPane.showMessageDialog(this, "Please select a category.");
            return;
        }
        if (description.isEmpty() || description.equals("Type something here...")) {
            JOptionPane.showMessageDialog(this, "Please describe your issue.");
            return;
        }

        // status is OPEN by default in DB schema
        String sql = "INSERT INTO tickets (user_id, subject, description) VALUES (?, ?, ?)";

        try (java.sql.Connection conn = carrentalsystem.core.DBConnection.getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, category);
            ps.setString(3, description);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Ticket submitted! Admin status: OPEN.");

                // Trigger the notification
                sendTicketNotification(userId, category);

                // Reset UI
                comboSelectCategory.setSelectedIndex(0);
                txtareaType.setText("Type something here...");
                txtareaType.setForeground(Color.GRAY);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void txtCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCityActionPerformed

    private void txtCityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCityFocusGained
        // TODO add your handling code here:
        if (txtCity.getText().equals("City")) {
            txtCity.setText("");
            txtCity.setForeground(TEXT_DARK);
        }
    }//GEN-LAST:event_txtCityFocusGained

    private void txtCityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCityFocusLost
        // TODO add your handling code here:
        if (txtCity.getText().trim().isEmpty()) {
            txtCity.setText("City");
            txtCity.setForeground(java.awt.Color.GRAY);
        }
    }//GEN-LAST:event_txtCityFocusLost

    private void txtProvinceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProvinceFocusGained
        // TODO add your handling code here:
        if (txtProvince.getText().equals("Province")) {
            txtProvince.setText("");
            txtProvince.setForeground(TEXT_DARK);
        }
    }//GEN-LAST:event_txtProvinceFocusGained

    private void txtProvinceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProvinceFocusLost
        // TODO add your handling code here:
        if (txtProvince.getText().trim().isEmpty()) {
            txtProvince.setText("Province");
            txtProvince.setForeground(java.awt.Color.GRAY);
        }
    }//GEN-LAST:event_txtProvinceFocusLost

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
    
    private void sendTicketNotification(int userId, String category) {
        String msg = "Your ticket regarding '" + category + "' has been opened successfully.";

        try {
            carrentalsystem.services.NotificationService service = new carrentalsystem.services.NotificationService();
            service.notify(userId, msg, "SYSTEM");

            // Update the red dot on the bell icon[cite: 34]
            if (dashboard != null) {
                dashboard.updateNotificationBadge();
            }
        } catch (Exception e) {
            System.err.println("Notification trigger failed: " + e.getMessage());
        }
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
    private javax.swing.JLabel lblEdit;
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
