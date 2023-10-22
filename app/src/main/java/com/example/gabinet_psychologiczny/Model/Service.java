package com.example.gabinet_psychologiczny.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "service_table")
public class Service {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "service_id")
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
