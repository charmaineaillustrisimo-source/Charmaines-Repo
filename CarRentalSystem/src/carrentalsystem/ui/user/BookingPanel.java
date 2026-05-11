/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package carrentalsystem.ui.user;
import java.awt.Color;
import carrentalsystem.ui.user.MainDashboard;
import carrentalsystem.utils.PriceCalculator;
import java.awt.CardLayout;
/**
 *
 * @author macbookairm1grey
 */
public class BookingPanel extends javax.swing.JPanel {

    private int currentCarId;
    private MainDashboard dashboard;
    private carrentalsystem.models.Car currentCar;
    private carrentalsystem.services.BookingService bookingService = new carrentalsystem.services.BookingService();
    
    public BookingPanel() {
        initComponents();
        // 1. Interactive Setup
        setupInteractivity();

        // 2. Styling
        pnlTitle.setBackground(new java.awt.Color(155, 121, 128, 220));
        pnlMainContainer.setBackground(new java.awt.Color(220, 200, 204, 200));
        java.awt.Color labelColor = new java.awt.Color(80, 50, 55);
        jLabel6.setForeground(labelColor);
        jLabel7.setForeground(labelColor);
        jLabel8.setForeground(labelColor);
        jLabel9.setForeground(labelColor);
        jLabel10.setForeground(labelColor);
        jLabel11.setForeground(labelColor);
        jLabel12.setForeground(labelColor);
        jLabel13.setForeground(labelColor);
        jLabel14.setForeground(labelColor);

        lblTitle.setForeground(java.awt.Color.WHITE);
        setOpaque(false);
    }
    
    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }
    
    public void prepareBooking(carrentalsystem.models.Car car) {
        if (car == null) {
            return;
        }

        this.currentCar = car;
        this.currentCarId = car.getCarId();

        String fullTitle = "BOOK " + car.getBrand().toUpperCase()
                + " " + car.getModel().toUpperCase();
        lblTitle.setText(fullTitle);
        lblTitle.setPreferredSize(null);

        // ── Auto-fill renter info from session ────────────────────────────
        carrentalsystem.models.User user
                = carrentalsystem.core.SessionManager.getCurrentUser();
        if (user != null) {
            FullName.setText(user.getFullName());
            Email.setText(user.getEmail());
            PhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        } else {
            FullName.setText("");
            Email.setText("");
            PhoneNumber.setText("");
        }

        // ── Auto-fill TODAY as pickup date and time ────────────────────────
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalTime nowTime = java.time.LocalTime.now();

        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        String todayStr = months[today.getMonthValue() - 1]
                + " " + today.getDayOfMonth() + ", " + today.getYear();

        int hour = nowTime.getHour();
        String ampm = hour >= 12 ? "PM" : "AM";
        int h12 = hour % 12;
        if (h12 == 0) {
            h12 = 12;
        }
        String timeStr = String.format("%d:%02d %s", h12, nowTime.getMinute(), ampm);

        PickupDate.setText(todayStr);
        PickupTime.setText(timeStr);

        // ── Default return = today + 1 day ────────────────────────────────
        java.time.LocalDate tomorrow = today.plusDays(1);
        String tomorrowStr = months[tomorrow.getMonthValue() - 1]
                + " " + tomorrow.getDayOfMonth() + ", " + tomorrow.getYear();
        ReturnDate.setText(tomorrowStr);
        RetrunTime.setText(timeStr);

        // ── Update total price display ─────────────────────────────────────
        recalculateTotal();

        this.revalidate();
        this.repaint();
    }
    
    private void setupInteractivity() {
        // Prevent typing, only allow picker
        PickupDate.setEditable(false);
        PickupTime.setEditable(false);
        ReturnDate.setEditable(false);
        RetrunTime.setEditable(false);

        PickupDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showDatePicker(PickupDate);
            }
        });
        ReturnDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showDatePicker(ReturnDate);
            }
        });
        PickupTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showTimePicker(PickupTime);
            }
        });
        RetrunTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showTimePicker(RetrunTime);
            }
        });
    }
    
    private void showDatePicker(javax.swing.JTextField target) {
        javax.swing.JDialog picker = new javax.swing.JDialog((java.awt.Frame) null, false);
        picker.setUndecorated(true);
        picker.setSize(300, 220);
        picker.setLayout(new java.awt.GridLayout(4, 1, 5, 5));
        picker.getContentPane().setBackground(java.awt.Color.WHITE);
        ((javax.swing.JPanel) picker.getContentPane()).setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1));
        
        picker.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
                picker.dispose(); // Close if user clicks away
            }
        });
        
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        javax.swing.JComboBox<String> monthCombo = new javax.swing.JComboBox<>(months);
        javax.swing.JSpinner daySpinner = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 31, 1));
        javax.swing.JSpinner yearSpinner = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(2026, 2026, 2030, 1));

        picker.add(new javax.swing.JLabel(" Select Date:", javax.swing.SwingConstants.CENTER));
        picker.add(monthCombo);
        picker.add(daySpinner);
        picker.add(yearSpinner);

        javax.swing.JButton confirm = new javax.swing.JButton("SET DATE");
        confirm.addActionListener(e -> {
            String selectedDate = monthCombo.getSelectedItem() + " " + daySpinner.getValue() + ", " + yearSpinner.getValue();
            target.setText(selectedDate);
            picker.dispose();
            recalculateTotal();
        });
        picker.add(confirm);
        picker.setLocationRelativeTo(target);
        picker.setVisible(true);
    }

    private void showTimePicker(javax.swing.JTextField target) {
        javax.swing.JDialog picker = new javax.swing.JDialog((java.awt.Frame) null, false);
        picker.setUndecorated(true);
        picker.setSize(220, 200);
        picker.setLayout(new java.awt.GridLayout(4, 1, 5, 5));
        picker.getContentPane().setBackground(java.awt.Color.WHITE);
        ((javax.swing.JPanel) picker.getContentPane()).setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        
        picker.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
                picker.dispose();
            }
        });
        
        javax.swing.JSpinner hour = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(12, 1, 12, 1));
        javax.swing.JSpinner minute = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(0, 0, 59, 1));
        minute.setEditor(new javax.swing.JSpinner.NumberEditor(minute, "00"));

        String[] periods = {"AM", "PM"};
        javax.swing.JComboBox<String> amPmCombo = new javax.swing.JComboBox<>(periods);

        javax.swing.JPanel timePanel = new javax.swing.JPanel();
        timePanel.setBackground(java.awt.Color.WHITE);
        timePanel.add(hour);
        timePanel.add(new javax.swing.JLabel(":"));
        timePanel.add(minute);

        picker.add(new javax.swing.JLabel("Select Time", javax.swing.SwingConstants.CENTER));
        picker.add(timePanel);
        picker.add(amPmCombo);

        javax.swing.JButton btn = new javax.swing.JButton("CONFIRM");
        btn.addActionListener(e -> {
            String formattedMinute = String.format("%02d", (int) minute.getValue());
            target.setText(hour.getValue() + ":" + formattedMinute + " " + amPmCombo.getSelectedItem());
            picker.dispose();
        });

        picker.add(btn);
        picker.setLocationRelativeTo(target);
        picker.setVisible(true);
    }
    
    /**
     * Calculates total price whenever dates change and shows it. Attach this to
     * date picker confirm actions.
     */
    private void recalculateTotal() {
        if (currentCar == null) {
            return;
        }
        try {
            String from = PickupDate.getText().trim();
            String to = ReturnDate.getText().trim();
            if (!from.isEmpty() && !to.isEmpty()
                    && !from.equals(to)) {
                double total = carrentalsystem.utils.PriceCalculator
                        .calculateTotal(from, to, currentCar.getBasePrice());
                int days = (int) Math.max(1,
                        carrentalsystem.utils.PriceCalculator.calculateDays(from, to));
                // ── SHORT title so it fits ─────────────────────────────────
            String carName = currentCar.getBrand() + " "
                    + currentCar.getModel();
            // Truncate if too long
            if (carName.length() > 20) {
                carName = carName.substring(0, 18) + "..";
            }
            lblTitle.setText(carName + "  |  " + days + " day"
                    + (days > 1 ? "s" : "")
                    + "  ₱" + String.format("%,.0f", total));
        }
        } catch (Exception ignored) { }
    }
    
    /**
     * Shows a dummy payment dialog. Returns a Payment object if user confirms,
     * null if user cancels.
     */
    private carrentalsystem.models.Payment showPaymentDialog(carrentalsystem.models.Booking booking, double total) {
        double secDeposit = 2000.0; // Standard security deposit
        double grandTotal = total + secDeposit;
        
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridBagLayout());
        panel.setBackground(new java.awt.Color(240, 234, 229));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        java.awt.GridBagConstraints gc = new java.awt.GridBagConstraints();
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(5, 5, 5, 5);

        // Title
        javax.swing.JLabel title = new javax.swing.JLabel("💳  Complete Your Payment");
        title.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 18));
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        panel.add(title, gc);

        // Total display
        javax.swing.JLabel lblTotal = new javax.swing.JLabel(
                "Total Amount: ₱" + String.format("%,.2f", total));
        lblTotal.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 16));
        lblTotal.setForeground(new java.awt.Color(45, 36, 34));
        gc.gridy = 1;
        panel.add(lblTotal, gc);

        // Security deposit note
        javax.swing.JLabel depNote = new javax.swing.JLabel(
                "Security Deposit: ₱2,000.00  (refundable after inspection)");
        depNote.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 12));
        depNote.setForeground(new java.awt.Color(120, 100, 90));
        gc.gridy = 2;
        panel.add(depNote, gc);

        // Payment method
        gc.gridy = 3;
        gc.gridwidth = 1;
        panel.add(new javax.swing.JLabel("Payment Method:"), gc);
        String[] methods = {"CASH", "GCASH", "BANK_TRANSFER", "CREDIT_CARD"};
        javax.swing.JComboBox<String> cbMethod = new javax.swing.JComboBox<>(methods);
        gc.gridx = 1;
        panel.add(cbMethod, gc);

        // Reference number (shown for non-cash)
        gc.gridx = 0;
        gc.gridy = 4;
        javax.swing.JLabel lblRef = new javax.swing.JLabel("Reference Number:");
        panel.add(lblRef, gc);
        javax.swing.JTextField txtRef = new javax.swing.JTextField("(CASH - N/A)");
        txtRef.setEnabled(false);
        gc.gridx = 1;
        panel.add(txtRef, gc);

        // Enable/disable reference based on method
        cbMethod.addActionListener(e -> {
            boolean isCash = "CASH".equals(cbMethod.getSelectedItem());
            txtRef.setEnabled(!isCash);
            txtRef.setText(isCash ? "(CASH - N/A)" : "");
        });

        // Damage section
        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 2;
        javax.swing.JLabel damageNote = new javax.swing.JLabel(
                "<html><i style='color:#886655;font-size:11px'>"
                + "⚠  Any damage charges will be assessed by the car owner "
                + "AFTER the rental period ends, per the Terms & Conditions.<br>"
                + "Renter is liable for all damages caused during the rental.</i></html>");
        panel.add(damageNote, gc);

        // Terms
        gc.gridy = 5;
        javax.swing.JLabel terms = new javax.swing.JLabel(
                "<html><i style='color:gray;font-size:11px'>"
                + "By confirming, you agree to pay the total amount above "
                + "and accept responsibility per the signed rental agreement.</i></html>");
        panel.add(terms, gc);

        int result = javax.swing.JOptionPane.showConfirmDialog(
                this, panel,
                "Payment Confirmation",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (result != javax.swing.JOptionPane.OK_OPTION) {
            return null;
        }

        carrentalsystem.models.Payment payment = new carrentalsystem.models.Payment();
        payment.setPaymentMethod(cbMethod.getSelectedItem().toString());
        payment.setReferenceNumber(txtRef.getText().trim());
        payment.setBaseAmount(total);
        payment.setSecurityDeposit(secDeposit);
        payment.setDamageAmount(0); // set by owner later
        payment.setTotalAmount(grandTotal);
        payment.setAmountPaid(grandTotal);
        payment.setRemainingBalance(0);
        return payment;
    }



    // Helper for summary rows
    private void addSummaryRow(javax.swing.JPanel p, String key, String val) {
        javax.swing.JLabel k = new javax.swing.JLabel(key);
        k.setFont(new java.awt.Font("Helvetica Neue",
                java.awt.Font.PLAIN, 12));
        k.setForeground(new java.awt.Color(80, 65, 60));
        javax.swing.JLabel v = new javax.swing.JLabel(val);
        v.setFont(new java.awt.Font("Helvetica Neue",
                java.awt.Font.BOLD, 12));
        v.setForeground(new java.awt.Color(45, 36, 34));
        p.add(k);
        p.add(v);
    }
    
    /**
     * Asks the renter to upload: - Valid Government ID (always required) -
     * Driver's License (only if the car has NO driver)
     *
     * Returns true if the user submits all required documents.
     */
    private String[] showRenterVerificationDialog(boolean carHasDriver) {

        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel,
                javax.swing.BoxLayout.Y_AXIS));
        panel.setBackground(new java.awt.Color(240, 234, 229));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ── Title ─────────────────────────────────────────────────────────
        javax.swing.JLabel title = new javax.swing.JLabel(
                "<html><b style='font-size:14px'>Identity Verification Required</b><br>"
                + "<span style='color:gray;font-size:12px'>"
                + "The car owner needs to verify your identity before approving.<br>"
                + "Documents are shared only with the car owner.</span><br><br></html>");
        title.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panel.add(title);

        // ── Required: Valid Government ID ─────────────────────────────────
        final String[] idPath = {null};
        javax.swing.JButton btnID = makeVerifButton(
                "📋  Upload Valid Government ID  (REQUIRED)");
        btnID.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        btnID.addActionListener(e -> {
            java.io.File f = chooseImageFile();
            if (f != null) {
                idPath[0] = f.getAbsolutePath();
                btnID.setText("✅  " + f.getName());
                btnID.setForeground(new java.awt.Color(0, 120, 0));
            }
        });
        panel.add(btnID);
        panel.add(javax.swing.Box.createVerticalStrut(10));

        // ── Conditional: Driver's License (only if no driver) ────────────
        final String[] licensePath = {null};
        if (!carHasDriver) {
            javax.swing.JLabel licNote = new javax.swing.JLabel(
                    "<html><b>This car has NO driver provided.</b><br>"
                    + "A valid driver's license is required to rent.</html>");
            licNote.setFont(new java.awt.Font("Helvetica Neue",
                    java.awt.Font.PLAIN, 12));
            licNote.setForeground(new java.awt.Color(160, 80, 40));
            licNote.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            panel.add(licNote);
            panel.add(javax.swing.Box.createVerticalStrut(6));

            javax.swing.JButton btnLicense = makeVerifButton(
                    "🪪  Upload Driver's License  (REQUIRED — No driver)");
            btnLicense.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            btnLicense.addActionListener(e -> {
                java.io.File f = chooseImageFile();
                if (f != null) {
                    licensePath[0] = f.getAbsolutePath();
                    btnLicense.setText("✅  " + f.getName());
                    btnLicense.setForeground(new java.awt.Color(0, 120, 0));
                }
            });
            panel.add(btnLicense);
            panel.add(javax.swing.Box.createVerticalStrut(10));
        } else {
            javax.swing.JLabel driverNote = new javax.swing.JLabel(
                    "<html>✔  This car includes a driver — no license needed.</html>");
            driverNote.setFont(new java.awt.Font("Helvetica Neue",
                    java.awt.Font.ITALIC, 12));
            driverNote.setForeground(new java.awt.Color(0, 120, 0));
            driverNote.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            panel.add(driverNote);
            panel.add(javax.swing.Box.createVerticalStrut(10));
        }

        // ── Terms reminder ────────────────────────────────────────────────
        javax.swing.JLabel terms = new javax.swing.JLabel(
                "<html><i style='color:gray;font-size:11px'>"
                + "By submitting, you confirm that the documents are genuine.<br>"
                + "False submissions will result in immediate account suspension."
                + "</i></html>");
        terms.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panel.add(terms);

        // ── Show dialog ───────────────────────────────────────────────────
        int result = javax.swing.JOptionPane.showConfirmDialog(
                this, panel,
                "Renter Verification — Identity Check",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (result != javax.swing.JOptionPane.OK_OPTION) {
            return null;
        }

        if (idPath[0] == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please upload your Valid Government ID.",
                    "Missing Document", javax.swing.JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (!carHasDriver && licensePath[0] == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please upload your Driver's License.\n"
                    + "Required for self-drive rentals.",
                    "Missing Document", javax.swing.JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Return paths — save to DB later after booking is created
        return new String[]{
            idPath[0],
            licensePath[0] != null ? licensePath[0] : ""
        };
    }

    private void saveRenterVerification(int bookingId, String idPath,String licensePath, boolean requiresLicense) throws java.sql.SQLException {
        String sql
                = "INSERT INTO renter_verifications "
                + "(booking_id, renter_id, valid_id_path, driver_license_path, requires_license) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection conn
                = carrentalsystem.core.DBConnection.getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);   // ← real booking ID now
            ps.setInt(2, carrentalsystem.core.SessionManager
                    .getCurrentUser().getUserId());
            ps.setString(3, idPath);
            ps.setString(4, licensePath);
            ps.setBoolean(5, requiresLicense);
            ps.executeUpdate();
        }
    }

    private javax.swing.JButton makeVerifButton(String text) {
        javax.swing.JButton btn = new javax.swing.JButton(text);
        btn.setFont(new java.awt.Font("Helvetica Neue",
                java.awt.Font.PLAIN, 13));
        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setBackground(new java.awt.Color(235, 228, 220));
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        return btn;
    }

    private java.io.File chooseImageFile() {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogTitle("Select Image File");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files (JPG, PNG)", "jpg", "jpeg", "png"));
        int r = fc.showOpenDialog(this);
        return (r == javax.swing.JFileChooser.APPROVE_OPTION)
                ? fc.getSelectedFile() : null;
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

        pnlTitle = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(240, 228, 230, 200));
                // 80, 80 gives it that smooth, modern look
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        lblTitle = new javax.swing.JLabel();
        pnlMainContainer = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                // Frosted glass effect: semi-transparent white-pink fill
                g2.setColor(new java.awt.Color(240, 228, 230, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                // Subtle pink border stroke
                g2.setColor(new java.awt.Color(155, 121, 128, 130));
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        PickupDate = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        PickupTime = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        ReturnDate = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        RetrunTime = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        PickupLocation = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        ReturnLocation = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        FullName = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        Email = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        PhoneNumber = new javax.swing.JTextField() {
            {
                // Internal padding: text stays away from the curves
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
                setOpaque(false);
                // This is the secret: sets the background color to be fully transparent
                setBackground(new java.awt.Color(0,0,0,0)); 
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                // SMOOTHING: Essential for the pill look
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the white shape
                g2.setColor(java.awt.Color.WHITE);
                // Use 50, 50 for the deep curve seen in your second pic
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(java.awt.Graphics g) {
                // MUST BE EMPTY to stop the square border from coming back
            }
        };
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnBook = btnBook = new javax.swing.JButton() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. BACKGROUND LOGIC
                if (getModel().isPressed()) {
                    g2.setColor(new java.awt.Color(50, 50, 50));
                } else if (getModel().isRollover()) {
                    g2.setColor(new java.awt.Color(175, 145, 150)); // lighter rose on hover
                } else {
                    g2.setColor(new java.awt.Color(125, 90, 102)); // darker on press
                }

                // Draw the pill shape (30, 30 or 40, 40 for more curve)
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // 2. TEXT LOGIC (Crucial: Replace super.paintComponent)
                g2.setColor(java.awt.Color.WHITE);
                g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth("BOOK")) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString("BOOK", x, y);

                g2.dispose();
                // REMOVED: super.paintComponent(g); <-- This is what causes the "did not change" bug
            }

            @Override
            public void setBorder(javax.swing.border.Border border) {
                // Correct: Leave empty to prevent the square focus box
            }
        };

        // These must be set for the custom painting to work properly
        btnBook.setContentAreaFilled(false);
        btnBook.setFocusPainted(false);
        btnBook.setBorderPainted(false);
        btnBook.setOpaque(false);

        setBackground(new java.awt.Color(223, 208, 209));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1100, 700));
        setLayout(new java.awt.GridBagLayout());

        pnlTitle.setBackground(new java.awt.Color(204, 204, 204));
        pnlTitle.setForeground(new java.awt.Color(255, 255, 255));
        pnlTitle.setMinimumSize(new java.awt.Dimension(500, 68));
        pnlTitle.setOpaque(false);
        pnlTitle.setPreferredSize(new java.awt.Dimension(752, 70));
        pnlTitle.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        lblTitle.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("RENT A CAR");
        lblTitle.setPreferredSize(new java.awt.Dimension(500, 70));
        pnlTitle.add(lblTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(120, 0, 30, 0);
        add(pnlTitle, gridBagConstraints);

        pnlMainContainer.setOpaque(false);

        PickupDate.setColumns(15);
        PickupDate.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        PickupDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PickupDate.addActionListener(this::PickupDateActionPerformed);

        PickupTime.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        PickupTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PickupTime.addActionListener(this::PickupTimeActionPerformed);

        ReturnDate.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        ReturnDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        RetrunTime.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        RetrunTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RetrunTime.addActionListener(this::RetrunTimeActionPerformed);

        PickupLocation.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        PickupLocation.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1));

        ReturnLocation.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        ReturnLocation.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1));

        FullName.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        FullName.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1));
        FullName.addActionListener(this::FullNameActionPerformed);

        Email.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        Email.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1));

        PhoneNumber.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        PhoneNumber.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pick-up Date");

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Pick-up Time");

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Return Date");

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Retrun Date");

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Pick-up Location");

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Return Location");

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Full Name");

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("E-mail");

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Phone Number");

        btnBook.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        btnBook.setText("BOOK");
        btnBook.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnBook.setMargin(new java.awt.Insets(10, 14, 2, 14));
        btnBook.setPreferredSize(new java.awt.Dimension(84, 50));
        btnBook.setContentAreaFilled(false);
        btnBook.setFocusPainted(false);
        btnBook.setBorderPainted(false);
        btnBook.setText("BOOK"); // Ensure text is set
        btnBook.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBook.addActionListener(this::btnBookActionPerformed);

        javax.swing.GroupLayout pnlMainContainerLayout = new javax.swing.GroupLayout(pnlMainContainer);
        pnlMainContainer.setLayout(pnlMainContainerLayout);
        pnlMainContainerLayout.setHorizontalGroup(
            pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainContainerLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel12)
                .addGap(193, 193, 193)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(98, 98, 98))
            .addGroup(pnlMainContainerLayout.createSequentialGroup()
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainContainerLayout.createSequentialGroup()
                                .addComponent(FullName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(PhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PickupDate, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel6)))
                                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(jLabel7))
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(PickupTime, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addComponent(jLabel8))
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(ReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel9))
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(RetrunTime, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PickupLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel10)))
                                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ReturnLocation))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainContainerLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel11)
                                        .addGap(223, 223, 223))))))
                    .addGroup(pnlMainContainerLayout.createSequentialGroup()
                        .addGap(303, 303, 303)
                        .addComponent(btnBook, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        pnlMainContainerLayout.setVerticalGroup(
            pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainContainerLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RetrunTime, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(PickupTime)
                    .addComponent(PickupDate)
                    .addComponent(ReturnDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PickupLocation)
                    .addComponent(ReturnLocation, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pnlMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PhoneNumber)
                    .addComponent(FullName, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                    .addComponent(Email))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btnBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        add(pnlMainContainer, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void PickupDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PickupDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PickupDateActionPerformed

    private void PickupTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PickupTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PickupTimeActionPerformed

    private void RetrunTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RetrunTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RetrunTimeActionPerformed

    private void FullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FullNameActionPerformed

    private void btnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookActionPerformed
        // ── Null Check ────────────────────────────────────────────────────
        if (currentCar == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error: No car selected.");
            return;
        }

        if (FullName.getText().isEmpty() || Email.getText().isEmpty()
                || PickupDate.getText().isEmpty() || ReturnDate.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please fill in all booking details.");
            return;
        }

        try {
            // ── 1. Renter verification FIRST ──
            boolean carHasDriver = currentCar.isHasDriver();
            String[] verificationPaths = showRenterVerificationDialog(carHasDriver);
            if (verificationPaths == null) {
                return;
            }

            // ── 2. Build booking model ──
            carrentalsystem.models.Booking b = new carrentalsystem.models.Booking();
            b.setCarId(currentCar.getCarId());
            b.setRenterId(carrentalsystem.core.SessionManager.getCurrentUser().getUserId());
            b.setOwnerId(currentCar.getOwnerId());
            b.setImagePath(currentCar.getImagePath());
            b.setPickupLocation(PickupLocation.getText());
            b.setReturnLocation(ReturnLocation.getText());
            b.setStatus("PENDING"); // <--- MANDATORY: Start as PENDING

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy");
            b.setStartDate(new java.sql.Date(sdf.parse(PickupDate.getText()).getTime()));
            b.setEndDate(new java.sql.Date(sdf.parse(ReturnDate.getText()).getTime()));

            double total = carrentalsystem.utils.PriceCalculator.calculateTotal(
                    PickupDate.getText(), ReturnDate.getText(), currentCar.getBasePrice());
            int days = (int) Math.max(1, carrentalsystem.utils.PriceCalculator.calculateDays(
                    PickupDate.getText(), ReturnDate.getText()));
            b.setTotalPrice(total);
            b.setDaysCount(days);

            // ── 3. Save booking to DB (PENDING status) ──
            carrentalsystem.services.BookingService bookingService = new carrentalsystem.services.BookingService();
            int generatedId = bookingService.submitRequest(b); // Ensure this method saves status as 'PENDING'

            if (generatedId != -1) {
                b.setBookingId(generatedId);

                // ── 4. Save verification documents ──
                saveRenterVerification(generatedId, verificationPaths[0],
                        verificationPaths[1].isEmpty() ? null : verificationPaths[1], !carHasDriver);

                // ── 5. AUTO-SUBMIT BOOKING CARD VIA MESSAGE ──
                // We send a message with the bookingId linked so the card appears in the chat
                String autoMsg = "Requesting to book " + currentCar.getBrand() + " " + currentCar.getModel();
                // CORRECT: 5 arguments (senderId, receiverId, carId, content, bookingId)
                new carrentalsystem.services.MessageService().sendMessage(
                        b.getRenterId(),
                        b.getOwnerId(),
                        b.getCarId(), // Added this
                        autoMsg,
                        generatedId);

                javax.swing.JOptionPane.showMessageDialog(dashboard,
                        "Booking request sent! Please wait for the owner's approval in your Inbox.");

                // ── 6. Redirect to Inbox to see the card ──
                if (dashboard != null) {
                    dashboard.getInboxPanel().loadData();
                    dashboard.getInboxPanel().openThread(b.getOwnerId(), b.getCarId(), "Car Owner");
                    ((java.awt.CardLayout) dashboard.getPnlMainContent().getLayout())
                            .show(dashboard.getPnlMainContent(), "inboxCard");
                }
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error saving booking: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnBookActionPerformed

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Email;
    private javax.swing.JTextField FullName;
    private javax.swing.JTextField PhoneNumber;
    private javax.swing.JTextField PickupDate;
    private javax.swing.JTextField PickupLocation;
    private javax.swing.JTextField PickupTime;
    private javax.swing.JTextField RetrunTime;
    private javax.swing.JTextField ReturnDate;
    private javax.swing.JTextField ReturnLocation;
    private javax.swing.JButton btnBook;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlMainContainer;
    private javax.swing.JPanel pnlTitle;
    // End of variables declaration//GEN-END:variables
}
