package com.snipers.wheel.model;

public class User {
    private String name;
    private String phone;
    private String location;

    // Constructors
    public User() {}

    public User(String name, String phone, String location) {
        this.name = name;
        this.phone = phone;
        this.location = location;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
