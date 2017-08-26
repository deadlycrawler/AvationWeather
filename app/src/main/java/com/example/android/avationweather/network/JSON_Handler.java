package com.example.android.avationweather.network;

import com.example.android.avationweather.Activities.MainActivity;
import com.example.android.avationweather.Weather.CloudDetails;
import com.example.android.avationweather.Weather.Weather;
import com.example.android.avationweather.Weather.WeatherScales;
import com.example.android.avationweather.Weather.WeatherValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by John on 8/22/2017.
 */

public class JSON_Handler {

    public static Weather extractFeatureFromJson(String weatherJSON) {


        try {
            JSONObject baseJsonResponse = new JSONObject(weatherJSON);
            JSONObject unitScales = baseJsonResponse.getJSONObject("Units");


            JSONArray cloudArray = baseJsonResponse.getJSONArray("Cloud-List");
            ArrayList<CloudDetails> CloudDetailsArrayList = new ArrayList<>();

            if (cloudArray.length() == 0) {
                CloudDetails cloudDetailsClass = new CloudDetails("Clear skies", "every level");
                CloudDetailsArrayList.add(cloudDetailsClass);
            } else {
                for (int i = 0; i < cloudArray.length(); i++) {

                    JSONArray cloudDetails = cloudArray.getJSONArray(i);
                    String cloudDensityString = cloudDetails.getString(0);
                    String cloudAltitudeString = cloudDetails.getString(1);
                    CloudDetails cloudDetailsClass = new CloudDetails(cloudDensityString, cloudAltitudeString);
                    CloudDetailsArrayList.add(cloudDetailsClass);
                }
            }


            String metar = baseJsonResponse.getString("Raw-Report");

            String mAltimiter = baseJsonResponse.getString("Altimeter");
            String mtemperture = baseJsonResponse.getString("Temperature");
            String mtime = baseJsonResponse.getString("Time");
            String mwindDirecton = baseJsonResponse.getString("Wind-Direction");
            String mwindSpeed = baseJsonResponse.getString("Wind-Speed");
            String gustFactor = baseJsonResponse.getString("Wind-Gust");
            String visibility = baseJsonResponse.getString("Visibility");


            String altimeterScale = unitScales.getString("Altimeter");
            String AltitudeScale = unitScales.getString("Altitude");
            String TempScale = unitScales.getString("Temperature");
            String VisiblityScale = unitScales.getString("Visibility");
            String WindSpeedScale = unitScales.getString("Wind-Speed");

            //weatherScales Constructer
            // public WeatherScales(String altimeterScale, String AltitudeScale, String TempScale, String VisiblityScale, String WindSpeedScale)
            WeatherScales weatherScale = new WeatherScales(altimeterScale, AltitudeScale, TempScale, VisiblityScale, WindSpeedScale);


            // public WeatherValues(String Altimiter, String temperture, String time, String windDirecton, String windSpeed, String gustFactor, String visibility){
            WeatherValues weatherValues = new WeatherValues(mAltimiter, mtemperture, mtime, mwindDirecton, mwindSpeed, gustFactor, visibility);

            // weather constructer below
            //  public Weather(String metar, ArrayList<CloudDetails> cloudsArrayList, WeatherValues weatherValues, WeatherScales weatherScale) {

            return new Weather(metar, CloudDetailsArrayList, weatherValues, weatherScale);


        } catch (JSONException e) {
            //if the inital JSON parse fails this method is called to see if the "noError" mesage JSON was called
            try {

//TODO: find way to show toasts from down here or a another way to display the error, now it just over write the metar text
                JSONObject baseJsonResponse = new JSONObject(weatherJSON);
                String Error = baseJsonResponse.getString("Error");

                //sets the state of noError to be false to prevent code from being run on nothing
                MainActivity.setNoError(false);

                return new Weather(Error,null,null,null);
            } catch (JSONException e1) {
                //left blank on purpose
            }
        }

        return null;
    }


}
