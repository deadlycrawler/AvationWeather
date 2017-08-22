package com.example.android.avationweather;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {


    //TODO: add a splash screen and find an artist to make a good spash screen to replace the crap you're going to put in it
    //TODO: change async task to loader
    //TODO: fix the settings screen to allow a user to enter a default weather station(after the loader is implimented)
    //TODO: Fix all the strings so that this can be easily translated
    //TODO: make the "plain buttion just start an intent with the metar info no need to wait to call one first
    //TODO: add a sub package for the weather class and objects for weather

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    Button FetchMetar;
    Button DetailViewButton;
    EditText userEnterdIcao;

    String requestedWeatherStationICAO;

    boolean fetched = false;
    boolean resuming = false;

    private static boolean noError = true;

    Weather mWeather;


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
    public void MetarFetch() {
        MainActivity.noError=true;

        String ICAO = userEnterdIcao.getText().toString();
        this.requestedWeatherStationICAO = ICAO;
        String fetching = "Fetching ";
        if (resuming) fetching = "Updating ";

        if (isConnectedToInternet()) {
            if (ICAO.length() == 4) {

                String mIcaoString;
                mIcaoString = ICAO;
                String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/" + mIcaoString;
                showToast(fetching + mIcaoString);
                avationAsyncTask task = new avationAsyncTask(AVWX_REQUEST_URL);
                task.execute();

            } else if (ICAO.length() == 3) {
                String mIcaoString;
                mIcaoString = "k" + ICAO;
                String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/" + mIcaoString;

                showToast(fetching + mIcaoString);
                avationAsyncTask task = new avationAsyncTask(AVWX_REQUEST_URL);


                task.execute();
            } else if (ICAO.length() > 4 || ICAO.length() < 3) {

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


    //allows the state of noError to be set
    public static void setNoError(Boolean bool){
        MainActivity.noError=bool;

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








    //AsyncTask does all the things
    private class avationAsyncTask extends AsyncTask<URL, Void, Weather> {


        String AVWX_REQUEST_URL = "";

        private avationAsyncTask(String AVWX_REQUEST_URL_fromAbove) {
            AVWX_REQUEST_URL = AVWX_REQUEST_URL_fromAbove;
        }


        @Override
        protected Weather doInBackground(URL... urls) {
            URL url = Networkhandler.createUrl(AVWX_REQUEST_URL);

            String jsonResponse = "";
            try {

                jsonResponse = Networkhandler.makeHttpRequest(url);
            } catch (IOException e) {
                showToast("fail on makeHttpRequest(url)");
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            }

            Weather weather = JSON_Handler.extractFeatureFromJson(jsonResponse);
            return weather;
        }


        //called when async task is done
        @Override
        protected void onPostExecute(Weather weather) {
            if (weather == null) {
                return;
            }

            updateUi(weather);
        }

        //called to update the UI
        private void updateUi(Weather weather) {

            TextView metar = (TextView) findViewById(R.id.putMetarHere);
            metar.setText(weather.getMetar());
            fetched = true;
            mWeather = weather;
        }
    }


}
