/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.user;

/**
 *
 * @author Sheanne
 */
public class AnalyticsPanel extends javax.swing.JPanel {

    /**
     * Creates new form AnalyticsPanel
     */
    public AnalyticsPanel() {
        initComponents();
    }
    
    // ── Chart data fields ─────────────────────────────────────
    private int[] last7DayActivity = new int[7];     // daily booking requests
    private double[] revenueBreakdown = new double[3];  // {PENDING, CONFIRMED, SUCCESSFUL}
    private int totalClicks = 0;
    private double totalRevenue = 0.0;

    public void loadData() {
        if (carrentalsystem.core.SessionManager.getCurrentUser() == null) {
            return;
        }

        new Thread(() -> {
            try {
                int userId = carrentalsystem.core.SessionManager
                        .getCurrentUser().getUserId();
                carrentalsystem.services.BookingService bSvc
                        = new carrentalsystem.services.BookingService();

                totalRevenue = bSvc.getRevenueLastDays(userId, 7);
                totalClicks = bSvc.getListingClicksLastDays(userId, 7);
                last7DayActivity = bSvc.getDailyActivityLast7Days(userId);
                revenueBreakdown = bSvc.getRevenueBreakdown(userId);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    // Update KPI text labels
                    jLabel7.setText("PHP " + String.format("%,.0f", totalRevenue));
                    jLabel3.setText(String.valueOf(totalClicks) + " clicks");

                    // Rebuild chart panels
                    rebuildCharts();
                });

            } catch (Exception e) {
                System.err.println("[AnalyticsPanel] loadData error: " + e.getMessage());
            }
        }).start();
    }
    
    // ── Inner class: Bar Chart for Daily Booking Activity ────
    private static class BarChartPanel extends javax.swing.JPanel {

        private final int[] values;
        private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        private final java.awt.Color BAR_COLOR = new java.awt.Color(153, 102, 0);
        private final java.awt.Color GRID_COLOR = new java.awt.Color(220, 220, 200);
        private final java.awt.Color TEXT_COLOR = new java.awt.Color(60, 40, 10);

        BarChartPanel(int[] values) {
            this.values = values;
            setBackground(new java.awt.Color(255, 255, 204));
            setPreferredSize(new java.awt.Dimension(400, 240));
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int padding = 36, barGap = 8;
            int chartH = h - padding * 2;
            int chartW = w - padding * 2;
            int count = values.length;
            int barW = (chartW - (count + 1) * barGap) / count;

            // Max value for scaling
            int maxVal = 1;
            for (int v : values) {
                if (v > maxVal) {
                    maxVal = v;
                }
            }

            // Grid lines
            g2.setColor(GRID_COLOR);
            g2.setStroke(new java.awt.BasicStroke(0.8f));
            for (int i = 0; i <= 4; i++) {
                int y = padding + (chartH * i / 4);
                g2.drawLine(padding, y, w - padding, y);
                g2.setColor(TEXT_COLOR);
                g2.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 10));
                g2.drawString(String.valueOf(maxVal - maxVal * i / 4), 2, y + 4);
                g2.setColor(GRID_COLOR);
            }

            // Bars
            for (int i = 0; i < count; i++) {
                int barH = (int) ((double) values[i] / maxVal * chartH);
                int x = padding + barGap + i * (barW + barGap);
                int y = padding + chartH - barH;

                // Bar fill with rounded top
                g2.setColor(BAR_COLOR);
                g2.fillRoundRect(x, y, barW, barH, 6, 6);

                // Value label on top
                g2.setColor(TEXT_COLOR);
                g2.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 11));
                String val = String.valueOf(values[i]);
                int valX = x + (barW - g2.getFontMetrics().stringWidth(val)) / 2;
                if (barH > 14) {
                    g2.drawString(val, valX, y - 2);
                }

                // Day label below
                g2.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 10));
                String day = i < days.length ? days[i] : "";
                int dayX = x + (barW - g2.getFontMetrics().stringWidth(day)) / 2;
                g2.drawString(day, dayX, h - 8);
            }

            // Baseline
            g2.setColor(TEXT_COLOR);
            g2.setStroke(new java.awt.BasicStroke(1.5f));
            g2.drawLine(padding, h - padding, w - padding, h - padding);

            g2.dispose();
        }
    }

    // ── Inner class: Pie Chart for Revenue Breakdown ──────────
    private static class PieChartPanel extends javax.swing.JPanel {

        private final double[] values; // {PENDING, CONFIRMED, SUCCESSFUL}
        private final String[] labels = {"Pending", "Confirmed", "Completed"};
        private final java.awt.Color[] COLORS = {
            new java.awt.Color(200, 160, 60), // gold  — pending
            new java.awt.Color(30, 100, 200), // blue  — confirmed
            new java.awt.Color(50, 160, 80) // green — successful
        };

        PieChartPanel(double[] values) {
            this.values = values;
            setBackground(new java.awt.Color(255, 255, 204));
            setPreferredSize(new java.awt.Dimension(400, 240));
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int diameter = Math.min(w, h) - 60;
            int x = 20, y = (h - diameter) / 2;

            double total = 0;
            for (double v : values) {
                total += v;
            }

            if (total == 0) {
                // No data — draw a grey circle with label
                g2.setColor(new java.awt.Color(200, 200, 180));
                g2.fillOval(x, y, diameter, diameter);
                g2.setColor(java.awt.Color.DARK_GRAY);
                g2.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 12));
                g2.drawString("No data yet", x + diameter / 2 - 30, y + diameter / 2);
                g2.dispose();
                return;
            }

            // Draw slices
            double startAngle = 0;
            for (int i = 0; i < values.length; i++) {
                double arc = (values[i] / total) * 360.0;
                g2.setColor(COLORS[i]);
                g2.fillArc(x, y, diameter, diameter, (int) startAngle, (int) arc);
                startAngle += arc;
            }

            // Outline
            g2.setColor(new java.awt.Color(255, 255, 204));
            g2.setStroke(new java.awt.BasicStroke(2f));
            g2.drawOval(x, y, diameter, diameter);

            // Legend on the right
            int legendX = x + diameter + 16;
            int legendY = y + 20;
            for (int i = 0; i < labels.length; i++) {
                g2.setColor(COLORS[i]);
                g2.fillRoundRect(legendX, legendY + i * 26, 14, 14, 4, 4);
                g2.setColor(new java.awt.Color(60, 40, 10));
                g2.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 11));
                String pct = total > 0
                        ? String.format("%.0f%%", values[i] / total * 100) : "0%";
                g2.drawString(labels[i] + "  " + pct, legendX + 18, legendY + i * 26 + 11);
            }

            g2.dispose();
        }
    }
    
    public static void recordListingClick(int carId) {
        new Thread(() -> {
            try {
                new carrentalsystem.services.BookingService().recordListingClick(carId);
            } catch (Exception e) {
                System.err.println("[AnalyticsPanel] recordListingClick error: " + e.getMessage());
            }
        }).start();
    }
    

    private void rebuildCharts() {
        // Bar chart in jPanel5 (Daily Clicks)
        jPanel5.removeAll();
        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel5.add(new BarChartPanel(last7DayActivity), java.awt.BorderLayout.CENTER);
        jPanel5.revalidate();
        jPanel5.repaint();

        // Pie chart in jPanel6 (Revenue Breakdown)
        jPanel6.removeAll();
        jPanel6.setLayout(new java.awt.BorderLayout());
        jPanel6.add(new PieChartPanel(revenueBreakdown), java.awt.BorderLayout.CENTER);
        jPanel6.revalidate();
        jPanel6.repaint();
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

        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1440, 1080));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 29)); // NOI18N
        jLabel2.setText("Listing Clicks");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Num of clicks");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Clicks.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Total clicks across all active listings");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Pro.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(169, 169, 169)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)))
                .addGap(54, 54, 54))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(57, 57, 57))
        );

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 490, 360));

        jPanel4.setBackground(new java.awt.Color(255, 255, 204));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 29)); // NOI18N
        jLabel6.setText("Listing Revenue");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel7.setText("PHP");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setText("Revenue from listings, past 7 days");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Pro.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 66, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addGap(57, 57, 57))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(67, 67, 67)
                .addComponent(jLabel8)
                .addGap(61, 61, 61))
        );

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 100, 490, 360));

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 29)); // NOI18N
        jLabel10.setText("Daily Clicks");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("GRAPH");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addComponent(jLabel11)
                .addContainerGap(210, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(123, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(119, 119, 119))
        );

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Pro.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(56, 56, 56))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 480, 490, 360));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(490, 360));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 29)); // NOI18N
        jLabel12.setText("Revenue Source Breakdown");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("PIE");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addComponent(jLabel13)
                .addContainerGap(210, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(123, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(119, 119, 119))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel12))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 480, 490, 360));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Pro User Analytics : Performance (Past 7 Days)");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, -1));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables
}
