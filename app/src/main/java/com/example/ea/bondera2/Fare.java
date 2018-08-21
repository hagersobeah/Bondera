package com.example.ea.bondera2;

/**
 * Created by Hagar on 2017-12-15.
 */

public class Fare {
    double kilometers;

    public Fare(double kilometers, double estimatedPrice) {
        this.kilometers = kilometers;
        this.estimatedPrice = estimatedPrice;
    }

    double estimatedPrice;
    double totalFare;

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    double addEstimatedTimePrice(double kilometers){  //and duration as second parameter

        return totalFare;
    }
}
