package com.mmr2410.firstscoutingapp;

import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

/**
 * Created by Cooper on 3/20/2014.
 */
public class GUI {

    String tag = "FIRST-Scouting";
    ArrayList<Integer> counterCount = new ArrayList<Integer>();
    int counterId = 1;
    int ORIENTATION_VERTICAL = LinearLayout.VERTICAL;
    int ORIENTATION_HORIZONTAL = LinearLayout.HORIZONTAL;

    public TextView newTextView(android.content.Context context,String text,LinearLayout ll){
        TextView t1 = new TextView(context);
        t1.setText(text);
        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.addView(t1);
        return t1;
    }

    public void newTextView(android.content.Context context,String text, int gravity, LinearLayout ll){
        TextView t1 = new TextView(context);
        t1.setText(text);
        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        t1.setGravity(gravity);
        ll.addView(t1);
    }

    public void newTextViewTitle(android.content.Context context,String text,int size,LinearLayout ll){
        TextView t1 = new TextView(context);
        t1.setText(text);
        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        t1.setGravity(Gravity.CENTER_HORIZONTAL);
        t1.setTextSize(size);
        t1.setTypeface(null, Typeface.BOLD);
        try{
            ll.addView(t1);
        }catch(Exception e){
            Log.e(tag, "You have an invalid linear layout,  " + e.toString());
        }
    }

    public void newDivider(android.content.Context context,int color, int height, LinearLayout ll){
        TextView t1 = new TextView(context);
        t1.setText("");
        t1.setBackgroundColor(color);
        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        ll.addView(t1);
    }

    public void newDivider(android.content.Context context,int height, LinearLayout ll){
        TextView t1 = new TextView(context);
        t1.setText("");
        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        ll.addView(t1);
    }

    public RadioGroup newMultipleChoice(android.content.Context context, LinearLayout ll, ArrayList list, String question, String... options) {
        LinearLayout l1 = new LinearLayout(context);
        l1.setOrientation(LinearLayout.VERTICAL);
//        l1.setGravity(Gravity.CENTER_HORIZONTAL);

        RadioGroup rg = new RadioGroup(context);
        rg.setOrientation(RadioGroup.HORIZONTAL);
//        rg.setGravity(Gravity.CENTER_HORIZONTAL);

        newTextView(context,question,l1);

        int a = 1;
        for(String s: options){
            RadioButton rb = new RadioButton(context);
            rb.setText(s);
            rb.setId(a);
            if(a == 1){
                rb.setChecked(true);
            }else{
                rb.setChecked(false);
            }
            rg.addView(rb);
            if(list!=null){
                list.add(rb);
            }
            a++;
        }
        l1.addView(rg);

        ll.addView(l1);

        return rg;
    }

    public EditText newNotesSection(android.content.Context context, LinearLayout ll, ArrayList rgs, String question){
        LinearLayout l1 = new LinearLayout(context);

        rgs.add(newTextView(context, question, l1));

        EditText textField = new EditText(context);
        textField.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        l1.addView(textField);

        rgs.add(textField);

        ll.addView(l1);

        return textField;
    }

    public RadioGroup newScale(android.content.Context context, LinearLayout ll, ArrayList list, String question, String lowestRating, String highestRating, int scaleSize){
        LinearLayout l1 = new LinearLayout(context);
        l1.setOrientation(LinearLayout.VERTICAL);
//        l1.setGravity(Gravity.CENTER_HORIZONTAL);

        newTextView(context, question, l1);

        RadioGroup rg = new RadioGroup(context);
        rg.setOrientation(RadioGroup.HORIZONTAL);
//        rg.setGravity(Gravity.CENTER_HORIZONTAL);

        newTextView(context,lowestRating,rg);

        int half;

        if(scaleSize%2>=0.5){
            half = (scaleSize/2) +1;
        }else{
            half = scaleSize/2;
        }


        for(int x = 1; x<=scaleSize; x++){
            RadioButton rb = new RadioButton(context);
            rb.setText("");
            rb.setId(x);
            if(x == half){
                rb.setChecked(true);
            }else{
                rb.setChecked(false);
            }
            rg.addView(rb);
            if(list!=null){
                list.add(rb);
            }
        }

        newTextView(context,highestRating,rg);

        l1.addView(rg);

        ll.addView(l1);

        return rg;

    }

    public Spinner newSelector(android.content.Context context, LinearLayout ll, ArrayList list, String question, String... options){
        LinearLayout l1 = new LinearLayout(context);
        l1.setOrientation(LinearLayout.VERTICAL);
//        l1.setGravity(Gravity.CENTER_HORIZONTAL);

        Spinner s1 = new Spinner(context);

        newTextView(context,question,l1);

        ArrayList<String> info = new ArrayList<String>();
        for(String s: options){
            info.add(s);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,info);
        s1.setAdapter(adapter);

        ll.addView(l1);

        list.add(s1);
        return s1;
    }

    public LinearLayout newCheckBoxes(android.content.Context context, LinearLayout ll, ArrayList list, int orientation, String question, String... options){
        LinearLayout l1 = new LinearLayout(context);
        l1.setOrientation(ORIENTATION_VERTICAL);

        newTextView(context,question,l1);

        LinearLayout l2 = new LinearLayout(context);
        l2.setOrientation(orientation);

        for(String s: options){
            CheckBox cb = new CheckBox(context);
            cb.setText(s);
            l2.addView(cb);
            list.add(cb);
        }

        l1.addView(l2);

        ll.addView(l1);

        return l2;
    }


    public LinearLayout newCounter(android.content.Context context, LinearLayout ll, ArrayList list, String s) {

        counterCount.add(0);

        LinearLayout l1 = new LinearLayout(context);
        l1.setOrientation(ORIENTATION_HORIZONTAL);

        LinearLayout l2 = new LinearLayout(context);
        l2.setOrientation(ORIENTATION_VERTICAL);
        l2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        LinearLayout l3 = new LinearLayout(context);
        l3.setOrientation(ORIENTATION_VERTICAL);
        l3.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        l3.setId(1);

        Button upButton = new Button(context);
        Button downButton = new Button(context);

        TextView t1 = new TextView(context);
        t1.setText(s);
        l2.addView(t1);

        // Shows the count variable
        final TextView countView = new TextView(context);
        countView.setText("0");
        l2.addView(countView);
//        list.add(countView);

        upButton.setText("+");
        upButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        upButton.setId(counterId);
        list.add(upButton);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = counterCount.get(view.getId()-1);
                a++;
                counterCount.set(view.getId() - 1, a);
                countView.setText(a + "");
            }
        });
        l3.addView(upButton);

        downButton.setText("-");
        downButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        downButton.setId(counterId);
        list.add(downButton);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = counterCount.get(view.getId()-1);
                a--;
                if(a<0){
                    a = 0;
                }
                counterCount.set(view.getId()-1,a);
                countView.setText(a + "");
            }
        });
        l3.addView(downButton);

        counterId++;

        l1.addView(l2);
        l1.addView(l3);
        ll.addView(l1);

        return l1;
    }
}
