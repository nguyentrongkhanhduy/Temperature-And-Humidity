package com.example.temperature_humidity.model;

public class HistoryUserModel {
    private String hisID;
    private TimeModel timeModel;
    private String email;
    private String room;
    private String building;
    private String uid;
    private String type;

    public String getHisID(){return hisID;}

    public TimeModel getTimeModel(){return timeModel;}

    public String getEmail() {
        return email;
    }

    public String getRoom() {
        return room;
    }

    public String getBuilding() {
        return building;
    }

    public String getUid() {
        return uid;
    }

    public String getType() {return type;}

    public HistoryUserModel(){

    }

    public HistoryUserModel(String hisID ,TimeModel timeModel,
                            String email, String room, String building,
                            String uid,String type){
        this.hisID = hisID;
        this.timeModel = timeModel;
        this.email = email;
        this.room = room;
        this.building = building;
        this.uid = uid;
        this.type = type;
    }
}
