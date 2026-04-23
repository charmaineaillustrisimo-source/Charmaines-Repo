/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.Booking;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author macbookairm1grey
 */
public interface IBookingService {
    int submitRequest(Booking booking) throws SQLException;

    void confirmBooking(int bookingId) throws SQLException;

    void completeTransaction(int bookingId) throws SQLException; // owner clicks Complete

    void cancelBooking(int bookingId, String reason) throws SQLException;

    List<Booking> getBookingsByRenter(int renterId) throws SQLException;

    List<Booking> getBookingsByOwner(int ownerId) throws SQLException;
}
