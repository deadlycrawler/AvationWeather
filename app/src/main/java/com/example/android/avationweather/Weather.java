package com.example.android.avationweather;


import java.io.Serializable;
import java.util.ArrayList;

public class Weather implements Serializable {


    ArrayList<CloudDetails> mClouds;
    WeatherValues weatherValues;
    WeatherScales weatherScale;

    private String metar;


    public Weather(String metar, ArrayList<CloudDetails> cloudsArrayList, WeatherValues weatherValues, WeatherScales weatherScale) {

        this.metar = metar;
        this.weatherScale = weatherScale;
        this.weatherValues = weatherValues;
        this.mClouds = cloudsArrayList;
    }

    public String getMetar() {
        return metar;
    }


    public ArrayList<CloudDetails> getmClouds() {
        return mClouds;
    }

    public WeatherValues getWeatherValues() {
        return weatherValues;
    }

    public WeatherScales getWeatherScale() {
        return weatherScale;
    }


}
