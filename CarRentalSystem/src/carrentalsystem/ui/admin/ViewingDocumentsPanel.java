/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrentalsystem.ui.admin;

import carrentalsystem.auth.LoginFrame;
/**
 *
 * @author macbookairm1grey
 */
import carrentalsystem.auth.LoginFrame;
import carrentalsystem.interfaces.IAdminService;
import carrentalsystem.services.AdminService;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class ViewingDocumentsPanel extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ViewingDocumentsPanel.class.getName());
    private final IAdminService adminService = new AdminService();
    private List<carrentalsystem.models.User> currentUsers;
    private carrentalsystem.models.User currentUser;
    
    public ViewingDocumentsPanel() {
        initComponents();

        // 1. Set the correct close operation so it doesn't close your whole app
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // 2. Setup your UI styles
        setupTableStyles();

        // 3. Center the window and maximize it
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        // 4. Safety Check: Only load if a user exists
        if (this.currentUser != null) {
            loadUserDocuments();
        } else {
            // If opened from main() or testing, show placeholders
            setupDocumentGallery("Placeholder", null);
        }
    }
    
    public ViewingDocumentsPanel(carrentalsystem.models.User user) {
        this.currentUser = user;
        initComponents();

        // This ensures ONLY this window closes, not the whole app
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        this.setLocationRelativeTo(null);
        loadUserDocuments();
    }
    
    private void setupTableStyles() {
        Color panelBg = new Color(48, 48, 46);
    }
    
    public class RoundedPanel extends JPanel {
    private int cornerRadius = 30; // Adjust this for more/less roundness

    public RoundedPanel() {
        setOpaque(false); // Critical: allows the corners to look rounded
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Use the background color set in NetBeans properties
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}
    
    public void setupDocumentGallery(String docName, String path) {
        // If there is no path, don't create a slot
        if (path == null || path.isEmpty()) {
            return;
        }

        // 1. Create the Label using your existing styling
        JLabel lblPic = new JLabel();
        lblPic.setPreferredSize(new java.awt.Dimension(200, 150));
        lblPic.setBorder(javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 60), 2),
                docName, 0, 0, null, Color.WHITE));

        // 2. Load and scale the image from the DB path
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            lblPic.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblPic.setText("Image not found");
        }

        // 3. Your existing Click Listener for Enlarging
        lblPic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showEnlargedImage(path);
            }
        });

        pnlImageContainer.add(lblPic);
    }
    
    private boolean areAllRequirementsMet() {
        return chkOCR.isSelected()
                && // Use the actual variable names from your Navigator
                chkOR.isSelected()
                && chkGovID.isSelected()
                && chkDriLicense.isSelected()
                && chkVehiclePhotos.isSelected()
                && chkPI.isSelected();
    }
    
    private void showEnlargedImage(String path) {
        // Create a pop-up window (JDialog)
        JDialog viewer = new JDialog(this, "Document Viewer", true);

        // Create a label to hold the full-size image
        JLabel lblFull = new JLabel(new ImageIcon(path));

        // Add a scroll pane in case the image is bigger than the screen
        JScrollPane scroll = new JScrollPane(lblFull);

        viewer.add(scroll);
        viewer.setSize(900, 700); // Set a large size for the viewer
        viewer.setLocationRelativeTo(this); // Center it on your dashboard
        viewer.setVisible(true);
    }
    
    private void enlargeCheckboxes() {
        JCheckBox[] boxes = {chkOR, chkOR, chkGovID, chkDriLicense, chkVehiclePhotos, chkPI};
        for (JCheckBox cb : boxes) {
            // This scales the rendering of the component
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 20)); // Match your dark aesthetic
        }
    }
    
    private void loadUserDocuments() {
        pnlImageContainer.removeAll();

        try (java.sql.Connection conn = carrentalsystem.core.DBConnection.getConnection()) {
            String type = currentUser.getUserType();

            if ("LISTER".equals(type) || "BOTH".equals(type)) {
                // Use existing SQL structure for Listers
                String sql = "SELECT * FROM lister_requirements WHERE user_id = ? ORDER BY submitted_at DESC LIMIT 1";
                java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, currentUser.getUserId());
                java.sql.ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    setupDocumentGallery("Valid ID", rs.getString("valid_id_path"));
                    setupDocumentGallery("Selfie", rs.getString("selfie_photo_path"));
                    setupDocumentGallery("LTO Doc", rs.getString("lto_document_path"));
                }
            } else {
                // Use existing SQL structure for Renters
                String sql = "SELECT valid_id_path FROM renter_verifications WHERE renter_id = ? LIMIT 1";
                java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, currentUser.getUserId());
                java.sql.ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    setupDocumentGallery("Renter ID", rs.getString("valid_id_path"));
                    pnlChecklist.setVisible(false); // Hide the lister checklist
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Access Error: " + e.getMessage());
        }

        pnlImageContainer.revalidate();
        pnlImageContainer.repaint();
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

        pnlTopBar = new javax.swing.JPanel();
        lblVettingAndApprovals = new javax.swing.JLabel();
        lblCarID = new javax.swing.JLabel();
        lblUsersCarID = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        pnlChatBox = new RoundedPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        pnlInputArea = new javax.swing.JPanel();
        txtChatInput = new javax.swing.JTextField();
        txtChatInput.setBackground(new java.awt.Color(48, 48, 46));
        txtChatInput.setForeground(java.awt.Color.WHITE);
        txtChatInput.setCaretColor(java.awt.Color.WHITE);
        txtChatInput.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnSendChat = new javax.swing.JButton();
        jScrollPaneChat = new javax.swing.JScrollPane();
        jScrollPaneChat.getViewport().setBackground(new java.awt.Color(38, 38, 36));
        pnlChatHistory = new javax.swing.JPanel();
        pnlDecision = new RoundedPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        pnlChecklist = new RoundedPanel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        pnlChecklistContainer1 = new javax.swing.JPanel();
        chkOCR = new javax.swing.JCheckBox();
        chkOR = new javax.swing.JCheckBox();
        chkGovID = new javax.swing.JCheckBox();
        chkDriLicense = new javax.swing.JCheckBox();
        chkVehiclePhotos = new javax.swing.JCheckBox();
        chkPI = new javax.swing.JCheckBox();
        pnlDocuments = new RoundedPanel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlImageContainer = new javax.swing.JPanel();
        pnlSideBarForDocuments2 = new RoundedPanel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnlImageContainer2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(3, 33, 33));

        pnlTopBar.setBackground(new java.awt.Color(30, 30, 30));
        pnlTopBar.setPreferredSize(new java.awt.Dimension(1290, 90));
        pnlTopBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblVettingAndApprovals.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblVettingAndApprovals.setForeground(new java.awt.Color(255, 255, 255));
        lblVettingAndApprovals.setText("VETTING & APPROVALS:");
        pnlTopBar.add(lblVettingAndApprovals, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        lblCarID.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblCarID.setForeground(new java.awt.Color(255, 255, 255));
        lblCarID.setText("CAR ID:");
        pnlTopBar.add(lblCarID, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, -1));

        lblUsersCarID.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblUsersCarID.setForeground(new java.awt.Color(255, 255, 255));
        lblUsersCarID.setPreferredSize(new java.awt.Dimension(100, 32));
        pnlTopBar.add(lblUsersCarID, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("OWNER:");
        pnlTopBar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setPreferredSize(new java.awt.Dimension(300, 32));
        pnlTopBar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, -1, -1));

        jButton4.setBackground(new java.awt.Color(30, 30, 30));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("X");
        jButton4.addActionListener(this::jButton4ActionPerformed);
        pnlTopBar.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1255, 13, 60, 60));

        getContentPane().add(pnlTopBar, java.awt.BorderLayout.NORTH);

        pnlMain.setBackground(new java.awt.Color(48, 48, 46));
        pnlMain.setForeground(new java.awt.Color(255, 255, 255));
        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlChatBox.setBackground(new java.awt.Color(38, 38, 36));
        pnlChatBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlChatBox.setMinimumSize(new java.awt.Dimension(300, 485));
        pnlChatBox.setOpaque(false);
        pnlChatBox.setPreferredSize(new java.awt.Dimension(400, 600));
        pnlChatBox.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CHAT BOX");
        pnlChatBox.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 370, -1));

        jSeparator2.setPreferredSize(new java.awt.Dimension(360, 10));
        pnlChatBox.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 42, -1, 501));

        pnlInputArea.setLayout(new java.awt.GridBagLayout());

        txtChatInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlInputArea.add(txtChatInput, gridBagConstraints);

        btnSendChat.setBackground(new java.awt.Color(0, 102, 204));
        btnSendChat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSendChat.setForeground(new java.awt.Color(255, 255, 255));
        btnSendChat.setText("SEND");
        btnSendChat.addActionListener(this::btnSendChatActionPerformed);
        pnlInputArea.add(btnSendChat, new java.awt.GridBagConstraints());

        pnlChatBox.add(pnlInputArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 543, 370, -1));

        jScrollPaneChat.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPaneChat.setPreferredSize(new java.awt.Dimension(400, 400));

        pnlChatHistory.setBackground(new java.awt.Color(38, 38, 36));
        pnlChatHistory.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlChatHistory.setForeground(new java.awt.Color(255, 255, 255));
        pnlChatHistory.setMaximumSize(new java.awt.Dimension(32767, 32767));
        pnlChatHistory.setMinimumSize(new java.awt.Dimension(10, 10));
        pnlChatHistory.setPreferredSize(new java.awt.Dimension(10, 10));
        pnlChatHistory.setLayout(new javax.swing.BoxLayout(pnlChatHistory, javax.swing.BoxLayout.Y_AXIS));
        jScrollPaneChat.setViewportView(pnlChatHistory);

        pnlChatBox.add(jScrollPaneChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 42, 370, 501));

        pnlMain.add(pnlChatBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, 390, 580));

        pnlDecision.setBackground(new java.awt.Color(38, 38, 36));
        pnlDecision.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlDecision.setMinimumSize(new java.awt.Dimension(300, 485));
        pnlDecision.setOpaque(false);
        pnlDecision.setPreferredSize(new java.awt.Dimension(400, 500));
        pnlDecision.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DECISION");
        pnlDecision.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jSeparator1.setPreferredSize(new java.awt.Dimension(380, 10));
        pnlDecision.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 102, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButton1.setText("PENDING CLARIFICATION");
        pnlDecision.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 380, 40));

        jButton2.setBackground(new java.awt.Color(0, 204, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButton2.setText("APPROVE");
        pnlDecision.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 380, 40));

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("REJECT");
        pnlDecision.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 380, 40));

        pnlMain.add(pnlDecision, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 380, -1, 210));

        pnlChecklist.setBackground(new java.awt.Color(38, 38, 36));
        pnlChecklist.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlChecklist.setMinimumSize(new java.awt.Dimension(300, 485));
        pnlChecklist.setOpaque(false);
        pnlChecklist.setPreferredSize(new java.awt.Dimension(400, 500));
        pnlChecklist.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("REQUIREMENTS CHECKLIST");
        pnlChecklist.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jSeparator3.setPreferredSize(new java.awt.Dimension(380, 10));
        pnlChecklist.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, -1, -1));

        pnlChecklistContainer1.setBackground(new java.awt.Color(38, 38, 36));
        pnlChecklistContainer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkOCR.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkOCR.setForeground(new java.awt.Color(255, 255, 255));
        chkOCR.setText("Original Certificate of Registration");
        pnlChecklistContainer1.add(chkOCR, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        chkOR.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkOR.setForeground(new java.awt.Color(255, 255, 255));
        chkOR.setText("Official Receipt of Vehicle Registration");
        pnlChecklistContainer1.add(chkOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        chkGovID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkGovID.setForeground(new java.awt.Color(255, 255, 255));
        chkGovID.setText("Valid Government ID");
        pnlChecklistContainer1.add(chkGovID, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        chkDriLicense.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkDriLicense.setForeground(new java.awt.Color(255, 255, 255));
        chkDriLicense.setText("Driver's License");
        pnlChecklistContainer1.add(chkDriLicense, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        chkVehiclePhotos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkVehiclePhotos.setForeground(new java.awt.Color(255, 255, 255));
        chkVehiclePhotos.setText("Vehicle Photos");
        pnlChecklistContainer1.add(chkVehiclePhotos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        chkPI.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkPI.setForeground(new java.awt.Color(255, 255, 255));
        chkPI.setText("Proof of Insurance");
        pnlChecklistContainer1.add(chkPI, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        pnlChecklist.add(pnlChecklistContainer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 380, 270));

        pnlMain.add(pnlChecklist, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, -1, 340));

        pnlDocuments.setBackground(new java.awt.Color(38, 38, 36));
        pnlDocuments.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlDocuments.setMinimumSize(new java.awt.Dimension(300, 485));
        pnlDocuments.setOpaque(false);
        pnlDocuments.setPreferredSize(new java.awt.Dimension(500, 700));
        pnlDocuments.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("UPLOADED DOCUMENTS");
        pnlDocuments.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jSeparator4.setPreferredSize(new java.awt.Dimension(480, 10));
        pnlDocuments.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, -1, -1));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(450, 600));
        jScrollPane1.getVerticalScrollBar().setBackground(new java.awt.Color(38, 38, 36));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16); // Makes scrolling feel smoother
        jScrollPane1.getViewport().setBackground(new java.awt.Color(38, 38, 36));

        pnlImageContainer.setBackground(new java.awt.Color(38, 38, 36));
        pnlImageContainer.setOpaque(false);
        pnlImageContainer.setLayout(new java.awt.GridLayout(0, 2, 10, 10));
        jScrollPane1.setViewportView(pnlImageContainer);

        pnlDocuments.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 480, 510));

        pnlMain.add(pnlDocuments, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 580));

        pnlSideBarForDocuments2.setBackground(new java.awt.Color(38, 38, 36));
        pnlSideBarForDocuments2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlSideBarForDocuments2.setMinimumSize(new java.awt.Dimension(300, 485));
        pnlSideBarForDocuments2.setOpaque(false);
        pnlSideBarForDocuments2.setPreferredSize(new java.awt.Dimension(500, 700));
        pnlSideBarForDocuments2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("UPLOADED DOCUMENTS");
        pnlSideBarForDocuments2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jSeparator5.setPreferredSize(new java.awt.Dimension(480, 10));
        pnlSideBarForDocuments2.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, -1, -1));

        jScrollPane3.setPreferredSize(new java.awt.Dimension(450, 600));
        jScrollPane1.getVerticalScrollBar().setBackground(new java.awt.Color(38, 38, 36));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16); // Makes scrolling feel smoother
        jScrollPane1.getViewport().setBackground(new java.awt.Color(38, 38, 36));

        pnlImageContainer2.setBackground(new java.awt.Color(38, 38, 36));
        pnlImageContainer2.setOpaque(false);
        pnlImageContainer2.setLayout(new java.awt.GridLayout(0, 2, 10, 10));
        jScrollPane3.setViewportView(pnlImageContainer2);

        pnlSideBarForDocuments2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 480, 510));

        pnlMain.add(pnlSideBarForDocuments2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 580));

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendChatActionPerformed

        String message = txtChatInput.getText().trim();

        if (!message.isEmpty()) {

            // Wrapper panel
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            messagePanel.setOpaque(false);

            // Message text area
            JTextArea txtMessage = new JTextArea("Admin: " + message);

            txtMessage.setLineWrap(true);
            txtMessage.setWrapStyleWord(true);

            txtMessage.setEditable(false);
            txtMessage.setFocusable(false);

            txtMessage.setForeground(Color.WHITE);
            txtMessage.setBackground(new Color(0, 102, 204));

            txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            txtMessage.setBorder(
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
            );

            // Width control
            txtMessage.setColumns(20);

            // Auto height adjustment
            txtMessage.setSize(250, Short.MAX_VALUE);

            // Rounded bubble panel
            JPanel bubble = new JPanel(new BorderLayout()) {

                @Override
                protected void paintComponent(Graphics g) {

                    Graphics2D g2 = (Graphics2D) g;

                    g2.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    g2.setColor(new Color(0, 102, 204));

                    g2.fillRoundRect(
                            0,
                            0,
                            getWidth(),
                            getHeight(),
                            25,
                            25
                    );

                    super.paintComponent(g);
                }
            };

            bubble.setOpaque(false);

            bubble.add(txtMessage);

            messagePanel.add(bubble);

            // Add message to chat history
            pnlChatHistory.add(messagePanel);

            // Refresh UI
            pnlChatHistory.revalidate();
            pnlChatHistory.repaint();

            // Clear input
            txtChatInput.setText("");

            // Scroll automatically
            SwingUtilities.invokeLater(() -> {

                JScrollBar vertical
                        = jScrollPaneChat.getVerticalScrollBar();

                vertical.setValue(vertical.getMaximum());

            });
        }
    }//GEN-LAST:event_btnSendChatActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed
/**/
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
        java.awt.EventQueue.invokeLater(() -> new ViewingDocumentsPanel().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSendChat;
    private javax.swing.JCheckBox chkDriLicense;
    private javax.swing.JCheckBox chkGovID;
    private javax.swing.JCheckBox chkOCR;
    private javax.swing.JCheckBox chkOR;
    private javax.swing.JCheckBox chkPI;
    private javax.swing.JCheckBox chkVehiclePhotos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneChat;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblCarID;
    private javax.swing.JLabel lblUsersCarID;
    private javax.swing.JLabel lblVettingAndApprovals;
    private javax.swing.JPanel pnlChatBox;
    private javax.swing.JPanel pnlChatHistory;
    private javax.swing.JPanel pnlChecklist;
    private javax.swing.JPanel pnlChecklistContainer1;
    private javax.swing.JPanel pnlDecision;
    private javax.swing.JPanel pnlDocuments;
    private javax.swing.JPanel pnlImageContainer;
    private javax.swing.JPanel pnlImageContainer2;
    private javax.swing.JPanel pnlInputArea;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlSideBarForDocuments2;
    private javax.swing.JPanel pnlTopBar;
    private javax.swing.JTextField txtChatInput;
    // End of variables declaration//GEN-END:variables
}
