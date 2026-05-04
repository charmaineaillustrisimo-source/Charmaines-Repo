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
        if (car == null) return;
        
        this.currentCar = car;
        this.currentCarId = car.getCarId();
        // Update the title label dynamically
        String fullTitle = "BOOK " + car.getBrand().toUpperCase() + " " + car.getModel().toUpperCase();
        lblTitle.setText(fullTitle);
        lblTitle.setPreferredSize(null);
        
        // Reset fields for a fresh booking
        FullName.setText("");
        Email.setText("");
        PhoneNumber.setText("");
        PickupDate.setText("");
        ReturnDate.setText("");
        
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
        // Null Check
        if (currentCar == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: No car selected for booking.");
            return;
        }
        
        // Validation
        if (FullName.getText().isEmpty() || Email.getText().isEmpty() || PickupDate.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all booking details.");
            return;
        }

        try {
            // 2. Prepare the Booking Model
            carrentalsystem.models.Booking newBooking = new carrentalsystem.models.Booking();
            newBooking.setCarId(currentCarId);
            newBooking.setRenterId(carrentalsystem.core.SessionManager.getCurrentUser().getUserId());
            
            // Pass the image
            newBooking.setImagePath(currentCar.getImagePath());
            
            // Convert String dates from pickers to SQL Dates[cite: 8]
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy");
            newBooking.setStartDate(new java.sql.Date(sdf.parse(PickupDate.getText()).getTime()));
            newBooking.setEndDate(new java.sql.Date(sdf.parse(ReturnDate.getText()).getTime()));

            // Calculate total using your utility[cite: 8]
            double total = carrentalsystem.utils.PriceCalculator.calculateTotal(
                    PickupDate.getText(), ReturnDate.getText(), currentCar.getBasePrice());
            newBooking.setTotalPrice(total);
            newBooking.setPickupLocation(PickupLocation.getText());
            newBooking.setReturnLocation(ReturnLocation.getText());

            // 3. Save to Database via Service[cite: 4]
            carrentalsystem.services.BookingService service = new carrentalsystem.services.BookingService();
            int generatedId = service.submitRequest(newBooking);

            if (generatedId != -1) {
                // Navigate to Inbox instead of My Rentals
                if (dashboard != null) {
                    java.awt.CardLayout cl = (java.awt.CardLayout) dashboard.getPnlMainContent().getLayout();

                    // Load inbox and open the conversation with the car owner
                    dashboard.getInboxPanel().loadData();
                    cl.show(dashboard.getPnlMainContent(), "inboxCard");
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error saving booking: " + e.getMessage());
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
