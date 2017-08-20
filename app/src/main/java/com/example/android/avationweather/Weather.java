package com.example.android.avationweather;


import java.io.Serializable;
import java.util.ArrayList;

class Weather implements Serializable {


    private ArrayList<CloudDetails> mClouds;
    private WeatherValues weatherValues;
    private WeatherScales weatherScale;

    private String metar;


    Weather(String metar, ArrayList<CloudDetails> cloudsArrayList, WeatherValues weatherValues, WeatherScales weatherScale) {

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
