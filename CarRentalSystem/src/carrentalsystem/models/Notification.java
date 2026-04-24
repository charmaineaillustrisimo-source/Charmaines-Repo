/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.models;
import java.sql.Timestamp;
/**
 *
 * @author macbookairm1grey
 */
public class Notification {
    
    private int notifId;
    private int userId;
    private String message;
    private String type;       // "ALERT", "RECEIPT", "SYSTEM"
    private boolean isRead;
    private Timestamp createdAt;

    public Notification() {}

    public Notification(int notifId, int userId, String message,
                        String type, boolean isRead, Timestamp createdAt) {
        this.notifId = notifId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // ── Getters ──────────────────────────────────────────────
    public int getNotifId()         { return notifId; }
    public int getUserId()          { return userId; }
    public String getMessage()      { return message; }
    public String getType()         { return type; }
    public boolean isRead()         { return isRead; }
    public Timestamp getCreatedAt() { return createdAt; }

    // ── Setters ──────────────────────────────────────────────
    public void setNotifId(int notifId)           { this.notifId = notifId; }
    public void setUserId(int userId)             { this.userId = userId; }
    public void setMessage(String message)        { this.message = message; }
    public void setType(String type)              { this.type = type; }
    public void setRead(boolean isRead)           { this.isRead = isRead; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Notification{notifId=" + notifId + ", type=" + type
                + ", isRead=" + isRead + "}";
    }
}
