package com.example.temperature_humidity.model;

public class ProfileModel {
    private String bornYear;
    private String email;
    private String id;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getBornYear() {
        return bornYear;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ProfileModel(){

    }

    public ProfileModel(String id, String email, String name, String bornYear){
        this.bornYear = bornYear;
        this.email = email;
        this.id = id;
        this.name = name;
    }
}
