package com.example.android.avationweather;


import java.io.Serializable;

//this class is to hold the scales for various mesurement settings, im only making this to simplify the construcer for my weather OBJ
public class WeatherScales implements Serializable {

    String altimeterScale;
    String AltitudeScale;
    String TempScale;
    String VisiblityScale;
    String WindSpeedScale;


    public WeatherScales(String altimeterScale, String AltitudeScale, String TempScale, String VisiblityScale, String WindSpeedScale) {
        this.altimeterScale=altimeterScale;
        this.AltitudeScale=AltitudeScale;
        this.TempScale=TempScale;
        this.VisiblityScale=VisiblityScale;
        this.WindSpeedScale=WindSpeedScale;

    }

    public String getAltimeterScale() {
        return altimeterScale;
    }

    public String getAltitudeScale() {
        return AltitudeScale;
    }

    public String getTempScale() {
        return TempScale;
    }

    public String getVisiblityScale() {
        return VisiblityScale;
    }

    public String getWindSpeedScale() {
        return WindSpeedScale;
    }
}
