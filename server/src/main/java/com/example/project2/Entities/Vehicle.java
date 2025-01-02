// Vehicle.java
package com.example.project2.Entities;

import jakarta.persistence.*;

@Entity
public class Vehicle {

    // Generate value for ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    // Model for the vehicle (eg. Rav4, Corolla.. )
    @Column(nullable = false)
    private String model;

    // Show if it is in Stock or not (True by default)
    @Column(nullable = false)
    private Boolean inStock = true;

    // the price of the vehicle
    @Column(nullable = false)
    private Long price;

    // the condition of the vehicle such as new, used
    @Column(nullable = false)
    private String condition;

    // the Year of making of the vehicle
    @Column(nullable = false)
    private int year;

    // Model for the vehicle (eg. Toyota, Hyundai.. )
    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String engineType;

    @Column(nullable = true)
    private String imgUrl;

    // Getters and Setters for the Vehicle entities.

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String url) {
        this.imgUrl = url;
    }
}