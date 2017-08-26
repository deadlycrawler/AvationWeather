package com.example.android.avationweather.Weather;

import java.io.Serializable;

//simple class to hold cloud densitys and altiudes

//narrowed class access no longer all public now private or package protected

public class CloudDetails implements Serializable{

    private String density;
    private String altitude;

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
