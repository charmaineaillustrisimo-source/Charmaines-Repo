/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.admin;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
/**
 *
 * @author EMY2025
 */
public class TableButtonRenderer extends DefaultTableCellRenderer{
    private final Color panelBg = new Color(48, 48, 46);
    private final Color lineGray = new Color(80, 80, 80);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setOpaque(false);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lineGray)); // Maintain row line
        
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background matching jPanel1
        g2.setColor(panelBg);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw the Rounded "View" Button Outline
        g2.setColor(new Color(80, 80, 80)); 
        int w = 80, h = 28;
        int x = (getWidth() - w) / 2;
        int y = (getHeight() - h) / 2;
        g2.drawRoundRect(x, y, w, h, 15, 15);
        
        g2.dispose();
        super.paintComponent(g);
    }
}


