package com.example.temperature_humidity.model;

public class DeviceModel {
    private String id;
    private String name;
    private String data;
    private String unit;
    private String onThreshold;
    private String offThreshold;
    private String building;
    private String room;

    public DeviceModel(String id, String name, String data, String unit, String onThreshold, String offThreshold, String room, String building) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.unit = unit;
        this.onThreshold = onThreshold;
        this.offThreshold = offThreshold;
        this.building = building;
        this.room = room;
    }

    public DeviceModel() {

    }

    public String getOffThreshold() {
        return offThreshold;
    }

    public String getOnThreshold() {
        return onThreshold;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getUnit() {
        return unit;
    }

    public String getRoom() {
        return room;
    }

    public String getBuilding() {
        return building;
    }
}
