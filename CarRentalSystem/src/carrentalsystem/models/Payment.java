/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.models;

/**
 *
 * @author macbookairm1grey
 */
public class Payment {
    private int    paymentId;
    private int    bookingId;
    private int    renterId;
    private String paymentMethod   = "CASH";
    private String referenceNumber;
    private double baseAmount;
    private double driverFee       = 0;
    private double fuelCharge      = 0;
    private double insuranceFee    = 0;
    private double damageAmount    = 0;
    private double otherCharges    = 0;
    private double discountAmount  = 0;
    private double securityDeposit = 2000;
    private double totalAmount;
    private double amountPaid;
    private double remainingBalance = 0;

    public int    getPaymentId()                        { return paymentId; }
    public void   setPaymentId(int v)                   { paymentId = v; }
    public int    getBookingId()                        { return bookingId; }
    public void   setBookingId(int v)                   { bookingId = v; }
    public int    getRenterId()                         { return renterId; }
    public void   setRenterId(int v)                    { renterId = v; }
    public String getPaymentMethod()                    { return paymentMethod; }
    public void   setPaymentMethod(String v)            { paymentMethod = v; }
    public String getReferenceNumber()                  { return referenceNumber; }
    public void   setReferenceNumber(String v)          { referenceNumber = v; }
    public double getBaseAmount()                       { return baseAmount; }
    public void   setBaseAmount(double v)               { baseAmount = v; }
    public double getDriverFee()                        { return driverFee; }
    public void   setDriverFee(double v)                { driverFee = v; }
    public double getFuelCharge()                       { return fuelCharge; }
    public void   setFuelCharge(double v)               { fuelCharge = v; }
    public double getInsuranceFee()                     { return insuranceFee; }
    public void   setInsuranceFee(double v)             { insuranceFee = v; }
    public double getDamageAmount()                     { return damageAmount; }
    public void   setDamageAmount(double v)             { damageAmount = v; }
    public double getOtherCharges()                     { return otherCharges; }
    public void   setOtherCharges(double v)             { otherCharges = v; }
    public double getDiscountAmount()                   { return discountAmount; }
    public void   setDiscountAmount(double v)           { discountAmount = v; }
    public double getSecurityDeposit()                  { return securityDeposit; }
    public void   setSecurityDeposit(double v)          { securityDeposit = v; }
    public double getTotalAmount()                      { return totalAmount; }
    public void   setTotalAmount(double v)              { totalAmount = v; }
    public double getAmountPaid()                       { return amountPaid; }
    public void   setAmountPaid(double v)               { amountPaid = v; }
    public double getRemainingBalance()                 { return remainingBalance; }
    public void   setRemainingBalance(double v)         { remainingBalance = v; }
}
