/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.ui.user;
import carrentalsystem.models.Booking;
import carrentalsystem.services.BookingService;
import carrentalsystem.core.SessionManager;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author macbookairm1grey
 */
public class BookingCardPanel extends JPanel{

        private final Booking booking;
        private final boolean isOwner;
        private JLabel lblStatus;

        public BookingCardPanel(Booking booking) {
            this.booking = booking;
            int currentUserId = SessionManager.getCurrentUser().getUserId();
            // Owner = the person who owns the car (not the renter)
            this.isOwner = (booking.getOwnerId() == currentUserId);

            setLayout(new BorderLayout(0, 0));
            setOpaque(false);
            setMaximumSize(new Dimension(450, 200));
            setPreferredSize(new Dimension(450, 200));

            buildCard();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // Taupe card background — matches your reference image
            g2.setColor(new Color(218, 208, 204));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            // Subtle border
            g2.setColor(new Color(180, 165, 160));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
        }

        private void buildCard() {
            // ── Header bar ──────────────────────────────────────
            JPanel pnlHeader = new JPanel(new BorderLayout());
            pnlHeader.setOpaque(false);
            pnlHeader.setBorder(BorderFactory.createEmptyBorder(14, 16, 8, 16));

            JLabel lblTitle = new JLabel("Car Rental Booking Form");
            lblTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
            lblTitle.setForeground(new Color(45, 36, 34));

            JLabel lblAuto = new JLabel("(auto-submitted)");
            lblAuto.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
            lblAuto.setForeground(new Color(130, 110, 105));

            pnlHeader.add(lblTitle, BorderLayout.WEST);
            pnlHeader.add(lblAuto, BorderLayout.EAST);

            // ── Details grid ────────────────────────────────────
            JPanel pnlDetails = new JPanel(new GridLayout(5, 2, 15, 10));
            pnlDetails.setOpaque(false);
            pnlDetails.setBorder(BorderFactory.createEmptyBorder(5, 25, 10, 25));

            addRow(pnlDetails, "Car:", booking.getCarBrand() + " " + booking.getCarModel());
            String pLoc = (booking.getPickupLocation() != null) ? booking.getPickupLocation() : "TBD";
            String pDate = (booking.getStartDate() != null) ? booking.getStartDate().toString() : "";
            addRow(pnlDetails, "Pick-up:", pLoc + (!pDate.isEmpty() ? "  |  " + pDate : ""));
            
            String dLoc = (booking.getReturnLocation() != null) ? booking.getReturnLocation() : "TBD";
            String dDate = (booking.getEndDate() != null) ? booking.getEndDate().toString() : "";
            addRow(pnlDetails, "Drop-off:", dLoc + (!dDate.isEmpty() ? "  |  " + dDate : ""));
            
            addRow(pnlDetails, "Total Price:", "P " + String.format("%,.0f", booking.getTotalPrice()));

            // Status row
            JLabel lblStatusKey = makeKey("Status:");
            lblStatus = makeStatusBadge(booking.getStatus());
            pnlDetails.add(lblStatusKey);
            pnlDetails.add(lblStatus);
            
            // Panel Height
            this.setPreferredSize(new Dimension(450, 260));
            this.setMaximumSize(new Dimension(450, 260));

            // ── Action buttons ──────────────────────────────────
            JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
            pnlActions.setOpaque(false);

            buildActionButtons(pnlActions);

            add(pnlHeader, BorderLayout.NORTH);
            add(pnlDetails, BorderLayout.CENTER);
            add(pnlActions, BorderLayout.SOUTH);
        }

        private void buildActionButtons(JPanel pnlActions) {
            String status = booking.getStatus() != null
                    ? booking.getStatus().toUpperCase() : "PENDING";

            if (isOwner) {
                // ── OWNER VIEW ──────────────────────────────────
                if ("PENDING".equals(status)) {
                    JButton btnApprove = makeButton("Approve", new Color(45, 36, 34));
                    JButton btnReject = makeButton("Reject", new Color(180, 80, 80));

                    btnApprove.addActionListener(e -> {
                        updateBookingStatus("CONFIRMED");
                        refreshCard("CONFIRMED");
                    });
                    btnReject.addActionListener(e -> {
                        updateBookingStatus("REJECTED");
                        refreshCard("REJECTED");
                    });

                    pnlActions.add(btnApprove);
                    pnlActions.add(btnReject);

                } else if ("CONFIRMED".equals(status)) {
                    JButton btnPaid = makeButton("Paid", new Color(60, 130, 80));
                    JButton btnNotPaid = makeButton("Not Paid", new Color(180, 80, 80));

                    btnPaid.addActionListener(e -> {
                        updateBookingStatus("SUCCESSFUL");
                        refreshCard("SUCCESSFUL");
                    });
                    btnNotPaid.addActionListener(e -> {
                        updateBookingStatus("CANCELLED");
                        refreshCard("CANCELLED");
                    });

                    pnlActions.add(btnPaid);
                    pnlActions.add(btnNotPaid);
                }

            } else {
                // ── RENTER VIEW ─────────────────────────────────
                // Renter only sees the status badge — no buttons
                // The badge in the details grid already shows it
                JLabel lblInfo = new JLabel(getRenterStatusMessage(status));
                lblInfo.setFont(new Font("Helvetica Neue", Font.ITALIC, 12));
                lblInfo.setForeground(new Color(100, 80, 75));
                pnlActions.add(lblInfo);
            }
        }

        private String getRenterStatusMessage(String status) {
            switch (status) {
                case "PENDING":
                    return "Waiting for owner approval...";
                case "CONFIRMED":
                    return "Approved — please prepare payment.";
                case "SUCCESSFUL":
                    return "Payment confirmed. Enjoy your trip!";
                case "REJECTED":
                    return "This booking was declined.";
                case "CANCELLED":
                    return "This booking was cancelled.";
                default:
                    return "";
            }
        }

        private void updateBookingStatus(String newStatus) {
            new Thread(() -> {
                try {
                    BookingService svc = new BookingService();
                    svc.updateStatus(booking.getBookingId(), newStatus);
                    booking.setStatus(newStatus);

                    // ── Send notification based on new status ──────────
                    carrentalsystem.services.NotificationService notifSvc
                            = new carrentalsystem.services.NotificationService();
                    String carName = booking.getCarBrand() + " " + booking.getCarModel();
                    int renterId = booking.getRenterId();
                    int ownerId = booking.getOwnerId();

                    switch (newStatus) {
                        case "CONFIRMED":
                            // Tell renter: approved
                            notifSvc.notify(renterId,
                                    "Your booking for " + carName + " was approved! Please prepare payment.",
                                    "RENTAL");
                            break;
                        case "REJECTED":
                            // Tell renter: rejected
                            notifSvc.notify(renterId,
                                    "Your booking for " + carName + " was declined by the owner.",
                                    "RENTAL");
                            break;
                        case "SUCCESSFUL":
                            // Tell renter: payment confirmed
                            notifSvc.notify(renterId,
                                    "Payment confirmed for " + carName + ". Enjoy your trip! Check your receipt.",
                                    "RENTAL");
                            break;
                        case "CANCELLED":
                            // Tell renter: not paid / cancelled
                            notifSvc.notify(renterId,
                                    "Your booking for " + carName + " was cancelled due to non-payment.",
                                    "RENTAL");
                            // Also tell owner
                            notifSvc.notify(ownerId,
                                    "Booking for " + carName + " marked as not paid and cancelled.",
                                    "RENTAL");
                            break;
                    }

                } catch (Exception e) {
                    javax.swing.SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()));
                }
            }).start();
        }

        private void refreshCard(String newStatus) {
            // Rebuild action buttons and update status badge
            lblStatus.setText(newStatus);
            lblStatus.setBackground(getStatusColor(newStatus));
            // Remove south panel and rebuild
            SwingUtilities.invokeLater(() -> {
                // Find and replace the SOUTH panel
                for (Component c : getComponents()) {
                    if (c instanceof JPanel) {
                        JPanel p = (JPanel) c;
                        if (p.getLayout() instanceof FlowLayout) {
                            p.removeAll();
                            buildActionButtons(p);
                            p.revalidate();
                            p.repaint();
                            break;
                        }
                    }
                }
            });
        }

        // ── Helpers ──────────────────────────────────────────────
        private void addRow(JPanel grid, String key, String value) {
            grid.add(makeKey(key));
            grid.add(makeValue(value != null ? value : "—"));
        }

        private JLabel makeKey(String text) {
            JLabel l = new JLabel(text);
            l.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
            l.setForeground(new Color(80, 65, 60));
            return l;
        }

        private JLabel makeValue(String text) {
            JLabel l = new JLabel("<html>" + text + "</html>");
            l.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
            l.setForeground(new Color(45, 36, 34));
            return l;
        }

        private JLabel makeStatusBadge(String status) {
            String s = status != null ? status.toUpperCase() : "PENDING";
            JLabel l = new JLabel(s) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            l.setFont(new Font("Helvetica Neue", Font.BOLD, 11));
            l.setForeground(Color.WHITE);
            l.setOpaque(false);
            l.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            l.setBackground(getStatusColor(s));
            return l;
        }

        private Color getStatusColor(String status) {
            switch (status) {
                case "CONFIRMED":
                    return new Color(60, 130, 80);
                case "SUCCESSFUL":
                    return new Color(40, 100, 60);
                case "REJECTED":
                case "CANCELLED":
                    return new Color(160, 60, 60);
                default:
                    return new Color(200, 150, 60); // PENDING orange
            }
        }

        private JButton makeButton(String text, Color bg) {
            JButton btn = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover()
                            ? bg.brighter() : bg);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(getText(),
                            (getWidth() - fm.stringWidth(getText())) / 2,
                            (getHeight() + fm.getAscent()) / 2 - 3);
                    g2.dispose();
                }

                @Override
                public void setBorder(javax.swing.border.Border b) {
                }
            };
            btn.setPreferredSize(new Dimension(110, 36));
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return btn;
        }
    
}
