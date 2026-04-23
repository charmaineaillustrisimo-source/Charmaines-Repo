/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.core;

/**
 *
 * @author macbookairm1grey
 */
public class AppConstants {
    // Account roles

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String TIER_FREE = "FREE";
    public static final String TIER_PRO = "PRO";
    
// Account statuses
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_BANNED = "BANNED";
    public static final String STATUS_DELETED = "DELETED";
    
// Car statuses
    public static final String CAR_PENDING = "PENDING_APPROVAL";
    public static final String CAR_ACTIVE = "ACTIVE";
    public static final String CAR_REJECTED = "REJECTED";
    public static final String CAR_ARCHIVED = "ARCHIVED";
    
// Booking statuses
    public static final String BOOKING_PENDING = "PENDING";
    public static final String BOOKING_CONFIRMED = "CONFIRMED";
    public static final String BOOKING_SUCCESSFUL = "SUCCESSFUL";
    public static final String BOOKING_REJECTED = "REJECTED";
    public static final String BOOKING_CANCELLED = "CANCELLED";
    
// Listing limits
    public static final int FREE_LISTING_LIMIT = 1;
    public static final int IDLE_TIMEOUT_MS = 10 * 60 * 1000; // 10 minutes
}
