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
    this.setLayout(new java.awt.GridBagLayout());
    this.setBackground(BACKGROUND_COLOR);
    
    // 2. Prepare Sidebar Buttons
    makeSidebarButton(lblSettings);
    makeSidebarButton(lblSup);
    
    // 3. Reset NetBeans defaults for manual layout control
    this.removeAll(); 
    panelAccountCard.setPreferredSize(null);
    panelAccountCard.setMinimumSize(null);
    panelSupportCard.setPreferredSize(null);
    panelSupportCard.setMinimumSize(null);
    panelSidebar.setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();

    // --- POSITION 1: SIDEBAR (Far Left) ---
    gbc.gridx = 0; 
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0; // Changed to 0 so it doesn't fight the footer for space
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.VERTICAL;
    gbc.insets = new java.awt.Insets(100, 40, 0, 0);
    this.add(panelSidebar, gbc);

    // --- POSITION 2: FILLER (The gap) ---
    JPanel middleFiller = new JPanel();
    middleFiller.setOpaque(false);
    gbc.gridx = 1;
    gbc.weightx = 1.0; 
    this.add(middleFiller, gbc);

    // --- POSITION 3: ACCOUNT CARD ---
    gbc.gridx = 2;
    gbc.weightx = 0.0;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new java.awt.Insets(40, 15, 40, 15);
    this.add(panelAccountCard, gbc);

    // --- POSITION 4: SUPPORT CARD (Far Right) ---
    gbc.gridx = 3;
    gbc.insets = new java.awt.Insets(40, 15, 40, 40);
    this.add(panelSupportCard, gbc);

    // --- POSITION 5: FOOTER (Bottom Center) ---
    // Using the variable name from your design tab: lblAboutUs
    lblAboutUs.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
    lblAboutUs.setForeground(new java.awt.Color(100, 100, 100));
    
    gbc.gridx = 0;
    gbc.gridy = 1;         // Row below the cards
    gbc.gridwidth = 4;     // Span across sidebar, filler, and both cards
    gbc.weighty = 1.0;     // This pushes EVERYTHING else up
    gbc.anchor = GridBagConstraints.SOUTH;
    gbc.insets = new java.awt.Insets(20, 0, 40, 0); // Bottom margin
    this.add(lblAboutUs, gbc);

    // 4. Final Styling
    applyModernStyling();
    setupInternalLayouts();
    
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
            // Define what happens when clicked
            if (label.getText().contains("Settings")) {
                System.out.println("Settings Clicked!");
                // Add your settings logic here
            } else if (label.getText().contains("Support")) {
                System.out.println("Support Clicked!");
                // Add your support logic here
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
    
    private void setupSwitchingLogic() {
        // Wrap panelAccountCard and panelSupportCard in a new container with CardLayout
        cardManager = new CardLayout();
        cardsContainer = new JPanel(cardManager);
        cardsContainer.setOpaque(false);

        cardsContainer.add(panelAccountCard, "ACCOUNT");
        cardsContainer.add(panelSupportCard, "SUPPORT");

        // Adjust your existing GridBagLayout to add 'cardsContainer' instead of individual cards
    }
    
    public void loadUserData() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            String path = user.getProfileImagePath();
            if (path != null && !path.isEmpty()) {
                ImageUtil.applyScaledImage(lblIconProfile, path, 100, 100);
            } else {
                lblIconProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/DefaultProfile.png")));
            }
            txtName.setText(user.getFullName());
            txtEmail.setText(user.getEmail());
            lblName.setText(user.getFullName().split(" ")[0]); // Show first name only in big label

            // Load Location if available in your User model
            // txtCity.setText(user.getCity()); 
            // Update header profile icon simultaneously
            if (dashboard != null) {
                dashboard.getHeaderPanel().updateProfileIcon(user.getProfileImagePath()); //
            }
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
        btnDelete = new javax.swing.JButton();
        btnDelete.setBackground(java.awt.Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(210, 210, 210), 1, true));
        lblLocation = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        txtProvince = new javax.swing.JTextField();
        btnSwitchAcc = new javax.swing.JButton();
        panelSidebar = new javax.swing.JPanel();
        lblSettings = new javax.swing.JLabel();
        lblSup = new javax.swing.JLabel();
        lblAboutUs = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

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
                .addContainerGap(87, Short.MAX_VALUE))
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
                .addContainerGap(65, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(panelSupportCard, gridBagConstraints);

        panelAccountCard.setBackground(new java.awt.Color(255, 255, 255));
        panelAccountCard.setMinimumSize(new java.awt.Dimension(500, 600));
        panelAccountCard.setPreferredSize(new java.awt.Dimension(500, 600));

        lblYourAcc.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblYourAcc.setText("Your Account");

        lblIconProfile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/DefaultAvatar.png"))); // NOI18N

        lblName.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblName.setText("Charmaine");

        lblEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/EditProfile.png"))); // NOI18N

        lblUpgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Upgrade.png"))); // NOI18N

        txtName.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtName.setText("Charmaine Illustrisimo");

        lblNameAcc.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblNameAcc.setText("Name");

        txtEmail.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtEmail.setText("charmainillustrisimo@gmail.com");
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        lbleEmail.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lbleEmail.setText("Email");

        txtPassword.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtPassword.setText("jPasswordField1");

        lblPassword.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblPassword.setText("Password");

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblLocation.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        lblLocation.setText("Location");

        txtCity.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtCity.setText("City");

        txtProvince.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        txtProvince.setText("Province");

        btnSwitchAcc.setText("Switch Account");

        javax.swing.GroupLayout panelAccountCardLayout = new javax.swing.GroupLayout(panelAccountCard);
        panelAccountCard.setLayout(panelAccountCardLayout);
        panelAccountCardLayout.setHorizontalGroup(
            panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccountCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccountCardLayout.createSequentialGroup()
                        .addComponent(btnSwitchAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(panelAccountCardLayout.createSequentialGroup()
                        .addComponent(lblLocation)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelAccountCardLayout.createSequentialGroup()
                        .addGroup(panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblYourAcc)
                            .addComponent(lblNameAcc)
                            .addComponent(lbleEmail)
                            .addComponent(lblPassword))
                        .addContainerGap(341, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccountCardLayout.createSequentialGroup()
                        .addGroup(panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelAccountCardLayout.createSequentialGroup()
                                .addComponent(lblIconProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                                .addComponent(lblEdit)
                                .addGap(28, 28, 28)
                                .addComponent(lblUpgrade, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24))
                            .addComponent(txtProvince, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCity, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(45, 45, 45))))
        );
        panelAccountCardLayout.setVerticalGroup(
            panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccountCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblYourAcc)
                .addGroup(panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAccountCardLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblName)
                                .addComponent(lblUpgrade, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccountCardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblIconProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(lblNameAcc)
                .addGap(2, 2, 2)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbleEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLocation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtProvince, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(panelAccountCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(btnSwitchAcc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        add(panelAccountCard, gridBagConstraints);

        panelSidebar.setPreferredSize(new java.awt.Dimension(200, 400));

        lblSettings.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Settings1.png"))); // NOI18N
        lblSettings.setText("   Settings");

        lblSup.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblSup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Support.png"))); // NOI18N
        lblSup.setText("   Support");

        javax.swing.GroupLayout panelSidebarLayout = new javax.swing.GroupLayout(panelSidebar);
        panelSidebar.setLayout(panelSidebarLayout);
        panelSidebarLayout.setHorizontalGroup(
            panelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSidebarLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSup, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(lblSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSidebarLayout.setVerticalGroup(
            panelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSidebarLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lblSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(lblSup, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(100, 20, 0, 0);
        add(panelSidebar, gridBagConstraints);

        lblAboutUs.setText("<html><center>About Us<br>© 2026 IT. All Rights Reserved.</center></html>");
        lblAboutUs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 100, 0);
        add(lblAboutUs, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

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
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtProvince;
    private javax.swing.JTextArea txtareaType;
    // End of variables declaration//GEN-END:variables
}
