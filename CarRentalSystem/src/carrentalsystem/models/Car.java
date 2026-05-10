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
    private String brand;
    private String model;
    private String type;
    private int seats;
    private String fuelType;
    private String transmission;      // "Gasoline","Diesel","Electric","Hybrid"
    private String condition;
    private String description;
    private double basePrice;
    private String imagePath;     // local file path stored in DB
    private int viewsCount;
    private boolean isPriority;   // PRO feature — appears first in feed
    private String status;        // "PENDING_APPROVAL","ACTIVE","REJECTED","ARCHIVED"
    private Timestamp createdAt;
    private String  color;
    private String plateNumber;
    private boolean hasDriver = false;
    private double driverFee = 0.0;
    private int year;
    private int mileageLimit = 0;
    private String fuelPolicy = "Full to Full";
    private String houseRules;

    
    public Car() {}
    
    public Car(int carId, int ownerId, String brand, String model,
               String type, int seats, String fuelType,
               String transmission,  String condition, 
               String description, double basePrice, String imagePath,
               int viewsCount, boolean isPriority, String status, Timestamp createdAt,
               String color, String plateNumber, boolean hasDriver, double driverFee,
               int year, int mileageLimit, String fuelPolicy, String houseRules) {
        this.carId = carId;
        this.ownerId = ownerId;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.seats = seats;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.condition = condition;
        this.description = description;
        this.basePrice = basePrice;
        this.imagePath = imagePath;
        this.viewsCount = viewsCount;
        this.isPriority = isPriority;
        this.status = "PENDING_APPROVAL";
        this.createdAt = createdAt;
        this.color = color;
        this.plateNumber = plateNumber;
        this.hasDriver = hasDriver;
        this.driverFee = driverFee;
        this.year = year;
        this.mileageLimit = mileageLimit;
        this.fuelPolicy = fuelPolicy;
        this.houseRules = houseRules;
        
    }

    // ── Getters ──────────────────────────────────────────────
    public int getCarId()             { return carId; }
    public int getOwnerId()           { return ownerId; }
    public String getBrand()          { return brand; }
    public String getModel()          { return model; }
    public String getType()           { return type; }
    public int getSeats()             { return seats; }
    public String getFuelType()       { return fuelType; }
    public String getTransmission()   { return transmission; }
    public String getCondition()      { return condition; }
    public String getDescription()    { return description; }
    public double getBasePrice()      { return basePrice; }
    public String getImagePath()      { return imagePath; }
    public int getViewsCount()        { return viewsCount; }
    public boolean isPriority()       { return isPriority; }
    public String getStatus()         { return status; }
    public Timestamp getCreatedAt()   { return createdAt; }
    public String  getColor()         { return color; }
    public String  getPlateNumber()   { return plateNumber; }
    public boolean isHasDriver()      { return hasDriver; }
    public double  getDriverFee()     { return driverFee; }
    public int     getYear()          { return year; }
    public int     getMileageLimit()  { return mileageLimit; }
    public String  getFuelPolicy()    { return fuelPolicy; }
    public String  getHouseRules()    { return houseRules; }

    // ── Setters ──────────────────────────────────────────────
    public void setCarId(int carId)                   { this.carId = carId; }
    public void setOwnerId(int ownerId)               { this.ownerId = ownerId; }
    public void setBrand(String brand)                { this.brand = brand; }
    public void setModel(String model)                { this.model = model; }
    public void setType(String type)                  { this.type = type; }
    public void setSeats(int seats)                   { this.seats = seats; }
    public void setFuelType(String fuelType)          { this.fuelType = fuelType; }
    public void setTransmission(String transmission)  { this.transmission = transmission; }
    public void setCondition(String condition)        { this.condition = condition; }
    public void setDescription(String description)    { this.description = description; }
    public void setBasePrice(double basePrice)        { this.basePrice = basePrice; }
    public void setImagePath(String imagePath)        { this.imagePath = imagePath; }
    public void setViewsCount(int viewsCount)         { this.viewsCount = viewsCount; }
    public void setPriority(boolean isPriority)       { this.isPriority = isPriority; }
    public void setStatus(String status)              { this.status = status; }
    public void setCreatedAt(Timestamp createdAt)     { this.createdAt = createdAt; }
    public void    setColor(String color)             { this.color = color; }
    public void    setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public void    setHasDriver(boolean hasDriver)    { this.hasDriver = hasDriver; }
    public void    setDriverFee(double driverFee)     { this.driverFee = driverFee; }
    public void    setYear(int year)                  { this.year = year; }
    public void    setMileageLimit(int v)             { this.mileageLimit = v; }
    public void    setFuelPolicy(String v)            { this.fuelPolicy = v; }
    public void    setHouseRules(String v)            { this.houseRules = v; }
    
    @Override
    public String toString() {
        return "Car{" + "brand=" + brand + ", model=" + model + ", price=" + basePrice + ", status=" + status + '}';
    }

    
}
