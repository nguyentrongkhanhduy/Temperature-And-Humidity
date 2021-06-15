package com.example.temperature_humidity.model;

import java.sql.Time;
import java.util.List;

public class ApprovedModel {
    private String email;
    private String uid;
    private TimeModel timeModel;


    public ApprovedModel(String email, String uid, TimeModel timeModel) {
        this.email = email;
        this.uid = uid;
        this.timeModel = timeModel;
    }

    public ApprovedModel(){

    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public TimeModel getTimeModel() {
        return timeModel;
    }
}
