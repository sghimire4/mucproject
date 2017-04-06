package com.bignerdranch.android.mucproject;

import android.util.Log;

import static java.lang.Math.cos;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

/**
 * Created by chris91 on 04/04/2017.
 */

public class Position {
    private double latitude;
    private double longitude;
    private long time;

    public Position() {
    }

    public Position(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longitude = longtitude;
    }

    public Position(double latitude, double longtitude, long time) {
        this.latitude = latitude;
        this.longitude = longtitude;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longitude;
    }

    public void setLongtitude(double longtitude) {
        this.longitude = longtitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isPositionWithinRange(Position pos){
        double f1, f2, l1, l2;
        double squaredDf, squaredDl;
        double fMean;
        double radOfEarth = 6371009.0;
        double Distance;
        //converting latitude and logitude from degrees to radians
        // f1 and l1 are the latitude and longitude of point of interest
        // f2 and l2 are the latitude and longitude of current position

        f1 = toRadians(pos.getLatitude());
        l1 = toRadians(pos.getLongtitude());

        f2 = toRadians(getLatitude());
        l2 = toRadians(getLongtitude());

        squaredDf = (f1 - f2)*(f1 - f2);
        squaredDl = (l1 - l2)*(l1 - l2);

        fMean = (f1 + f2)/2;

        Distance = radOfEarth*(sqrt(squaredDf + (cos(fMean)*cos(fMean)*squaredDl)));
        Log.d("Position", "Distance: " + Distance);

        if(Distance <= 3.0){
            return true;
        }
        else{
            return false;
        }
    }
}

