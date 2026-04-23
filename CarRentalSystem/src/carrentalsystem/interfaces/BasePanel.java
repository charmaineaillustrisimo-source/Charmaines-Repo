/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.interfaces;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author macbookairm1grey
 */
public abstract class BasePanel extends JPanel{
    protected JLabel lblLoading = new JLabel("Loading...", SwingConstants.CENTER);

    public BasePanel() {
        setLayout(new BorderLayout());
        lblLoading.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    /**
     * REQUIRED: all data loading goes here, called on open and on refresh
     */
    public abstract void loadData();

    protected void showLoading() {
        add(lblLoading, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    protected void hideLoading() {
        remove(lblLoading);
        revalidate();
        repaint();
    }
}
