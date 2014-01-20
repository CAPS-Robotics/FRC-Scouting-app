package com.mmr2410.firstscoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends ActionBarActivity {
    Button hostB, clientB, settingsB, NewScheduleB, ssSaveB,hostStartB;
    Spinner cospinner;
    Spinner mispinner;
    Spinner s1;
    ArrayAdapter<String> communicationOptions;
    ArrayList<String> matchInfoFiles = new ArrayList<String>();
    ArrayList<LinearLayout> DeviceOptionLayouts = new ArrayList<LinearLayout>();
    ArrayList<Spinner> DeviceListSpinners;
    ArrayAdapter<String> adapter;
    File tempFile, fileLocation;
    List<String> files = new ArrayList<String>();
    List<String> btDevices, listBuffer, btDeviceNames = new ArrayList<String>();
    List<String> scheduledFiles = new ArrayList<String>();
    int lastScreen = 0;
    int currentScreen = R.layout.activity_main;
    BluetoothAdapter bt;
    Display display;
    FileOutputStream fos;
    FileInputStream fis;
    InputStream in;
    BufferedReader reader,reader2;


    Point pointBuffer = new Point();
    LinearLayout ssMainLayout, ssGenerated, ll, l1, l2;
    TextView t1, t2, t3, t4, t5, t6, t7;
    EditText e1, e2, matchNumInput, deviceNumInput, ssFileName;
    ArrayList<EditText> teamNums;
    ArrayList<EditText> devices;
    int numTeams = 0;
    int numDevices = 0;
    int componentWidth = 0;
    int loopTimes = 0;
    String stringBuffer;
    String tag = "FIRST-Scouting";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        display = getWindowManager().getDefaultDisplay();
//        setTheme(android.R.style.Theme_Black);
        super.onCreate(savedInstanceState);
        toHomeScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onBackPressed() {
        toHomeScreen();
    }

    public void toHomeScreen() {
        lastScreen = 0;
        setContentView(R.layout.activity_main);
        currentScreen = R.layout.activity_main;
        hostB = (Button) findViewById(R.id.newScheduleB);
        clientB = (Button) findViewById(R.id.ClientB);
        settingsB = (Button) findViewById(R.id.SettingsB);
        hostB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHostScreen();
            }
        });
        clientB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toClientScreen();

            }
        });
        settingsB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toSettingsScreen();
            }
        });

    }

    public void toHostScreen() {
        DeviceListSpinners = new ArrayList<Spinner>();
        scheduledFiles = new ArrayList<String>();
        btDevices = new ArrayList<String>();
        bt = BluetoothAdapter.getDefaultAdapter();
        lastScreen = currentScreen;
        setContentView(R.layout.host);
        ll = (LinearLayout)findViewById(R.id.Devices);
        ll.removeAllViews();
        fileLocation = new File(getFilesDir().getAbsolutePath());
        for (int i = 0; i < fileLocation.listFiles().length; i++) {
            scheduledFiles.add(fileLocation.listFiles()[i].getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scheduledFiles);
        s1 = (Spinner) findViewById(R.id.scheduleSpinner);
        try {
            s1.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(tag,e.toString());
        }

        Set<BluetoothDevice> pairedDevices = bt.getBondedDevices();
        btDevices.add("<None>");
        btDevices.add("<This Device>");
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                btDevices.add(device.getName() + "\n" + device.getAddress() + "");
            }
        }else{
            Log.e(tag,"No bluetooth Devices found, ignoring for now...");
        }

        s1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ll = (LinearLayout)findViewById(R.id.Devices);
                ll.removeAllViews();
                try {
                    in = new FileInputStream(getFilesDir().getAbsolutePath() + "/" + arg0.getSelectedItem().toString());
                    reader = new BufferedReader(new InputStreamReader(in));
                    stringBuffer = reader.readLine();
                } catch (Exception e) {
                    Log.e(tag, e.toString());
                }
                try{
                    numDevices = Integer.parseInt(stringBuffer);
                }catch(Exception e){
                    Log.e(tag,"Error parsing file. Unrecognized file format?");
                }
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, btDevices);
                DeviceListSpinners = new ArrayList<Spinner>();
                for(int i = 1; i<=numDevices;i++){
                    l1 = new LinearLayout(MainActivity.this);
                    l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,100));
                    t1 = new TextView(MainActivity.this);
                    t1.setText("Device " + i + ":");
                    t1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
                    t1.setGravity(Gravity.CENTER_VERTICAL);
                    l1.addView(t1);
                    s1 = new Spinner(MainActivity.this);
                    s1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    s1.setAdapter(adapter);
                    l1.addView(s1);
                    DeviceListSpinners.add(s1);
                    ll.addView(l1);
                }
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, btDevices);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.e(tag,"Nothing selected, how did you do that?");
            }
        });
        currentScreen = R.layout.host;
        NewScheduleB = (Button) findViewById(R.id.newScheduleB);
        NewScheduleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toScheduleSetupScreen();
            }
        });
        s1 = (Spinner)findViewById(R.id.scheduleSpinner);
        hostStartB = (Button)findViewById(R.id.HostStart);
        hostStartB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //makes file for host settings.
                    tempFile = new File(getCacheDir().getAbsolutePath()+"hostInfo");
                    if(tempFile.mkdirs()){
                        tempFile.createNewFile();
                        Log.d(tag,"made temp file");
                    }else{
                        Log.d(tag,"found temp host file in cache directory");
                    }
                }catch(Exception e){
                    Log.e(tag,"unable to find or create temp file");
                }
                try {
                    fos = new FileOutputStream(tempFile);
                    Log.d(tag,"output stream created");
                    s1 = (Spinner)findViewById(R.id.scheduleSpinner);
                    fos.write(s1.getSelectedItem().toString().getBytes());
                    fos.write("\n".getBytes());
                    Log.d(tag,s1.getSelectedItem().toString());
                    for(Spinner s:DeviceListSpinners){
                        fos.write(s.getSelectedItem().toString().getBytes());
                        fos.write("\n".getBytes());
                    }
                    Log.d(tag,"wrote to host temp file");
                    Log.d(tag,"flushing and closing output stream");
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.e(tag,e.toString());
                } catch (IOException e1) {
                    Log.e(tag,e1.toString());
                }
                toHostMonitor();
            }
        });
    }

    public void toClientScreen() {
        lastScreen = currentScreen;
        setContentView(R.layout.client_screen);
        currentScreen = R.layout.client_screen;
    }

    public void toSettingsScreen() {
        lastScreen = currentScreen;
        setContentView(R.layout.settings_screen);
        currentScreen = R.layout.settings_screen;
        cospinner = (Spinner) findViewById(R.id.scheduleSpinner);
        communicationOptions = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, android.R.layout.simple_spinner_item);
        cospinner.setAdapter(communicationOptions);
        mispinner = (Spinner) findViewById(R.id.matchInfoSpinner);

        for (File file : fileLocation.listFiles()) {
            files.add(file.getName());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, files);
        mispinner.setAdapter(adapter);
    }

    public void toScheduleSetupScreen() {
        lastScreen = currentScreen;
        currentScreen = R.layout.schedule_setup;
        setContentView(R.layout.schedule_setup);
        ssMainLayout = (LinearLayout) findViewById(R.id.ssMainL);
        ssGenerated = (LinearLayout) findViewById(R.id.ssGenerated);
        ssSaveB = (Button) findViewById(R.id.ssb);
        ssFileName = (EditText) findViewById(R.id.ssFileName);
        matchNumInput = (EditText) findViewById(R.id.MatchesInput);
        matchNumInput.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    ssGenerated.removeAllViews();
                    teamNums = new ArrayList<EditText>();
                    devices = new ArrayList<EditText>();
                    newMatch(1, Integer.parseInt(matchNumInput.getText().toString()), ssGenerated);
                    return true;
                } else {
                    return false;
                }
            }
        });

        deviceNumInput = (EditText) findViewById(R.id.DeviceNumInputInput);

        ssSaveB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    fos = openFileOutput(ssFileName.getText().toString(), Context.MODE_PRIVATE);
                } catch (FileNotFoundException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
//                                }
                try {
                    stringBuffer = deviceNumInput.getText().toString() + "\n";
                    fos.write(stringBuffer.getBytes());
                    stringBuffer = matchNumInput.getText().toString() + "\n";
                    fos.write(stringBuffer.getBytes());
                    for (EditText e : teamNums) {
                        stringBuffer = e.getText().toString() + " ";
                        fos.write(stringBuffer.getBytes());
                        if (e.getId() == 115) {
                            fos.write(",".getBytes());
                        }
                    }
                    fos.write("\n".getBytes());
                    for (EditText e : devices) {
                        stringBuffer = e.getText().toString() + " ";
                        fos.write(stringBuffer.getBytes());
                        if (e.getId() == 115) {
                            fos.write(",".getBytes());
                        }
                    }
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();///////////////////////////////// make something for this
                }
            }
        });

    }

    public void toHostMonitor(){
        setContentView(R.layout.host_monitor);

        ll = (LinearLayout)findViewById(R.id.host_monitor_layout);

        t1 = new TextView(this);
        try {
            t1.setText(reader.readLine().toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }
        files = new ArrayList<String>();//TODO
        try{
            fileLocation = new File(getCacheDir().getAbsolutePath()+"hostInfo");
        }catch(Exception e){Log.e(tag,e.toString());}

        try {
            reader = new BufferedReader(new FileReader(fileLocation));
            reader2 = new BufferedReader(new FileReader(fileLocation));
            Log.d(tag,"made buffered readers");
            t1 = new TextView(this);
            t1.setText(reader.readLine().toString());
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ll.addView(t1);
        } catch (FileNotFoundException e) {
            Log.e(tag,e.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }

        files = new ArrayList<String>();
        btDeviceNames = new ArrayList<String>();
        try{
            reader2.readLine();
            while(reader2.readLine()!=null){
                btDeviceNames.add(reader.readLine().toString());
            }
        }catch(Exception e){Log.e(tag,e.toString());}
        listBuffer = new ArrayList<String>();
        for(int i = 1;i<=btDeviceNames.size();i++){
            listBuffer.add(Integer.toString(i));
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBuffer);
        for(int i = 1; i<=btDeviceNames.size();i++){

            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,5));
            t1.setBackgroundColor(Color.RED);
            ll.addView(t1);

            l1 = new LinearLayout(this);
            l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            l1.setOrientation(LinearLayout.HORIZONTAL);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            t1.setText(btDeviceNames.get(i - 1) + " : ");
            l1.addView(t1);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(50, 1));
            t1.setText("");
            l1.addView(t1);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            t1.setText("Assigned Device: ");
            l1.addView(t1);
            s1 = new Spinner(this);
            s1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)s1.getLayoutParams();
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); //null pointer exception
//            s1.setLayoutParams(params);
            s1.setAdapter(adapter);
            l1.addView(s1);
            ll.addView(l1);

        }

    }

    /**
     * <p/>
     * Adds a section used for imputing a match. Uses matchNum to display the match number and ll is the linear layout used to add the components to.
     */
    void newMatch(int matchNum, int numOfMatches, LinearLayout ll) {
        loopTimes = 1;
        while (loopTimes < numOfMatches + 1) {
            display = getWindowManager().getDefaultDisplay();
            display.getSize(pointBuffer);
            l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.HORIZONTAL);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
            ll.addView(t1);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));
            t1.setBackgroundColor(Color.RED);
            ll.addView(t1);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
            ll.addView(t1);
            t2 = new TextView(this);
            t2.setText("Match " + loopTimes + ":");
            l1.addView(t2);
            t3 = new TextView(this);
            t3.setText("");
            t3.setLayoutParams(new LayoutParams(pointBuffer.x / 2, 1));
            l1.addView(t3);
            t4 = new TextView(this);
            t4.setText("Time:");
            l1.addView(t4);
            e1 = new EditText(this);
            e1.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);  //this is special...
            e1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            l1.addView(e1);
            l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
            ll.addView(l1);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
            t5 = new TextView(this);
            t5.setText("Teams: ");
            l2.addView(t5);
            t6 = new TextView(this);
            t6.setText("");
            t6.setLayoutParams(new LayoutParams(pointBuffer.x / 10, 1));
            l2.addView(t6);
            componentWidth = (pointBuffer.x / 10) + 106; //106 is the width of the "Teams: " text
            numTeams = 1;
            while (numTeams <= 6) {
                if (componentWidth + 233 <= pointBuffer.x) {// each team + text space = 233
                    t7 = new TextView(this);
                    t7.setText("Team " + numTeams + ":");
                    l2.addView(t7);
                    e2 = new EditText(this);
                    e2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    e2.setLayoutParams(new LayoutParams(140, LayoutParams.MATCH_PARENT));
                    if (numTeams == 6) {
                        e2.setId(115);
                    }
                    teamNums.add(e2);
                    l2.addView(e2);
                    componentWidth += 233;
                    numTeams++;
                } else {
                    componentWidth = 0;
                    ll.addView(l2);
                    l2 = new LinearLayout(this);
                    l2.setOrientation(LinearLayout.HORIZONTAL);
                    l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
                }
            }
            ll.addView(l2);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
            t1.setBackgroundColor(Color.BLUE);
            ll.addView(t1);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
            t5 = new TextView(this);
            t5.setText("Devices: ");
            l2.addView(t5);
            t6 = new TextView(this);
            t6.setText("");
            t6.setLayoutParams(new LayoutParams(pointBuffer.x / 10, 1));
            l2.addView(t6);
            componentWidth = (pointBuffer.x / 10) + 120;
            numDevices = 1;
            while (numDevices <= 6) {
                if (componentWidth + 233 <= pointBuffer.x) {// each team + text space = 233
                    t7 = new TextView(this);
                    t7.setText("Team " + numDevices + ":");
                    l2.addView(t7);
                    e2 = new EditText(this);
                    e2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    e2.setLayoutParams(new LayoutParams(140, LayoutParams.MATCH_PARENT));
                    if (numDevices == 6) {
                        e2.setId(115);
                    }
                    devices.add(e2);
                    l2.addView(e2);
                    componentWidth += 233;
                } else {
                    componentWidth = 0;
                    ll.addView(l2);
                    l2 = new LinearLayout(this);
                    l2.setOrientation(LinearLayout.HORIZONTAL);
                    l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
                }
                numDevices++;
            }
            ll.addView(l2);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
            t5 = new TextView(this);
            t5.setText("Score Keeper: ");
            l2.addView(t5);
            e2 = new EditText(this);
            e2.setInputType(InputType.TYPE_CLASS_NUMBER);
            e2.setLayoutParams(new LayoutParams(140, LayoutParams.MATCH_PARENT));
            devices.add(e2);
            l2.addView(e2);
            ll.addView(l2);
            loopTimes++;
        }
    }
}
