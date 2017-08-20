package com.example.android.avationweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

//todo: Test this class
public class DetailedMetarActivity extends AppCompatActivity {

    Weather weather;
    TextView metarDetails;
    WeatherValues weatherValues;
    WeatherScales weatherScales;


    public DetailedMetarActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_metar);

        metarDetails = (TextView) findViewById(R.id.metarDetails);

        weather = (Weather) getIntent().getSerializableExtra("weatherObject");
        weatherValues = weather.getWeatherValues();

        String time = weatherValues.getMtime();
        StringBuilder sb = new StringBuilder(time);

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);


        time = sb.toString() + "Z";


        weatherScales = weather.getWeatherScale();

        String windSpeedScale = weatherScales.getWindSpeedScale();
        String TempScale = weatherScales.getTempScale();
        String AltScale = weatherScales.getAltimeterScale();
        String DegreeSymbol = "°";//this is just a degree character, JSON responce doesnt have any specified sclae so i added in my own
        String visibilityScale = weatherScales.getVisiblityScale();


        String VerbosePart1 =
                "Time: " + time + "\n" +
                        "Wind direction: " + weatherValues.getMwindDirecton() + "\n";
        String VerboseWindPart = "Wind Speed: " + weatherValues.getMwindSpeed() + " " + windSpeedScale + "\n";


        String VerbosePart2 = "\n";


        try {
            int gust = Integer.parseInt(weatherValues.getGustFactor());
            VerbosePart2 = "Gusting up to: " + gust + "\n" + "\n";
        } catch (NumberFormatException e) {
            //Left empty on purpose, basically using this instead of an if statement
        }


        String VerbosePart3 =
                "Visiblity: " + weatherValues.getVisibility() + " " + visibilityScale + "\n" +
                        "Tempature: " + weatherValues.getMtemperture() + " "+DegreeSymbol + TempScale + "\n" +
                        "Altimiter setting: " + weatherValues.getmAltimiter() + " " + AltScale + "\n\n";


        String VerboseDisplay = VerbosePart1 + VerboseWindPart + VerbosePart2 + VerbosePart3 + "clouds: ";

        int arraySize = weather.getmClouds().size();
        for (int i = 0; i < arraySize; i++) {

            ArrayList<CloudDetails> cloudDetailsArrayList = weather.getmClouds();
            String density = cloudDetailsArrayList.get(i).getDensity();
            String altitude = cloudDetailsArrayList.get(i).getAltitude();

            VerboseDisplay = VerboseDisplay + density + " at " + altitude + "\n";


        }


        metarDetails.setText(VerboseDisplay);


    }
}
