package com.mmr2410.firstscoutingapp;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    Button hostB, clientB,sendB, settingsB, NewScheduleB, ssSaveB,hostStartB,b1;
    Spinner challengeSelector;
    Spinner s1;
    ArrayList<Spinner> DeviceListSpinners,devices;
    ArrayAdapter<String> adapter;
    ArrayList<TextView> connectionStatus;
    ArrayList<LinearLayout> matchLayout;
    ArrayList<Integer> contentViews = new ArrayList<Integer>();
    File tempFile;
    List<String> files = new ArrayList<String>();
    List<String> btDevices, btDeviceNames = new ArrayList<String>();
    List<String> scheduledFiles = new ArrayList<String>();
    ArrayList<CheckBox> teamCheckBoxes = new ArrayList<CheckBox>();
    int lastScreen = 0;
    int currentScreen = R.layout.activity_main;
    Display display;
    FileOutputStream fos;
    Point pointBuffer = new Point();
    LinearLayout ssMainLayout, ssGenerated, ll, l1, l2;
    TextView t1, t2, t3, t4, t5, t6, t7;
    EditText e1, e2, matchNumInput, deviceNumInput, ssFileName,competitionInput;
    ArrayList<EditText> teamNums, times;
    int numTeams,numDevices,componentWidth,loopTimes = 0;
    String tag = "FIRST-Scouting";
    String fileLocation;
    ListView lv1;
    ProgressBar pb;
    DataHandling dh;
    Bluetooth bt;

//    new WIFI().execute("http://www.thefirstalliance.org/api/api.json.php?action=list-teams").get()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath()+"/FIRST-Scouting/";
        Log.d(tag,fileLocation);
        dh = new DataHandling(fileLocation);
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
        bt = new Bluetooth(this);
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
        Log.d(tag,"before: "+contentViews.toString());
        if(contentViews.size()<=1){
            Log.d(tag,(contentViews.size()<=1)+", "+(contentViews.get(contentViews.size()-1)==R.layout.activity_main)+", "+contentViews.get(contentViews.size()-1)+", "+R.layout.activity_main);
            finish();
        }
        ArrayList<Integer> info = new ArrayList<Integer>();
        for(int i = 0; i<contentViews.size()-1;i++){
            info.add(contentViews.get(i));
        }
        contentViews = info;
        try{
        switch(contentViews.get(contentViews.size()-1)){
            case R.layout.activity_main: toHomeScreen();
                break;
            case R.layout.host: toHostScreen();
                break;
            case R.layout.client_screen: toClientScreen();
                break;
            case R.layout.join_host: toSendScreen();
                break;
            case R.layout.settings_screen: toSettingsScreen();
                break;
            case R.layout.schedule_setup: toScheduleSetupScreen();
                break;
            case R.layout.host_monitor: Log.d(tag,"need to make way of going back to host monitor if this shows up");
                break;
            case R.layout.scouting_options:
                info = new ArrayList<Integer>();
                for(int i = 0; i<contentViews.size()-1;i++){
                    info.add(contentViews.get(i));
                }
                contentViews = info;
                toClientScreen();
                break;
            case R.layout.scouting: Log.d(tag,"if you see this, scouting needs to be handled when pressing back");
                break;
            default: finish();
                break;
        }
        }catch(Exception e){
            Log.e(tag,e.toString());
        }
        info = new ArrayList<Integer>();
        for(int i = 0; i<contentViews.size()-1;i++){
            info.add(contentViews.get(i));
        }
        contentViews = info;
        Log.d(tag,"after: "+contentViews.toString());
    }

    public void toHomeScreen() {
        lastScreen = 0;
        setContentView(R.layout.activity_main);
        contentViews.add(R.layout.activity_main);
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
        setContentView(R.layout.host);
        contentViews.add(R.layout.host);
        DeviceListSpinners = new ArrayList<Spinner>();
        scheduledFiles = new ArrayList<String>();
        btDevices = new ArrayList<String>();
        lastScreen = currentScreen;
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

        btDevices.add("<None>");
        btDevices.add("<This Device>");
        if (bt.getPairedDevices().size() > 0) {
            for (BluetoothDevice device : bt.getPairedDevices()) {
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
        contentViews.add(R.layout.client_screen);
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

        Button b = new Button(this);
        b.setText("Recieve Data");
        b.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bluetooth bt = new Bluetooth(MainActivity.this);
                bt.recieveFromDevice();
            }
        });
        ll.addView(b);
 
    }

    public void handleReceiveFromDevice(String data) {
        Log.d(tag,data);
    }

    public void toSendScreen() {
        setContentView(R.layout.join_host);
        contentViews.add(R.layout.join_host);
        btDevices = new ArrayList<String>();
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        for(BluetoothDevice b: bt.getPairedDevices()){
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
        setContentView(R.layout.schedule_setup);
        contentViews.add(R.layout.schedule_setup);
        lastScreen = currentScreen;
        currentScreen = R.layout.schedule_setup;
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
                    ArrayList<Integer> info = new ArrayList<Integer>();
                    for(int i = 0; i<contentViews.size()-1;i++){
                        info.add(contentViews.get(i));
                    }
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
        contentViews.add(R.layout.host_monitor);
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
                    }else{
                        Bluetooth b = new Bluetooth(MainActivity.this);
                        b.sendData(s,"hi");
                    }
                    i++;
                }
                dh.updateJSONArray(fileLocation + "schedules/" + fileName, 0, "devicenums", info);
            }
        });

        l1.addView(b1);
        ll.addView(l1);

    }

    public void toScoutingOptions(final String fileName){
        setContentView(R.layout.scouting_options);
        contentViews.add(R.layout.scouting_options);
        ll = (LinearLayout)findViewById(R.id.scoutingOptionsLayout);

        teamCheckBoxes = new ArrayList<CheckBox>();

        newTextViewTitle(fileName,25,ll);

        newTextView("Assigned device numbers:",ll);

        l1 = new LinearLayout(this);
        l1.setOrientation(LinearLayout.HORIZONTAL);

        ArrayList<Integer> deviceNums = new ArrayList<Integer>();
        for(String s:dh.getJSONArrayFromMatch(fileName,0,"devicenums")){
            deviceNums.add(Integer.parseInt(s));
            newTextView(s + ", ", l1);
        }

        ll.addView(l1);

        for(int i = 1; i<=Integer.parseInt(dh.getJSONStringFromMatch(fileName, 0, "matches"));i++){
            CheckBox c;
            newDivider(Color.BLUE, 5, ll);
            l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.VERTICAL);
            newTextViewTitle("Match " + i, 17, l1);
            for(int a = 0; a<dh.getJSONArrayFromMatch(fileName, i, "teams").size();a++){
                c = new CheckBox(this);
                c.setText(dh.getJSONArrayFromMatch(fileName, i, "teams").get(a));
                c.setEnabled(true);
                c.setId(i);
                for(int b = 0; b<deviceNums.size();b++){
                    if(Integer.parseInt(dh.getJSONArrayFromMatch(fileName,i,"devices").get(a).toString())==deviceNums.get(b)){
                        c.setChecked(true);
                    }
                }

                l1.addView(c);
                teamCheckBoxes.add(c);

            }

            b1 = new Button(this);
            b1.setText("Go");
            b1.setId(i);
            b1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(tag, fileName);
                    ArrayList<String> info = new ArrayList<String>();
                    for (CheckBox c : teamCheckBoxes) {
                        if (c.getId() == view.getId()&&c.isChecked()) {
                            info.add(c.getText().toString());
                        }
                    }
                    dh.updateJSONArray(fileLocation+"schedules/"+fileName, 0, "teamsscouted", info);

                    toScouting(fileName,view.getId());
                }
            });
            l1.addView(b1);

            ll.addView(l1);
        }
    }

    public void toScouting(String fileName,int matchNum){
        setContentView(R.layout.scouting);
        contentViews.add(R.layout.scouting);
        EditText e;
        RadioGroup rg;
        ArrayList<RadioGroup> autonomousShot = new ArrayList<RadioGroup>();
        RadioButton rb;
        CheckBox c;
        ArrayList<CheckBox> running = new ArrayList<CheckBox>();
        ArrayList<CheckBox> cbAutonomous = new ArrayList<CheckBox>();
        ArrayList<CheckBox> cbTeleop = new ArrayList<CheckBox>();
        NumberPicker np;
        ArrayList<NumberPicker> teleopnp = new ArrayList<NumberPicker>();
        ArrayList<EditText> notes = new ArrayList<EditText>();
        ArrayList<EditText> totalScores = new ArrayList<EditText>();

        ll = (LinearLayout)findViewById(R.id.scoutingLayout);
        newTextViewTitle(fileName+" - Match "+matchNum,25,ll);

        newDivider(20,ll);

        ArrayList<String> teamsToScout = dh.getJSONArrayFromMatch(fileName,0,"teamsscouted");

        newTextView("Does it run?",ll);
        l1 = new LinearLayout(this);
        l1.setOrientation(LinearLayout.VERTICAL);
        for(int i = 0;i<teamsToScout.size();i++){
            c = new CheckBox(this);
            c.setText(teamsToScout.get(i)+"");
            c.setEnabled(true);
            c.setId(i);
            c.setChecked(true);
            l1.addView(c);
            running.add(c);
        }
        ll.addView(l1);


        for(int i = 0; i<teamsToScout.size();i++){
            l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.VERTICAL);
            l1.setId(i);
            newDivider(Color.BLUE, 5, l1);
            newTextViewTitle(teamsToScout.get(i)+" - Autonomous", 17, l1);

            c = new CheckBox(this);
            c.setId(i);
            c.setText("Reached their zone?");
            l1.addView(c);
            cbAutonomous.add(c);

            HorizontalScrollView hsv = new HorizontalScrollView(this);
            hsv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

            rg = new RadioGroup(this);
            rg.setId(i);
            rg.setOrientation(RadioGroup.HORIZONTAL);
            rg.setGravity(Gravity.CENTER_HORIZONTAL);
            rg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            rb = new RadioButton(this);
            rb.setText("None");
            rb.setChecked(true);
            rg.addView(rb);

            rb = new RadioButton(this);
            rb.setText("Ground");
            rg.addView(rb);

            rb = new RadioButton(this);
            rb.setText("Top");
            rg.addView(rb);

            rb = new RadioButton(this);
            rb.setText("Hot Goal");
            rg.addView(rb);

            hsv.addView(rg);
            l1.addView(hsv);
            autonomousShot.add(rg);


            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);

            newTextView("Score: ",l2);

            e = new EditText(this);
            e.setInputType(InputType.TYPE_CLASS_NUMBER);
            e.setLayoutParams(new LayoutParams(150,LayoutParams.WRAP_CONTENT));
            e.setHint(0 + "");
            e.setId(0);

            l2.addView(e);
            l1.addView(l2);

            newTextView("Notes:",l1);

            e = new EditText(this);
            e.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            l1.addView(e);
            notes.add(e);

            ll.addView(l1);

        }
        newDivider(Color.BLUE,5,ll);
        newDivider(5,ll);

        for(int i = 0; i<teamsToScout.size();i++){
            l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.VERTICAL);
            newDivider(Color.RED,5,l1);
            newTextViewTitle(teamsToScout.get(i)+" - TELEOP", 17, l1);

            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);

            c = new CheckBox(this);
            c.setId(i);
            c.setText("Can Pass");
            l2.addView(c);
            cbTeleop.add(c);

            c = new CheckBox(this);
            c.setId(i);
            c.setText("Can Shoot");
            l2.addView(c);
            cbTeleop.add(c);

            l1.addView(l2);

            newDivider(10, l1);

            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setGravity(Gravity.CENTER_HORIZONTAL);

            LinearLayout l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.VERTICAL);

            newTextView("Total Shots:   ", l3);

            np = new NumberPicker(this);
            np.setId(i);
            np.setValue(0);
            np.setMinValue(0);
            np.setMaxValue(99);
            np.setWrapSelectorWheel(false);
            l3.addView(np);
            teleopnp.add(np);

            l2.addView(l3);

            l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.VERTICAL);

            newTextView("Ground Goal:   ", l3);

            np = new NumberPicker(this);
            np.setId(i);
            np.setValue(0);
            np.setMinValue(0);
            np.setMaxValue(99);
            np.setWrapSelectorWheel(false);
            l3.addView(np);
            teleopnp.add(np);

            l2.addView(l3);

            l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.VERTICAL);

            newTextView("Top Goal:   ", l3);

            np = new NumberPicker(this);
            np.setId(i);
            np.setValue(0);
            np.setMinValue(0);
            np.setMaxValue(99);
            np.setWrapSelectorWheel(false);
            l3.addView(np);
            teleopnp.add(np);

            l2.addView(l3);

            l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.VERTICAL);

            newTextView("Hot Goal:   ", l3);

            np = new NumberPicker(this);
            np.setId(i);
            np.setValue(0);
            np.setMinValue(0);
            np.setMaxValue(99);
            np.setWrapSelectorWheel(false);
            l3.addView(np);
            teleopnp.add(np);

            l2.addView(l3);


            l1.addView(l2);

            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);

            l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.VERTICAL);

            newTextView("Truss Shots:   ", l3);

            np = new NumberPicker(this);
            np.setId(i);
            np.setValue(0);
            np.setMinValue(0);
            np.setMaxValue(99);
            np.setWrapSelectorWheel(false);
            l3.addView(np);
            teleopnp.add(np);
            l2.addView(l3);

            l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.VERTICAL);

            newTextView("Total Catches:   ", l3);

            np = new NumberPicker(this);
            np.setId(i);
            np.setValue(0);
            np.setMinValue(0);
            np.setMaxValue(99);
            np.setWrapSelectorWheel(false);
            l3.addView(np);
            teleopnp.add(np);
            l2.addView(l3);

            l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.VERTICAL);

            newTextView("Total Score:   ", l3);

            np = new NumberPicker(this);
            np.setId(i);
            np.setValue(0);
            np.setMinValue(0);
            np.setMaxValue(99);
            np.setWrapSelectorWheel(false);
            l3.addView(np);
            teleopnp.add(np);
            l2.addView(l3);

            l1.addView(l2);

            ll.addView(l1);
        }
        newDivider(Color.RED,5,ll);
//        newDivider(5,ll);

        for(int i = 0; i<teamsToScout.size();i++){

            l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.VERTICAL);

            newDivider(Color.WHITE, 5, l1);
            newTextViewTitle(teamsToScout.get(i)+" - Post-Game", 17, l1);

            newTextView("Final Notes:",l1);

            e = new EditText(this);
            e.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            e.setId(i);
            l1.addView(e);
            notes.add(e);

            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);

            newTextView("Final Blue Alliance Score:", l2);

            e = new EditText(this);
            e.setId(i);
            e.setLayoutParams(new LayoutParams(150,LayoutParams.WRAP_CONTENT));
            e.setInputType(InputType.TYPE_CLASS_NUMBER);
            l2.addView(e);
            totalScores.add(e);

            l1.addView(l2);

            newDivider(15, ll);

            l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);

            newTextView("Final Red Alliance Score:", l2);

            e = new EditText(this);
            e.setId(i);
            e.setLayoutParams(new LayoutParams(150,LayoutParams.WRAP_CONTENT));
            e.setInputType(InputType.TYPE_CLASS_NUMBER);
            l2.addView(e);
            totalScores.add(e);

            l1.addView(l2);

            newDivider(15, ll);

            ll.addView(l1);

        }
        newDivider(Color.WHITE,5,ll);

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
            l2.setGravity(Gravity.CENTER_HORIZONTAL);
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
                    l2.setGravity(Gravity.CENTER_HORIZONTAL);
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
            l2.setGravity(Gravity.CENTER_HORIZONTAL);
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
                    l2.setGravity(Gravity.CENTER_HORIZONTAL);
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

    public void newDivider(int height, LinearLayout ll){
        t1 = new TextView(this);
        t1.setText("");
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
            dh.writeJSONArray("teamsscouted",info);
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
