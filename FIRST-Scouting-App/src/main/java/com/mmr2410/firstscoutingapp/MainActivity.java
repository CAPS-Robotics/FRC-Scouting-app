package com.mmr2410.firstscoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends ActionBarActivity {
    Button hostB, clientB,sendB, settingsB, NewScheduleB, ssSaveB,hostStartB,b1;
    Spinner challengeSelector;
    Spinner s1;
    ArrayList<Spinner> DeviceListSpinners,devices;
    ArrayAdapter<String> adapter;
    ArrayList<TextView> connectionStatus;
    ArrayList<LinearLayout> matchLayout;
    File tempFile;
    List<String> files = new ArrayList<String>();
    List<String> btDevices, btDeviceNames = new ArrayList<String>();
    List<String> scheduledFiles = new ArrayList<String>();
    Set<BluetoothDevice> pairedDevices;
    int lastScreen = 0;
    int currentScreen = R.layout.activity_main;
    BluetoothAdapter bt;
    Display display;
    FileOutputStream fos;
    Point pointBuffer = new Point();
    LinearLayout ssMainLayout, ssGenerated, ll, l1, l2;
    TextView t1, t2, t3, t4, t5, t6, t7;
    EditText e1, e2, matchNumInput, deviceNumInput, ssFileName,competitionInput;
    ArrayList<EditText> teamNums, times;
    int numTeams,numDevices,componentWidth,loopTimes = 0;
    String tag = "FIRST-Scouting";
    String fileLocation = "storage/sdcard0/FIRST-Scouting/";
    ListView lv1;
    ProgressBar pb;
    DataHandling dh = new DataHandling();

//    new WIFI().execute("http://www.thefirstalliance.org/api/api.json.php?action=list-teams").get()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        display = getWindowManager().getDefaultDisplay();
//        setTheme(android.R.style.Theme_Black);
        super.onCreate(savedInstanceState);
        try{
            tempFile = new File(fileLocation);
            tempFile.mkdirs();
            tempFile = new File(fileLocation+"schedules");
            tempFile.mkdirs();
            tempFile = new File(fileLocation+"matchdata");
            tempFile.mkdirs();
        }catch(Exception e){Log.e(tag,e.toString());}
        toHomeScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add("Home");
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
        sendB = (Button)findViewById(R.id.SendB);
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
        sendB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toSendScreen();

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
        tempFile = new File(fileLocation+"schedules");
        for (int i = 0; i < tempFile.listFiles().length; i++) {
            scheduledFiles.add(tempFile.listFiles()[i].getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scheduledFiles);
        s1 = (Spinner) findViewById(R.id.scheduleSpinner);
        try {
            s1.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(tag,e.toString());
        }

        pairedDevices = bt.getBondedDevices();
        btDevices.add("<None>");
        btDevices.add("<This Device>");
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                btDevices.add(device.getName() + "");
            }
        }else{
            Log.e(tag,"No bluetooth Devices found, ignoring for now...");
        }

        s1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ll = (LinearLayout)findViewById(R.id.Devices);
                ll.removeAllViews();

                try{
                    numDevices = Integer.parseInt(dh.getJSONStringFromMatch( arg0.getSelectedItem().toString(),0,"devices"));
                }catch(Exception e){
                    Log.e(tag,"Not able to get number of matches. File not JSON?");
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
                ArrayList<String> info = new ArrayList<String>();
                for(Spinner s:DeviceListSpinners){
                    info.add(s.getSelectedItem().toString());
                }
                s1 = (Spinner)findViewById(R.id.scheduleSpinner);
                dh.updateJSONArray(fileLocation+"schedules/"+ s1.getSelectedItem().toString(),0,"assigneddevices",info);
                toHostMonitor(s1.getSelectedItem().toString());
            }
        });
    }

    public void toClientScreen(){
        setContentView(R.layout.client_screen);
        tempFile = new File(fileLocation+"schedules/");
        files = new ArrayList<String>();
        matchLayout = new ArrayList<LinearLayout>();
        final ArrayList<String> fileNames = new ArrayList<String>();
        final ArrayList<LinearLayout> l = new ArrayList<LinearLayout>();

        ll = (LinearLayout)findViewById(R.id.clientLayout);

        int a = 0;

        for (File file : tempFile.listFiles()) {
            files.add(file.getName());

            l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.VERTICAL);
            l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            l1.setId(a);

            newDivider(Color.RED, 5, l1);
            String s = dh.getJSONStringFromMatch(file.getName(), 0, "schedule");
            newTextViewTitle(s, 20, l1);
            fileNames.add(s);

            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

            newTextView(dh.getJSONStringFromMatch(file, 0, "competition"), l2);
            newTextView(dh.getJSONStringFromMatch(file,1,"startdate"),Gravity.RIGHT,l2);

            l1.addView(l2);

            for(int i = 1; i<=Integer.parseInt(dh.getJSONStringFromMatch(file, 0, "matches"));i++){

                newDivider(Color.BLUE,5,l1);

                l2 = new LinearLayout(this);
                newTextView("match " + i, l2);
                newTextView(dh.getJSONStringFromMatch(file, i, "time"), Gravity.RIGHT, l2);
                l1.addView(l2);
            }

            matchLayout.add(l1);

            l1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(tag, "You clicked the match layout.");
                    view.setBackgroundColor(Color.BLUE);
                    // go to screen for selecting match and other options, then to actual scouting
                    int i = 0;
                    for(LinearLayout ll: l){
                        if(ll.getId() == view.getId()){
                            Log.d(tag,i+",  "+fileNames.get(i));
                            toScoutingOptions(fileNames.get(i));
                        }
                        i++;
                    }
                }
            });


            ll.addView(l1);
            l.add(l1);
            a++;
        }
 
    }

    public void toSendScreen() {
        setContentView(R.layout.join_host);
        btDevices = new ArrayList<String>();
        bt = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = bt.getBondedDevices();
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        for(BluetoothDevice b: pairedDevices){
            btDevices.add(b.getName());
        }
        lv1 = (ListView)findViewById(R.id.hostList);
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, btDevices);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pb.setVisibility(View.VISIBLE);
            }
        });
    }

    public void toSettingsScreen() {
        Log.d(tag,"someone tried to go to settings, what they don't know is that it isn't done yet!! XD");
//        setContentView(R.layout.settings_screen);
    }

    public void toScheduleSetupScreen() {
        lastScreen = currentScreen;
        currentScreen = R.layout.schedule_setup;
        setContentView(R.layout.schedule_setup);
        ssMainLayout = (LinearLayout) findViewById(R.id.ssMainL);
        ssGenerated = (LinearLayout) findViewById(R.id.ssGenerated);
        ssSaveB = (Button) findViewById(R.id.ssb);
        ssFileName = (EditText) findViewById(R.id.ssFileName);
        competitionInput = (EditText)findViewById(R.id.competitionInput);
        deviceNumInput = (EditText)findViewById(R.id.DeviceNumInput);

        challengeSelector = (Spinner)findViewById(R.id.challengeSpinner);

        ArrayList<String> challengeOptions = new ArrayList<String>();
        challengeOptions.add("Aerial Assist");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, challengeOptions);

        challengeSelector.setAdapter(adapter);

        matchNumInput = (EditText) findViewById(R.id.MatchesInput);
        matchNumInput.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                ssGenerated.removeAllViews();
                teamNums = new ArrayList<EditText>();
                devices = new ArrayList<Spinner>();
                times = new ArrayList<EditText>();
                try{
                    if(Integer.parseInt(matchNumInput.getText().toString())>50){
                        newMatch(1, Integer.parseInt(matchNumInput.getText().toString()), ssGenerated);
                    }else{
                        newMatch(1, Integer.parseInt(matchNumInput.getText().toString()), ssGenerated);
                    }
                }catch(Exception e){Log.e(tag,"Might be no input, but here is the error anyways: "+toString());}
                return false;
            }
        });

        deviceNumInput = (EditText) findViewById(R.id.DeviceNumInput);
        deviceNumInput.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                try{
                    ArrayList<String> info = new ArrayList<String>();
                    for(int a = 1; a<=Integer.parseInt(deviceNumInput.getText().toString());a++){
                        info.add(a+"");
                    }

                    for(Spinner s:devices){
                        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, info);
                        s.setAdapter(adapter);
                    }
                }catch(Exception e){
                    Log.e(tag,e.toString());
                    Log.e(tag,deviceNumInput.getText().toString());
                    Log.e(tag,deviceNumInput.getText().toString().equals("1")+"");
                }
                return false;
            }
        });

        ssSaveB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(newScheduleFile(ssFileName.getText().toString(),Integer.parseInt(matchNumInput.getText().toString()),Integer.parseInt(deviceNumInput.getText().toString()),teamNums,devices,times)){

                    toHostScreen();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter a valid schedule name", Toast.LENGTH_SHORT);
                    toast.show();
                } //TODO improve on error check

            }
        });

    }

    public void toHostMonitor(final String fileName){
        setContentView(R.layout.host_monitor);
        bt = BluetoothAdapter.getDefaultAdapter();
        connectionStatus = new ArrayList<TextView>();

        ll = (LinearLayout)findViewById(R.id.host_monitor_layout);

        try {
            t1 = new TextView(this);
            t1.setText(dh.getJSONStringFromMatch(fileLocation+"schedules/"+fileName, 0, "schedule"));
            t1.setTextSize(20);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            t1.setGravity(Gravity.CENTER);
            ll.addView(t1);
        } catch (Exception e) {
            Log.e(tag,e.toString());
        }

        btDeviceNames = dh.getJSONArrayFromMatch(fileName, 0, "assigneddevices");
        Log.d(tag,btDeviceNames.toString());

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, btDeviceNames);

        t1 = new TextView(this);
        t1.setLayoutParams(new LayoutParams(1, 10));
        ll.addView(t1);

        for(int i = 1; i<=btDeviceNames.size();i++){

            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,5));
            t1.setBackgroundColor(Color.BLUE);
            ll.addView(t1);

            l1 = new LinearLayout(this);
            l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            l1.setOrientation(LinearLayout.HORIZONTAL);
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            t1.setText(btDeviceNames.get(i - 1) + " : ");
            l1.addView(t1);
            t1 = new TextView(this);
            if(btDeviceNames.get(i-1).toString().equals("<This Device>")){
                t1.setText("Connected");
                t1.setTextColor(Color.GREEN);
            }else{
                t1.setText(" Disconnected");
                t1.setTextColor(Color.RED);
            }
            t1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            connectionStatus.add(t1);
            l1.addView(t1);
            ll.addView(l1);
        }
        t1 = new TextView(this);
        t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,5));
        t1.setBackgroundColor(Color.BLUE);
        ll.addView(t1);

        l1 = new LinearLayout(this);
        l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        l1.setGravity(Gravity.CENTER);
        t1 = new TextView(this);
        t1.setLayoutParams(new LayoutParams(1,20));

        b1 = new Button(this);
        b1.setText("Send Data");
        b1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> info = new ArrayList<String>();
                int i = 1;
                for(String s:btDeviceNames){
                    if(s.equals("<This Device>")){
                        info.add(i+"");
                    }
                    i++;
                }
                dh.updateJSONArray(fileLocation + "schedules/" + fileName, 0, "devicenums", info);
                //TODO send info to the other devices
            }
        });

        l1.addView(b1);
        ll.addView(l1);

    }

    public void toScoutingOptions(String fileName){
        setContentView(R.layout.scouting_options);
        ll = (LinearLayout)findViewById(R.id.scoutingOptionsLayout);

        newTextViewTitle(fileName,20,ll);
    }

    /**
     * <p/>
     * Adds a section used for imputing a match. Uses matchNum to display the match number and ll is the linear layout used to add the components to.
     */
    void newMatch(int matchNum, int numOfMatches, LinearLayout ll) {
        loopTimes = matchNum;
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
            t2.setTextColor(Color.WHITE);
            l1.addView(t2);
            t3 = new TextView(this);
            t3.setText("");
            t3.setLayoutParams(new LayoutParams(pointBuffer.x / 2, 1));
            l1.addView(t3);
            t4 = new TextView(this);
            t4.setText("Time:");
            l1.addView(t4);
            e1 = new EditText(this);
            e1.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
            e1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            e1.setId(loopTimes);
            times.add(e1);
            l1.addView(e1);
            l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
            ll.addView(l1);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 50));
            t5 = new TextView(this);
            t5.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            t5.setText("Teams: ");
            t5.setTextColor(Color.WHITE);
            l2.addView(t5);
            ll.addView(l2);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
            componentWidth = 0;
            numTeams = 1;
            while (numTeams <= 6) {
                if (componentWidth + 233 <= pointBuffer.x) {// each team + text space = 233
                    t7 = new TextView(this);
                    t7.setText("Team " + numTeams + ":");
                    l2.addView(t7);
                    e2 = new EditText(this);
                    e2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    e2.setLayoutParams(new LayoutParams(140, LayoutParams.MATCH_PARENT));
                    e2.setId(loopTimes);
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
            t1 = new TextView(this);
            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
            ll.addView(t1);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 50));
            t5 = new TextView(this);
            t5.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            t5.setText("Devices: ");
            t5.setTextColor(Color.WHITE);
            l2.addView(t5);
            ll.addView(l2);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
            componentWidth = 0;
            numDevices = 1;
            while (numDevices <= 6) {
                if (componentWidth + 233 <= pointBuffer.x) {// each team + text space = 233
                    t7 = new TextView(this);
                    t7.setText("Team " + numDevices + ":");
                    l2.addView(t7);

                    Spinner s = new Spinner(this);
                    s.setLayoutParams(new LayoutParams(140, LayoutParams.MATCH_PARENT));
                    s.setId(loopTimes);
                    ArrayList<String> info = new ArrayList<String>();
                    info.add("1");

                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, info);
                    s.setAdapter(adapter);
                    devices.add(s);
                    l2.addView(s);

                    e2 = new EditText(this);
                    e2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    e2.setLayoutParams(new LayoutParams(140, LayoutParams.MATCH_PARENT));
                    e2.setId(loopTimes);
//                    devices.add(e2);
//                    l2.addView(e2);
                    componentWidth += 233;
                    numDevices++;
                } else {
                    componentWidth = 0;
                    ll.addView(l2);
                    l2 = new LinearLayout(this);
                    l2.setOrientation(LinearLayout.HORIZONTAL);
                    l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 75));
                }
            }
            ll.addView(l2);
            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
            ll.addView(l2);
            loopTimes++;
        }
    }

    public void newTextView(String text,LinearLayout ll){
        t1 = new TextView(this);
        t1.setText(text);
        t1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        ll.addView(t1);
    }

    public void newTextView(String text, int gravity, LinearLayout ll){
        t1 = new TextView(this);
        t1.setText(text);
        t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        t1.setGravity(gravity);
        ll.addView(t1);
    }

    public void newTextViewTitle(String text,int size,LinearLayout ll){
        t1 = new TextView(this);
        t1.setText(text);
        t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        t1.setGravity(Gravity.CENTER_HORIZONTAL);
        t1.setTextSize(size);
        t1.setTypeface(null, Typeface.BOLD);
        try{
            ll.addView(t1);
        }catch(Exception e){
            Log.e(tag,"You have an invalid linear layout,  "+e.toString());
        }
    }
      
    public void newDivider(int color, int height, LinearLayout ll){
        t1 = new TextView(this);
        t1.setText("");
        t1.setBackgroundColor(color);
        t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height));
        ll.addView(t1);
    }

    public boolean newScheduleFile(String fileName, int numMatches, int numDevices,ArrayList<EditText> teamNums, ArrayList<Spinner> deviceNums, ArrayList<EditText> times){
        if(fileName != null){
            try {
            tempFile = new File(fileLocation+"schedules/"+fileName);
            tempFile.createNewFile();
            fos = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e2) {
            Log.e(tag, e2.toString());
        } catch (IOException e) {
            Log.e(tag,e.toString());
        }

            ArrayList<String> deviceNames = new ArrayList<String>();
            for(int i = 0; i<numDevices;i++){
                deviceNames.add("<None>");
            }

            dh.beginJSON(fos);
            dh.newJSONArray("data");

            dh.newJSONObject();
            dh.newJSONName("schedule",fileName);  //Match info starts at the index of 1, the info for the whole schedule is at "match" (or the index of) 0.
            dh.newJSONName("matches", numMatches);
            dh.newJSONName("devices",numDevices);
            dh.newJSONName("competition",competitionInput.getText().toString());
            dh.newJSONName("challenge",challengeSelector.getSelectedItem().toString());
            dh.newJSONName("host",bt.getAddress());
            ArrayList<String> info = new ArrayList<String>();
            dh.writeJSONArray("devicenums", info);
            dh.writeJSONArray("assigneddevices",deviceNames);
            dh.newJSONName("startdate", ""); //TODO Make inputs for dates
            dh.newJSONName("enddate","");
            dh.endJSONObject();

            info = new ArrayList<String>();
            info.add(deviceNumInput.getText().toString());
            info.add(matchNumInput.getText().toString());

            for(int i = 1; i<=numMatches;i++){
                dh.newJSONObject();
                dh.newJSONName("number",i);

                info = new ArrayList<String>();
                for(EditText e:teamNums){
                    if(e.getId()==i){
                        info.add(e.getText().toString());
                    }
                }
                dh.writeJSONArray("teams",info);

                info = new ArrayList<String>();
                for(Spinner s:deviceNums){
                    if(s.getId()==i){
                        info.add(s.getSelectedItem().toString());
                    }
                }

                dh.writeJSONArray("devices", info);

                for(EditText e:times){
                    if(e.getId()==i){
                        dh.newJSONName("time", e.getText().toString());
                    }
                }

                info = new ArrayList<String>(); //TODO add dates

                dh.writeJSONArray("date", info);

                dh.endJSONObject();
            }
            dh.endJSONArray();
            dh.endJSON();
            return true;

        }else{
            return false;
        }

    }

}
