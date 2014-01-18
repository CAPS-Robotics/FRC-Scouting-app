package com.mmr2410.firstscoutingapp;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
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
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends ActionBarActivity {
    Button hostB, clientB, settingsB, NewScheduleB, ssSaveB;
    Spinner cospinner;
    Spinner mispinner;
    Spinner s1, s2, s3, s4, s5, s6, s7, s8;
    ArrayAdapter<String> communicationOptions;
    ArrayList<String> matchInfoFiles = new ArrayList<String>();
    ArrayList<LinearLayout> DeviceOptionLayouts = new ArrayList<LinearLayout>();
    ArrayList<Spinner> DeviceListSpinners;
    ArrayAdapter<String> adapter;
    File testFile, fileLocation, scheduleFile;
    List<String> files = new ArrayList<String>();
    List<String> btDevices = new ArrayList<String>();
    List<String> scheduledFiles = new ArrayList<String>();
    String testFileName = "Ultimate-Ascent";
    String testFileContents = "Climb to victory!";
    int lastScreen = 0;
    int currentScreen = R.layout.activity_main;
    BluetoothAdapter bt;
    Display display;
    FileOutputStream fos;
    FileInputStream fis;
    InputStream in;
    BufferedReader reader;

    Point pointBuffer = new Point();
    LinearLayout ssMainLayout, ssGenerated, l1, l2;
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;
    EditText e1, e2, e3, e4, e5, e6, e7, matchNumInput, deviceNumInput, ssFileName;
    TimePicker p1;
    ArrayList<LinearLayout> lls;
    ArrayList<EditText> teamNums;
    ArrayList<EditText> devices;
    int numTeams = 0;
    int numDevices = 0;
    int componentWidth = 0;
    int loopTimes = 0;
    String stringBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        display = getWindowManager().getDefaultDisplay();
//        setTheme(android.R.style.Theme_Black);
        super.onCreate(savedInstanceState);
        try {
            fos = openFileOutput(testFileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(testFileContents.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fileLocation = new File(getFilesDir().getAbsolutePath());
        toHomeScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onBackPressed() {
        if (lastScreen == 0) {
            finish();
        } else if (lastScreen == R.layout.activity_main) {
            toHomeScreen();
        } else if (lastScreen == R.layout.client_screen) {
            toClientScreen();
        } else if (lastScreen == R.layout.host) {
            toHostScreen();
        } else if (lastScreen == R.layout.settings_screen) {
            toSettingsScreen();
        }
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
        bt = BluetoothAdapter.getDefaultAdapter();
        lastScreen = currentScreen;
        setContentView(R.layout.host);
        fileLocation = new File(getFilesDir().getAbsolutePath());
        for (int i = 0; i < fileLocation.listFiles().length; i++) {
            scheduledFiles.add(fileLocation.listFiles()[i].getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scheduledFiles);
        s1 = (Spinner) findViewById(R.id.scheduleSpinner);
        try {
            s1.setAdapter(adapter);
        } catch (Exception e) {
        }
        s1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    in = new FileInputStream(getFilesDir().getAbsolutePath() + "/" + arg0.getSelectedItem().toString());
                    Log.e("FIRST-Scouting", "Success!!");
                } catch (Exception e) {
                    System.out.println(e.toString());
                    Log.e("FIRST-Scouting", e.toString());
                }
                reader = new BufferedReader(new InputStreamReader(in));
                try {
                    Log.e("FIRST-Scouting", reader.readLine().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("FIRST-Scouting", e.toString());
                }

                Set<BluetoothDevice> pairedDevices = bt.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        btDevices.add(device.getName() + "\n" + device.getAddress() + "");
                    }
                }
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, btDevices);
                s1 = new Spinner(MainActivity.this);
                DeviceListSpinners.add(s1);
                for (Spinner s : DeviceListSpinners) {
                    try {
                        s.setAdapter(adapter);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

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
                System.out.println(getFilesDir().getAbsolutePath().toString());
                try {
                    fos = openFileOutput(ssFileName.getText().toString(), Context.MODE_PRIVATE);
                } catch (FileNotFoundException e2) {
                    // TODO Auto-generated catch block///////////////////////////////// make something for this
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
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();///////////////////////////////// make something for this
                }
            }
        });

    }


    /**
     * <p/>
     * Adds a section used for inputing a match. Uses matchNum to display the match number and ll is the linear layout used to add the components to.
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
