package com.example.finalprojectgamebook.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomePostLookingForGame {
    private List<Role> roles = new ArrayList();
    private String UserName;
    private String UserId;
    private String description;
    private String gameName;

    public HomePostLookingForGame(){};

    public HomePostLookingForGame(List<Role> roles, String userName, String userId,String description,String gameName) {
        this.roles = roles;
        UserName = userName;
        UserId = userId;
        this.description = description;
        this.gameName = gameName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getGameName() {
        return gameName;
    }

}
