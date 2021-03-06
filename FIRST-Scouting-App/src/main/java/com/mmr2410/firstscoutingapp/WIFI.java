package com.mmr2410.firstscoutingapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Cooper on 1/27/14.
 */
public class WIFI extends AsyncTask<URL, String, String> {

    BufferedReader reader;
    URL url;
    String tag = "FIRST-Scouting";

    @Override
    protected String doInBackground(URL... urls) {
        url = urls[0];
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        } catch (IOException e) {
            Log.e(tag,"ERROR CODE 201:  "+e.toString());
            Log.e(tag,"You might not have access to the internets");
        }
        try {
//            Log.d(tag,reader.readLine());
            return reader.readLine().toString()+"";
        } catch (IOException e) {
            Log.e(tag,"ERROR CODE 202:  "+e.toString());
        }
        return null;
    }

    protected void onPostExecute(String result) {
        Log.d(tag,result+"");
    }
}