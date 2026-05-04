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
public class History {
    private int carId;
    private String brand;
    private String model;
    private double price;
    private Timestamp createdAt;
    private String imagePath;
    
    // Getters 
    public int getCarId()           { return carId; }
    public String getBrand()        { return brand; }
    public String getModel()        { return model; }
    public double getPrice()        { return price; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String getImagePath()    { return imagePath; }
    
    
    // Setters
    public void setCarId(int carId)                 { this.carId = carId; }
    public void setBrand(String brand)              { this.brand = brand; }
    public void setModel(String model)              { this.model = model; }
    public void setPrice(double price)              { this.price = price; }
    public void setCreatedAt (Timestamp createdAt)  { this.createdAt = createdAt; }
    public void setImagePath(String imagePath)      { this.imagePath = imagePath; }
}
