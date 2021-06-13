package com.example.temperature_humidity.model;

import java.util.List;

public class RoomModel {

    private String idRoom;
    private List<TimeModel> listtime;

    public RoomModel(){

    }

    public RoomModel( String idRoom, List<TimeModel> listtime){
        this.idRoom = idRoom;
        this.listtime = listtime;
    }

    public String getIdRoom() { return idRoom; }

    public List<TimeModel> getListtime() {
        return listtime;
    }
}
