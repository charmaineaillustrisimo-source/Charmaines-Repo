/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.utils;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.net.URL;
import javax.imageio.ImageIO;
/**
 *
 * @author macbookairm1grey
 */
public class ImageUtil {
    
    // The directory within your project where images will be saved
    private static final String UPLOAD_DIR = "src/carrentalsystem/resources/uploads/";

    /**
     * Copies a selected file to the project's upload directory.
     * Use this in AddListPanel when the user selects a car photo.
     */
    public static String copyImage(File sourceFile) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            // Create a unique name using timestamp to avoid overwriting
            String fileName = System.currentTimeMillis() + "_" + sourceFile.getName();
            File destFile = new File(dir, fileName);

            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destFile.getPath(); // Store this path in the database
        } catch (Exception e) {
            System.err.println("[ImageUtil] Copy failed: " + e.getMessage());
            return null;
        }
    }
    public static JLabel loadScaled(String imagePath, int width, int height) {
        JLabel lbl = new JLabel();
        lbl.setPreferredSize(new Dimension(width, height));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0xDDD8D0)); // placeholder grey-beige
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        if (imagePath == null || imagePath.isBlank()) {
            lbl.setIcon(null);
            lbl.setText("No Image");
            return lbl;
        }

        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            if (img == null) return lbl;
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
            lbl.setOpaque(false);
            lbl.setText("");
        } catch (Exception e) {
            System.err.println("[ImageUtil] File not found: " + imagePath);
        }
        return lbl;
    }

    /**
     * Load an image from the CLASSPATH (resources folder inside the project).
     * Use for: background images, icons bundled with the project.
     *
     * Example: ImageUtil.loadResource("/resources/images/car-bg-dark.jpg", 1440, 1024)
     */
    public static JLabel loadResource(String resourcePath, int width, int height) {
        JLabel lbl = new JLabel();
        lbl.setPreferredSize(new Dimension(width, height));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0x2C2220));

        try {
            URL url = ImageUtil.class.getResource(resourcePath);
            if (url == null) return lbl;
            BufferedImage img = ImageIO.read(url);
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
            lbl.setOpaque(false);
        } catch (Exception e) {
            System.err.println("[ImageUtil] Resource not found: " + resourcePath);
        }
        return lbl;
    }

    /**
     * Load an icon from the resources/icons folder and scale it.
     * Use for: bell icon, hamburger icon, spec row icons in CarDetailPanel.
     *
     * Example: ImageUtil.loadIcon("/resources/icons/bell.png", 22, 22)
     */
    public static ImageIcon loadIcon(String resourcePath, int width, int height) {
        try {
            URL url = ImageUtil.class.getResource(resourcePath);
            if (url == null) return null;
            BufferedImage img = ImageIO.read(url);
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("[ImageUtil] Icon not found: " + resourcePath);
            return null;
        }
    }

    /**
     * Crop an image into a circle — for the avatar in ProfilePanel.
     * Size = diameter (width = height).
     *
     * Example: ImageUtil.cropCircle(user.getAvatarPath(), 72)
     */
    public static JLabel cropCircle(String imagePath, int size) {
        JLabel lbl = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (getIcon() != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
                    super.paintComponent(g2);
                    g2.dispose();
                } else {
                    // Draw grey circle placeholder
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0xAAAAAA));
                    g2.fillOval(0, 0, size, size);
                    g2.dispose();
                }
            }
        };
        lbl.setPreferredSize(new Dimension(size, size));
        lbl.setOpaque(false);

        if (imagePath == null || imagePath.isBlank()) return lbl;

        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            if (img == null) return lbl;
            Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.err.println("[ImageUtil] Avatar not found: " + imagePath);
        }
        return lbl;
    }
    
    public static void applyScaledImage(JLabel targetLabel, String imagePath, int width, int height) {
        JLabel result = loadScaled(imagePath, width, height);
        targetLabel.setIcon(result.getIcon());
        targetLabel.setText(result.getText());
        targetLabel.setOpaque(result.isOpaque());
    }
    
    public static void applyScaledImage(javax.swing.JButton button, String path, int w, int h) {
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(path);
            java.awt.Image img = icon.getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new javax.swing.ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Error loading button image: " + e.getMessage());
        }
    }
    
    // Add this to your ImageUtil.java if not present
    public static void setInternalIcon(javax.swing.JLabel label, String resourcePath) {
        try {
            java.net.URL imgURL = ImageUtil.class.getResource(resourcePath);
            if (imgURL != null) {
                label.setIcon(new javax.swing.ImageIcon(imgURL));
            }
        } catch (Exception e) {
            System.err.println("Icon not found: " + resourcePath);
        }
    }
    
    
    /**
     * Apply a circular cropped image directly onto an existing JLabel. Use this
     * for profile pictures in ProfilePanel.
     */
    public static void applyCircleImage(javax.swing.JLabel label, String imagePath, int size) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }
        try {
            BufferedImage raw = ImageIO.read(new File(imagePath));
            if (raw == null) {
                return;
            }

            // Create a circular masked image
            BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2 = circle.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            Image scaled = raw.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();

            label.setIcon(new ImageIcon(circle));
            label.setText("");
        } catch (Exception e) {
            System.err.println("[ImageUtil] Circle crop failed: " + e.getMessage());
        }
    }

    /**
     * Apply a circular cropped image onto a JButton (for header profile icon).
     */
    public static void applyCircleImage(javax.swing.JButton button, String imagePath, int size) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }
        try {
            BufferedImage raw = ImageIO.read(new File(imagePath));
            if (raw == null) {
                return;
            }

            BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2 = circle.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            Image scaled = raw.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();

            button.setIcon(new ImageIcon(circle));
        } catch (Exception e) {
            System.err.println("[ImageUtil] Circle crop (button) failed: " + e.getMessage());
        }
    }
}
