/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.utils;

/**
 *
 * @author macbookairm1grey
 */
public class Validator {
    // ── Email ─────────────────────────────────────────────────
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }

    // ── Password ──────────────────────────────────────────────
    public static boolean isPasswordSecure(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        return hasLetter && hasDigit;
    }

    public static boolean passwordsMatch(String password, String confirm) {
        if (password == null || confirm == null) {
            return false;
        }
        return password.equals(confirm);
    }

    // ── Text fields ───────────────────────────────────────────
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean isValidUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]{3,30}$");
    }

    // ── Price ─────────────────────────────────────────────────
    public static boolean isValidPrice(String priceStr) {
        if (priceStr == null || priceStr.isBlank()) {
            return false;
        }
        try {
            double price = Double.parseDouble(priceStr);
            return price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ── Date ──────────────────────────────────────────────────
    public static boolean isValidDateRange(java.sql.Date startDate,
            java.sql.Date endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return endDate.after(startDate);
    }
}
