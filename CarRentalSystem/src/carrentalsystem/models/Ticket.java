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
public class Ticket {
    
    private int ticketId;
    private int userId;
    private String subject;
    private String description;
    private String adminReply;   // null until admin responds
    private String status;       // "OPEN" or "RESOLVED"
    private Timestamp createdAt;

    // ── Extra field populated via JOIN in AdminService ──
    private String userFullName; // from users.full_name — shown in admin table

    public Ticket() {}

    public Ticket(int ticketId, int userId, String subject, String description,
                  String adminReply, String status, Timestamp createdAt) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.subject = subject;
        this.description = description;
        this.adminReply = adminReply;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ── Getters ──────────────────────────────────────────────
    public int getTicketId()          { return ticketId; }
    public int getUserId()            { return userId; }
    public String getSubject()        { return subject; }
    public String getDescription()    { return description; }
    public String getAdminReply()     { return adminReply; }
    public String getStatus()         { return status; }
    public Timestamp getCreatedAt()   { return createdAt; }
    public String getUserFullName()   { return userFullName; }

    // ── Setters ──────────────────────────────────────────────
    public void setTicketId(int ticketId)             { this.ticketId = ticketId; }
    public void setUserId(int userId)                 { this.userId = userId; }
    public void setSubject(String subject)            { this.subject = subject; }
    public void setDescription(String description)    { this.description = description; }
    public void setAdminReply(String adminReply)      { this.adminReply = adminReply; }
    public void setStatus(String status)              { this.status = status; }
    public void setCreatedAt(Timestamp createdAt)     { this.createdAt = createdAt; }
    public void setUserFullName(String userFullName)  { this.userFullName = userFullName; }

    @Override
    public String toString() {
        return "Ticket{ticketId=" + ticketId + ", subject=" + subject
                + ", status=" + status + "}";
    }
}
