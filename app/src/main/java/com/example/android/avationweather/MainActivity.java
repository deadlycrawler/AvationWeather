package com.example.android.avationweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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



public class MainActivity extends AppCompatActivity /*implements SharedPreferences.OnSharedPreferenceChangeListener*/ {

//TODO: handle more possible netWork errors, what happens when theres no internet or an invalid ICAO is entered
    //TODO: enable simplified to make the metar human readable
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Start();
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

    private class avationAsyncTask extends AsyncTask<URL, Void, Weather> {

        String AVWX_REQUEST_URL="";

        private avationAsyncTask(String AVWX_REQUEST_URL_fromAbove){AVWX_REQUEST_URL=AVWX_REQUEST_URL_fromAbove;}


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

        private Weather extractFeatureFromJson(String weatherJSON) {

            try {
                JSONObject baseJsonResponse = new JSONObject(weatherJSON);
                String metar = baseJsonResponse.getString("Raw-Report");
                // Create a new {@link Event} object
                return new Weather(metar);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }

        private void updateUi(Weather weather) {

            TextView metar = (TextView) findViewById(R.id.putMetarHere);
            metar.setText(weather.getMetar());
        }
    }

    public void Start(){

        EditText userEnterdIcao = (EditText)findViewById(R.id.putICAOhere);
        String ICAO= userEnterdIcao.getText().toString();




        if(ICAO.length()==4){


        String mIcaoString = "kbab";
        mIcaoString=ICAO;
        String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/" + mIcaoString;
        Context contrxt=MainActivity.this;
        Toast.makeText(getApplicationContext(),"Fetching "+mIcaoString,Toast.LENGTH_LONG).show();
        avationAsyncTask task = new avationAsyncTask(AVWX_REQUEST_URL);
        task.execute();

        }else if(ICAO.length()==3){
            String mIcaoString = "kbab";
            mIcaoString="k"+ICAO;
            String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/" + mIcaoString;
            Context contrxt=MainActivity.this;
            Toast.makeText(getApplicationContext(),"fetthing "+mIcaoString,Toast.LENGTH_LONG).show();
            avationAsyncTask task = new avationAsyncTask(AVWX_REQUEST_URL);
            task.execute();
        }else if(ICAO.length()>4){

            Context contrxt = MainActivity.this;
            Toast.makeText(contrxt,"check length of ICAO",Toast.LENGTH_LONG).show();
            Log.d(MainActivity.LOG_TAG,"else running wihtout toast");
        }


    }


}
