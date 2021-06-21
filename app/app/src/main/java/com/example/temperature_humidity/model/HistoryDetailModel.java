package com.example.temperature_humidity.model;

public class HistoryDetailModel {
    private String id;
    private String name;
    private String data;
    private String unit;
    private String user;
    private String time;
    private String building;
    private String room;
    private String mode;

    public HistoryDetailModel() {

    }

    public HistoryDetailModel(String id, String name, String data, String unit, String user, String time, String building, String room, String mode) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.unit = unit;
        this.user = user;
        this.time = time;
        this.building = building;
        this.room = room;
        this.mode = mode;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoom() {
        return room;
    }

    public String getUnit() {
        return unit;
    }

    public String getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public String getMode() {
        return mode;
    }
}
