package com.mmr2410.firstscoutingapp;

import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            jwriter.flush();
            jwriter.close();
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

    public String getJSONStringFromMatch(String fileName, int matchNum, String var){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("storage/sdcard0/FIRST-Scouting/schedules/"+fileName)));
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }

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

    public String getJSONStringFromMatch(File file, int matchNum, String var){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }

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

    public ArrayList<String> getJSONArrayFromMatch(String fileName, int matchNum, String var){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("storage/sdcard0/FIRST-Scouting/schedules/"+fileName)));
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }
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
            jarray = jobject2.getJSONArray(var);
            ArrayList<String> info = new ArrayList<String>();
            for(int i = 0;i<jarray.length();i++){
                info.add(jarray.get(i).toString());
            }
            return info;
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 318:  " + e.toString());
        }
        return null;
    }

    public ArrayList<String> getJSONArrayFromMatch(File file, int matchNum, String var){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }
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
            jarray = jobject2.getJSONArray(var);
            ArrayList<String> info = new ArrayList<String>();
            for(int i = 0;i<jarray.length();i++){
                info.add(jarray.get(i).toString());
            }
            return info;
        } catch (JSONException e) {
            Log.e(tag, "ERROR CODE 318:  " + e.toString());
        }
        return null;
    }

    public void updateJSONString(String fileLocation, int matchNum, String var, String content){
        FileInputStream in = null;
        BufferedReader reader;
        File f = new File(fileLocation);
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fileLocation);
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        }
        reader = new BufferedReader(new InputStreamReader(in));

        try {
            f.createNewFile();
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e2) {
            Log.e(tag, e2.toString());
        } catch (IOException e) {
            Log.e(tag, e.toString());
        }

        try {
            jobject = new JSONObject(reader.readLine());
            jarray = jobject.getJSONArray("data");
            jobject2 = jarray.getJSONObject(matchNum);
            jobject2.put(var, content);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.write(jobject.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateJSONArray(String fileLocation, int matchNum, String var, ArrayList<String> content){
        FileInputStream in = null;
        BufferedReader reader;
        File f = new File(fileLocation);
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fileLocation);
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        }
        reader = new BufferedReader(new InputStreamReader(in));

        JSONArray jarray2 = new JSONArray();
        for(String s:content){
            jarray2.put(s);
        }

        try {
            jobject = new JSONObject(reader.readLine());
            jarray = jobject.getJSONArray("data");
            jobject2 = jarray.getJSONObject(matchNum);
            jobject2.put(var,jarray2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            f.createNewFile();
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e2) {
            Log.e(tag, e2.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }


        try {
            out.write(jobject.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
