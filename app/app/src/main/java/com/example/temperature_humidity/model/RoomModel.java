package com.example.temperature_humidity.model;

import java.util.List;

public class RoomModel {

    private String idRoom;
    private List<ApprovedModel> approvedModel;

    public RoomModel(){

    }

    public RoomModel( String idRoom, List<ApprovedModel> approvedModel){
        this.idRoom = idRoom;
        this.approvedModel = approvedModel;
    }

    public String getIdRoom() { return idRoom; }

    public List<ApprovedModel> getListtime() {
        return approvedModel;
    }
}
