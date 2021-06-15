package com.example.temperature_humidity.model;

import java.sql.Time;
import java.util.List;

public class ApprovedModel {
    private String email;
    private TimeModel timeModel;;

    public ApprovedModel(String email, TimeModel timeModel) {
        this.email = email;
        this.timeModel = timeModel;
    }

    public ApprovedModel(){

    }

    public String getEmail() {
        return email;
    }

    public TimeModel getTimeModel() {
        return timeModel;
    }
}
