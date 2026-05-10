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
public class ListerRequirement {
    private int    requirementId;
    private int    userId;
    private String userFullName;   // JOINed from users table for display
    private String userEmail;      // JOINed from users table
    private String ltoDocumentPath;
    private String selfiePhotoPath;
    private String validIdPath;
    private String status;         // PENDING, APPROVED, REJECTED
    private String adminNote;
    private Timestamp submittedAt;
    private Timestamp reviewedAt;
    
    // ── Getters & Setters ─────────────────────────────────────────────────

    public int getRequirementId()                      { return requirementId; }
    public void setRequirementId(int id)               { this.requirementId = id; }

    public int getUserId()                             { return userId; }
    public void setUserId(int userId)                  { this.userId = userId; }

    public String getUserFullName()                    { return userFullName; }
    public void setUserFullName(String name)           { this.userFullName = name; }

    public String getUserEmail()                       { return userEmail; }
    public void setUserEmail(String email)             { this.userEmail = email; }

    public String getLtoDocumentPath()                 { return ltoDocumentPath; }
    public void setLtoDocumentPath(String path)        { this.ltoDocumentPath = path; }

    public String getSelfiePhotoPath()                 { return selfiePhotoPath; }
    public void setSelfiePhotoPath(String path)        { this.selfiePhotoPath = path; }

    public String getValidIdPath()                     { return validIdPath; }
    public void setValidIdPath(String path)            { this.validIdPath = path; }

    public String getStatus()                          { return status; }
    public void setStatus(String status)               { this.status = status; }

    public String getAdminNote()                       { return adminNote; }
    public void setAdminNote(String note)              { this.adminNote = note; }

    public Timestamp getSubmittedAt()                  { return submittedAt; }
    public void setSubmittedAt(Timestamp t)            { this.submittedAt = t; }

    public Timestamp getReviewedAt()                   { return reviewedAt; }
    public void setReviewedAt(Timestamp t)             { this.reviewedAt = t; }

}
