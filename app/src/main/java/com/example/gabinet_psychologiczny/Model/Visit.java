package com.example.gabinet_psychologiczny.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;


    private int paymentStatus;
    private int visitStatus;

    public Visit(int serviceId, int patientId, String description, LocalDate day , LocalTime startTime, LocalTime endTime, int paymentStatus, int visitStatus) {
        this.serviceId = serviceId;
        this.patientId = patientId;
        this.description = description;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.paymentStatus = paymentStatus;
        this.visitStatus = visitStatus;
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

    public void setPaymentStatus(int paymentStatus) { this.paymentStatus = paymentStatus; }

    public void setVisitStatus(int visitStatus) { this.visitStatus = visitStatus; }


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

    public LocalDate getDay() { return day; }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public int getVisitStatus() { return visitStatus; }
}
