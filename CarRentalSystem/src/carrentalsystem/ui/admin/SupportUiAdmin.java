/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.admin;
import carrentalsystem.interfaces.IAdminService;
import carrentalsystem.services.AdminService;
import carrentalsystem.models.Ticket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author User
 */
public class SupportUiAdmin extends javax.swing.JPanel {
    
    private final IAdminService adminService = new AdminService();
    private List<Ticket> ticketList;
    
    public SupportUiAdmin() {
        initComponents();
        setupTableStyles(); // Your existing styling logic
        loadTicketData();
    }
    
    private void setupTableStyles() {
        java.awt.Color themeColor = new java.awt.Color(48, 48, 46);
        this.setBackground(themeColor);
        HeaderPanel.setBackground(themeColor);
        jScrollPane1.setBackground(themeColor);
        jScrollPane1.getViewport().setBackground(themeColor);
        TableContent.setBackground(themeColor);
        TableContent.setForeground(java.awt.Color.WHITE);
        TableContent.setRowHeight(60);
    }

    public void loadTicketData() {
        try {
            DefaultTableModel model = (DefaultTableModel) TableContent.getModel();
            model.setRowCount(0);

            // Updated to use the correct service method for full user info[cite: 14]
            ticketList = adminService.getAllTicketsWithUserInfo();

            // Update the badge label[cite: 26, 31]
            jLabel3.setText(ticketList.size() + " Open");

            for (Ticket t : ticketList) {
                model.addRow(new Object[]{
                    t.getUserFullName(), // User[cite: 14]
                    t.getSubject(), // Issue
                    t.getCreatedAt(), // Submitted
                    t.getStatus(), // Status
                    "MANAGE" // Action Column
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add MouseListener logic if you haven't yet to trigger this method
    private void handleTicketAction(int row) {
        // 1. Get the Ticket object from our list
        carrentalsystem.models.Ticket ticket = ticketList.get(row);

        // 2. Create a custom panel for the dialog
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(450, 300));

        // 3. Display User Details (Original Input)
        String detailText = "User: " + ticket.getUserFullName() + "\n"
                + "Subject: " + ticket.getSubject() + "\n"
                + "Submitted: " + ticket.getCreatedAt() + "\n\n"
                + "--- USER DESCRIPTION ---\n"
                + ticket.getDescription();

        JTextArea txtDetails = new JTextArea(detailText);
        txtDetails.setEditable(false);
        txtDetails.setLineWrap(true);
        txtDetails.setWrapStyleWord(true);
        txtDetails.setBackground(new Color(240, 240, 240));

        // 4. Input area for Admin Reply
        JTextArea txtReply = new JTextArea();
        txtReply.setLineWrap(true);
        txtReply.setWrapStyleWord(true);
        txtReply.setBorder(BorderFactory.createTitledBorder("Admin Response / Reply"));

        // 5. Layout assembly
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(txtDetails), new JScrollPane(txtReply));
        splitPane.setDividerLocation(150);
        panel.add(splitPane, BorderLayout.CENTER);

        // 6. Show the Dialog with custom buttons
        String[] options = {"Send Reply & Resolve", "Close Ticket (No Reply)", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, panel,
                "Manage Ticket #" + ticket.getTicketId(),
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        // 7. Execute Service Logic
        try {
            if (choice == 0) { // Send Reply & Resolve
                String reply = txtReply.getText().trim();
                if (reply.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a response before resolving.");
                    return;
                }
                adminService.replyToTicket(ticket.getTicketId(), reply); // Sets status to RESOLVED
                JOptionPane.showMessageDialog(this, "Response sent and ticket resolved.");
            } else if (choice == 1) { // Close Ticket (No Reply)
                adminService.closeTicket(ticket.getTicketId()); // Sets status to RESOLVED[cite: 13]
                JOptionPane.showMessageDialog(this, "Ticket closed without reply.");
            }

            loadTicketData(); // Refresh the table

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating ticket: " + e.getMessage());
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

        HeaderPanel = new javax.swing.JPanel();
        SupportTickets = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                // The value 30 creates the perfect 'pill' arc
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        jScrollPane1 = new javax.swing.JScrollPane();
        TableContent = new javax.swing.JTable();

        setBackground(new java.awt.Color(43, 43, 43));
        setPreferredSize(new java.awt.Dimension(1000, 570));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HeaderPanel.setBackground(new java.awt.Color(43, 43, 43));
        HeaderPanel.setMaximumSize(new java.awt.Dimension(32767, 80));
        HeaderPanel.setMinimumSize(new java.awt.Dimension(384, 43));
        HeaderPanel.setPreferredSize(new java.awt.Dimension(900, 80));
        HeaderPanel.setLayout(new java.awt.GridBagLayout());

        SupportTickets.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        SupportTickets.setForeground(new java.awt.Color(255, 255, 255));
        SupportTickets.setText("Support Tickets");
        SupportTickets.setPreferredSize(new java.awt.Dimension(262, 43));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        HeaderPanel.add(SupportTickets, gridBagConstraints);

        jLabel3.setBackground(new java.awt.Color(229, 192, 123));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("jLabel3");
        jLabel3.setPreferredSize(new java.awt.Dimension(85, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        HeaderPanel.add(jLabel3, gridBagConstraints);

        add(HeaderPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 100));

        jScrollPane1.setBackground(new java.awt.Color(48, 48, 46));
        jScrollPane1.setForeground(new java.awt.Color(43, 43, 43));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 500));

        TableContent.setBackground(new java.awt.Color(43, 43, 43));
        TableContent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User", "Issue", "Priority", "Submitted", "Action"
            }
        ));
        TableContent.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = TableContent.rowAtPoint(evt.getPoint());
                int col = TableContent.columnAtPoint(evt.getPoint());
                // Check if the "Action" column (Index 4) was clicked
                if (col == 4 && row != -1) {
                    handleTicketAction(row);
                }
            }
        });
        jScrollPane1.setViewportView(TableContent);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 950, 460));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JLabel SupportTickets;
    private javax.swing.JTable TableContent;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
