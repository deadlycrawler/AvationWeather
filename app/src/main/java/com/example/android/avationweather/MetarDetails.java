package com.example.android.avationweather;

//this class holds metar data

public class MetarDetails {
    //TODO: add gust factor to constructor

    String mAltimiter;
    String mtemperture;
    String mtime;
    String mwindDirecton;
    String mwindSpeed;

    public MetarDetails(String Altimiter,String temperture,String time,String windDirecton,String windSpeed) {

         mAltimiter=Altimiter;
         mtemperture=temperture;
         mtime=time;
         mwindDirecton=windDirecton;
         mwindSpeed=windSpeed;

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
}
