package com.example.android.avationweather;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import static com.example.android.avationweather.MainActivity.LOG_TAG;

//this class is used as part of how to make a Loader Work, its not currently working correctly tho

public class WeatherLoader extends AsyncTaskLoader{

    private String mUrl;
    public WeatherLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    //basicly the same as the async do in background
    @Override
    public Weather loadInBackground() {


        URL url = Networkhandler.createUrl(mUrl);

        String jsonResponse = "";
        try {

            jsonResponse = Networkhandler.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }

        Weather weather = JSON_Handler.extractFeatureFromJson(jsonResponse);
        return weather;
    }
}
