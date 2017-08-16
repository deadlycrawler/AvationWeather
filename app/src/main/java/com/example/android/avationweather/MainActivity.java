package com.example.android.avationweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String LOG_TAG = MainActivity.class.getSimpleName();
//    private String testJSON = "{\n" +
//            "  \"Altimeter\": \"3000\", \n" +
//            "  \"Cloud-List\": [\n" +
//            "    [\n" +
//            "      \"OVC\", \n" +
//            "      \"130\"\n" +
//            "    ]\n" +
//            "  ], \n" +
//            "  \"Dewpoint\": \"18\", \n" +
//            "  \"Flight-Rules\": \"VFR\", \n" +
//            "  \"Other-List\": [], \n" +
//            "  \"Raw-Report\": \"KCVS 161158Z AUTO 27004KT 10SM OVC130 18/18 A3000 RMK AO2 SLP098 T01790179 10199 20173 53010 $\", \n" +
//            "  \"Remarks\": \"RMK AO2 SLP098 T01790179 10199 20173 53010 $\", \n" +
//            "  \"Remarks-Info\": {\n" +
//            "    \"Dew-Decimal\": \"17.9\", \n" +
//            "    \"Temp-Decimal\": \"17.9\"\n" +
//            "  }, \n" +
//            "  \"Runway-Vis-List\": [], \n" +
//            "  \"Station\": \"KCVS\", \n" +
//            "  \"Temperature\": \"18\", \n" +
//            "  \"Time\": \"161158Z\", \n" +
//            "  \"Units\": {\n" +
//            "    \"Altimeter\": \"inHg\", \n" +
//            "    \"Altitude\": \"ft\", \n" +
//            "    \"Temperature\": \"C\", \n" +
//            "    \"Visibility\": \"sm\", \n" +
//            "    \"Wind-Speed\": \"kt\"\n" +
//            "  }, \n" +
//            "  \"Visibility\": \"10\", \n" +
//            "  \"Wind-Direction\": \"270\", \n" +
//            "  \"Wind-Gust\": \"\", \n" +
//            "  \"Wind-Speed\": \"04\", \n" +
//            "  \"Wind-Variable-Dir\": []\n" +
//            "}";

    private static String mIcaoString = "kcvs";
    private static String AVWX_REQUEST_URL = "https://avwx.rest/api/metar/" + mIcaoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avationAsyncTask task = new avationAsyncTask();
        task.execute();

    }


    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settingsDefaultICAO_key))) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            String ICAO= sharedPreferences.getString(getString(R.string.settingsDefaultICAO_key), getString(R.string.settingsDefaultICAO));

            this.mIcaoString=ICAO;
            System.out.println(ICAO);
            avationAsyncTask task = new avationAsyncTask();
            task.execute();
        }
    }

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
                Toast.makeText(context, "fail on HTTPRequest", Toast.LENGTH_LONG);
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
}
