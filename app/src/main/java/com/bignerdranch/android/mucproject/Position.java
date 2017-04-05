package com.bignerdranch.android.mucproject;

/**
 * Created by chris91 on 04/04/2017.
 */

public class Position {
    private double latitude;
    private double longtitude;
    private long time;


    public Position(double latitude, double longtitude, long time) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

