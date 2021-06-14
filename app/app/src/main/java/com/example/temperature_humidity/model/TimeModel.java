package com.example.temperature_humidity.model;

public class TimeModel {
    private String startTime;
    private String endTime;

    /*
        TimeModel exampleTime {
            startTime = "14/6/2021 2"
            endTime = "14/6/2021 4"
        }
    */

    public TimeModel() {}

    public TimeModel(String startTime, String endTime){
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
