package com.example.android.avationweather;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Weather> {


    //TODO: add a splash screen and find an artist to make a good spash screen to replace the crap you're going to put in it
    //TODO: fix the settings screen to allow a user to enter a default weather station(after the loader is implimented)
    //TODO: Fix all the strings so that this can be easily translated
    //TODO: make the "plain buttion just start an intent with the metar info no need to wait to call one first
    //TODO: add a sub package for the weather class and objects for weather
    //TODO: fix the metar getting reset on screen rotation

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int WEATHER_LOADER_ID = 1;
    private static boolean noError = true;
    Button FetchMetar;
    Button DetailViewButton;
    EditText userEnterdIcao;
    String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/";
    String userRequestedICAO;
    String concatinatedURL;
    boolean fetched = false;
    Weather mWeather;

    //allows the state of noError to be set
    public static void setNoError(Boolean bool) {
        MainActivity.noError = bool;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml assets
        FetchMetar = (Button) findViewById(R.id.fetchMetar);
        DetailViewButton = (Button) findViewById(R.id.metarDetails);
        userEnterdIcao = (EditText) findViewById(R.id.putICAOhere);
        FetchMetar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                MetarFetch();
            }
        });
        DetailViewButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                detailedView();
            }
        });


    }

    //where it all starts, start is called when you hit the "fetch Metar" button
    //TODO: EVALUATE THIS METHOD AND SEE IF I CANT SIMPLIFY IT
    public void MetarFetch() {
        MainActivity.noError = true;
        this.userRequestedICAO = userEnterdIcao.getText().toString();
        String fetching = "Fetching ";
        //TODO: fix for non 4 digit cases
        if (isConnectedToInternet()) {
            if (userRequestedICAO.length() == 4) {


                concatinatedURL = this.AVWX_REQUEST_URL + userRequestedICAO;
                showToast(fetching + userRequestedICAO);

                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(WEATHER_LOADER_ID, null, this);

            } else {

                showToast("check length of ICAO");
            }
        } else {
            showToast("Check Network Connectivity");

        }

    }

    //called when user presses the detailed View button
    private void detailedView() {

        if (noError) {

            DetailedMetarActivity activty = new DetailedMetarActivity();
            Intent i = new Intent(MainActivity.this, activty.getClass());
            i.putExtra("weatherObject", mWeather);
            startActivity(i);
        } else {
            showToast("Weather station was invalid");
        }
    }

    public void showToast(final String toast) {


        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isConnectedToInternet() {


        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {


        return new WeatherLoader(this, concatinatedURL);
    }

    @Override
    public void onLoadFinished(Loader loader, Weather weather) {
        if (weather == null) {
            return;
        }

        updateUi(weather);


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    //called to update the UI
    private void updateUi(Weather weather) {

        TextView metar = (TextView) findViewById(R.id.putMetarHere);
        metar.setText(weather.getMetar());
        fetched = true;
        mWeather = weather;
    }


}
