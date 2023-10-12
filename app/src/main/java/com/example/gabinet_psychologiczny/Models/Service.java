package com.example.gabinet_psychologiczny.Models;

public class Service {
    private int id;
    private String name;
    private double pricePerHour;



    public Service(int id, String name, double pricePerHour) {
        this.id = id;
        this.name = name;
        this.pricePerHour = pricePerHour;
    }


    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }
}
