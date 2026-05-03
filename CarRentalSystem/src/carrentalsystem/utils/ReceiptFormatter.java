/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.utils;
import carrentalsystem.models.Booking;
import carrentalsystem.models.Car;
import carrentalsystem.models.User;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author macbookairm1grey
 */
public class ReceiptFormatter {
    
    /**
     * Builds the full receipt text that gets stored in
     * receipts.receipt_content. Called by BookingService.completeTransaction()
     * before INSERT into receipts.
     *
     * @param booking The completed Booking object
     * @param car The rented Car object
     * @param renter The User who rented the car
     * @return Formatted receipt string
     */
    public static String format(Booking booking, Car car, User renter) {
        long days = ChronoUnit.DAYS.between(
                booking.getStartDate().toLocalDate(),
                booking.getEndDate().toLocalDate()
        );
        if (days == 0) days = 1;
        
        double dailyRate = car.getBasePrice();
        double totalPrice = booking.getTotalPrice();

        return "===================================\n"
                + "          RENTAL RECEIPT           \n"
                + "===================================\n"
                + "Booking ID   : #" + booking.getBookingId() + "\n"
                + "-----------------------------------\n"
                + "Renter       : " + renter.getFullName() + "\n"
                + "Username     : @" + renter.getUsername() + "\n"
                + "-----------------------------------\n"
                + "Vehicle      : " + car.getBrand() + " " + car.getModel() + "\n"
                + "Type         : " + car.getType() + "\n" // Replaced Plate Number
                + "-----------------------------------\n"
                + "Pick-up Date : " + booking.getStartDate() + "\n"
                + "Return Date  : " + booking.getEndDate() + "\n"
                + "Duration     : " + days + " day(s)\n"
                + "Rate/Day     : PHP " + String.format("%,.2f", dailyRate) + "\n"
                + "-----------------------------------\n"
                + "TOTAL PAID   : PHP " + String.format("%,.2f", totalPrice) + " (Cash)\n"
                + "===================================\n"
                + "    Thank you for using RentACar!  \n"
                + "===================================\n";
    }

    /**
     * Short one-line summary for notification messages. Example: "Your trip for
     * Toyota Vios is complete. PHP 10,000 paid."
     */
    public static String shortSummary(Car car, double totalPrice) {
        return "Your trip for " + car.getBrand() + " " + car.getModel()
                + " is complete. PHP " + String.format("%,.2f", totalPrice) + " paid.";
    }
}
