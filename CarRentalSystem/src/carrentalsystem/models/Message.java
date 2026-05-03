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
public class Message {
    
    private int messageId;
    private int senderId;
    private int receiverId;
    private int carId;
    private int bookingId;
    private String content;
    private boolean read;
    private Timestamp createdAt;

    // ── Transient display fields (not in DB, set by service) ──
    private String senderName;
    private String senderAvatar;   // file path
    private String carName;        // brand + model
    
    // ── Getters ──────────────────────────────────────────────
    public int getMessageId()            { return messageId; }
    public int getSenderId()             { return senderId; }
    public int getReceiverId()           { return receiverId; }
    public int getCarId()                { return carId; }
    public int getBookingId()            { return bookingId; }
    public String getContent()           { return content; }
    public boolean isRead()              { return read; }
    public Timestamp getCreatedAt()      { return createdAt; }
    public String getSenderName()        { return senderName; }
    public String getSenderAvatar()      { return senderAvatar; }
    public String getCarName()           { return carName; }
    
    // ── Setters ──────────────────────────────────────────────
    public void setMessageId(int v)      { this.messageId = v; }
    public void setSenderId(int v)       { this.senderId = v; }
    public void setReceiverId(int v)     { this.receiverId = v; }
    public void setCarId(int v)          { this.carId = v; }
    public void setBookingId(int v)      { this.bookingId = v; }
    public void setContent(String v)     { this.content = v; }
    public void setRead(boolean v)       { this.read = v; }
    public void setCreatedAt(Timestamp v){ this.createdAt = v; }
    public void setSenderName(String v)  { this.senderName = v; }
    public void setSenderAvatar(String v){ this.senderAvatar = v; }
    public void setCarName(String v)     { this.carName = v; }
    
}
