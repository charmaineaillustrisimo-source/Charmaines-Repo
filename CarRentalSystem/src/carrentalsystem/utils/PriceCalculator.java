/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author macbookairm1grey
 */
public class PriceCalculator {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");

    /**
     * Calculates the total rental cost based on duration and daily rate.
     */
    public static double calculateTotal(String pickUpDate, String returnDate, double dailyRate) {
        try {
            Date d1 = DATE_FORMAT.parse(pickUpDate);
            Date d2 = DATE_FORMAT.parse(returnDate);

            long diffInMillies = Math.abs(d2.getTime() - d1.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            // Minimum 1-day charge even for same-day returns
            if (diffInDays <= 0) {
                diffInDays = 1;
            }

            return diffInDays * dailyRate;
        } catch (Exception e) {
            System.err.println("Price Calculation Error: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Formats the currency to PHP standard (e.g., PHP 2,500.00).
     */
    public static String formatCurrency(double amount) {
        return String.format("PHP %,.2f", amount);
    }
}
