/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.models.Payment;
import java.sql.*;
/**
 *
 * @author macbookairm1grey
 */
public class PaymentService {
    /**
     * Fetch the payment record for a booking.
     */
    public Payment getPaymentByBookingId(int bookingId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE booking_id = ? LIMIT 1";
        try (Connection c = carrentalsystem.core.DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapPayment(rs);
            }
        }
        return null;
    }

    /**
     * Save a payment record and update booking status to SUCCESSFUL.
     */
    public int recordPayment(Payment p) throws SQLException {
        String sql
                = "INSERT INTO payments "
                + "(booking_id, renter_id, payment_method, reference_number, "
                + " base_amount, driver_fee, fuel_charge, insurance_fee, "
                + " damage_amount, other_charges, discount_amount, security_deposit, "
                + " total_amount, amount_paid, remaining_balance, payment_status) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'COMPLETED')";

        try (Connection c = carrentalsystem.core.DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getBookingId());
            ps.setInt(2, p.getRenterId());
            ps.setString(3, p.getPaymentMethod());
            ps.setString(4, p.getReferenceNumber());
            ps.setDouble(5, p.getBaseAmount());
            ps.setDouble(6, p.getDriverFee());
            ps.setDouble(7, p.getFuelCharge());
            ps.setDouble(8, p.getInsuranceFee());
            ps.setDouble(9, p.getDamageAmount());
            ps.setDouble(10, p.getOtherCharges());
            ps.setDouble(11, p.getDiscountAmount());
            ps.setDouble(12, p.getSecurityDeposit());
            ps.setDouble(13, p.getTotalAmount());
            ps.setDouble(14, p.getAmountPaid());
            ps.setDouble(15, p.getRemainingBalance());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                p.setPaymentId(id);
                // Mark booking as SUCCESSFUL and create receipt notification
                completeBooking(p);
                return id;
            }
        }
        return -1;
    }

    private void completeBooking(Payment p) throws SQLException {
        // 1. Update booking status
        try (Connection c = carrentalsystem.core.DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(
                "UPDATE bookings SET status='SUCCESSFUL' WHERE booking_id=?")) {
            ps.setInt(1, p.getBookingId());
            ps.executeUpdate();
        }

        // 2. Insert receipt record
        String rcNum = String.format("RC-%04d-%06d",
                java.time.Year.now().getValue(), p.getBookingId());
        try (Connection c = carrentalsystem.core.DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO receipts (booking_id, receipt_content, receipt_number, renter_id) "
                + "VALUES (?,?,?,?)")) {
            ps.setInt(1, p.getBookingId());
            ps.setString(2, "Payment completed. Amount: ₱" + p.getTotalAmount());
            ps.setString(3, rcNum);
            ps.setInt(4, p.getRenterId());
            ps.executeUpdate();
        }

        // 3. Send receipt notification to renter
        String msg = "🧾 Your receipt " + rcNum + " is ready! "
                + "Click here to view your rental receipt.";
        try (Connection c = carrentalsystem.core.DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO notifications (user_id, message, type) VALUES (?,?,'RECEIPT')")) {
            ps.setInt(1, p.getRenterId());
            ps.setString(2, msg);
            ps.executeUpdate();
        }
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setBookingId(rs.getInt("booking_id"));
        p.setRenterId(rs.getInt("renter_id"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setReferenceNumber(rs.getString("reference_number"));
        p.setBaseAmount(rs.getDouble("base_amount"));
        p.setDriverFee(rs.getDouble("driver_fee"));
        p.setFuelCharge(rs.getDouble("fuel_charge"));
        p.setInsuranceFee(rs.getDouble("insurance_fee"));
        p.setDamageAmount(rs.getDouble("damage_amount"));
        p.setOtherCharges(rs.getDouble("other_charges"));
        p.setDiscountAmount(rs.getDouble("discount_amount"));
        p.setSecurityDeposit(rs.getDouble("security_deposit"));
        p.setTotalAmount(rs.getDouble("total_amount"));
        p.setAmountPaid(rs.getDouble("amount_paid"));
        p.setRemainingBalance(rs.getDouble("remaining_balance"));
        return p;
    }
}
