package com.example.gabinet_psychologiczny.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Service {

    private int id;

    private String name;
    private double pricePerHour;

    public Service(String name, double pricePerHour) {
        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
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
