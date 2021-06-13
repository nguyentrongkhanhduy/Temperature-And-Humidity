package com.example.temperature_humidity.model;

import java.util.List;

public class RoomModel {

    private String idRoom;
    private List<TimeModel> timeModel;

    public RoomModel(){

    }

    public RoomModel( String idRoom, List<TimeModel> timeModel){
        this.idRoom = idRoom;
        this.timeModel = timeModel;
    }

    public String getIdRoom() { return idRoom; }

    public List<TimeModel> getListtime() {
        return timeModel;
    }
}
