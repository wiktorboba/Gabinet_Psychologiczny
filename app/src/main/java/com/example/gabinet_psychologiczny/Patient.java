package com.example.gabinet_psychologiczny;

public class Patient {
    private int id;
    private String name;
    private String lastName;
    private int age;
    private String phoneNumber;


    public Patient(int id, String name, String lastName, int age, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }



    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
