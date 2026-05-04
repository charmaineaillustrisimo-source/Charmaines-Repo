/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package carrentalsystem.ui.admin;
import javax.swing.*;
import java.awt.*;

public class CirclePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        // We don't call super.paintComponent so it stays transparent
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        // Draws a circle that fills the entire panel
        g2d.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
