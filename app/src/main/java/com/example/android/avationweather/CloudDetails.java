package com.example.android.avationweather;

import java.io.Serializable;

//simple class to hold cloud densitys and altiudes

//narrowed class access no longer all public now private or package protected

class CloudDetails implements Serializable{

    private String density;
    private String altitude;

    CloudDetails(String density, String altitiude){
        this.density=density;
        this.altitude=altitiude;

    }

    String getDensity() {
        return density;
    }

    String getAltitude() {
        return altitude;
    }
}
