package com.example.ea.bondera2;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

/**
 * Created by ea on 11/20/2017.
 */

public class User {
    String userID;
    String username;
    String email;
    String dateOfBirth;
    String gender;
    int numberOfTrips;

    public User(String userID, int numberOfTrips) {
        this.userID=userID;
        this.numberOfTrips = numberOfTrips;
    }






    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getNumberOfTrips() {
        return numberOfTrips;
    }

    public void setNumberOfTrips(int numberOfTrips) {
        this.numberOfTrips = numberOfTrips;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUsername(String username) {
        username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }


    public User(String userID,String username, String email, String dateOfBirth, String gender) {
        this.username = username;
        this.userID = userID;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
    public User(){}

    public User(String username, String email, String dateOfBirth) {
        this.username = username;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;


    }
    public User(String userID){
        this.userID= userID;
    }
}
