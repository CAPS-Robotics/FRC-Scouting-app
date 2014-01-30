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
    String s1;



    /**
     * Get string from a json file and from a specific variable type.
     * @param url
     * @param variable
     * @return
     */
    public ArrayList<String> getString(URL url,String variable){
        try {
            jobject = new JSONObject(new WIFI().execute(url).get().toString());
        } catch (JSONException e) {
            Log.e(tag,"ERROR CODE 301:  "+e.toString());
        } catch (InterruptedException e) {
            Log.e(tag,"ERROR CODE 302:  "+e.toString());
        } catch (ExecutionException e) {
            Log.e(tag,"ERROR CODE 303:  "+e.toString());
        }
        strings = new ArrayList<String>();
        try {
            Log.d(tag,jobject.getJSONArray("data").toString());
            jarray = jobject.getJSONArray("data");
            for(int i = 0; i<jarray.length();i++){
                actor = jarray.getJSONObject(i);
                strings.add(actor.getString(variable));
            }
            return strings;
        } catch (JSONException e) {
            Log.e(tag,"ERROR CODE 304:  "+ e.toString());
            return null;
        }
    }

//    public String getString(String data,String variable){
//        try {
//            jobject = new JSONObject(data);
//        } catch (JSONException e) {
//            Log.e(tag,e.toString());
//        }
//        s1 = "";
//        for(String s: strings){
//            s1 += s + ", ";
//        }
//
//        return s1;
//    }

}
