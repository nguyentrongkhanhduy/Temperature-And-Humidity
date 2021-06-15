package com.example.temperature_humidity.model;

public class RequestModel {
    private String reqID;
    private TimeModel timeModel;
    private String email;
    private String room;
    private String building;
    private String uid;

    public TimeModel getTimeModel() {
        return timeModel;
    }

    public String getEmail() {
        return email;
    }

    public String getRoom() {
        return room;
    }

    public String getBuilding() {
        return building;
    }

    public String getReqID() {
        return reqID;
    }

    public String getUid() {
        return uid;
    }

    public RequestModel(){

    }

    public RequestModel(String reqID, TimeModel timeModel, String email, String room, String building, String uid){
        this.reqID = reqID;
        this.timeModel = timeModel;
        this.email = email;
        this.room = room;
        this.building = building;
        this.uid = uid;
    }
}
