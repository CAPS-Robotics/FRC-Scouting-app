package com.mmr2410.firstscoutingapp;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Cooper on 1/28/14.
 */
public class DataHandling {

    JSONObject jobject,actor;
    JSONArray jarray;
    JsonReader jreader;
    ArrayList<String>strings;
    String tag = "FIRST-Scouting";

    /**
     * Get string from a json file and from a specific variable type.
     * @param url
     * @param variable
     * @return
     */
    public ArrayList<String> getString(URL url,String variable){
        try {
            jobject = new JSONObject(new WIFI().execute(url).get());
        } catch (JSONException e) {
            Log.e(tag,e.toString());
        } catch (InterruptedException e) {
            Log.e(tag,e.toString());
        } catch (ExecutionException e) {
            Log.e(tag,e.toString());
        }
        strings = new ArrayList<String>();
        try {
            jarray = jobject.getJSONArray("data");
            for(int i = 0; i<jarray.length();i++){
                actor = jarray.getJSONObject(i); //try jarray.getString();
                strings.add(actor.getString(variable));
            }
            return strings;
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
