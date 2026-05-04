/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.user;

/**
 *
 * @author tomac
 */
public final class RentalCalendar extends javax.swing.JPanel {
    private java.time.LocalDate currentDisplayDate = java.time.LocalDate.now();
    // Stores Day Number -> List of Statuses (e.g., 2 -> ["Confirmed", "Pending"])
private java.util.Map<Integer, java.util.List<String>> bookingData = new java.util.HashMap<>();

    /**
     * Creates new form RentalCalendar
     */
    public RentalCalendar() {
        initComponents();
        // CLEAR THE CANVAS
        // We wipe everything NetBeans did so our manual layouts take over correctly
        this.removeAll();
        contentPanel.removeAll();
        panelSidebarContainer.removeAll();

        // OUTER PANEL (The Pink Background)
        this.setLayout(new java.awt.BorderLayout());
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 25, 20, 25));
        this.setBackground(new java.awt.Color(223, 208, 209));

        // WHITE CARD SETUP (contentPanel)
        contentPanel.setLayout(new java.awt.BorderLayout(25, 15));
        contentPanel.setOpaque(false); // Let the custom paintComponent handle the white round rect
        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // THE MAIN BODY (Calendar + Sidebar)
        javax.swing.JPanel mainBody = new javax.swing.JPanel(new java.awt.BorderLayout(20, 0));
        mainBody.setOpaque(false);

        // CALENDAR SIDE
        calendarGrid.removeAll();
        calendarGrid.setLayout(new java.awt.GridLayout(0, 7, 1, 1));
        calendarGrid.setBackground(new java.awt.Color(180, 180, 180)); 
        
        // SIDEBAR SIDE (Filter + Legend)
        javax.swing.JPanel sidebar = new javax.swing.JPanel();
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new java.awt.Dimension(220, 0));
        
        // Filter Cars Box
        filterCars.setPreferredSize(new java.awt.Dimension(220, 350));
        filterCars.setMaximumSize(new java.awt.Dimension(220, 350));
        sidebar.add(filterCars);
        sidebar.add(javax.swing.Box.createVerticalStrut(20));
        
        // Legend Section
        javax.swing.JPanel legendArea = new javax.swing.JPanel(new java.awt.GridLayout(0, 1, 0, 8));
        legendArea.setOpaque(false);
        legendArea.add(lblLegend);
        legendArea.add(combine(lblGreenIcon, lblBooked));
        legendArea.add(combine(lblGoldIcon, lblConfirmed));
        legendArea.add(combine(lblBlueIcon, lblPending));
        legendArea.add(combine(lblRedIcon, lblMaintenance));
        sidebar.add(legendArea);
        
        mainBody.add(calendarGrid, java.awt.BorderLayout.CENTER);
        mainBody.add(sidebar, java.awt.BorderLayout.EAST);
        contentPanel.add(mainBody, java.awt.BorderLayout.CENTER);
        
        // FINAL ASSEMBLY
        this.add(topContainer, java.awt.BorderLayout.NORTH);
        this.add(contentPanel, java.awt.BorderLayout.CENTER);
        
        loadData();
        
        this.revalidate();
        this.repaint();
    }

    private javax.swing.JPanel combine(javax.swing.JLabel icon, javax.swing.JLabel text) {
        javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        icon.setPreferredSize(new java.awt.Dimension(40, 15)); // Matches the thin bar style
        p.add(icon);
        p.add(text);
        return p;
    }

    private void setupCalendarDays() {
        // 1. Set the Title based on current date
        String monthName = currentDisplayDate.getMonth().getDisplayName(
                java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);
        lblMonthYear.setText("Rental Calendar - " + monthName + " " + currentDisplayDate.getYear());

        // 2. Add Day Headers (Sun, Mon, etc.)
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String h : headers) {
            javax.swing.JLabel headerLabel = new javax.swing.JLabel(h, javax.swing.SwingConstants.CENTER);
            headerLabel.setOpaque(true);
            headerLabel.setBackground(new java.awt.Color(186, 150, 150));
            headerLabel.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 24));
            headerLabel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK));
            calendarGrid.add(headerLabel);
        }

        // 3. Calculate start of month
        java.time.LocalDate firstOfMonth = currentDisplayDate.withDayOfMonth(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        int daysInMonth = currentDisplayDate.lengthOfMonth();

        // 4. Fill Leading Empty Slots (Previous Month days)
        java.time.LocalDate prevMonth = currentDisplayDate.minusMonths(1);
        int prevDays = prevMonth.lengthOfMonth();
        for (int i = startDayOfWeek - 1; i >= 0; i--) {
            javax.swing.JPanel cell = createDayCell(prevDays - i);
            cell.setOpaque(false);
            if (cell.getComponentCount() > 0) {
                cell.getComponent(0).setForeground(java.awt.Color.LIGHT_GRAY);
            }
            calendarGrid.add(cell);
        }

        // 5. Fill Current Month Days (DYNAMIC LOGIC)
        for (int day = 1; day <= daysInMonth; day++) {
            javax.swing.JPanel cell = createDayCell(day);

            // Check if there is booking data for this specific day
            if (bookingData.containsKey(day)) {
                java.util.List<String> statuses = new java.util.ArrayList<>(bookingData.get(day));
                for (String status : statuses) {
                    switch (status) {
                        case "Booked":
                            addRentalBar(cell, new java.awt.Color(34, 139, 34));
                            break;
                        case "Confirmed":
                            addRentalBar(cell, new java.awt.Color(184, 134, 11));
                            break;
                        case "Pending":
                            addRentalBar(cell, new java.awt.Color(30, 144, 255));
                            break;
                        case "Maintenance":
                            addRentalBar(cell, new java.awt.Color(178, 34, 34));
                            break;
                    }
                }
            }

            calendarGrid.add(cell);
        }

        // 6. Fill Remaining Slots
        int totalCells = 42;
        int remaining = totalCells - calendarGrid.getComponentCount();
        for (int i = 1; i <= remaining; i++) {
            javax.swing.JPanel cell = createDayCell(i);
            cell.getComponent(0).setEnabled(false);
            calendarGrid.add(cell);
        }
    }

    private javax.swing.JPanel createDayCell(int dayNumber) {
        javax.swing.JPanel cell = new javax.swing.JPanel();
        cell.setBackground(new java.awt.Color(220, 220, 220)); // Default light gray
        cell.setLayout(new javax.swing.BoxLayout(cell, javax.swing.BoxLayout.Y_AXIS));
        cell.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Day Number Label
        javax.swing.JLabel lbl = new javax.swing.JLabel(String.valueOf(dayNumber));
        lbl.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        cell.add(lbl);

        return cell;
    }

private void addRentalBar(javax.swing.JPanel cell, java.awt.Color color) {
        javax.swing.JPanel bar = new javax.swing.JPanel();
        bar.setBackground(color);
        bar.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 8)); // Thin bar
        bar.setMinimumSize(new java.awt.Dimension(0, 8));
        bar.setPreferredSize(new java.awt.Dimension(100, 8));

        cell.add(javax.swing.Box.createVerticalStrut(5)); // Space between bars
        cell.add(bar);
    }


    public void addBooking(int day, String status) {
        // 1. Store the data in our map
        bookingData.computeIfAbsent(day, k -> new java.util.ArrayList<>()).add(status);

        // 2. Refresh the UI
        calendarGrid.removeAll();
        setupCalendarDays();
        calendarGrid.revalidate();
        calendarGrid.repaint();

    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topContainer = new javax.swing.JPanel();
        btnPrevious = new javax.swing.JButton();
        lblMonthYear = new javax.swing.JLabel();
        btnFollowing = new javax.swing.JButton();
        contentPanel = // Inside the 'Custom Creation Code' for contentPanel in NetBeans:
        new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // This fills the white card based on the current size of the panel
                g2.setColor(java.awt.Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                // Subtle border
                g2.setColor(new java.awt.Color(210, 210, 210));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);

                g2.dispose();
            }
        };
        panelSearchContainer = new javax.swing.JPanel();
        panelSidebarContainer = new javax.swing.JPanel();
        filterCars = filterCars = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                // Do NOT call super.paintComponent(g); - this is what causes the square corners!
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. Paint the Main Rounded Background
                g2.setColor(getBackground()); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // 2. Paint the Header Separator Line
                // This is the line that separates "Filter Cars" from the list
                g2.setColor(new java.awt.Color(150, 150, 150)); // Gray color
                g2.drawLine(0, 50, getWidth(), 50); 

                // 3. Paint the Outer Thin Border
                g2.setColor(new java.awt.Color(100, 100, 100)); // Darker gray for the outline
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

                g2.dispose();
            }
        };
        filtercarsPanel = new javax.swing.JPanel();
        filtercarsLabel = new javax.swing.JLabel();
        lblLegend = new javax.swing.JLabel();
        lblGreenIcon = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); // Uses the background color you set in properties
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // 10 is the roundness
                g2.dispose();
            }
        };
        ;
        lblBooked = new javax.swing.JLabel();
        lblGoldIcon = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); // Uses the background color you set in properties
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // 10 is the roundness
                g2.dispose();
            }
        };
        lblConfirmed = new javax.swing.JLabel();
        lblBlueIcon = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); // Uses the background color you set in properties
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // 10 is the roundness
                g2.dispose();
            }
        };
        lblPending = new javax.swing.JLabel();
        lblRedIcon = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); // Uses the background color you set in properties
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // 10 is the roundness
                g2.dispose();
            }
        };
        lblMaintenance = new javax.swing.JLabel();
        calendarGrid = new javax.swing.JPanel();
        lblSun = new javax.swing.JLabel();
        lblMmon = new javax.swing.JLabel();
        lblTue = new javax.swing.JLabel();
        lblWed = new javax.swing.JLabel();
        lblThu = new javax.swing.JLabel();
        lblSat = new javax.swing.JLabel();
        lblFri = new javax.swing.JLabel();

        setBackground(new java.awt.Color(223, 208, 209));
        setPreferredSize(new java.awt.Dimension(1100, 700));
        setLayout(new java.awt.BorderLayout());

        topContainer.setOpaque(false);

        btnPrevious.setText("<");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        topContainer.add(btnPrevious);

        lblMonthYear.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblMonthYear.setText("Rent A Car Calendar -    ");
        topContainer.add(lblMonthYear);

        btnFollowing.setText(">");
        btnFollowing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFollowingActionPerformed(evt);
            }
        });
        topContainer.add(btnFollowing);

        add(topContainer, java.awt.BorderLayout.NORTH);

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new java.awt.BorderLayout());

        panelSearchContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSearchContainer.setMaximumSize(new java.awt.Dimension(1000, 80));
        panelSearchContainer.setMinimumSize(new java.awt.Dimension(1000, 80));
        panelSearchContainer.setPreferredSize(new java.awt.Dimension(1000, 80));
        contentPanel.add(panelSearchContainer, java.awt.BorderLayout.NORTH);
        panelSearchContainer.getAccessibleContext().setAccessibleDescription("");

        panelSidebarContainer.setPreferredSize(new java.awt.Dimension(200, 500));
        panelSidebarContainer.setLayout(new javax.swing.BoxLayout(panelSidebarContainer, javax.swing.BoxLayout.Y_AXIS));

        filterCars.setBackground(new java.awt.Color(234, 221, 221));
        filterCars.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        filtercarsPanel.setBackground(new java.awt.Color(234, 221, 221));
        filtercarsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        filtercarsLabel.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        filtercarsLabel.setText("Filter Cars");

        javax.swing.GroupLayout filtercarsPanelLayout = new javax.swing.GroupLayout(filtercarsPanel);
        filtercarsPanel.setLayout(filtercarsPanelLayout);
        filtercarsPanelLayout.setHorizontalGroup(
            filtercarsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(filtercarsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        filtercarsPanelLayout.setVerticalGroup(
            filtercarsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filtercarsPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(filtercarsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        filterCars.add(filtercarsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 60));

        panelSidebarContainer.add(filterCars);

        lblLegend.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblLegend.setText("Legend");
        panelSidebarContainer.add(lblLegend);

        lblGreenIcon.setBackground(new java.awt.Color(34, 139, 34));
        lblGreenIcon.setOpaque(true);
        panelSidebarContainer.add(lblGreenIcon);

        lblBooked.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblBooked.setText("Booked");
        panelSidebarContainer.add(lblBooked);

        lblGoldIcon.setBackground(new java.awt.Color(184, 134, 11));
        lblGoldIcon.setOpaque(true);
        panelSidebarContainer.add(lblGoldIcon);

        lblConfirmed.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblConfirmed.setText("Confirmed");
        panelSidebarContainer.add(lblConfirmed);

        lblBlueIcon.setBackground(new java.awt.Color(30, 144, 255));
        lblBlueIcon.setOpaque(true);
        panelSidebarContainer.add(lblBlueIcon);

        lblPending.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblPending.setText("Pending");
        panelSidebarContainer.add(lblPending);

        lblRedIcon.setBackground(new java.awt.Color(178, 34, 34));
        lblRedIcon.setOpaque(true);
        panelSidebarContainer.add(lblRedIcon);

        lblMaintenance.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblMaintenance.setText("Maintenance");
        panelSidebarContainer.add(lblMaintenance);

        contentPanel.add(panelSidebarContainer, java.awt.BorderLayout.EAST);

        calendarGrid.setBackground(new java.awt.Color(102, 102, 102));
        calendarGrid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        calendarGrid.setLayout(new java.awt.GridLayout(0, 7));

        lblSun.setBackground(new java.awt.Color(186, 150, 150));
        lblSun.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblSun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSun.setText("Sun");
        lblSun.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSun.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblSun.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblSun.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSun.setMaximumSize(new java.awt.Dimension(51, 100));
        lblSun.setOpaque(true);
        calendarGrid.add(lblSun);

        lblMmon.setBackground(new java.awt.Color(186, 150, 150));
        lblMmon.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblMmon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMmon.setText("Mon");
        lblMmon.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblMmon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblMmon.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblMmon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMmon.setMaximumSize(new java.awt.Dimension(10, 10));
        lblMmon.setMinimumSize(new java.awt.Dimension(10, 10));
        lblMmon.setOpaque(true);
        lblMmon.setPreferredSize(new java.awt.Dimension(10, 10));
        calendarGrid.add(lblMmon);

        lblTue.setBackground(new java.awt.Color(186, 150, 150));
        lblTue.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblTue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTue.setText("Tue");
        lblTue.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblTue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTue.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblTue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTue.setOpaque(true);
        calendarGrid.add(lblTue);

        lblWed.setBackground(new java.awt.Color(186, 150, 150));
        lblWed.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblWed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWed.setText("Wed");
        lblWed.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblWed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblWed.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblWed.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblWed.setOpaque(true);
        calendarGrid.add(lblWed);

        lblThu.setBackground(new java.awt.Color(186, 150, 150));
        lblThu.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThu.setText("Thu");
        lblThu.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblThu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblThu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblThu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblThu.setOpaque(true);
        lblThu.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        calendarGrid.add(lblThu);

        lblSat.setBackground(new java.awt.Color(186, 150, 150));
        lblSat.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblSat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSat.setText("Sat");
        lblSat.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblSat.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblSat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSat.setOpaque(true);
        calendarGrid.add(lblSat);

        lblFri.setBackground(new java.awt.Color(186, 150, 150));
        lblFri.setFont(new java.awt.Font("Serif", 1, 30)); // NOI18N
        lblFri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFri.setText("Fri");
        lblFri.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblFri.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblFri.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblFri.setOpaque(true);
        lblFri.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        calendarGrid.add(lblFri);

        contentPanel.add(calendarGrid, java.awt.BorderLayout.CENTER);

        add(contentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFollowingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFollowingActionPerformed
        currentDisplayDate = currentDisplayDate.plusMonths(1);
        loadData();
    }//GEN-LAST:event_btnFollowingActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        currentDisplayDate = currentDisplayDate.minusMonths(1);
        loadData();
    }//GEN-LAST:event_btnPreviousActionPerformed

    
    public static void main(String[] args) {
    javax.swing.JFrame frame = new javax.swing.JFrame();
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    frame.add(new RentalCalendar()); // This puts your panel inside a window
    frame.pack();
    frame.setLocationRelativeTo(null); // Centers the window
    frame.setVisible(true);
}
    
    /**
     * Fetch real bookings from the DB and paint them on the calendar. Call this
     * whenever the panel becomes visible or the month changes.
     */
    public void loadData() {
        if (carrentalsystem.core.SessionManager.getCurrentUser() == null) {
            return;
        }

        // Clear previous data
        bookingData.clear();

        new Thread(() -> {
            try {
                int userId = carrentalsystem.core.SessionManager.getCurrentUser().getUserId();
                int year = currentDisplayDate.getYear();
                int month = currentDisplayDate.getMonthValue();

                carrentalsystem.services.BookingService svc
                        = new carrentalsystem.services.BookingService();
                java.util.List<carrentalsystem.models.Booking> bookings
                        = svc.getBookingsForMonth(userId, year, month);

                for (carrentalsystem.models.Booking b : bookings) {
                    if (b.getStartDate() == null || b.getEndDate() == null) {
                        continue;
                    }

                    java.time.LocalDate start = b.getStartDate().toLocalDate();
                    java.time.LocalDate end = b.getEndDate().toLocalDate();

                    // Map DB status → legend label
                    String calStatus;
                    switch (b.getStatus().toUpperCase()) {
                        case "CONFIRMED":
                            calStatus = "Confirmed";
                            break;
                        case "PENDING":
                            calStatus = "Pending";
                            break;
                        case "SUCCESSFUL":
                            calStatus = "Booked";
                            break;
                        default:
                            calStatus = "Booked";
                            break;
                    }

                    // Paint every day in the date range
                    java.time.LocalDate cursor = start;
                    while (!cursor.isAfter(end)) {
                        // Only paint days that belong to the displayed month
                        if (cursor.getYear() == year && cursor.getMonthValue() == month) {
                            final int day = cursor.getDayOfMonth();
                            final String status = calStatus;
                            bookingData
                                    .computeIfAbsent(day, k -> new java.util.ArrayList<>())
                                    .add(status);
                        }
                        cursor = cursor.plusDays(1);
                    }
                }

                // Refresh the grid on the EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    calendarGrid.removeAll();
                    setupCalendarDays();
                    calendarGrid.revalidate();
                    calendarGrid.repaint();
                });

            } catch (Exception e) {
                System.err.println("[RentalCalendar] loadData error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFollowing;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JPanel calendarGrid;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel filterCars;
    private javax.swing.JLabel filtercarsLabel;
    private javax.swing.JPanel filtercarsPanel;
    private javax.swing.JLabel lblBlueIcon;
    private javax.swing.JLabel lblBooked;
    private javax.swing.JLabel lblConfirmed;
    private javax.swing.JLabel lblFri;
    private javax.swing.JLabel lblGoldIcon;
    private javax.swing.JLabel lblGreenIcon;
    private javax.swing.JLabel lblLegend;
    private javax.swing.JLabel lblMaintenance;
    private javax.swing.JLabel lblMmon;
    private javax.swing.JLabel lblMonthYear;
    private javax.swing.JLabel lblPending;
    private javax.swing.JLabel lblRedIcon;
    private javax.swing.JLabel lblSat;
    private javax.swing.JLabel lblSun;
    private javax.swing.JLabel lblThu;
    private javax.swing.JLabel lblTue;
    private javax.swing.JLabel lblWed;
    private javax.swing.JPanel panelSearchContainer;
    private javax.swing.JPanel panelSidebarContainer;
    private javax.swing.JPanel topContainer;
    // End of variables declaration//GEN-END:variables
}
