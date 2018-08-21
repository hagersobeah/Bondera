package com.example.ea.bondera2;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import  com.example.ea.bondera2.MapsActivity;
import com.google.firebase.database.Exclude;

/**
 * Created by ea on 11/30/2017.
 */

public class Trip {

    //on trip start
    public String tripID;
    public String destination;
    public String origin;
    public String totalDistance;
    public String startTime;
    //on trip finish
    public String endTime;
    public String tripDuration;
    public String tripFare;




    public Trip(){

    }

    public Trip(String endTime, String tripDuration) {
        this.endTime = endTime;
        this.tripDuration = tripDuration;
    }

    public Trip(String tripID, String destination, String origin, String totalDistance, String startTime, String endTime, String tripDuration, String tripFare) {
        this.tripID = tripID;
        this.destination = destination;
        this.origin = origin;
        this.totalDistance = totalDistance;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tripDuration = tripDuration;
        this.tripFare = tripFare;
    }
    public Trip( String destination, String origin, String totalDistance, String startTime, String endTime, String tripDuration, String tripFare) {
        this.destination = destination;
        this.origin = origin;
        this.totalDistance = totalDistance;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tripDuration = tripDuration;
        this.tripFare = tripFare;
    }

    public Trip(String origin){
        this.origin=origin;
    }

    public Trip(String origin,String destination, String startTime, String totalDistance) {
        this.origin = origin;
        this.destination = destination;
        this.totalDistance = totalDistance;
        this.startTime = startTime;
    }

    public String getOrigin() { return origin; }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getTripFare() {
        return tripFare;
    }

    public void setTripFare(String tripFare) {
        this.tripFare = tripFare;
    }

   /* @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tripID", tripID);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("origin", origin);
        result.put("destination", destination);
        result.put("totalDistance", totalDistance);
        result.put("tripDuration", tripDuration);
        result.put("tripFare", tripFare);

        return result;
    }*/
}

