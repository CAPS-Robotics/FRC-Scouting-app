package com.mmr2410.firstscoutingapp;

import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
    String dirLocation;

    public DataHandling(String loc){
        dirLocation = loc;
    }

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
            e.printStackTrace();
        }
    }

    public void writeJSONArray(String var, ArrayList content){
        try {
            jwriter.name(var);
            jwriter.beginArray();
            for(Object s1: content){
                jwriter.value(s1.toString());
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

    public void newJSONName(String var,ArrayList content){
        try {
            jwriter.name(var);
            jwriter.beginArray();
            for(Object o:content){
                jwriter.value(o+"");
            }
            jwriter.endArray();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 315:  " + e.toString());
        }
    }

    public String getJSONStringFromMatch(String fileName, int matchNum, String var){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(dirLocation+"schedules/"+fileName)));
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
            reader = new BufferedReader(new FileReader(new File(dirLocation+"schedules/"+fileName)));
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
            Log.e(tag, "ERROR CODE 318:  " + e.getMessage());
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

    public void saveScouting(String fileName, String matchNum, ArrayList<Integer> redAlliance, ArrayList<Integer> blueAlliance, ArrayList<Integer> autoScore, ArrayList<Boolean> autoAttempts,
                             ArrayList<String> autoNotes,ArrayList<Integer> groundScore,ArrayList<Integer> groundAttempts,ArrayList<Integer> topScore,ArrayList<Integer> topAttempts,
                             ArrayList<Integer> hotScore,ArrayList<Integer> trussThrows,ArrayList<Integer> trussAttempts,ArrayList<Integer> totalScore,ArrayList<Integer> catches,
                             ArrayList<Integer> catchAttempts,ArrayList<Integer> speed,ArrayList<String> finalNotes ) {
        File f = new File(dirLocation+"scouted/"+fileName+"/"+matchNum);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        beginJSON(fos);

        newJSONName("match", matchNum);
        writeJSONArray("redAlliance",redAlliance);
        writeJSONArray("blueAlliance",blueAlliance);
        writeJSONArray("autoScore",autoScore);
        writeJSONArray("autoAttempts",autoAttempts);
        writeJSONArray("autoNotes",autoNotes);
        writeJSONArray("groundScore",groundScore);
        writeJSONArray("groundAttempts",groundAttempts);
        writeJSONArray("topScore",topScore);
        writeJSONArray("topAttempts",topAttempts);
        writeJSONArray("hotScore",hotScore);
        writeJSONArray("trussThrows",trussThrows);
        writeJSONArray("trussAttempts",trussAttempts);
        writeJSONArray("totalScore",totalScore);
        writeJSONArray("catches",catches);
        writeJSONArray("catchAttempts",catchAttempts);
        writeJSONArray("speed",speed);
        writeJSONArray("finalNotes",finalNotes);

        Log.d(tag,"saved");
    }
    
    /**
     * Will merge all files in a directory to a single file (name specified), will delete previous files if deleteIndividuleFiles is true.
     * Specify full path with directory param.
     */
    public File mergeFilesInDir(String directory, String fileName, Boolean deleteIndividuleFiles){
        
        File dir = new File(directory);
        
        File[] files = dir.listFiles();
        
        FileInputStream fis;
        
        File finalFile = new File(directory,fileName);
        
        FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(finalFile, false);
			 out = new BufferedWriter(fstream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        Log.d(tag,files.length+"");
        
        for(File f: files){
            try {
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
				String aLine;
				while ((aLine = in.readLine()) != null) {
                    Log.d(tag,f.getName()+",  "+aLine);
					out.write(aLine);
                    out.newLine();
				}
 
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        }
        try {
            out.close();
            out.flush();
        } catch (IOException e) {
           Log.e(tag,e.getMessage());
        }
        
        return finalFile;
        
    }

    public ArrayList getJSONValuesFromAnalytics(File file,String var) {
        ArrayList list = new ArrayList();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }

        ArrayList<JSONObject> sections = new ArrayList<JSONObject>();

        String nextSection = "";
        try {
            nextSection = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(nextSection!=null){
            JSONObject jo = null;
            try {
                jo = new JSONObject(nextSection);

                list.add(jo.get(var));

                nextSection = reader.readLine();

            } catch (JSONException e) {
                Log.e(tag,"tried to turn something that wasn't a json object into an object");
                Log.e(tag,"what it tried to convert: \""+nextSection+"\"");
            } catch (IOException e) {
                Log.e(tag,"Failed to read next line");
            }


        }

        return list;
    }

    public ArrayList filterTeams(File f, ArrayList<String> var, ArrayList value){
        ArrayList teams = new ArrayList();

        ArrayList teamNumbers = getJSONValuesFromAnalytics(f,"teamNum");

        boolean flag;
        int x = 0;
        for(Object o: teamNumbers){
            flag = true;
            int y = 0;
            for(String s: var){
                if(!getJSONValuesFromAnalytics(f,s).get(x).toString().equals(value.get(y).toString())){
                    flag = false;
                }
                y++;
            }
            x++;
            if(flag){
                teams.add(o);
            }
        }

        //

        return teams;
    }
}
