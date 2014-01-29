package com.mmr2410.firstscoutingapp;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Cooper on 1/28/14.
 */
public class DataHandling {

    JSONObject jobject;
    JsonReader jreader;
    String tag = "FIRST-Scouting";

    /**
     * Get string from a json file and from a specific variable type.
     * @param url
     * @param variable
     * @return
     */
    public String getString(URL url,String variable){
        try {
            jobject = new JSONObject(new WIFI().execute(url).get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            return jobject.getString(variable);
        } catch (JSONException e) {
            Log.e(tag, e.toString());
            return null;
        }
    }

    public String getString(String data,String variable){
        try {
            jobject = new JSONObject(data);
        } catch (JSONException e) {
            Log.e(tag,e.toString());
        }
        try {
            return jobject.getString(variable);
        } catch (JSONException e) {
            Log.e(tag, e.toString());
            return null;
        }
    }

}
