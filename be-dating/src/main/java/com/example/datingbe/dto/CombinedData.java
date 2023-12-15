package com.example.datingbe.dto;

import com.example.datingbe.entity.User;
import lombok.Data;

@Data
public class CombinedData {
    private User userLogged;
    private User userCheckLocation;
    private double myDistance;

    public CombinedData() {
    }

    public CombinedData(User userLogged, User userCheckLocation, double distance) {
        this.userLogged = userLogged;
        this.userCheckLocation = userCheckLocation;
        myDistance = distance;
    }
}
