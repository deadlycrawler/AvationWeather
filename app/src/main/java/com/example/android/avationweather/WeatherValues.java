package com.example.android.avationweather;

//this class is to hold the values for various weather paramaters settings, im only making this to simplify the construcer for my weather OBJ

import java.io.Serializable;

public class WeatherValues implements Serializable {

    String mAltimiter;
    String mtemperture;
    String mtime;
    String mwindDirecton;
    String mwindSpeed;
    String gustFactor;
    String visibility;

    public WeatherValues(String Altimiter, String temperture, String time, String windDirecton, String windSpeed, String gustFactor, String visibility){
        mAltimiter = Altimiter;
        mtemperture = temperture;
        mtime = time;
        mwindDirecton = windDirecton;
        mwindSpeed = windSpeed;
        this.gustFactor = gustFactor;
        this.visibility = visibility;

    }

    public String getmAltimiter() {
        return mAltimiter;
    }

    public String getMtemperture() {
        return mtemperture;
    }

    public String getMtime() {
        return mtime;
    }

    public String getMwindDirecton() {
        return mwindDirecton;
    }

    public String getMwindSpeed() {
        return mwindSpeed;
    }

    public String getGustFactor() {
        return gustFactor;
    }

    public String getVisibility() {
        return visibility;
    }
}
