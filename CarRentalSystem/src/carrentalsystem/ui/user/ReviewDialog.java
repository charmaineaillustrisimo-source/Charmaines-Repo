/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.ui.user;
import carrentalsystem.models.User;
import carrentalsystem.services.ReviewService;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author macbookairm1grey
 */
public class ReviewDialog extends JDialog {

    private int rating = 0;
    private JTextArea txtComment;
    private JButton[] starButtons = new JButton[5];
    private boolean submitted = false;

    public ReviewDialog(Frame parent, int carId, int reviewerId) {
        super(parent, "Leave a Review", true);
        setLayout(new BorderLayout(10, 10));
        setSize(400, 350);
        setLocationRelativeTo(parent);

        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Star Rating Panel
        JPanel pnlStars = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (int i = 0; i < 5; i++) {
            int index = i + 1;
            starButtons[i] = new JButton("☆");
            starButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 30));
            starButtons[i].setBorderPainted(false);
            starButtons[i].setContentAreaFilled(false);
            starButtons[i].addActionListener(e -> setRating(index));
            pnlStars.add(starButtons[i]);
        }

        txtComment = new JTextArea(5, 20);
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        txtComment.setBorder(BorderFactory.createTitledBorder("Your Experience"));

        JButton btnSubmit = new JButton("Submit Review");
        btnSubmit.setBackground(new Color(45, 36, 34)); // Match your theme
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.addActionListener(e -> {
            if (rating == 0) {
                JOptionPane.showMessageDialog(this, "Please select a star rating.");
                return;
            }
            try {
                new ReviewService().submitReview(carId, reviewerId, rating, txtComment.getText());
                submitted = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        pnlContent.add(new JLabel("How was your experience?"));
        pnlContent.add(pnlStars);
        pnlContent.add(new JScrollPane(txtComment));
        pnlContent.add(Box.createVerticalStrut(10));
        pnlContent.add(btnSubmit);

        add(pnlContent, BorderLayout.CENTER);
    }

    private void setRating(int r) {
        this.rating = r;
        for (int i = 0; i < 5; i++) {
            starButtons[i].setText(i < r ? "⭐" : "☆");
        }
    }

    public boolean isSubmitted() {
        return submitted;
    }
}
