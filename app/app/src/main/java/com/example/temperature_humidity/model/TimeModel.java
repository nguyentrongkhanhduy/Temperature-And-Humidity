package com.example.temperature_humidity.model;

public class TimeModel {
    private String startTime;
    private String endTime;
    private String date;

    /*
        TimeModel exampleTime {
            startTime = "14/6/2021 2"
            endTime = "14/6/2021 4"
        }
    */

    public String getDate() {
        return date;
    }

    public TimeModel() {}

    public TimeModel(String startTime, String endTime, String date){
        this.date = date;
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
