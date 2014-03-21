package com.mmr2410.firstscoutingapp;

import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cooper on 3/20/2014.
 */
public class GUI {

    String tag = "FIRST-Scouting";
    TextView t1;

    public void newTextView(android.content.Context context,String text,LinearLayout ll){
        TextView t1 = new TextView(context);
        t1.setText(text);
        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.addView(t1);
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

    public RadioGroup newMultipleChoice(android.content.Context context, LinearLayout ll, ArrayList rgs, String question, String... options) {
        LinearLayout l1 = new LinearLayout(context);
        l1.setOrientation(LinearLayout.VERTICAL);
        l1.setGravity(Gravity.CENTER_HORIZONTAL);
        newTextView(context, question, l1);

        RadioGroup rg = new RadioGroup(context);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        rg.setGravity(Gravity.CENTER_HORIZONTAL);

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
            a++;
        }
        l1.addView(rg);
        rgs.add(rg);

        ll.addView(l1);

        return rg;
    }
}
