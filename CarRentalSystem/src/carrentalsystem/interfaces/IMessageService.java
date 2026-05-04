/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.Message;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author macbookairm1grey
 */
public interface IMessageService {
    /**
     * Returns the latest message per conversation partner for a user. Used to
     * populate the left conversation list in InboxPanel.
     */
    List<Message> getConversations(int userId) throws SQLException;

    /**
     * Returns the full ordered message thread between two users about a car.
     * Pass carId = 0 to get all messages between the two users regardless of
     * car.
     */
    List<Message> getThread(int userId, int otherUserId, int carId) throws SQLException;

    /**
     * Inserts a new message row into the messages table. Pass carId = 0 if not
     * related to a specific car.
     */
    void sendMessage(int senderId, int receiverId, int carId, String content)
            throws SQLException;

    /**
     * Marks all messages from senderId to receiverId as is_read = TRUE. Called
     * when the receiver opens the thread.
     */
    void markThreadRead(int receiverId, int senderId) throws SQLException;

    /**
     * Returns count of unread messages for a user. Used to show the badge
     * number on the bell/inbox icon.
     */
    int countUnread(int userId) throws SQLException;
}
