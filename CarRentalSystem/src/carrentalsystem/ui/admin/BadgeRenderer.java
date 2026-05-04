/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.admin;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints; // Fixed import
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

public class BadgeRenderer extends javax.swing.table.DefaultTableCellRenderer{
    private final java.awt.Color lineGray = new java.awt.Color(80, 80, 80);
    private final java.awt.Color panelBg = new java.awt.Color(48, 48, 46);

    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        javax.swing.JLabel c = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        c.setOpaque(false); // Important: Let us draw the rounded background manually
        
        // Add the horizontal bottom line so it matches other rows
        c.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, lineGray));
        
        String plan = (value != null) ? value.toString().toLowerCase() : "";
        
        if (plan.equals("free")) {
            c.setForeground(new java.awt.Color(30, 144, 255)); // Blue text
        } else if (plan.equals("pro")) {
            c.setForeground(new java.awt.Color(139, 69, 19));  // Brown/Bronze text
        } else {
            c.setForeground(java.awt.Color.WHITE);
        }

        return c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the row background first
        g2.setColor(new java.awt.Color(48, 48, 46));
        g2.fillRect(0, 0, getWidth(), getHeight());

        String text = getText().toLowerCase();
        // Choose Badge Color
        if (text.equals("free")) {
            g2.setColor(new java.awt.Color(200, 230, 255)); // Light Blue Badge
        } else if (text.equals("pro")) {
            g2.setColor(new java.awt.Color(255, 245, 200)); // Light Yellow Badge
        } else {
            g2.setColor(new java.awt.Color(48, 48, 46)); // Default transparent
        }

        // Draw the "Pill" Shape: centered and rounded
        int x = (getWidth() - 70) / 2;
        int y = (getHeight() - 25) / 2;
        g2.fillRoundRect(x, y, 70, 25, 25, 25);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
