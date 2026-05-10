/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package carrentalsystem.ui.user;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
/**
 *
 * @author macbookairm1grey
 */
public class CarDetailsPanel extends javax.swing.JPanel {

    /**
     * Creates new form CarDetailsPanel
     */
    private MainDashboard dashboard;
    private carrentalsystem.models.Car currentCar;                           
    private carrentalsystem.services.UserService userService = new carrentalsystem.services.UserService(); 
    private javax.swing.JPanel pnlReviews = new javax.swing.JPanel();
    private javax.swing.JLabel lblAvgRating = new javax.swing.JLabel("No reviews yet");
    
    public CarDetailsPanel() {
        initComponents();
        spDescription.setBorder(null);
        
        // Restore dark button color (initComponents overrides the anonymous initializer)
        btnRentNow.setBackground(new java.awt.Color(98, 89, 85));
        btnRentNow.setForeground(java.awt.Color.WHITE);

        // Spec pills need opaque=false for rounded painting to show
        lblTransmission.setOpaque(false);
        lblSeats.setOpaque(false);
        lblFuel.setOpaque(false);
        lblCondition.setOpaque(false);
        
        // Owner label
        lblOwnerName.setOpaque(false);
        lblOwnerName.setBackground(new java.awt.Color(240, 240, 240));
        lblOwnerName.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12));

        // Fix 3: Set explicit size
        setPreferredSize(new java.awt.Dimension(1100, 700));
        setMinimumSize(new java.awt.Dimension(1100, 700));
        
        // Ensure the rounded painting works for the new labels
        lblColor.setOpaque(false);
        lblPlateNumber.setOpaque(false);
        lblMileage.setOpaque(false);

        // Set colors to match your dark brown/cream theme
        Color detailColor = new Color(45, 36, 34);
        lblColor.setForeground(detailColor);
        lblPlateNumber.setForeground(detailColor);
        lblMileage.setForeground(detailColor);
    

        // Reviews title
        javax.swing.JLabel lblReviewsTitle = new javax.swing.JLabel("⭐ Customer Reviews");
        lblReviewsTitle.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 15));
        lblReviewsTitle.setForeground(new java.awt.Color(45, 36, 34));

        // Reviews container with scroll
        pnlReviews = new javax.swing.JPanel();
        pnlReviews.setLayout(new javax.swing.BoxLayout(
                pnlReviews, javax.swing.BoxLayout.Y_AXIS));
        pnlReviews.setOpaque(false);

        javax.swing.JScrollPane spReviews = new javax.swing.JScrollPane(pnlReviews);
        spReviews.setBorder(null);
        spReviews.setOpaque(false);
        spReviews.getViewport().setOpaque(false);
        spReviews.setPreferredSize(new java.awt.Dimension(460, 160));

        // ── Add to panel using AbsoluteConstraints (matches your layout) ──────
        // Adjust the x/y coordinates to fit below your existing spec rows.
        add(lblReviewsTitle,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 628, 300, 28));
        add(spReviews,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 860, 180));
        
    }
    
    public void setCarDetails(carrentalsystem.models.Car car) {
        if (car == null) return;
        
        // Save the car to class variable
        this.currentCar = car;
        
        // Title (Brand + Model)
        lblTitle.setText(car.getBrand().toUpperCase() + " " + car.getModel().toUpperCase());
        lblOwnerName.setText("Listed by: loading...");
        new Thread(() -> {
            try {
                carrentalsystem.models.User owner = userService.getUserById(car.getOwnerId());

                // Build the display string
                String displayName;
                if (owner != null && owner.getFullName() != null && !owner.getFullName().isBlank()) {
                    displayName = "Listed by: " + owner.getFullName();
                } else {
                    displayName = "Listed by: Unknown";
                }

                // Always update Swing components on the Event Dispatch Thread
                javax.swing.SwingUtilities.invokeLater(() -> lblOwnerName.setText(displayName));

            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(() -> lblOwnerName.setText("Listed by: Unknown"));
                System.err.println("[CarDetailsPanel] Could not load owner: " + e.getMessage());
            }
        }).start();

        // Price (Dynamic value + fixed "/day")
        // This formats 2500 into 2,500.00
        String formattedPrice = String.format("%,.2f", car.getBasePrice());
        lblPriceValue.setText("PHP " + formattedPrice + "/day");
        
        // Owner/lister name — uses car.getOwnerId() mapped to a name via your service
        // For now set a placeholder; replace with real owner lookup when UserService is ready
        lblOwnerName.setText("Listed by: Car Owner");

        // Specs (Capsules)
        lblTransmission.setText(car.getTransmission());
        lblSeats.setText(car.getSeats() + " Seaters");
        lblFuel.setText(car.getFuelType());
        lblCondition.setText(car.getCondition());
        
        // Color and Plate Number
        lblColor.setText(car.getColor() != null ? car.getColor() : "Not specified");
        lblPlateNumber.setText(car.getPlateNumber() != null ? car.getPlateNumber() : "Not specified");
        
        loadCarReviews(car.getCarId());
        
        // Description
        taDescription.setText(car.getDescription());

        // 5. Image Loading
        if (car.getImagePath() != null && !car.getImagePath().isEmpty()) {
            carrentalsystem.utils.ImageUtil.applyScaledImage(lblCarImage, car.getImagePath(), 560, 240);
        }
    }

    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }
    
    /**
     * Loads reviews for the given car and displays them below the description.
     */
    private void loadCarReviews(int carId) {
        new Thread(() -> {
            try {
                java.util.List<carrentalsystem.models.Review> reviews
                        = new carrentalsystem.services.ReviewService().getReviewsForCar(carId);

                javax.swing.SwingUtilities.invokeLater(() -> {
                    pnlReviews.removeAll();
                    pnlReviews.setLayout(new javax.swing.BoxLayout(
                            pnlReviews, javax.swing.BoxLayout.Y_AXIS));
                    pnlReviews.setOpaque(false);

                    if (reviews.isEmpty()) {
                        javax.swing.JLabel none = new javax.swing.JLabel(
                                "No reviews yet. Be the first to review!");
                        none.setFont(new java.awt.Font("Helvetica Neue",
                                java.awt.Font.ITALIC, 13));
                        none.setForeground(new java.awt.Color(150, 130, 120));
                        pnlReviews.add(none);
                    } else {
                        // Average rating
                        double avg = reviews.stream()
                                .mapToInt(carrentalsystem.models.Review::getRating)
                                .average().orElse(0);
                        lblAvgRating.setText("⭐  " + String.format("%.1f", avg)
                                + " / 5.0  (" + reviews.size() + " reviews)");
                        lblAvgRating.setFont(new java.awt.Font("Helvetica Neue",
                                java.awt.Font.BOLD, 14));
                        pnlReviews.add(lblAvgRating);
                        pnlReviews.add(javax.swing.Box.createVerticalStrut(8));

                        for (carrentalsystem.models.Review r : reviews) {
                            pnlReviews.add(buildReviewRow(r));
                            pnlReviews.add(javax.swing.Box.createVerticalStrut(6));
                        }
                    }
                    pnlReviews.revalidate();
                    pnlReviews.repaint();
                });
            } catch (Exception e) {
                System.err.println("[CarDetailsPanel] Reviews error: " + e.getMessage());
            }
        }).start();
    }

    private javax.swing.JPanel buildReviewRow(carrentalsystem.models.Review r) {
        javax.swing.JPanel row = new javax.swing.JPanel();
        row.setLayout(new javax.swing.BoxLayout(row, javax.swing.BoxLayout.Y_AXIS));
        row.setBackground(new java.awt.Color(248, 244, 241));
        row.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(210, 200, 195), 1, true),
                javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        String stars = "⭐".repeat(r.getRating())
                + "☆".repeat(5 - r.getRating());
        javax.swing.JLabel lblStars = new javax.swing.JLabel(
                stars + "  " + r.getReviewerName());
        lblStars.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.BOLD, 12));
        lblStars.setForeground(new java.awt.Color(45, 36, 34));

        javax.swing.JLabel lblComment = new javax.swing.JLabel(
                "<html>" + (r.getComment() != null ? r.getComment() : "No comment.") + "</html>");
        lblComment.setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 12));
        lblComment.setForeground(new java.awt.Color(80, 65, 60));

        row.add(lblStars);
        row.add(javax.swing.Box.createVerticalStrut(4));
        row.add(lblComment);
        return row;
    }
    
    public void displayCarDetails(carrentalsystem.models.Car car) {
        this.currentCar = car;

        // 1. Text Info
        lblTitle.setText(car.getBrand() + " " + car.getModel());
        lblPriceValue.setText("PHP " + String.format("%,.0f", car.getBasePrice()));
        lblSeats.setText(car.getSeats() + " Seaters");
        lblFuel.setText(car.getFuelType());
        lblTransmission.setText(car.getTransmission());
        lblCondition.setText(car.getCondition());
        taDescription.setText(car.getDescription());

        // 2. New Functional Labels (Color, Plate, Mileage)
        lblColor.setText(car.getColor());
        lblPlateNumber.setText(car.getPlateNumber());
        // Format mileage with a comma (e.g., 15,000)
        lblMileage.setText(String.format("%,d", car.getMileageLimit()) + " km");

        // 3. Load the Image
        // Make sure you have ImageUtil in your project
        carrentalsystem.utils.ImageUtil.applyScaledImage(lblCarImage, car.getImagePath(), 600, 350);

        // 4. Update Owner Info
        try {
            carrentalsystem.models.User owner = userService.getUserById(car.getOwnerId());
            if (owner != null) {
                lblOwnerName.setText("Owner: " + owner.getFullName());
            }
        } catch (Exception e) {
            lblOwnerName.setText("Owner: Not Available");
        }
        
        loadCarReviews(car.getCarId());
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCarImage = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Create a rounded rectangle clip (30px radius)
                java.awt.geom.RoundRectangle2D roundedRect = new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setClip(roundedRect);

                super.paintComponent(g2);
                g2.dispose();
            }
        };
        lblTitle = new javax.swing.JLabel();
        lblPriceValue = new javax.swing.JLabel();
        lblOwnerName = new javax.swing.JLabel();
        btnRentNow = new javax.swing.JButton() {
            {
                // 1. Basic Setup
                setOpaque(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setForeground(java.awt.Color.WHITE);
                setBackground(new java.awt.Color(98, 89, 85)); // Your dark taupe color
                setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // 2. Paint Rounded Background
                // Change color slightly if the button is pressed
                if (getModel().isArmed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new java.awt.Color(118, 109, 105)); // Slightly lighter hover color
                } else {
                    g2.setColor(getBackground());
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.dispose();
                super.paintComponent(g); // This draws the "RENT NOW" text
            }
        };
        btnContactOwner = new javax.swing.JButton() {
            {
                // 1. Basic Setup
                setOpaque(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setForeground(java.awt.Color.WHITE);
                setBackground(new java.awt.Color(98, 89, 85)); // Your dark taupe color
                setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // 2. Paint Rounded Background
                // Change color slightly if the button is pressed
                if (getModel().isArmed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new java.awt.Color(118, 109, 105)); // Slightly lighter hover color
                } else {
                    g2.setColor(getBackground());
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.dispose();
                super.paintComponent(g); // This draws the "RENT NOW" text
            }
        };
        pnlSpecRow = new javax.swing.JPanel();
        lblTransmission = new javax.swing.JLabel() {
            {
                setOpaque(false); // Make background transparent so we can paint the round shape
                setBackground(new java.awt.Color(245, 245, 245)); // Light grey
                // Padding: top, left, bottom, right (45px left for the icon)
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 45, 5, 15));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass — semi-transparent dark brown fill
                g2.setColor(new java.awt.Color(45, 36, 34, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Subtle lighter border stroke for the glass edge
                g2.setColor(new java.awt.Color(255, 255, 255, 55));
                g2.setStroke(new java.awt.BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblSeats = new javax.swing.JLabel() {
            {
                setOpaque(false); // Make background transparent so we can paint the round shape
                setBackground(new java.awt.Color(245, 245, 245)); // Light grey
                // Padding: top, left, bottom, right (45px left for the icon)
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 45, 5, 15));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass — semi-transparent dark brown fill
                g2.setColor(new java.awt.Color(45, 36, 34, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Subtle lighter border stroke for the glass edge
                g2.setColor(new java.awt.Color(255, 255, 255, 55));
                g2.setStroke(new java.awt.BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblFuel = new javax.swing.JLabel() {
            {
                setOpaque(false); // Make background transparent so we can paint the round shape
                setBackground(new java.awt.Color(245, 245, 245)); // Light grey
                // Padding: top, left, bottom, right (45px left for the icon)
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 45, 5, 15));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass — semi-transparent dark brown fill
                g2.setColor(new java.awt.Color(45, 36, 34, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Subtle lighter border stroke for the glass edge
                g2.setColor(new java.awt.Color(255, 255, 255, 55));
                g2.setStroke(new java.awt.BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblCondition = new javax.swing.JLabel() {
            {
                setOpaque(false); // Make background transparent so we can paint the round shape
                setBackground(new java.awt.Color(245, 245, 245)); // Light grey
                // Padding: top, left, bottom, right (45px left for the icon)
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 45, 5, 15));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass — semi-transparent dark brown fill
                g2.setColor(new java.awt.Color(45, 36, 34, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Subtle lighter border stroke for the glass edge
                g2.setColor(new java.awt.Color(255, 255, 255, 55));
                g2.setStroke(new java.awt.BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblColor = new javax.swing.JLabel() {
            {
                setOpaque(false); // Make background transparent so we can paint the round shape
                setBackground(new java.awt.Color(245, 245, 245)); // Light grey
                // Padding: top, left, bottom, right (45px left for the icon)
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 45, 5, 15));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass — semi-transparent dark brown fill
                g2.setColor(new java.awt.Color(45, 36, 34, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Subtle lighter border stroke for the glass edge
                g2.setColor(new java.awt.Color(255, 255, 255, 55));
                g2.setStroke(new java.awt.BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblPlateNumber = new javax.swing.JLabel() {
            {
                setOpaque(false); // Make background transparent so we can paint the round shape
                setBackground(new java.awt.Color(245, 245, 245)); // Light grey
                // Padding: top, left, bottom, right (45px left for the icon)
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 45, 5, 15));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass — semi-transparent dark brown fill
                g2.setColor(new java.awt.Color(45, 36, 34, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Subtle lighter border stroke for the glass edge
                g2.setColor(new java.awt.Color(255, 255, 255, 55));
                g2.setStroke(new java.awt.BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblMileage = new javax.swing.JLabel() {
            {
                setOpaque(false); // Make background transparent so we can paint the round shape
                setBackground(new java.awt.Color(245, 245, 245)); // Light grey
                // Padding: top, left, bottom, right (45px left for the icon)
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 45, 5, 15));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // Frosted glass — semi-transparent dark brown fill
                g2.setColor(new java.awt.Color(45, 36, 34, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Subtle lighter border stroke for the glass edge
                g2.setColor(new java.awt.Color(255, 255, 255, 55));
                g2.setStroke(new java.awt.BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        spDescription = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea() {
            {
                setOpaque(false);
                setLineWrap(true);
                setWrapStyleWord(true);
                setBackground(new java.awt.Color(45, 36, 34));   // dark brown
                setForeground(new java.awt.Color(230, 220, 215)); // warm cream text
                setCaretColor(new java.awt.Color(230, 220, 215)); // cursor matches text
                setFont(new java.awt.Font("Helvetica Neue", java.awt.Font.PLAIN, 14));
                setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 18, 15, 18));
            }

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                // Fill rounded background with dark brown
                g2.setColor(new java.awt.Color(45, 36, 34));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCarImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCarImage.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblCarImage.setPreferredSize(new java.awt.Dimension(560, 350));
        add(lblCarImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 560, 260));

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTitle.setText("Car Title Placeholder");
        lblTitle.setPreferredSize(new java.awt.Dimension(600, 350));
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 400, 50));

        lblPriceValue.setFont(new java.awt.Font("Helvetica Neue", 1, 45)); // NOI18N
        lblPriceValue.setText("Price Value /day");
        lblPriceValue.setPreferredSize(new java.awt.Dimension(400, 45));
        add(lblPriceValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 100, 400, 45));

        lblOwnerName.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblOwnerName.setForeground(new java.awt.Color(6, 6, 6));
        lblOwnerName.setText("Listed by: Owner Name");
        lblOwnerName.setPreferredSize(new java.awt.Dimension(400, 35));
        add(lblOwnerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 350, 400, 35));

        btnRentNow.setBackground(new java.awt.Color(245, 245, 245));
        btnRentNow.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        btnRentNow.setText("RENT NOW");
        btnRentNow.setPreferredSize(new java.awt.Dimension(200, 50));
        btnRentNow.addActionListener(this::btnRentNowActionPerformed);
        add(btnRentNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 570, 260, -1));

        btnContactOwner.setBackground(new java.awt.Color(245, 245, 245));
        btnContactOwner.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        btnContactOwner.setText("CONTACT OWNER");
        btnContactOwner.setPreferredSize(new java.awt.Dimension(200, 50));
        btnContactOwner.addActionListener(this::btnContactOwnerActionPerformed);
        add(btnContactOwner, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 570, 260, -1));

        pnlSpecRow.setBackground(new java.awt.Color(255, 255, 255));
        pnlSpecRow.setOpaque(false);
        pnlSpecRow.setPreferredSize(new java.awt.Dimension(800, 60));
        pnlSpecRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 7));

        lblTransmission.setBackground(new java.awt.Color(45, 36, 34));
        lblTransmission.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblTransmission.setForeground(new java.awt.Color(230, 220, 215));
        lblTransmission.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Transimission Type.png"))); // NOI18N
        lblTransmission.setText("jLabel1");
        lblTransmission.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        lblTransmission.setIconTextGap(10);
        lblTransmission.setPreferredSize(new java.awt.Dimension(150, 45));
        pnlSpecRow.add(lblTransmission);

        lblSeats.setBackground(new java.awt.Color(45, 36, 34));
        lblSeats.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblSeats.setForeground(new java.awt.Color(230, 220, 215));
        lblSeats.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Seaters.png"))); // NOI18N
        lblSeats.setText("jLabel1");
        lblSeats.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        lblSeats.setIconTextGap(10);
        lblSeats.setPreferredSize(new java.awt.Dimension(150, 45));
        pnlSpecRow.add(lblSeats);

        lblFuel.setBackground(new java.awt.Color(45, 36, 34));
        lblFuel.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblFuel.setForeground(new java.awt.Color(230, 220, 215));
        lblFuel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Fuel Type.png"))); // NOI18N
        lblFuel.setText("jLabel1");
        lblFuel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        lblFuel.setIconTextGap(10);
        lblFuel.setPreferredSize(new java.awt.Dimension(150, 45));
        pnlSpecRow.add(lblFuel);

        lblCondition.setBackground(new java.awt.Color(45, 36, 34));
        lblCondition.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblCondition.setForeground(new java.awt.Color(230, 220, 215));
        lblCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Condition.png"))); // NOI18N
        lblCondition.setText("jLabel1");
        lblCondition.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        lblCondition.setIconTextGap(10);
        lblCondition.setPreferredSize(new java.awt.Dimension(150, 45));
        pnlSpecRow.add(lblCondition);

        lblColor.setBackground(new java.awt.Color(45, 36, 34));
        lblColor.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblColor.setForeground(new java.awt.Color(230, 220, 215));
        lblColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Color.png"))); // NOI18N
        lblColor.setText("jLabel1");
        lblColor.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        lblColor.setIconTextGap(10);
        lblColor.setPreferredSize(new java.awt.Dimension(150, 45));
        pnlSpecRow.add(lblColor);

        lblPlateNumber.setBackground(new java.awt.Color(45, 36, 34));
        lblPlateNumber.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblPlateNumber.setForeground(new java.awt.Color(230, 220, 215));
        lblPlateNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/PlateNumber.png"))); // NOI18N
        lblPlateNumber.setText("jLabel1");
        lblPlateNumber.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        lblPlateNumber.setIconTextGap(10);
        lblPlateNumber.setPreferredSize(new java.awt.Dimension(150, 45));
        pnlSpecRow.add(lblPlateNumber);

        lblMileage.setBackground(new java.awt.Color(45, 36, 34));
        lblMileage.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        lblMileage.setForeground(new java.awt.Color(230, 220, 215));
        lblMileage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrentalsystem/ui/user/Icons/Mileage.png"))); // NOI18N
        lblMileage.setText("jLabel1");
        lblMileage.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        lblMileage.setIconTextGap(10);
        lblMileage.setPreferredSize(new java.awt.Dimension(150, 45));
        pnlSpecRow.add(lblMileage);

        add(pnlSpecRow, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 980, 130));

        spDescription.setBorder(null);
        spDescription.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spDescription.setOpaque(false);
        spDescription.setPreferredSize(new java.awt.Dimension(980, 150));

        taDescription.setEditable(false);
        taDescription.setBackground(new java.awt.Color(45, 36, 34));
        taDescription.setColumns(20);
        taDescription.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        taDescription.setForeground(new java.awt.Color(230, 220, 215));
        taDescription.setRows(5);
        taDescription.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 18, 15, 18));
        taDescription.setCaretColor(new java.awt.Color(230, 220, 215));
        taDescription.setOpaque(false);
        spDescription.setViewportView(taDescription);

        add(spDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 150, 410, 200));
    }// </editor-fold>//GEN-END:initComponents

    private void btnRentNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentNowActionPerformed
        // TODO add your handling code here:
        if (dashboard != null && currentCar != null) {
            // 1. Get the booking panel instance from the dashboard
            carrentalsystem.ui.user.BookingPanel bp = dashboard.getBookingPanel1();

            // 2. Pass the car data to the panel
            bp.prepareBooking(currentCar);

            // 3. Switch the card view
            java.awt.CardLayout cl = (java.awt.CardLayout) dashboard.getPnlMainContent().getLayout();
            cl.show(dashboard.getPnlMainContent(), "bookingCard");
        }
    }//GEN-LAST:event_btnRentNowActionPerformed

    private void btnContactOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactOwnerActionPerformed
        // TODO add your handling code here:
        if (dashboard != null && currentCar != null) {
            // This directs the user to the Inbox tab in the Main Dashboard
            CardLayout cl = (CardLayout) dashboard.getPnlMainContent().getLayout();
            cl.show(dashboard.getPnlMainContent(), "inboxCard");

            // Optional: Pre-select the conversation with the car owner
            dashboard.getInboxPanel().selectConversationWith(currentCar.getOwnerId());
        }
    }//GEN-LAST:event_btnContactOwnerActionPerformed

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        // Soft shadow
        g2.setColor(new java.awt.Color(0, 0, 0, 15));
        g2.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8, 36, 36);
        // White card background
        g2.setColor(java.awt.Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 36, 36);
        g2.dispose();
        super.paintComponent(g);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnContactOwner;
    private javax.swing.JButton btnRentNow;
    private javax.swing.JLabel lblCarImage;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblCondition;
    private javax.swing.JLabel lblFuel;
    private javax.swing.JLabel lblMileage;
    private javax.swing.JLabel lblOwnerName;
    private javax.swing.JLabel lblPlateNumber;
    private javax.swing.JLabel lblPriceValue;
    private javax.swing.JLabel lblSeats;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTransmission;
    private javax.swing.JPanel pnlSpecRow;
    private javax.swing.JScrollPane spDescription;
    private javax.swing.JTextArea taDescription;
    // End of variables declaration//GEN-END:variables
}
