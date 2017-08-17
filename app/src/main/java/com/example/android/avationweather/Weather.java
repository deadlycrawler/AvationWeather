package com.example.android.avationweather;


import java.io.Serializable;
import java.util.ArrayList;

public class Weather implements Serializable{


    private String metar;
    String mAltimiter;
    String mtemperture;
    String mtime;
    String mwindDirecton;
    String mwindSpeed;

    ArrayList<CloudDetails> mClouds;



    public Weather(String metar, String Altimiter, String temperture, String time, String windDirecton, String windSpeed, ArrayList<CloudDetails> cloudsArrayList) {

        mAltimiter=Altimiter;
        mtemperture=temperture;
        mtime=time;
        mwindDirecton=windDirecton;
        mwindSpeed=windSpeed;
        this.metar=metar;

        mClouds=cloudsArrayList;

    }
    public String getMetar() {
        return metar;
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

    public ArrayList<CloudDetails> getmClouds() {
        return mClouds;
    }
}
