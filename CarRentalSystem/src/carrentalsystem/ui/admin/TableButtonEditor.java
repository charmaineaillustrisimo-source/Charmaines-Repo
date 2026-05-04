/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalsystem.ui.admin;
import java.awt.Component;
import javax.swing.DefaultCellEditor; // Required for 'extends'
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
/**
 *
 * @author EMY2025
 */
public class TableButtonEditor extends DefaultCellEditor{
    private JButton button;
    private String label;
    private boolean isPushed;
    
    public TableButtonEditor(JTextField textField) {
        super(textField);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "View" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // ACTION: Put your logic here (e.g., show a popup or change panel)
            JOptionPane.showMessageDialog(button, "Viewing details for this user!");
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}

