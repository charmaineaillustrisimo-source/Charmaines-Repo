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
import carrentalsystem.models.ListerRequirement;
import carrentalsystem.services.AdminService;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class ViewingDocumentsPanel extends javax.swing.JFrame {
    
    private static final Logger logger = Logger.getLogger(ViewingDocumentsPanel.class.getName());
    private final IAdminService adminService = new AdminService();
    private ListerRequirement currentReq;
    
    public ViewingDocumentsPanel() {
        initComponents();
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    public ViewingDocumentsPanel(carrentalsystem.models.ListerRequirement req) {
        initComponents(); // Initialize components first
        this.currentReq = req;

        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        // Safety Guard: Only load if we have data
        if (this.currentReq != null) {
            lblUsersCarID.setText(String.valueOf(currentReq.getUserId()));
            jLabel2.setText(currentReq.getUserFullName());
            loadRequirementDocuments();
        } else {
            // If opened without data, don't crash, just show a message
            JOptionPane.showMessageDialog(this, "No requirement data received.");
        }
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
    
    private boolean areAllRequirementsMet() {
        // Updated to only check the requirements you decided to keep
        return chkOCR.isSelected()
                && chkOR.isSelected()
                && chkGovID.isSelected();
    }
    
    private void showEnlargedImage(String path) {
        JDialog viewer = new JDialog(this, "Detailed View", true);
        viewer.add(new JScrollPane(new JLabel(new ImageIcon(path))));
        viewer.setSize(1000, 800);
        viewer.setLocationRelativeTo(this);
        viewer.setVisible(true);
    }
    
    private void enlargeCheckboxes() {
        // Only include the checkboxes you want to keep visible and styled
        JCheckBox[] boxes = {chkOCR, chkOR, chkGovID};
        for (JCheckBox cb : boxes) {
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            cb.setForeground(Color.WHITE);
        }
    }
    
    private void loadRequirementDocuments() {
        if (currentReq == null) {
            return;
        }

        pnlImageContainer.removeAll();

        // Use the paths from the requirement object
        setupDocumentGallery("LTO / Vehicle Document", currentReq.getLtoDocumentPath());
        setupDocumentGallery("Valid Government ID", currentReq.getValidIdPath());
        setupDocumentGallery("Selfie Verification", currentReq.getSelfiePhotoPath());

        pnlImageContainer.revalidate();
        pnlImageContainer.repaint();
    }

    public void setupDocumentGallery(String docName, String path) {
        if (path == null || path.isEmpty()) {
            return;
        }

        JLabel lblPic = new JLabel();
        lblPic.setPreferredSize(new java.awt.Dimension(400, 300)); // Larger for admin review
        lblPic.setHorizontalAlignment(JLabel.CENTER);
        lblPic.setBorder(javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100), 1),
                docName, 0, 0, new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));

        // Use ImageUtil if available, otherwise manual scale
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            lblPic.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblPic.setText("Error loading image");
            lblPic.setForeground(Color.RED);
        }

        lblPic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showEnlargedImage(path);
            }
        });

        pnlImageContainer.add(lblPic);
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
        btnPending = new javax.swing.JButton();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        pnlChecklist = new RoundedPanel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        pnlChecklistContainer1 = new javax.swing.JPanel();
        chkOCR = new javax.swing.JCheckBox();
        chkOR = new javax.swing.JCheckBox();
        chkGovID = new javax.swing.JCheckBox();
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

        btnPending.setBackground(new java.awt.Color(255, 102, 0));
        btnPending.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnPending.setText("PENDING CLARIFICATION");
        btnPending.addActionListener(this::btnPendingActionPerformed);
        pnlDecision.add(btnPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 380, 40));

        btnApprove.setBackground(new java.awt.Color(0, 204, 51));
        btnApprove.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnApprove.setText("APPROVE");
        btnApprove.addActionListener(this::btnApproveActionPerformed);
        pnlDecision.add(btnApprove, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 380, 40));

        btnReject.setBackground(new java.awt.Color(255, 0, 0));
        btnReject.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnReject.setForeground(new java.awt.Color(255, 255, 255));
        btnReject.setText("REJECT");
        btnReject.addActionListener(this::btnRejectActionPerformed);
        pnlDecision.add(btnReject, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 380, 40));

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

    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        // TODO add your handling code here:
        if (currentReq == null) {
            return;
        }

        if (!areAllRequirementsMet()) {
            JOptionPane.showMessageDialog(this,
                    "Please check all visible requirement boxes before approving.",
                    "Incomplete Review", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm Approval for " + currentReq.getUserFullName() + "?",
                "Approve Lister", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // AdminService handles: lister_requirements status, users.lister_status, users.user_type = 'BOTH'
                adminService.approveListerVerification(currentReq.getRequirementId(), currentReq.getUserId());
                JOptionPane.showMessageDialog(this, "User approved! They now have Lister access.");
                this.dispose();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnApproveActionPerformed

    private void btnPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendingActionPerformed
        // TODO add your handling code here:
        if (currentReq == null) {
            return;
        }

        StringBuilder missing = new StringBuilder();
        if (!chkGovID.isSelected()) {
            missing.append("Valid ID, ");
        }
        if (!chkOCR.isSelected()) {
            missing.append("Vehicle Registration, ");
        }

        String msg = "Admin: We need clearer copies of the following: "
                + (missing.length() > 0 ? missing.toString() : "Documents")
                + ". Please update your application.";

        txtChatInput.setText(msg);
        btnSendChatActionPerformed(null);
        JOptionPane.showMessageDialog(this, "Request for clarification sent.");
    }//GEN-LAST:event_btnPendingActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        if (currentReq == null) {
            return;
        }

        String reason = JOptionPane.showInputDialog(this, "Enter reason for rejection:");
        if (reason != null && !reason.trim().isEmpty()) {
            try {
                adminService.rejectListerVerification(currentReq.getRequirementId(), currentReq.getUserId(), reason);
                JOptionPane.showMessageDialog(this, "Application rejected.");
                this.dispose();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Rejection failed: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnRejectActionPerformed
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
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnPending;
    private javax.swing.JButton btnReject;
    private javax.swing.JButton btnSendChat;
    private javax.swing.JCheckBox chkGovID;
    private javax.swing.JCheckBox chkOCR;
    private javax.swing.JCheckBox chkOR;
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
