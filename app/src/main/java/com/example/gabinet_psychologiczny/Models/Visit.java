package com.example.gabinet_psychologiczny.Models;

public class Visit {
    private int id;
    private Service service;
    private Patient patient;
    private String description;
    private String startDate;
    private String endDate;

    //TODO change to enum
    private boolean paymentStatus;
    private boolean tookPlaceStatus;



    public Visit(int id, Service service, Patient patient, String description, String startDate, String endDate, boolean paymentStatus, boolean tookPlaceStatus) {
        this.id = id;
        this.service = service;
        this.patient = patient;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentStatus = paymentStatus;
        this.tookPlaceStatus = tookPlaceStatus;
    }


    // Getters
    public int getId() {
        return id;
    }

    public Service getService() {
        return service;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public boolean isTookPlaceStatus() {
        return tookPlaceStatus;
    }
}
