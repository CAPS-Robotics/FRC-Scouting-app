package com.mmr2410.firstscoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Cooper on 2/15/14.
 */
public class Bluetooth extends Thread{
    String tag = "FIRST-Scouting";
    BluetoothServerSocket bss;
    UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BufferedReader reader;
    MainActivity parent;

    public Bluetooth(MainActivity parent) {
        this.parent = parent;
    }

    public void recieveFromDevice(){
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();

        try {
            bss = bt.listenUsingRfcommWithServiceRecord(tag, applicationUUID);
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 400:  " + e.toString());
        }
        final MainActivity fparent = parent;
        new Thread(new Runnable() {
            public void run() {
                boolean bool = true;
                int i = 0;
                while(bool){
                    BluetoothSocket connection;
                    try {
                        connection = bss.accept();
                        InputStream stream = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(stream));
                        fparent.handleReceiveFromDevice(reader.readLine());
                        stream.close();
                    } catch (IOException e) {
                        Log.e(tag, "ERROR CODE 401:  " + e.toString());
                    }
                    if(i>5000){
                        Log.d(tag,"didn't recieve data, closing thread");
                        bool = false;
                    }
                    i++;
                }
            }
        }).start();
        try {
            bss.close();
        } catch (IOException e) {
            Log.e(tag, "ERROR CODE 401:  " + e.getMessage());
        }
    }

    public void sendData(String host, String data){
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bt.getBondedDevices();
        BluetoothSocket socket;

        for(BluetoothDevice b:pairedDevices){
            if(b.getName().equals(host)){
                try {
                    Log.d(tag,"sending to "+host+"...");
                    socket = b.createRfcommSocketToServiceRecord(applicationUUID);
                    OutputStream out = socket.getOutputStream();
                    out.write(data.getBytes());
                    socket.close();
                    Log.d(tag,"done.");
                } catch (IOException e) {
                    Log.e(tag, "ERROR CODE 402:  " + e.toString());
                }
            }
        }
    }

    public Set<BluetoothDevice> getPairedDevices(){
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        return bt.getBondedDevices();
    }

    public ArrayList<String> getDeviceNames(){
        ArrayList<String> btDevices = new ArrayList<String>();
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bt.getBondedDevices();

        for(BluetoothDevice b:pairedDevices){
            btDevices.add(b.getName());
        }

        return btDevices;
    }

}
