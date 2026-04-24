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
public class Receipt {
    
    private int receiptId;
    private int bookingId;
    private String receiptContent;  // full formatted receipt text stored in DB
    private Timestamp issuedAt;

    // ── Extra fields populated via JOIN in BookingService ──
    private String carModel;        // for display in ReceiptFrame
    private String renterFullName;  // for display in ReceiptFrame

    public Receipt() {}

    public Receipt(int receiptId, int bookingId,
                   String receiptContent, Timestamp issuedAt) {
        this.receiptId = receiptId;
        this.bookingId = bookingId;
        this.receiptContent = receiptContent;
        this.issuedAt = issuedAt;
    }

    // ── Getters ──────────────────────────────────────────────
    public int getReceiptId()           { return receiptId; }
    public int getBookingId()           { return bookingId; }
    public String getReceiptContent()   { return receiptContent; }
    public Timestamp getIssuedAt()      { return issuedAt; }
    public String getCarModel()         { return carModel; }
    public String getRenterFullName()   { return renterFullName; }

    // ── Setters ──────────────────────────────────────────────
    public void setReceiptId(int receiptId)               { this.receiptId = receiptId; }
    public void setBookingId(int bookingId)               { this.bookingId = bookingId; }
    public void setReceiptContent(String receiptContent)  { this.receiptContent = receiptContent; }
    public void setIssuedAt(Timestamp issuedAt)           { this.issuedAt = issuedAt; }
    public void setCarModel(String carModel)              { this.carModel = carModel; }
    public void setRenterFullName(String renterFullName)  { this.renterFullName = renterFullName; }

    @Override
    public String toString() {
        return "Receipt{receiptId=" + receiptId + ", bookingId=" + bookingId + "}";
    }
}
