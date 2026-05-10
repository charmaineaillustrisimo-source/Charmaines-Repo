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
public class Review {
    private int       reviewId;
    private int       carId;
    private int       reviewerId;
    private String    reviewerName;
    private int       rating;
    private String    comment;
    private Timestamp createdAt;

    public int    getReviewId()                    { return reviewId; }
    public void   setReviewId(int v)               { reviewId = v; }
    public int    getCarId()                       { return carId; }
    public void   setCarId(int v)                  { carId = v; }
    public int    getReviewerId()                  { return reviewerId; }
    public void   setReviewerId(int v)             { reviewerId = v; }
    public String getReviewerName()                { return reviewerName; }
    public void   setReviewerName(String v)        { reviewerName = v; }
    public int    getRating()                      { return rating; }
    public void   setRating(int v)                 { rating = v; }
    public String getComment()                     { return comment; }
    public void   setComment(String v)             { comment = v; }
    public Timestamp getCreatedAt()                { return createdAt; }
    public void   setCreatedAt(Timestamp v)        { createdAt = v; }
}
