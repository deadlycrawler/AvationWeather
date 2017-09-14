package com.example.android.avationweather.Activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.example.android.avationweather.R;
import com.example.android.avationweather.Weather.CloudDetails;
import com.example.android.avationweather.Weather.Weather;
import com.example.android.avationweather.Weather.WeatherScales;
import com.example.android.avationweather.Weather.WeatherValues;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


//this class displays the metar in a text View in a human readable version


public class DetailedMetarActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

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


        // Obtain a reference to the SharedPreferences file  And register to be notified of preference changes
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);


        metarDetails = (TextView) findViewById(R.id.metarDetails);

        weather = (Weather) getIntent().getSerializableExtra("weatherObject");
        weatherValues = weather.getWeatherValues();


        String time = weatherValues.getMtime();

        StringBuilder sbTime = new StringBuilder(time);

        //removes the month prefix now its just the hours and minutes
        sbTime.deleteCharAt(0);
        sbTime.deleteCharAt(0);

        sbTime.deleteCharAt(sbTime.length() - 1);
        //adds a colon to the time so that the display looks more timely
        sbTime.insert(2, ":");
        time = sbTime.toString() + "Z";

        //formats the date basied of the system time, and then adds the date the metar specified, metars only specify day of the month and not

        //gets the current zulu date
        String date = new SimpleDateFormat("MMM yyyy").format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());


        String dateOfMonth;

        //gets the time from metar and truncates everything but the first 2 digits(date)
        StringBuilder sbDate = new StringBuilder(weatherValues.getMtime());
        while (sbDate.length() > 2) sbDate.deleteCharAt(sbDate.length() - 1);
        dateOfMonth = sbDate.toString();


        weatherScales = weather.getWeatherScale();

        String windSpeedScale = weatherScales.getWindSpeedScale();
        String AltScale = weatherScales.getAltimeterScale();
        String DegreeSymbol = "°";//this is just a degree character, JSON responce doesnt have any specified scale so i added in my own
        String visibilityScale = weatherScales.getVisiblityScale();


        String VerbosePart1 =
                "Date: " + dateOfMonth + " " + date + "\n" +
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


        String temp = " ";

        Boolean Celsius;
        Celsius = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.tempScale_key), false);

        String TempScale = " ";

        //truncates un needed Zeros in the Temperature
        DecimalFormat df = new DecimalFormat("0.#");

        //converts Temp depending on the state of Celsius boolean
        if (Celsius) {
            temp = df.format(Double.parseDouble(weatherValues.getMtemperture()));
            TempScale = weatherScales.getTempScale();
        }

        if (!Celsius) {
            temp = df.format(Double.parseDouble(weatherValues.getMtemperture()) * 1.8 + 32);
            TempScale = "F";
        }




        String VerbosePart3 =
                "Visiblity: " + weatherValues.getVisibility() + " " + visibilityScale + "\n" +
                        "Tempature: " + temp + " " + DegreeSymbol + TempScale + "\n" +
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
