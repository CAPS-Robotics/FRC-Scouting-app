package com.mmr2410.firstscoutingapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cooper on 1/27/14.
 */
public class WIFI extends AsyncTask<String, String, String> {

    BufferedReader reader;
    URL url;
    String tag = "FIRST-Scouting";

    @Override
    protected String doInBackground(String... urls) {
        try {
            url = new URL(urls[0]);
        } catch (MalformedURLException e) {
            Log.e(tag,e.toString());
        }
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            Log.d(tag,reader.readLine());
            return reader.readLine().toString()+"";
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }
        return null;
    }

    protected void onPostExecute(String result) {
        Log.d(tag,result+"");
    }
}