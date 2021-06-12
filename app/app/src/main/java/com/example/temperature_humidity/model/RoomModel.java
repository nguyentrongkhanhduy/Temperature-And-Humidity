package com.example.temperature_humidity.model;

public class RoomModel {
    private Boolean isUsed;
    private String startPeriod;
    private String endPeriod;

    public RoomModel(){

    }

    public RoomModel(Boolean isUsed, String startPeriod, String endPeriod){
        this.isUsed = isUsed;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public String getStartPeriod() {
        return startPeriod;
    }
}
