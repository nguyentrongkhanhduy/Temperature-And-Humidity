package com.example.temperature_humidity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "temphumid.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String dropQuery = "DROP TABLE Account;";
        String createQuery = "CREATE TABLE Account (" +
                "AccountID  INTEGER PRIMARY KEY," +
                "Username   TEXT    NOT NULL UNIQUE," +
                "Password   TEXT    NOT NULL," +
                "Privilege  TEXT    NOT NULL)"; // +
                /*" CREATE TABLE Information (" +
                "    PhoneNumber TEXT    PRIMARY KEY," +
                "    AccountID   INTEGER UNIQUE" +
                "                        NOT NULL," +
                "    FullName    TEXT," +
                "    BirthYear   INTEGER," +
                "    Email       TEXT    UNIQUE," +
                "    FOREIGN KEY (" +
                "        AccountID" +
                "    )" +
                "    REFERENCES Account (AccountID) " +
                ");" +
                "CREATE TABLE Room (" +
                "    RoomID             INTEGER PRIMARY KEY," +
                "    SensorID           INTEGER NOT NULL," +
                "    Building           TEXT    NOT NULL," +
                "    FireAlarmThreshold REAL    NOT NULL" +
                ");" +
                "CREATE TABLE Device (" +
                "    DeviceID         INTEGER PRIMARY KEY," +
                "    RoomID           INTEGER NOT NULL," +
                "    DeviceType       INTEGER NOT NULL," +
                "    TurnOnThreshold  REAL    NOT NULL," +
                "    TurnOffThreshold REAL    NOT NULL," +
                "    Level            INTEGER," +
                "    FOREIGN KEY (" +
                "        RoomID" +
                "    )" +
                "    REFERENCES Room (RoomID) " +
                ");" +
                "CREATE TABLE MANAGES (" +
                "    Username TEXT    NOT NULL," +
                "    RoomID   INTEGER NOT NULL" +
                "                     UNIQUE," +
                "    FOREIGN KEY (" +
                "        Username" +
                "    )" +
                "    REFERENCES Account (Username)," +
                "    FOREIGN KEY (" +
                "        RoomID" +
                "    )" +
                "    REFERENCES Room (RoomID)," +
                "    PRIMARY KEY (" +
                "        Username," +
                "        RoomID" +
                "    )" +
                ");" +
                "CREATE TABLE Session (" +
                "    AccountID INTEGER NOT NULL," +
                "    RoomID    INTEGER NOT NULL," +
                "    SID       INTEGER UNIQUE" +
                "                      NOT NULL," +
                "    TimeStart TEXT    NOT NULL," +
                "    TimeEnd   TEXT    NOT NULL," +
                "    FOREIGN KEY (" +
                "        AccountID" +
                "    )" +
                "    REFERENCES Account (AccountID)," +
                "    FOREIGN KEY (" +
                "        RoomID" +
                "    )" +
                "    REFERENCES Room (RoomID)," +
                "    PRIMARY KEY (" +
                "        AccountID," +
                "        RoomID," +
                "        SID" +
                "    )" +
                ");" +
                "CREATE TABLE SessionNoti (" +
                "    SID       INTEGER NOT NULL," +
                "    SNID      INTEGER UNIQUE" +
                "                      NOT NULL," +
                "    Event     INTEGER NOT NULL," +
                "    ExactTime TEXT    NOT NULL," +
                "    FOREIGN KEY (" +
                "        SID" +
                "    )" +
                "    REFERENCES Session (SID)," +
                "    PRIMARY KEY (" +
                "        SID," +
                "        SNID" +
                "    )" +
                ");"; */

        //db.execSQL(dropQuery);
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertAccount(DbAccountModel accountModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ID",accountModel.getID());
        cv.put("Username",accountModel.getUsername());
        cv.put("Password",accountModel.getPassword());
        cv.put("Privilege",accountModel.getPrivilege());

        long result = db.insert("Account", null, cv);
        return (result != -1);
    }
    public List<DbAccountModel> getAllAccounts() {
        List<DbAccountModel> listResult = new ArrayList<DbAccountModel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Account";

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(0);
                String Username = cursor.getString(1);
                String Password = cursor.getString(2);
                String Privilege = cursor.getString(3);

                DbAccountModel accountModel = new DbAccountModel(ID,Username,Password,Privilege);

                listResult.add(accountModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listResult;
    }
}
