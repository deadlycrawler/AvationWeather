package com.example.android.avationweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailedMetarActivity extends AppCompatActivity {

    Weather mWeather;
    TextView metarDetails;



   public DetailedMetarActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_metar);

        metarDetails=(TextView)findViewById(R.id.metarDetails);

        mWeather= (Weather) getIntent().getSerializableExtra("weatherObject");


       String VerboseDisplay="Wind direction: "+ mWeather.getMwindDirecton()+"\n\n\n"+
                             "Wind Speed: "+mWeather.getMwindSpeed()+"\n\n\n"+
                             "Tempature: "+mWeather.getMtemperture()  +"\n\n\n";


    }
}
