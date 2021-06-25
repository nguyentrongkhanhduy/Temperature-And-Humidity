package com.example.temperature_humidity.model;

public class NotificationModel {
    private String time;
    private String temp_humid;
    private String temp_humid_unit;
    private String event;
    private String building;
    private String room;

    public NotificationModel() {}

    public NotificationModel(String time, String temp_humid, String temp_humid_unit, String event, String building, String room) {
        this.time = time;
        this.temp_humid = temp_humid;
        this.temp_humid_unit = temp_humid_unit;
        this.event = event;
        this.building = building;
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public String getTemp_humid() {
        return temp_humid;
    }

    public String getTemp_humid_unit() {
        return temp_humid_unit;
    }

    public String getEvent() {
        return event;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoom() {
        return room;
    }
}
