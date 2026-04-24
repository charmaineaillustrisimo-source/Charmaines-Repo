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
public class Car {
    
    private int carId;
    private int ownerId;
    private String model;
    private String brand;
    private int year;
    private String plateNumber;
    private String color;
    private int seats;
    private String fuelType;      // "Gasoline","Diesel","Electric","Hybrid"
    private String description;
    private double basePrice;
    private int mileageLimit;     // 0 = unlimited
    private String fuelPolicy;
    private String houseRules;
    private String imagePath;     // local file path stored in DB
    private int viewsCount;
    private boolean isPriority;   // PRO feature — appears first in feed
    private String status;        // "PENDING_APPROVAL","ACTIVE","REJECTED","ARCHIVED"
    private Timestamp createdAt;
    
    public Car() {}
    
    public Car(int carId, int ownerId, String model, String brand, int year,
               String plateNumber, String color, int seats, String fuelType,
               String description, double basePrice, int mileageLimit,
               String fuelPolicy, String houseRules, String imagePath,
               int viewsCount, boolean isPriority, String status, Timestamp createdAt) {
        this.carId = carId;
        this.ownerId = ownerId;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.plateNumber = plateNumber;
        this.color = color;
        this.seats = seats;
        this.fuelType = fuelType;
        this.description = description;
        this.basePrice = basePrice;
        this.mileageLimit = mileageLimit;
        this.fuelPolicy = fuelPolicy;
        this.houseRules = houseRules;
        this.imagePath = imagePath;
        this.viewsCount = viewsCount;
        this.isPriority = isPriority;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ── Getters ──────────────────────────────────────────────
    public int getCarId()             { return carId; }
    public int getOwnerId()           { return ownerId; }
    public String getModel()          { return model; }
    public String getBrand()          { return brand; }
    public int getYear()              { return year; }
    public String getPlateNumber()    { return plateNumber; }
    public String getColor()          { return color; }
    public int getSeats()             { return seats; }
    public String getFuelType()       { return fuelType; }
    public String getDescription()    { return description; }
    public double getBasePrice()      { return basePrice; }
    public int getMileageLimit()      { return mileageLimit; }
    public String getFuelPolicy()     { return fuelPolicy; }
    public String getHouseRules()     { return houseRules; }
    public String getImagePath()      { return imagePath; }
    public int getViewsCount()        { return viewsCount; }
    public boolean isPriority()       { return isPriority; }
    public String getStatus()         { return status; }
    public Timestamp getCreatedAt()   { return createdAt; }

    // ── Setters ──────────────────────────────────────────────
    public void setCarId(int carId)                   { this.carId = carId; }
    public void setOwnerId(int ownerId)               { this.ownerId = ownerId; }
    public void setModel(String model)                { this.model = model; }
    public void setBrand(String brand)                { this.brand = brand; }
    public void setYear(int year)                     { this.year = year; }
    public void setPlateNumber(String plateNumber)    { this.plateNumber = plateNumber; }
    public void setColor(String color)                { this.color = color; }
    public void setSeats(int seats)                   { this.seats = seats; }
    public void setFuelType(String fuelType)          { this.fuelType = fuelType; }
    public void setDescription(String description)    { this.description = description; }
    public void setBasePrice(double basePrice)        { this.basePrice = basePrice; }
    public void setMileageLimit(int mileageLimit)     { this.mileageLimit = mileageLimit; }
    public void setFuelPolicy(String fuelPolicy)      { this.fuelPolicy = fuelPolicy; }
    public void setHouseRules(String houseRules)      { this.houseRules = houseRules; }
    public void setImagePath(String imagePath)        { this.imagePath = imagePath; }
    public void setViewsCount(int viewsCount)         { this.viewsCount = viewsCount; }
    public void setPriority(boolean isPriority)       { this.isPriority = isPriority; }
    public void setStatus(String status)              { this.status = status; }
    public void setCreatedAt(Timestamp createdAt)     { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Car{carId=" + carId + ", brand=" + brand + ", model=" + model
                + ", status=" + status + "}";
    }
}
