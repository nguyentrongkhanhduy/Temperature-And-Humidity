package com.example.temperature_humidity.model;

public class UsableRooms {
    private String building;
    private String room;

    public UsableRooms() {

    }

    public UsableRooms(String building, String room) {
        this.building = building;
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoom() {
        return room;
    }
}
