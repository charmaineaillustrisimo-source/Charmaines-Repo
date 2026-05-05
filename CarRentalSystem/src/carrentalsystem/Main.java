/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem;
import carrentalsystem.ui.user.MainDashboard;
/**
 *
 * @author macbookairm1grey
 */
public class Main {
   
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // fallback to default
        }

        java.awt.EventQueue.invokeLater(() -> new MainDashboard().setVisible(true));
    }

}
