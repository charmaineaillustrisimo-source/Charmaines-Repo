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
public class User {

    
    private int userId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private String role;       // "ADMIN" or "USER"
    private String tier;       // "FREE" or "PRO"
    private String status;     // "ACTIVE", "BANNED", "DELETED"
    private boolean isVerified;
    private Timestamp lastLogin;
    private Timestamp createdAt;
    
    public User() {}

    public User(int userId, String fullName, String phoneNumber,
                String email, String password, String role,
                String tier, String status, boolean isVerified,
                Timestamp lastLogin, Timestamp createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.tier = tier;
        this.status = status;
        this.isVerified = isVerified;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
    }

    // ── Getters ──────────────────────────────────────────────
    public int getUserId()          { return userId; }
    public String getFullName()     { return fullName; }
    public String getPhoneNumber()  { return phoneNumber; }
    public String getEmail()        { return email; }
    public String getPassword()     { return password; }
    public String getRole()         { return role; }
    public String getTier()         { return tier; }
    public String getStatus()       { return status; }
    public boolean isVerified()     { return isVerified; }
    public Timestamp getLastLogin() { return lastLogin; }
    public Timestamp getCreatedAt() { return createdAt; }

    // ── Setters ──────────────────────────────────────────────
    public void setUserId(int userId)               { this.userId = userId; }
    public void setFullName(String fullName)         { this.fullName = fullName; }
    public void setPhoneNumber(String phoneNumber)   { this.phoneNumber = phoneNumber; }
    public void setEmail(String email)               { this.email = email; }
    public void setPassword(String password)         { this.password = password; }
    public void setRole(String role)                 { this.role = role; }
    public void setTier(String tier)                 { this.tier = tier; }
    public void setStatus(String status)             { this.status = status; }
    public void setVerified(boolean isVerified)      { this.isVerified = isVerified; }
    public void setLastLogin(Timestamp lastLogin)    { this.lastLogin = lastLogin; }
    public void setCreatedAt(Timestamp createdAt)    { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", fullName=" + fullName + ", role=" + role + "}";
    }
    
}
