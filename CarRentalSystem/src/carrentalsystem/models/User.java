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
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String tier;
    private String status;
    private boolean isVerified;
    private Timestamp lastLogin;
    private Timestamp createdAt;;
    private String city;
    private String province;
    private String profileImagePath;

    public User() {}

    public User(int userId, String username, String fullName, String email,
                String password, String role, String tier, String status,
                boolean isVerified, Timestamp lastLogin, Timestamp createdAt, 
                String city, String province, String profileImagePath) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.tier = tier;
        this.status = status;
        this.isVerified = isVerified;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.city = city;
        this.province = province;
        this.profileImagePath = profileImagePath;
    }

    // ── Getters ──────────────────────────────────────────────
    public int getUserId()          { return userId; }
    public String getUsername()     { return username; }
    public String getFullName()     { return fullName; }
    public String getEmail()        { return email; }
    public String getPassword()     { return password; }
    public String getRole()         { return role; }
    public String getTier()         { return tier; }
    public String getStatus()       { return status; }
    public boolean isVerified()     { return isVerified; }
    public Timestamp getLastLogin() { return lastLogin; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String getCity()         { return city; }
    public String getProvince()     { return province; }
    public String getProfileImagePath() { return profileImagePath; }

    // ── Setters ──────────────────────────────────────────────
    public void setUserId(int userId)             { this.userId = userId; }
    public void setUsername(String username)       { this.username = username; }
    public void setFullName(String fullName)       { this.fullName = fullName; }
    public void setEmail(String email)             { this.email = email; }
    public void setPassword(String password)       { this.password = password; }
    public void setRole(String role)               { this.role = role; }
    public void setTier(String tier)               { this.tier = tier; }
    public void setStatus(String status)           { this.status = status; }
    public void setVerified(boolean isVerified)    { this.isVerified = isVerified; }
    public void setLastLogin(Timestamp lastLogin)  { this.lastLogin = lastLogin; }
    public void setCreatedAt(Timestamp createdAt)  { this.createdAt = createdAt; }
    public void setCity(String city)               { this.city = city; }
    public void setProvince(String province)       { this.province = province; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username=" + username
                + ", fullName=" + fullName + ", role=" + role + "}";
    }    
}
