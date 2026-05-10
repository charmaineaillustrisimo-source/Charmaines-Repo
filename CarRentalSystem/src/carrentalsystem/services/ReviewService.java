/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.models.Review;
import java.sql.*;
import java.util.*;

/**
 *
 * @author macbookairm1grey
 */
public class ReviewService {
    public List<Review> getReviewsForCar(int carId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql
                = "SELECT cr.*, u.full_name AS reviewer_name "
                + "FROM car_reviews cr "
                + "JOIN users u ON cr.reviewer_id = u.user_id "
                + "WHERE cr.car_id = ? ORDER BY cr.created_at DESC LIMIT 20";
        try (Connection c = carrentalsystem.core.DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setCarId(rs.getInt("car_id"));
                r.setReviewerId(rs.getInt("reviewer_id"));
                r.setReviewerName(rs.getString("reviewer_name"));
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(r);
            }
        }
        return list;
    }

    /**
     * Submit a review — one per user per car (DB has UNIQUE constraint).
     */
    public void submitReview(int carId, int reviewerId, int rating, String comment)
            throws SQLException {
        String sql
                = "INSERT INTO car_reviews (car_id, reviewer_id, rating, comment) "
                + "VALUES (?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE rating=VALUES(rating), comment=VALUES(comment)";
        try (Connection c = carrentalsystem.core.DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setInt(2, reviewerId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            ps.executeUpdate();
        }
    }
}
