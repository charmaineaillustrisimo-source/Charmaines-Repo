/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.models;
import java.sql.Date;
import java.sql.Timestamp;
/**
 *
 * @author macbookairm1grey
 */
public class Booking {
    
    private int bookingId;
    private int carId;
    private int renterId;
    private Date startDate;
    private Date endDate;
    private double totalPrice;
    private String imagePath;
    private String status; // "PENDING","CONFIRMED","SUCCESSFUL","REJECTED","CANCELLED"
    private Timestamp createdAt;
    private String renterName;
    private String ownerName;

    // ── Extra fields populated via JOIN in BookingService (not in DB column) ──
    private String carBrand;
    private String carModel;
    private int ownerId;       // from cars.owner_id — needed by InboxPanel
    private String pickupLocation;
    private String returnLocation;
    private int daysCount = 1;
    private String pickupTime;
    private String returnTime;
    private String purpose = "Personal";
    private double damageAmount = 0.0;
    private String damageNote;


    public Booking() {}

    public Booking(int bookingId, int carId, int renterId, Date startDate,
                   Date endDate, double totalPrice, String imagePath, String status,
                   Timestamp createdAt, int daysCount, String pickupTime, String returnTime,
                   String purpose, double damageAmount, String damageNote) {
        this.bookingId = bookingId;
        this.carId = carId;
        this.renterId = renterId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.imagePath = imagePath;
        this.status = status;
        this.createdAt = createdAt;
        this.daysCount = daysCount;
        this.pickupTime = pickupTime;
        this.returnTime = returnTime;
        this.purpose = purpose;
        this.damageAmount = damageAmount;
        this.damageNote = damageNote;
    }

    // ── Getters ──────────────────────────────────────────────
    public int getBookingId()           { return bookingId; }
    public int getCarId()               { return carId; }
    public int getRenterId()            { return renterId; }
    public Date getStartDate()          { return startDate; }
    public Date getEndDate()            { return endDate; }
    public double getTotalPrice()       { return totalPrice; }
    public String getImagePath()        { return imagePath; }
    public String getStatus()           { return status; }
    public Timestamp getCreatedAt()     { return createdAt; }
    public String getCarBrand()         { return carBrand; }
    public String getCarModel()         { return carModel; }
    public int getOwnerId()             { return ownerId; }
    public String getPickupLocation()   { return pickupLocation; }
    public String getReturnLocation()   { return returnLocation; }
    public String getRenterName()       { return renterName; }
    public String getOwnerName()        { return ownerName; }
    public int    getDaysCount()        { return daysCount; }
    public String getPickupTime()       { return pickupTime; }
    public String getReturnTime()       { return returnTime; }
    public String getPurpose()          { return purpose; }
    public double getDamageAmount()     { return damageAmount; }
    public String getDamageNote()       { return damageNote; }

    
    // ── Setters ──────────────────────────────────────────────
    public void setBookingId(int bookingId)           { this.bookingId = bookingId; }
    public void setCarId(int carId)                   { this.carId = carId; }
    public void setRenterId(int renterId)             { this.renterId = renterId; }
    public void setStartDate(Date startDate)          { this.startDate = startDate; }
    public void setEndDate(Date endDate)              { this.endDate = endDate; }
    public void setTotalPrice(double totalPrice)      { this.totalPrice = totalPrice; }
    public void setImagePath (String imagePath)       { this.imagePath = imagePath; }
    public void setStatus(String status)              { this.status = status; }
    public void setCreatedAt(Timestamp createdAt)     { this.createdAt = createdAt; }
    public void setCarBrand(String carBrand)          { this.carBrand = carBrand; }
    public void setCarModel(String carModel)          { this.carModel = carModel; }
    public void setOwnerId(int ownerId)               { this.ownerId = ownerId; }
    public void setPickupLocation(String v)           { this.pickupLocation = v; }
    public void setReturnLocation(String v)           { this.returnLocation = v; }
    public void setRenterName(String name)            { this.renterName = name; }
    public void setOwnerName(String name)             { this.ownerName = name; }
    public void   setDaysCount(int v)                 { this.daysCount = v; }
    public void   setPickupTime(String v)             { this.pickupTime = v; }
    public void   setReturnTime(String v)             { this.returnTime = v; }
    public void   setPurpose(String v)                { this.purpose = v; }
    public void   setDamageAmount(double v)           { this.damageAmount = v; }
    public void   setDamageNote(String v)             { this.damageNote = v; }
    
    @Override
    public String toString() {
        return "Booking{bookingId=" + bookingId + ", carId=" + carId
                + ", status=" + status + "}";
    }
}
