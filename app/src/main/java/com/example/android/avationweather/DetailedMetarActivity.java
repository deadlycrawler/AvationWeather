package com.example.android.avationweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailedMetarActivity extends AppCompatActivity {

    Weather mWeather;
    TextView metarDetails;


    public DetailedMetarActivity() {
    }


    //TODO: ADD SCALES TO VARIOUS DISPLAYED TEXT EXTRACING FROM json TO ACCOUNT FOR FOREIGN MESUREMENT SYSTEMS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_metar);

        metarDetails = (TextView) findViewById(R.id.metarDetails);

        mWeather = (Weather) getIntent().getSerializableExtra("weatherObject");

        String time = mWeather.getMtime();
        StringBuilder sb=new StringBuilder(time);

        sb.deleteCharAt(sb.length()-1);sb.deleteCharAt(sb.length()-1);sb.deleteCharAt(sb.length()-1);



        time = sb.toString()+" Zulu";


        String VerbosePart1 =
                "time: "+ time+"\n"+
                "Wind direction: " + mWeather.getMwindDirecton() + "\n" +
                "Wind Speed: " + mWeather.getMwindSpeed() + "\n" +
                "Tempature: " + mWeather.getMtemperture() + "\n" +
                "Altimiter setting: " + mWeather.getmAltimiter() + "\n\n";






        String VerboseDisplay = VerbosePart1 + "clouds: ";

        int arraySize = mWeather.getmClouds().size();
        for (int i = 0; i < arraySize; i++) {

            ArrayList<CloudDetails> cloudDetailsArrayList = mWeather.getmClouds();
            String density = cloudDetailsArrayList.get(i).getDensity();
            String altitude = cloudDetailsArrayList.get(i).getAltitude();

            VerboseDisplay = VerboseDisplay + density + " at " + altitude + "\n";


        }


        metarDetails.setText(VerboseDisplay);


    }
}
