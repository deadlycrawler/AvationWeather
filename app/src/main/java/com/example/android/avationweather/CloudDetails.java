package com.example.android.avationweather;

import java.io.Serializable;

//simple class to hold cloud densitys and altiudes

public class CloudDetails implements Serializable{

    String density;
    String altitude;

    public CloudDetails(String density, String altitiude){
        this.density=density;
        this.altitude=altitiude;

    }

    public String getDensity() {
        return density;
    }

    public String getAltitude() {
        return altitude;
    }
}
