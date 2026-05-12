/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.ui.user;
import carrentalsystem.models.Booking;
import carrentalsystem.services.BookingService;
import carrentalsystem.core.SessionManager;
import carrentalsystem.models.Payment;
import carrentalsystem.services.PaymentService;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException; // Real SQL Exception
import java.text.SimpleDateFormat;

/**
 *
 * @author macbookairm1grey
 */
public class BookingCardPanel extends JPanel{

    private final Booking booking;
    private final boolean isOwner;
    private JLabel lblStatus;
    private final BookingService bookingService = new BookingService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

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
        removeAll();

        // ── Header ──
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 16, 4, 16));
        JLabel lblTitle = new JLabel("Car Rental Booking Form");
        lblTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        // ── Details ──
        // Using 7 rows to display Car, Pickup (Date+Loc), Return (Date+Loc), Price, and Status
        JPanel pnlDetails = new JPanel(new GridLayout(7, 2, 10, 6));
        pnlDetails.setOpaque(false);
        pnlDetails.setBorder(BorderFactory.createEmptyBorder(5, 25, 10, 25));

        addRow(pnlDetails, "Car:", booking.getCarBrand() + " " + booking.getCarModel());

        // Pick-up Info
        String pickupStr = (booking.getStartDate() != null ? dateFormat.format(booking.getStartDate()) : "TBD");
        addRow(pnlDetails, "Pick-up Date:", pickupStr);
        addRow(pnlDetails, "Pick-up Loc:", (booking.getPickupLocation() != null ? booking.getPickupLocation() : "TBD"));

        // Return Info
        String returnStr = (booking.getEndDate() != null ? dateFormat.format(booking.getEndDate()) : "TBD");
        addRow(pnlDetails, "Return Date:", returnStr);
        addRow(pnlDetails, "Return Loc:", (booking.getReturnLocation() != null ? booking.getReturnLocation() : "TBD"));

        addRow(pnlDetails, "Total Price:", "P " + String.format("%,.0f", booking.getTotalPrice()));

        pnlDetails.add(makeKey("Status:"));
        lblStatus = makeStatusBadge(booking.getStatus());
        pnlDetails.add(lblStatus);

        // ── Actions ──
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        pnlActions.setOpaque(false);
        buildActionButtons(pnlActions);

        add(pnlHeader, BorderLayout.NORTH);
        add(pnlDetails, BorderLayout.CENTER);
        add(pnlActions, BorderLayout.SOUTH);
    }

    private void buildActionButtons(JPanel pnlActions) {
        String status = booking.getStatus() != null ? booking.getStatus().toUpperCase() : "PENDING";

        if (isOwner) {
            if ("PENDING".equals(status)) {
                JButton btnVerify = makeButton("Verification", new Color(70, 130, 180));
                btnVerify.addActionListener(e -> {
                    Window win = SwingUtilities.getWindowAncestor(this);
                    if (win instanceof MainDashboard) {
                        ((MainDashboard) win).getInboxPanel().showRenterVerification(booking.getBookingId());
                    }
                });
                pnlActions.add(btnVerify);

                JButton btnApprove = makeButton("Approve", new Color(45, 36, 34));
                btnApprove.addActionListener(e -> updateBookingStatus("CONFIRMED"));
                pnlActions.add(btnApprove);

                JButton btnReject = makeButton("Reject", new Color(180, 80, 80));
                btnReject.addActionListener(e -> updateBookingStatus("REJECTED"));
                pnlActions.add(btnReject);
            }
        } else {
            if ("CONFIRMED".equals(status)) {
                JButton btnPay = makeButton("PAY NOW", new Color(60, 130, 80));
                btnPay.addActionListener(e -> handlePaymentFlow());
                pnlActions.add(btnPay);
            } else if ("SUCCESSFUL".equals(status)) {
                JButton btnReview = makeButton("Review", new Color(155, 121, 128));
                btnReview.addActionListener(e -> {
                    Window win = SwingUtilities.getWindowAncestor(this);
                    if (win instanceof MainDashboard) {
                        ((MainDashboard) win).getInboxPanel().triggerReview(booking);
                    }
                });
                pnlActions.add(btnReview);
            } else {
                pnlActions.add(new JLabel("<html><i>" + getRenterStatusMessage(status) + "</i></html>"));
            }
        }
    }

    private String getRenterStatusMessage(String status) {
        switch (status) {
            case "PENDING":
                return "Waiting for owner approval...";
            case "CONFIRMED":
                return "Approved — please complete payment.";
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

    private void handlePaymentFlow() {
        String[] methods = {"GCash", "Bank Transfer", "Cash on Pickup"};
        int choice = JOptionPane.showOptionDialog(this, "Select Payment Method", "Payment Gateway",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, methods, methods[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        String method = methods[choice];
        String ref = "N/A";

        if (choice < 2) {
            ref = JOptionPane.showInputDialog(this, "Enter " + method + " Reference Number:", "Transaction Reference", JOptionPane.PLAIN_MESSAGE);
            if (ref == null || ref.trim().isEmpty()) {
                return;
            }
        }

        Payment p = new Payment();
        p.setBookingId(booking.getBookingId());
        p.setRenterId(booking.getRenterId());
        p.setAmountPaid(booking.getTotalPrice());
        p.setTotalAmount(booking.getTotalPrice());
        p.setReferenceNumber(ref);
        p.setPaymentMethod(method.toUpperCase().replace(" ", "_"));

        try {
            new PaymentService().recordPayment(p);
        } catch (java.sql.SQLException ex) {
            System.getLogger(BookingCardPanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        JOptionPane.showMessageDialog(this, choice == 2 ? "Cash intent saved! Pay owner at pickup." : "Payment Recorded!");
        updateBookingStatus("SUCCESSFUL");
    }

    private void updateBookingStatus(String newStatus) {
        new Thread(() -> {
            try {
                bookingService.updateStatus(booking.getBookingId(), newStatus);
                booking.setStatus(newStatus);

                carrentalsystem.services.NotificationService notifSvc = new carrentalsystem.services.NotificationService();
                String carName = booking.getCarBrand() + " " + booking.getCarModel();

                if ("CONFIRMED".equals(newStatus)) {
                    notifSvc.notify(booking.getRenterId(), "Booking approved for " + carName + "! Please pay.", "RENTAL");
                } else if ("SUCCESSFUL".equals(newStatus)) {
                    notifSvc.notify(booking.getRenterId(), "Payment successful for " + carName + "!", "RENTAL");
                }

                refreshCard(newStatus);
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()));
            }
        }).start();
    }

    private void refreshCard(String newStatus) {
        SwingUtilities.invokeLater(() -> {
            lblStatus.setText(newStatus);
            lblStatus.setBackground(getStatusColor(newStatus));
            buildCard();
            revalidate();
            repaint();
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

    private static class SQLException {

        public SQLException() {
        }
    }
    
}
