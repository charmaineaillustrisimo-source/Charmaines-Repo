/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.utils;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
/**
 *
 * @author macbookairm1grey
 */
public class ImageUtil {
    
    /**
     * Load an image from a FILE PATH (stored in cars.image_path in DB)
     * and scale it to the given size. Returns a grey placeholder if missing.
     *
     * Use for: car photos in CarCardPanel and CarDetailPanel.
     */
    public static JLabel loadScaled(String imagePath, int width, int height) {
        JLabel lbl = new JLabel();
        lbl.setPreferredSize(new Dimension(width, height));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0xDDD8D0)); // placeholder grey-beige
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        if (imagePath == null || imagePath.isBlank()) return lbl;

        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            if (img == null) return lbl;
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
            lbl.setOpaque(false);
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
}
