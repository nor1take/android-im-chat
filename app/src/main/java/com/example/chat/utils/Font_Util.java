package com.example.chat.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

public class Font_Util {
    public static void setFont(TextView tv, int weight, Activity activity){
        Typeface face = null;
        switch (weight) {
            case -3:
                face = Typeface.createFromAsset(activity.getAssets(),
                        "fonts/SourceHanSerifSC-ExtraLight.otf");
                break;
            case -2:
                face = Typeface.createFromAsset(activity.getAssets(),
                        "fonts/SourceHanSerifSC-Light.otf");
                break;

            case -1:
                face = Typeface.createFromAsset(activity.getAssets(),
                        "fonts/SourceHanSerifSC-Regular.otf");
                break;
            case 0:
                face = Typeface.createFromAsset(activity.getAssets(),
                        "fonts/SourceHanSerifSC-Medium.otf");
                break;
            case 1:
                face = Typeface.createFromAsset(activity.getAssets(),
                        "fonts/SourceHanSerifSC-SemiBold.otf");
                break;
            case 2:
                face = Typeface.createFromAsset(activity.getAssets(),
                        "fonts/SourceHanSerifSC-Bold.otf");
                break;
            case 3:
                face = Typeface.createFromAsset(activity.getAssets(),
                        "fonts/SourceHanSerifSC-Heavy.otf");
                break;


        }
        tv.setTypeface(face);
    }
}
