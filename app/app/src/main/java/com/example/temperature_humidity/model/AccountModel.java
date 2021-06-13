package com.example.temperature_humidity.model;

public class AccountModel {
    private String uid;
    private Boolean isAdmin;
    private ProfileModel profileModel;

    public AccountModel(String uid, Boolean isAdmin, ProfileModel profileModel) {
        this.uid = uid;
        this.isAdmin = isAdmin;
        this.profileModel = profileModel;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public String getUid() {
        return uid;
    }


}
