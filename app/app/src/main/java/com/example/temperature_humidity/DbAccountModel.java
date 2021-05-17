package com.example.temperature_humidity;

public class DbAccountModel {
    private int ID;
    private String Username;
    private String Password;
    private String Privilege;

    @Override
    public String toString() {
        return  " ID = " + ID +
                ", Username =" + Username + '\'' +
                ", Password = " + Password + '\'' +
                ", Privilege = " + Privilege + '\'';
    }

    public DbAccountModel(int ID, String username, String password, String privilege) {
        this.ID = ID;
        Username = username;
        Password = password;
        Privilege = privilege;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPrivilege() {
        return Privilege;
    }

    public void setPrivilege(String privilege) {
        Privilege = privilege;
    }
}
