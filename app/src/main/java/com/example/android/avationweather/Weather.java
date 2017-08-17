package com.example.android.avationweather;


import java.io.Serializable;

public class Weather implements Serializable{


    private String metar;
    String mAltimiter;
    String mtemperture;
    String mtime;
    String mwindDirecton;
    String mwindSpeed;

    public Weather(String metar,String Altimiter,String temperture,String time,String windDirecton,String windSpeed) {

        mAltimiter=Altimiter;
        mtemperture=temperture;
        mtime=time;
        mwindDirecton=windDirecton;
        mwindSpeed=windSpeed;
        this.metar=metar;

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
}
