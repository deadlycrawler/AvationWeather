package com.example.android.avationweather;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity /*implements SharedPreferences.OnSharedPreferenceChangeListener*/ {

    //TODO: handle more possible netWork errors, what happens when there's no internet or an invalid ICAO is entered
    //TODO: handle response when a non existent airport was selected


    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    Button FetchMetar;
    Button DetailViewButton;
    EditText userEnterdIcao;

    boolean fetched = false;

    Weather mWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchMetar = (Button) findViewById(R.id.fetchMetar);
        DetailViewButton = (Button) findViewById(R.id.metarDetails);
        userEnterdIcao = (EditText) findViewById(R.id.putICAOhere);
//        userEnterdIcao.setSelectAllOnFocus(true);


        //selects all text inside of the edit text when clicked on
//        userEnterdIcao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEnterdIcao.selectAll();
//
//            }
//        });

        FetchMetar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Start();
            }
        });

        DetailViewButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (fetched == true) {

                    DetailedMetarActivity activty = new DetailedMetarActivity();
                    Intent i = new Intent(MainActivity.this, activty.getClass());
                    i.putExtra("weatherObject", mWeather);

                    startActivity(i);


                } else {

                    Toast.makeText(MainActivity.this, "you want me to show you details of nothing?", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Obtain a reference to the SharedPreferences file for this app
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        // prefs.registerOnSharedPreferenceChangeListener(this);


    }


//    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//        if (key.equals(getString(R.string.settings_ICAO_key))) {
//           // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//            String ICAO= key.toString();
//
//
//
//
//            avationAsyncTask task = new avationAsyncTask();
//            task.execute();
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Start() {


        String ICAO = userEnterdIcao.getText().toString();


        if (ICAO.length() == 4) {


            String mIcaoString = "kbab";
            mIcaoString = ICAO;
            String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/" + mIcaoString;
            Context contrxt = MainActivity.this;
            Toast.makeText(getApplicationContext(), "Fetching " + mIcaoString, Toast.LENGTH_LONG).show();
            avationAsyncTask task = new avationAsyncTask(AVWX_REQUEST_URL);
            task.execute();

        } else if (ICAO.length() == 3) {
            String mIcaoString = "kbab";
            mIcaoString = "k" + ICAO;
            String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/" + mIcaoString;
            Context contrxt = MainActivity.this;
            Toast.makeText(getApplicationContext(), "fetthing " + mIcaoString, Toast.LENGTH_LONG).show();
            avationAsyncTask task = new avationAsyncTask(AVWX_REQUEST_URL);
            task.execute();
        } else if (ICAO.length() > 4) {

            Context contrxt = MainActivity.this;
            Toast.makeText(contrxt, "check length of ICAO", Toast.LENGTH_LONG).show();
            Log.d(MainActivity.LOG_TAG, "else running wihtout toast");
        }


    }

    private class avationAsyncTask extends AsyncTask<URL, Void, Weather> {

        String AVWX_REQUEST_URL = "";

        private avationAsyncTask(String AVWX_REQUEST_URL_fromAbove) {
            AVWX_REQUEST_URL = AVWX_REQUEST_URL_fromAbove;
        }


        @Override
        protected Weather doInBackground(URL... urls) {
            URL url = createUrl(AVWX_REQUEST_URL);

            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Context context = MainActivity.this;
                Toast.makeText(context, "fail on makeHttpRequest(url)", Toast.LENGTH_LONG);
            }

            Weather weather = extractFeatureFromJson(jsonResponse);

            return weather;
        }


        @Override
        protected void onPostExecute(Weather weather) {
            if (weather == null) {
                return;
            }

            updateUi(weather);
        }


        private URL createUrl(String stringUrl) {

            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                Context context = MainActivity.this;
                Toast.makeText(context, "fail on HTTPRequest", Toast.LENGTH_LONG).show();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
//TODO: add a way to prevent crashing when the response isn't correct
        private Weather extractFeatureFromJson(String weatherJSON) {


            try {
                JSONObject baseJsonResponse = new JSONObject(weatherJSON);
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


                // Create a new {@link Event} object
                return new Weather(metar, mAltimiter, mtemperture, mtime, mwindDirecton, mwindSpeed, gustFactor, CloudDetailsArrayList);


            } catch (JSONException e) {

                try {
                    JSONObject baseJsonResponse = new JSONObject(weatherJSON);
                    String Error=  baseJsonResponse.getString("Error");
                    Context context = MainActivity.this;
                    Toast.makeText(context,Error,Toast.LENGTH_LONG).show();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }


            return null;
        }

        private void updateUi(Weather weather) {

            TextView metar = (TextView) findViewById(R.id.putMetarHere);
            metar.setText(weather.getMetar());
            fetched = true;
            mWeather = weather;
        }
    }


}
