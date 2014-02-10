package com.mmr2410.firstscoutingapp;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Cooper on 1/28/14.
 */
public class DataHandling {

    JSONObject jobject,jobject2,actor;
    JSONArray jarray;
    JsonReader jreader;
    JsonWriter jwriter;
    ArrayList<String>strings;
    String tag = "FIRST-Scouting";
    int a=0;



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

    public void beginJSON(OutputStream out){
        try {
            jwriter = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            jwriter.beginObject();
        } catch (UnsupportedEncodingException e) {
            Log.e(tag,"ERROR CODE 305:  "+ e.toString());
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 306:  " + e.toString());
        }
    }

    public void endJSON(){
        try {
            jwriter.endObject();
            jwriter.close();
            jwriter.flush();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 307:  " + e.toString());
        }
    }

    public void writeJSONArray(String var, ArrayList<String> content){
        try {
            jwriter.name(var);
            jwriter.beginArray();
            for(String s1: content){
                jwriter.value(s1);
            }
            jwriter.endArray();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 308:  " + e.toString());
        }
    }

    public void writeJSONObject(String var, ArrayList<String> content){
        try {
            jwriter.name(var);
            jwriter.beginArray();
            jwriter.name("content");
            jwriter.beginArray();
            for(String s1: content){
                jwriter.value(s1);
            }
            jwriter.endArray();
            jwriter.endArray();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 309:  " + e.toString());
        }
    }

    public void newJSONArray(String var){
        try {
            jwriter.name(var);
            jwriter.beginArray();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 310:  " + e.toString());
        }
    }

    public void endJSONArray(){
        try {
            jwriter.endArray();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 311:  " + e.toString());
        }
    }

    public void newJSONObject(){
        try {
            jwriter.beginObject();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 312:  " + e.toString());
        }
    }

    public void endJSONObject(){
        try {
            jwriter.endObject();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 313:  " + e.toString());
        }
    }

    public void newJSONName(String var,String content){
        try {
            jwriter.name(var).value(content);
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 314:  " + e.toString());
        }
    }

    public void newJSONName(String var,int content){
        try {
            jwriter.name(var).value(content);
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 315:  " + e.toString());
        }
    }

    public void newJSONName(String var,ArrayList<String> content){
        try {
            jwriter.name(var);
            jwriter.beginArray();
            for(String s:content){
                jwriter.value(s);
            }
            jwriter.endArray();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 315:  " + e.toString());
        }
    }

    public String getJSONStringFromMatch(BufferedReader reader, int matchNum, String var){
        try {
            jobject = new JSONObject(reader.readLine());
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 316:  " + e.toString());
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 317:  " + e.toString());
        }
        try {
            jarray = jobject.getJSONArray("data");
            jobject2 = jarray.getJSONObject(matchNum);
            return jobject2.getString(var);
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 318:  " + e.toString());
        }
        return null;
    }

    public String getJSONStringFromTempFile(BufferedReader reader, String var){
        try {
            jobject = new JSONObject(reader.readLine());
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 316:  " + e.toString());
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 317:  " + e.toString());
        }
        try {
            jarray = jobject.getJSONArray("data");
            jobject2 = jarray.getJSONObject(0);
            return jobject2.getString(var);
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 318:  " + e.toString());
        }
        return null;
    }

    public ArrayList<String> getJSONArrayFromTempFile(BufferedReader reader, String var){
        try {
            jobject = new JSONObject(reader.readLine()); //TODO Null pointer exception here
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 316:  " + e.toString());
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 317:  " + e.toString());
        }
        try {
            jarray = jobject.getJSONArray("data");
            jobject2 = jarray.getJSONObject(0);
            ArrayList<String> info = new ArrayList<String>();
            jarray = jobject2.getJSONArray(var);
            for(int i = 0;i<jarray.length();i++){
                info.add(jarray.get(i).toString());
            }
            return info;
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 318:  " + e.toString());
        }
        return null;
    }
}
