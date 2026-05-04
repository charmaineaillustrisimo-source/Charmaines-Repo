/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package carrentalsystem.ui.user;

/**
 *
 * @author macbookairm1grey
 */
public class InboxPanel extends javax.swing.JPanel {

    /**
     * Creates new form InboxPanel
     */
    private MainDashboard dashboard;
    private final carrentalsystem.interfaces.IMessageService messageService = new carrentalsystem.services.MessageService();
    private int activeOtherUserId = -1;
    private int activeCarId = -1;
    private String activeContactName = "";

    public InboxPanel() {
        initComponents();
        
        // BoxLayout
        lblRightAvatar.setMaximumSize(new java.awt.Dimension(200, 80));
        lblRightName.setMaximumSize(new java.awt.Dimension(200, 40));
        sprRight.setMaximumSize(new java.awt.Dimension(200, 1));
    }

    public void setDashboard(MainDashboard d) {
        this.dashboard = d;
    }

    public void loadData() {
        // Refresh the conversation list when inbox is opened
        if (carrentalsystem.core.SessionManager.getCurrentUser() == null) {
            return;
        }
        if (pnlConvList != null) {
            loadConversations();
        }
    }

    private void loadConversations() {
        pnlConvList.removeAll();
        new Thread(() -> {
            try {
                int uid = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();
                java.util.List<carrentalsystem.models.Message> convs
                        = messageService.getConversations(uid);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    pnlConvList.removeAll();
                    if (convs.isEmpty()) {
                        javax.swing.JLabel empty = new javax.swing.JLabel("  No conversations yet");
                        empty.setForeground(new java.awt.Color(180, 160, 155));
                        empty.setFont(new java.awt.Font("Helvetica Neue",
                                java.awt.Font.PLAIN, 14));
                        pnlConvList.add(empty);
                    }
                    for (carrentalsystem.models.Message m : convs) {
                        int myId = carrentalsystem.core.SessionManager
                                .getCurrentUser().getUserId();
                        int otherId = m.getSenderId() == myId
                                ? m.getReceiverId() : m.getSenderId();
                        pnlConvList.add(buildConvRow(m, otherId, myId));
                        pnlConvList.add(javax.swing.Box.createVerticalStrut(4));
                    }
                    pnlConvList.revalidate();
                    pnlConvList.repaint();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private javax.swing.JPanel buildConvRow(carrentalsystem.models.Message m, int otherId, int myId) {
        String initLetter = (m.getSenderName() != null && !m.getSenderName().isEmpty()) ? String.valueOf(m.getSenderName().charAt(0)).toUpperCase() : "?";

        javax.swing.JPanel row = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(otherId == activeOtherUserId
                        ? new java.awt.Color(220, 215, 225)
                        : new java.awt.Color(236, 236, 240));
                g2.fillRoundRect(4, 2, getWidth() - 8, getHeight() - 4, 14, 14);
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setPreferredSize(new java.awt.Dimension(290, 72));
        row.setMaximumSize(new java.awt.Dimension(290, 72));
        row.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        row.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 14, 8, 14));

        // Avatar with initial
        javax.swing.JLabel avatar = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(155, 121, 128));
                g2.fillOval(0, 0, 44, 44);
                g2.setColor(java.awt.Color.WHITE);
                g2.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 18));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                g2.drawString(initLetter,
                        (44 - fm.stringWidth(initLetter)) / 2,
                        (44 + fm.getAscent()) / 2 - 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new java.awt.Dimension(44, 44));

        // Text block
        javax.swing.JPanel pnlText = new javax.swing.JPanel(new java.awt.GridLayout(2, 1));
        pnlText.setOpaque(false);
        pnlText.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));

        javax.swing.JLabel lblName = new javax.swing.JLabel(
                m.getSenderName() != null ? m.getSenderName() : "User");
        lblName.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 14));
        lblName.setForeground(new java.awt.Color(45, 36, 34));

        String raw = m.getContent() != null ? m.getContent() : "";
        String preview = raw.startsWith("BOOKING_CARD::")
                ? "📋 Booking Request"
                : (raw.length() > 32 ? raw.substring(0, 32) + "…" : raw);
        javax.swing.JLabel lblPreview = new javax.swing.JLabel(preview);
        lblPreview.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 12));
        lblPreview.setForeground(new java.awt.Color(120, 100, 95));

        pnlText.add(lblName);
        pnlText.add(lblPreview);

        // Unread dot
        javax.swing.JLabel dot = new javax.swing.JLabel(!m.isRead() ? "●" : "");
        dot.setForeground(new java.awt.Color(116, 185, 255));

        row.add(avatar, java.awt.BorderLayout.WEST);
        row.add(pnlText, java.awt.BorderLayout.CENTER);
        row.add(dot, java.awt.BorderLayout.EAST);

        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                activeOtherUserId = otherId;
                activeCarId = m.getCarId();
                activeContactName = m.getSenderName() != null ? m.getSenderName() : "User";
                openThread(otherId, 0, activeContactName);
                row.repaint();
            }
        });
        return row;
    }

    public void openThread(int otherId, int carId, String contactName) {
        lblContactName.setText(contactName);
        lblRightName.setText(contactName);
        
        lblRightAvatar.setIcon(null);
        lblRightAvatar.setText("");
        lblRightAvatar.revalidate();
        lblRightAvatar.repaint();
        
        pnlMessages.removeAll();
        

        new Thread(() -> {
            try {
                int myId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();
                messageService.markThreadRead(myId, otherId);
                java.util.List<carrentalsystem.models.Message> thread = messageService.getThread(myId, otherId, 0);
                
                carrentalsystem.services.UserService userSvc = new carrentalsystem.services.UserService();
                String otherUserImagePath = userSvc.getUserById(otherId).getProfileImagePath();

                javax.swing.SwingUtilities.invokeLater(() -> {
                    if (otherUserImagePath != null && !otherUserImagePath.isEmpty()) {
                        // Use ImageUtil to create the circular crop
                        javax.swing.JLabel circleAvatar = carrentalsystem.utils.ImageUtil.cropCircle(otherUserImagePath, 80);
                        lblRightAvatar.setIcon(circleAvatar.getIcon());
                    } else {
                        lblRightAvatar.setIcon(null); // Fallback to initial letter logic in paintComponent
                    }
                    lblRightAvatar.repaint();
                    
                    pnlMessages.removeAll();
                    for (carrentalsystem.models.Message msg : thread) {
                        boolean isMine = msg.getSenderId() == myId;
                        pnlMessages.add(buildMessageBubble(msg, isMine));
                        pnlMessages.add(javax.swing.Box.createVerticalStrut(5));
                    }
                    pnlMessages.revalidate();
                    pnlMessages.repaint();
                    javax.swing.JScrollBar bar = spCenter.getVerticalScrollBar();
                    javax.swing.SwingUtilities.invokeLater(
                            () -> bar.setValue(bar.getMaximum()));
                });
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to load chat thread: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private javax.swing.JPanel buildMessageBubble(carrentalsystem.models.Message m, boolean isMine) {

        javax.swing.JPanel wrapper = new javax.swing.JPanel(new java.awt.FlowLayout(isMine ? java.awt.FlowLayout.RIGHT : java.awt.FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new java.awt.Dimension(
                Integer.MAX_VALUE, Short.MAX_VALUE));

        // Detect booking card
        if (m.getContent() != null && m.getContent().startsWith("BOOKING_CARD::")) {
            try {
                int bookingId = Integer.parseInt(m.getContent().replace("BOOKING_CARD::", "").trim());
                carrentalsystem.services.BookingService bSvc = new carrentalsystem.services.BookingService();
                carrentalsystem.models.Booking booking = bSvc.getBookingById(bookingId);
                if (booking != null) {
                    BookingCardPanel card = new BookingCardPanel(booking);
                    wrapper.add(card);
                    return wrapper;
                }
            } catch (Exception e) {e.printStackTrace(); }
        }

        // Regular bubble
        java.awt.Color bubbleColor = isMine
                ? new java.awt.Color(155, 121, 128)
                : new java.awt.Color(240, 240, 240);

        javax.swing.JLabel bubble = new javax.swing.JLabel(
                "<html><body style='width:260px;padding:4px'>"
                + m.getContent() + "</body></html>") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bubbleColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bubble.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 14));
        bubble.setForeground(isMine ? java.awt.Color.WHITE : java.awt.Color.BLACK);
        bubble.setOpaque(false);
        bubble.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        
        wrapper.add(bubble);
        return wrapper;
    }

    private void sendMessageAction() {
        String text = txtMessage.getText().trim();
        if (text.isEmpty() || text.equals("Type a message.")
                || activeOtherUserId < 0) {
            return;
        }
        txtMessage.setText("");
        int myId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();
        new Thread(() -> {
            try {
                messageService.sendMessage(myId, activeOtherUserId, activeCarId, text);
                javax.swing.SwingUtilities.invokeLater(() -> 
                openThread(activeOtherUserId, 0, activeContactName));
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

        pnlLeft = new javax.swing.JPanel();
        lblMessageHeader = new javax.swing.JLabel();
        spLeft = new javax.swing.JScrollPane();
        pnlConvList = new javax.swing.JPanel();
        pnlCenter = new javax.swing.JPanel();
        pnlContactHeader = new javax.swing.JPanel();
        lblContactName = new javax.swing.JLabel();
        spCenter = new javax.swing.JScrollPane();
        pnlMessages = new javax.swing.JPanel();
        pnlInputBar = new javax.swing.JPanel();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        pnlRight = new javax.swing.JPanel();
        lblRightAvatar = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. Draw Background Circle (matches your conversation sidebar)
                int size = Math.min(getWidth(), getHeight());
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g2.setColor(new java.awt.Color(155, 121, 128));
                g2.fillOval(x, y, size, size);

                // 2. Logic to handle Initial Letter (matches your concept)
                String nameText = lblRightName.getText();
                String initLetter = (nameText != null && !nameText.isEmpty() && !nameText.equals("name"))
                ? String.valueOf(nameText.trim().charAt(0)).toUpperCase() : "?";

                // 3. Render Image if found, otherwise render Initial
                if (getIcon() != null && getIcon().getIconWidth() > 0) {
                    java.awt.Shape clip = new java.awt.geom.Ellipse2D.Double(0, 0, getWidth(), getHeight());
                    g2.setClip(clip);
                    super.paintComponent(g2); // Paints the loaded avatar image
                } else {
                    // Placeholder fallback (matches your sidebar rendering style)
                    g2.setColor(java.awt.Color.WHITE);
                    g2.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 32)); // Larger font for right panel
                    java.awt.FontMetrics fm = g2.getFontMetrics();
                    int txtX = (getWidth() - fm.stringWidth(initLetter)) / 2;
                    int txtY = (getHeight() + fm.getAscent()) / 2 - 4;
                    g2.drawString(initLetter, txtX, txtY);
                }
                g2.dispose();
            }
        };
        lblRightName = new javax.swing.JLabel();
        sprRight = new javax.swing.JSeparator();
        fillerRight = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1100, 700));
        setPreferredSize(new java.awt.Dimension(1100, 700));
        setLayout(new java.awt.BorderLayout());

        pnlLeft.setBackground(new java.awt.Color(236, 236, 240));
        pnlLeft.setPreferredSize(new java.awt.Dimension(300, 700));
        pnlLeft.setLayout(new java.awt.BorderLayout());

        lblMessageHeader.setFont(new java.awt.Font("Helvetica Neue", 1, 20)); // NOI18N
        lblMessageHeader.setText("Messages");
        lblMessageHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 16, 1, 1));
        lblMessageHeader.setPreferredSize(new java.awt.Dimension(300, 60));
        pnlLeft.add(lblMessageHeader, java.awt.BorderLayout.NORTH);

        spLeft.setBorder(null);
        spLeft.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spLeft.setOpaque(false);

        pnlConvList.setLayout(new javax.swing.BoxLayout(pnlConvList, javax.swing.BoxLayout.Y_AXIS));
        spLeft.setViewportView(pnlConvList);

        pnlLeft.add(spLeft, java.awt.BorderLayout.CENTER);

        add(pnlLeft, java.awt.BorderLayout.WEST);

        pnlCenter.setBackground(new java.awt.Color(255, 255, 255));
        pnlCenter.setLayout(new java.awt.BorderLayout());

        pnlContactHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnlContactHeader.setPreferredSize(new java.awt.Dimension(100, 64));
        pnlContactHeader.setLayout(new java.awt.BorderLayout());

        lblContactName.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        lblContactName.setText("Select a conversation");
        lblContactName.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        pnlContactHeader.add(lblContactName, java.awt.BorderLayout.CENTER);

        pnlCenter.add(pnlContactHeader, java.awt.BorderLayout.NORTH);

        spCenter.setBackground(new java.awt.Color(255, 255, 255));
        spCenter.setBorder(null);
        spCenter.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spCenter.setOpaque(false);

        pnlMessages.setBackground(new java.awt.Color(255, 255, 255));
        pnlMessages.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        pnlMessages.setLayout(new javax.swing.BoxLayout(pnlMessages, javax.swing.BoxLayout.Y_AXIS));
        spCenter.setViewportView(pnlMessages);

        pnlCenter.add(spCenter, java.awt.BorderLayout.CENTER);

        pnlInputBar.setBackground(new java.awt.Color(255, 255, 255));
        pnlInputBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));
        pnlInputBar.setPreferredSize(new java.awt.Dimension(100, 68));
        pnlInputBar.setLayout(new java.awt.BorderLayout(8, 0));

        txtMessage.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        txtMessage.setText("Type a message.");
        txtMessage.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));
        txtMessage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMessageFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMessageFocusLost(evt);
            }
        });
        pnlInputBar.add(txtMessage, java.awt.BorderLayout.CENTER);

        btnSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/SendIcon.png"))); // NOI18N
        btnSend.setBorderPainted(false);
        btnSend.setContentAreaFilled(false);
        btnSend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSend.addActionListener(this::btnSendActionPerformed);
        pnlInputBar.add(btnSend, java.awt.BorderLayout.EAST);

        pnlCenter.add(pnlInputBar, java.awt.BorderLayout.SOUTH);

        add(pnlCenter, java.awt.BorderLayout.CENTER);

        pnlRight.setBackground(new java.awt.Color(236, 236, 240));
        pnlRight.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 20, 20, 20));
        pnlRight.setPreferredSize(new java.awt.Dimension(240, 700));
        pnlRight.setLayout(new javax.swing.BoxLayout(pnlRight, javax.swing.BoxLayout.Y_AXIS));

        lblRightAvatar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRightAvatar.setAlignmentX(0.5F);
        lblRightAvatar.setMaximumSize(new java.awt.Dimension(80, 80));
        lblRightAvatar.setMinimumSize(new java.awt.Dimension(80, 80));
        lblRightAvatar.setPreferredSize(new java.awt.Dimension(80, 80));
        pnlRight.add(lblRightAvatar);

        lblRightName.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        lblRightName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRightName.setText("name");
        lblRightName.setAlignmentX(0.5F);
        lblRightName.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 4, 0));
        lblRightName.setMaximumSize(new java.awt.Dimension(200, 40));
        pnlRight.add(lblRightName);

        sprRight.setMaximumSize(new java.awt.Dimension(200, 1));
        pnlRight.add(sprRight);
        pnlRight.add(fillerRight);

        add(pnlRight, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        sendMessageAction();
    }//GEN-LAST:event_btnSendActionPerformed

    private void txtMessageFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMessageFocusGained
        // TODO add your handling code here:
        if (txtMessage.getText().equals("Type a message.")) {
            txtMessage.setText("");
            txtMessage.setForeground(java.awt.Color.BLACK); // Change to active text color
        }
    }//GEN-LAST:event_txtMessageFocusGained

    private void txtMessageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMessageFocusLost
        // TODO add your handling code here:
        if (txtMessage.getText().isEmpty()) {
            txtMessage.setForeground(new java.awt.Color(120, 100, 95)); // Matches your muted gray[cite: 10]
            txtMessage.setText("Type a message.");
        }
    }//GEN-LAST:event_txtMessageFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.Box.Filler fillerRight;
    private javax.swing.JLabel lblContactName;
    private javax.swing.JLabel lblMessageHeader;
    private javax.swing.JLabel lblRightAvatar;
    private javax.swing.JLabel lblRightName;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlContactHeader;
    private javax.swing.JPanel pnlConvList;
    private javax.swing.JPanel pnlInputBar;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlMessages;
    private javax.swing.JPanel pnlRight;
    private javax.swing.JScrollPane spCenter;
    private javax.swing.JScrollPane spLeft;
    private javax.swing.JSeparator sprRight;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
