/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.Notification;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author macbookairm1grey
 */
public interface INotificationService {
    void send(int userId, String message, String type) throws SQLException;

    List<Notification> getUnread(int userId) throws SQLException;

    void markAllRead(int userId) throws SQLException;

    int countUnread(int userId) throws SQLException; // for bell badge
}
