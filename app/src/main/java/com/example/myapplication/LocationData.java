package com.example.myapplication;

public class LocationData {
    private int Id;
    private String Name;

    // Default constructor required for Firebase
    public LocationData() {}

    public LocationData(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    @Override
    public String toString() {
        return Name; // Display Name in the spinner
    }
}
