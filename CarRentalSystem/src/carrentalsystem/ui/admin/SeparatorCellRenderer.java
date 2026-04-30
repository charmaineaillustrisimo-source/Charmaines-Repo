/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.admin;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
/**
 *
 * @author EMY2025
 */
public class SeparatorCellRenderer extends DefaultTableCellRenderer{
    private static final Color SEPARATOR_COLOR = new Color(150, 150, 150);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        
        // Let the default renderer handle standard cell colors and value display
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // --- THE CUSTOM SEPARATOR LOGIC ---
        
        // 1. Set the background/foreground to match your dark theme
        // You can customize this further to match your existing logic.
        if (!isSelected) {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        // 2. Add the Bottom Line Border
        // MatteBorder args: Top, Left, Bottom, Right, Color
        // This draws a line only 1 pixel tall at the BOTTOM.
        setBorder(new MatteBorder(0, 0, 1, 0, SEPARATOR_COLOR));

        // Optional: Ensure the text is vertically centered in the row
        setVerticalAlignment(SwingConstants.CENTER);

        return this;
    }
}
