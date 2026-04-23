/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.interfaces;
import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for ALL modal dialogs in the RentACar system.
 *
 * This class extends JDialog — so every class that extends BaseDialog IS a
 * JDialog. Show it with: myDialog.setVisible(true);
 *
 * Use this for: 
 * BookingFormDialog — date picker + price calculator
 * ManageBookingDialog — cancel / re-request booking 
 * EditListingDialog — edit existing car listing 
 * DeleteConfirmDialog — 'Are you sure?' overlays
 * TicketReplyDialog — admin reply to a support ticket
 *
 * HOW TO USE: MyDialog dlg = new MyDialog(parentFrame, 'Dialog Title');
 * dlg.setVisible(true); // blocks until user closes if (dlg.isConfirmed()) { //
 * do something with the result }
 */

public abstract class BaseDialog extends JDialog{
    /**
     * true only if the user clicked the primary action button (not just
     * closed).
     */
    protected boolean confirmed = false;

    /**
     * Constructor.
     *
     * @param parent The JFrame or JPanel's ancestor frame. Pass: (Frame)
     * SwingUtilities.getWindowAncestor(this) when calling from inside a JPanel.
     * @param title The dialog window title shown in the title bar.
     */
    public BaseDialog(Frame parent, String title) {
        super(parent, title, true);  // true = MODAL (blocks parent window)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    /**
     * REQUIRED: validate all input fields before the action runs.
     *
     * Return true → inputs are valid, proceed to onConfirm(). Return false →
     * inputs are invalid, show error, block the action.
     *
     * Example (BookingFormDialog): if (startDate == null) {
     * lblError.setText('Pick a start date'); return false; } if
     * (endDate.before(startDate)) { lblError.setText('End must be after
     * start'); return false; } return true;
     */
    public abstract boolean validateInputs();
    /**
     * REQUIRED: the action to run when the primary button is clicked.
     *
     * Always call validateInputs() first inside this method.
     * If valid: set confirmed = true, call your service method, then dispose().
     *
     * Example (BookingFormDialog):
     *   public void onConfirm() {
     *       if (!validateInputs()) return;
     *       try {
     *           bookingService.submitRequest(buildBooking());
     *           confirmed = true;
     *           dispose();
     *       } catch (SQLException e) {
     *           JOptionPane.showMessageDialog(this, e.getMessage());
     *       }
     *   }
     */
    public abstract void onConfirm();
 
    /**
     * Returns true if the user clicked the primary action button.
     * Returns false if the user just closed the dialog or clicked Cancel.
     * Check this in the calling panel after setVisible(true) returns.
     */
    public boolean isConfirmed() {
    return confirmed;
}
 
    /**
     * Helper: show an error message in an error JLabel inside the dialog.
     * Call this from validateInputs() when a field is wrong.
     * The label must already exist in your dialog's UI.
     */
    protected void showError(JLabel lblError, String message) {
    lblError.setText(message);
    lblError.setVisible(true);
    pack(); // resize dialog if label appears
}
 
    /**
     * Helper: hide the error label (call when user starts fixing input).
     */
    protected void hideError(JLabel lblError) {
    lblError.setText("");
    lblError.setVisible(false);
}
 
    /**
     * Helper: wire the primary button to onConfirm().
     * Call this inside your buildUI() method after creating the button.
     * Example: wireConfirmButton(btnBook);
     */
    protected void wireConfirmButton(JButton btn) {
    btn.addActionListener(e -> onConfirm());

    }
}
