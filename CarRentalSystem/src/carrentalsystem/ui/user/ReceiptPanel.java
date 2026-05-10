/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.ui.user;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author macbookairm1grey
 */
public class ReceiptPanel extends JDialog{
    private final int bookingId;
    private carrentalsystem.models.Booking booking;
    private carrentalsystem.models.Payment payment;

    // ── Colors matching the reference image ──────────────────────────────
    private static final Color BG = new Color(240, 234, 229);
    private static final Color DARK = new Color(45, 36, 34);
    private static final Color SECTION_BG = new Color(248, 244, 241);
    private static final Color HEADER_BG = new Color(45, 36, 34);
    private static final Color GOLD = new Color(180, 150, 120);
    private static final Color LIGHT_LINE = new Color(210, 200, 195);

    public ReceiptPanel(Frame parent, int bookingId) {
        super(parent, "Receipt — RentACar", true);
        this.bookingId = bookingId;
        loadData();
        buildUI();
        setSize(1000, 820);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    // ── Static factory ────────────────────────────────────────────────────
    public static void show(Component parent, int bookingId) {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(parent);
        new ReceiptPanel(frame, bookingId).setVisible(true);
    }

    // ── Load booking + payment from DB ────────────────────────────────────
    private void loadData() {
        try {
            carrentalsystem.services.BookingService bs
                    = new carrentalsystem.services.BookingService();
            booking = bs.getBookingById(bookingId);

            carrentalsystem.services.PaymentService ps
                    = new carrentalsystem.services.PaymentService();
            payment = ps.getPaymentByBookingId(bookingId);
        } catch (Exception e) {
            System.err.println("[ReceiptPanel] Load error: " + e.getMessage());
        }
    }

    // ── Build the UI ──────────────────────────────────────────────────────
    private void buildUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(buildReceiptContent());
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Print / Close buttons
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        btnBar.setBackground(DARK);

        JButton btnClose = makeButton("Close", new Color(100, 90, 85));
        btnClose.addActionListener(e -> dispose());
        btnBar.add(btnClose);

        add(btnBar, BorderLayout.SOUTH);
    }

    private JPanel buildReceiptContent() {
        JPanel root = new JPanel();
        root.setBackground(BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        root.add(buildTopHeader());
        root.add(Box.createVerticalStrut(16));
        root.add(buildInfoRow());
        root.add(Box.createVerticalStrut(16));
        root.add(buildBottomRow());
        root.add(Box.createVerticalStrut(16));
        root.add(buildFooter());

        return root;
    }

    // ── TOP HEADER: Logo | RECEIPT | Receipt# | Car image ────────────────
    private JPanel buildTopHeader() {
        JPanel p = new JPanel(new BorderLayout(20, 0));
        p.setBackground(BG);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        // Left: company info
        JPanel left = new JPanel();
        left.setBackground(BG);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("YOUR RENT A CAR");
        name.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        name.setForeground(DARK);

        JLabel tagline = new JLabel("Drive Your Journey");
        tagline.setFont(new Font("Helvetica Neue", Font.ITALIC, 12));
        tagline.setForeground(GOLD);

        JLabel addr = new JLabel("📍  123 Car Street, Drive City, DC 10001");
        addr.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
        JLabel phone = new JLabel("📞  +63 912 345 6789");
        phone.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
        JLabel email = new JLabel("✉  yourrentacar@email.com");
        email.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));

        left.add(name);
        left.add(tagline);
        left.add(Box.createVerticalStrut(8));
        left.add(addr);
        left.add(phone);
        left.add(email);

        // Center: RECEIPT title + dates
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(2, 4, 2, 4);

        JLabel rcptTitle = new JLabel("RECEIPT");
        rcptTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 36));
        rcptTitle.setForeground(DARK);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        center.add(rcptTitle, gc);

        // Receipt number badge
        String rcNum = generateReceiptNumber();
        JLabel rcBadge = new JLabel(" " + rcNum + " ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        rcBadge.setForeground(Color.WHITE);
        rcBadge.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        rcBadge.setOpaque(false);
        rcBadge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        gc.gridy = 1;
        gc.gridwidth = 2;
        center.add(rcBadge, gc);

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");
        String today = df.format(new Date());
        String now = tf.format(new Date());

        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.WEST;
        int row = 2;
        addLabelPair(center, gc, row++, "Date Issued", today);
        addLabelPair(center, gc, row++, "Time Issued", now);
        addLabelPair(center, gc, row, "Payment Date", today);

        p.add(left, BorderLayout.WEST);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // ── INFO ROW: Customer | Vehicle | Driver | Rental ───────────────────
    private JPanel buildInfoRow() {
        JPanel p = new JPanel(new GridLayout(1, 4, 10, 0));
        p.setBackground(BG);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));

        p.add(buildCustomerSection());
        p.add(buildVehicleSection());
        p.add(buildDriverSection());
        p.add(buildRentalDetailsSection());
        return p;
    }

    private JPanel buildCustomerSection() {
        String name = booking != null ? booking.getRenterName() : "—";
        String phone = "—";
        try {
            carrentalsystem.models.User u = new carrentalsystem.services.UserService()
                    .getUserById(booking != null ? booking.getRenterId() : 0);
            if (u != null) {
                phone = u.getPhoneNumber();
            }
        } catch (Exception ignored) {
        }

        JPanel s = sectionPanel("👤  CUSTOMER INFORMATION");
        addInfoRow(s, "Name", name);
        addInfoRow(s, "Contact No.", phone);
        addInfoRow(s, "Valid ID", "Driver's License");
        return s;
    }

    private JPanel buildVehicleSection() {
        String brand = "—", model = "—", plate = "—", color = "—",
                type = "—", trans = "—", fuel = "—", cond = "—";
        if (booking != null) {
            brand = booking.getCarBrand();
            model = booking.getCarModel();
        }
        try {
            carrentalsystem.models.Car car = new carrentalsystem.services.CarService()
                    .getCarById(booking != null ? booking.getCarId() : 0);
            if (car != null) {
                plate = nvl(car.getPlateNumber());
                color = nvl(car.getColor());
                type = nvl(car.getType());
                trans = nvl(car.getTransmission());
                fuel = nvl(car.getFuelType());
                cond = nvl(car.getCondition());
            }
        } catch (Exception ignored) {
        }

        JPanel s = sectionPanel("🚗  VEHICLE INFORMATION");
        addInfoRow(s, "Make & Model", brand + " " + model);
        addInfoRow(s, "Plate Number", plate);
        addInfoRow(s, "Color", color);
        addInfoRow(s, "Vehicle Type", type);
        addInfoRow(s, "Transmission", trans);
        addInfoRow(s, "Fuel Type", fuel);
        addInfoRow(s, "Condition", cond);
        return s;
    }

    private JPanel buildDriverSection() {
        JPanel s = sectionPanel("🚘  DRIVER ARRANGEMENT");

        boolean hasDriver = false;
        try {
            carrentalsystem.models.Car car = new carrentalsystem.services.CarService()
                    .getCarById(booking != null ? booking.getCarId() : 0);
            if (car != null) {
                hasDriver = car.isHasDriver();
            }
        } catch (Exception ignored) {
        }

        JLabel badge = new JLabel(hasDriver
                ? "✔  WITH DRIVER (Provided by Car Owner)"
                : "✘  SELF-DRIVE (No driver provided)");
        badge.setForeground(hasDriver ? new Color(0, 120, 50) : DARK);
        badge.setFont(new Font("Helvetica Neue", Font.BOLD, 11));
        s.add(badge);
        s.add(Box.createVerticalStrut(8));

        if (hasDriver) {
            addInfoRow(s, "Note", "Driver included per owner's terms.");
        } else {
            addInfoRow(s, "Note", "Renter is responsible for driving.");
        }
        return s;
    }

    private JPanel buildRentalDetailsSection() {
        String start = "—", end = "—", pickup = "—", drop = "—";
        int days = 1;
        if (booking != null) {
            if (booking.getStartDate() != null) {
                start = booking.getStartDate().toString();
            }
            if (booking.getEndDate() != null) {
                end = booking.getEndDate().toString();
            }
            pickup = nvl(booking.getPickupLocation());
            drop = nvl(booking.getReturnLocation());
            days = booking.getDaysCount() > 0 ? booking.getDaysCount() : 1;
        }

        JPanel s = sectionPanel("📅  RENTAL DETAILS");
        addInfoRow(s, "Start Date", start);
        addInfoRow(s, "End Date", end);
        addInfoRow(s, "Total Duration", days + " Day" + (days > 1 ? "s" : ""));
        addInfoRow(s, "Pickup Location", pickup);
        addInfoRow(s, "Drop-off Location", drop);
        return s;
    }

    // ── BOTTOM ROW: Payment Breakdown | Payment Details | Notes ──────────
    private JPanel buildBottomRow() {
        JPanel p = new JPanel(new GridLayout(1, 3, 10, 0));
        p.setBackground(BG);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        p.add(buildPaymentBreakdown());
        p.add(buildPaymentDetails());
        p.add(buildNotes());
        return p;
    }

    private JPanel buildPaymentBreakdown() {
        double basePerDay = booking != null ? booking.getTotalPrice() / Math.max(1, booking.getDaysCount()) : 0;
        int days = booking != null ? Math.max(1, booking.getDaysCount()) : 1;
        double subtotal = basePerDay * days;
        double driverFee = payment != null ? payment.getDriverFee() : 0;
        double fuelCharge = payment != null ? payment.getFuelCharge() : 0;
        double insuranceFee = payment != null ? payment.getInsuranceFee() : 0;
        double otherCharge = payment != null ? payment.getOtherCharges() : 0;
        double discount = payment != null ? payment.getDiscountAmount() : 0;
        double total = payment != null ? payment.getTotalAmount()
                : (subtotal + driverFee + fuelCharge + insuranceFee);

        JPanel s = sectionPanel("📋  PAYMENT BREAKDOWN");

        // Rental charges sub-table
        JPanel rentTable = new JPanel(new GridLayout(0, 2, 4, 2));
        rentTable.setBackground(DARK);
        rentTable.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        addTableRow(rentTable, "RENTAL CHARGES", "", Color.WHITE, Color.WHITE, Font.BOLD);
        addTableRow(rentTable, "Rate (Per Day)", "₱" + fmt(basePerDay), Color.WHITE, new Color(200, 200, 200), Font.PLAIN);
        addTableRow(rentTable, "No. of Days", String.valueOf(days), Color.WHITE, new Color(200, 200, 200), Font.PLAIN);
        addTableRow(rentTable, "Subtotal", "₱" + fmt(subtotal), Color.WHITE, Color.WHITE, Font.BOLD);
        s.add(rentTable);
        s.add(Box.createVerticalStrut(6));

        JPanel addlTable = new JPanel(new GridLayout(0, 2, 4, 2));
        addlTable.setBackground(DARK);
        addlTable.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        addTableRow(addlTable, "ADDITIONAL CHARGES", "", Color.WHITE, Color.WHITE, Font.BOLD);
        addTableRow(addlTable, "Driver Fee", "₱" + fmt(driverFee), Color.WHITE, new Color(200, 200, 200), Font.PLAIN);
        addTableRow(addlTable, "Fuel Charge", "₱" + fmt(fuelCharge), Color.WHITE, new Color(200, 200, 200), Font.PLAIN);
        addTableRow(addlTable, "Insurance Fee", "₱" + fmt(insuranceFee), Color.WHITE, new Color(200, 200, 200), Font.PLAIN);
        addTableRow(addlTable, "Other Charges", "₱" + fmt(otherCharge), Color.WHITE, new Color(200, 200, 200), Font.PLAIN);
        s.add(addlTable);
        s.add(Box.createVerticalStrut(6));

        // Discount
        JPanel discPanel = new JPanel(new GridLayout(1, 2));
        discPanel.setBackground(DARK);
        discPanel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        addTableRow(discPanel, "DISCOUNT", "-₱" + fmt(discount), Color.WHITE, new Color(220, 180, 100), Font.BOLD);
        s.add(discPanel);
        s.add(Box.createVerticalStrut(10));

        // Total
        JPanel totalPanel = new JPanel(new GridLayout(0, 1));
        totalPanel.setBackground(DARK);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTotalTitle = new JLabel("TOTAL AMOUNT");
        lblTotalTitle.setForeground(Color.WHITE);
        lblTotalTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        JLabel lblTotalValue = new JLabel("₱" + fmt(total));
        lblTotalValue.setForeground(Color.WHITE);
        lblTotalValue.setFont(new Font("Helvetica Neue", Font.BOLD, 26));
        totalPanel.add(lblTotalTitle);
        totalPanel.add(lblTotalValue);
        s.add(totalPanel);
        return s;
    }

    private JPanel buildPaymentDetails() {
        String method = payment != null ? payment.getPaymentMethod() : "CASH";
        String refNum = payment != null ? nvl(payment.getReferenceNumber()) : "—";
        double paid = payment != null ? payment.getAmountPaid() : 0;
        double deposit = payment != null ? payment.getSecurityDeposit() : 2000;
        double remaining = payment != null ? payment.getRemainingBalance() : 0;

        JPanel s = sectionPanel("💳  PAYMENT DETAILS");
        addInfoRow(s, "Payment Method", method);
        addInfoRow(s, "Reference Number", refNum);
        addInfoRow(s, "Amount Paid", "₱" + fmt(paid));
        addInfoRow(s, "Security Deposit", "₱" + fmt(deposit));
        addInfoRow(s, "Refundable", "Yes (After Inspection)");

        s.add(Box.createVerticalStrut(10));
        JPanel balRow = new JPanel(new GridLayout(1, 2));
        balRow.setBackground(LIGHT_LINE);
        balRow.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JLabel k = new JLabel("Remaining Balance");
        k.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        JLabel v = new JLabel("₱" + fmt(remaining));
        v.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        v.setHorizontalAlignment(SwingConstants.RIGHT);
        balRow.add(k);
        balRow.add(v);
        s.add(balRow);
        return s;
    }

    private JPanel buildNotes() {
        JPanel s = sectionPanel("📝  NOTE");
        String[] notes = {
            "Vehicle released with full tank.",
            "No visible damages at handover.",
            "Return vehicle on or before end date\nto avoid additional charges.",
            "Renter is liable for any damage\nper the signed rental agreement.",
            "For concerns, contact our office."
        };
        for (String note : notes) {
            JLabel lbl = new JLabel("<html>• " + note.replace("\n", "<br>") + "</html>");
            lbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
            lbl.setForeground(DARK);
            s.add(lbl);
            s.add(Box.createVerticalStrut(4));
        }
        return s;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(DARK);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lbl = new JLabel("♥   THANK YOU FOR CHOOSING YOUR RENT A CAR!   ♥");
        lbl.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    // ── Shared UI helpers ─────────────────────────────────────────────────
    private JPanel sectionPanel(String title) {
        JPanel p = new JPanel();
        p.setBackground(SECTION_BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_LINE, 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JLabel header = new JLabel(title);
        header.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        header.setForeground(DARK);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LIGHT_LINE));
        p.add(header);
        p.add(Box.createVerticalStrut(8));
        return p;
    }

    private void addInfoRow(JPanel panel, String key, String value) {
        JPanel row = new JPanel(new GridLayout(1, 2, 4, 0));
        row.setBackground(SECTION_BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel k = new JLabel(key + " :");
        k.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
        k.setForeground(new Color(100, 90, 85));

        JLabel v = new JLabel(value);
        v.setFont(new Font("Helvetica Neue", Font.BOLD, 11));
        v.setForeground(DARK);

        row.add(k);
        row.add(v);
        panel.add(row);
        panel.add(Box.createVerticalStrut(2));
    }

    private void addTableRow(JPanel grid, String key, String val,
            Color keyColor, Color valColor, int style) {
        JLabel k = new JLabel(key);
        k.setFont(new Font("Helvetica Neue", style, 11));
        k.setForeground(keyColor);
        JLabel v = new JLabel(val);
        v.setFont(new Font("Helvetica Neue", style, 11));
        v.setForeground(valColor);
        v.setHorizontalAlignment(SwingConstants.RIGHT);
        grid.add(k);
        grid.add(v);
    }

    private void addLabelPair(JPanel p, GridBagConstraints gc,
            int row, String key, String val) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.WEST;
        JLabel k = new JLabel(key + " : ");
        k.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
        k.setForeground(new Color(100, 90, 85));
        p.add(k, gc);
        gc.gridx = 1;
        JLabel v = new JLabel(val);
        v.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        v.setForeground(DARK);
        p.add(v, gc);
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private String generateReceiptNumber() {
        if (booking != null) {
            return String.format("RC-%04d-%06d",
                    java.time.Year.now().getValue(), booking.getBookingId());
        }
        return "RC-" + System.currentTimeMillis();
    }

    private String fmt(double d) {
        return String.format("%,.2f", d);
    }

    private String nvl(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }
}
