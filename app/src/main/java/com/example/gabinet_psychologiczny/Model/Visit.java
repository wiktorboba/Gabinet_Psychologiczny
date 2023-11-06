package com.example.gabinet_psychologiczny.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "visit_table", foreignKeys = {

        @ForeignKey(
                entity = Patient.class,
                parentColumns = "patient_id",
                childColumns = "patient_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),

        @ForeignKey(
                entity = Service.class,
                parentColumns = "service_id",
                childColumns = "service_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})
public class Visit {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "visit_id")
    private int id;

    @ColumnInfo(name = "service_id", index = true)
    private int serviceId;

    @ColumnInfo(name = "patient_id", index = true)
    private int patientId;
    private String description;


    //TODO change to Date or LocalDate
    private String day;
    private String startTime;
    private String endTime;


    //TODO change to enum
    private boolean paymentStatus;
    private boolean tookPlaceStatus;

    public Visit(int serviceId, int patientId, String description, String day , String startTime, String endTime, boolean paymentStatus, boolean tookPlaceStatus) {
        this.serviceId = serviceId;
        this.patientId = patientId;
        this.description = description;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.paymentStatus = paymentStatus;
        this.tookPlaceStatus = tookPlaceStatus;
    }


    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setServiceId(int id) {
        this.serviceId = id;
    }

    public void setPatientId(int id) {
        this.patientId = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // Getters
    public int getId() {
        return id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getDescription() {
        return description;
    }

    public String getDay() { return day; }

    public String getStartTime() { return startTime; }

    public String getEndTime() {
        return endTime;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public boolean isTookPlaceStatus() {
        return tookPlaceStatus;
    }
}
