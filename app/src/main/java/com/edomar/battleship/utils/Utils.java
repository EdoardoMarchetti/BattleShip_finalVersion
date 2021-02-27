package com.edomar.battleship.utils;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.edomar.battleship.R;

public class Utils {

    private static final String TAG = "Utils";

    public static void setFlagOfBadge(String selectedFlag, ImageView flagBadge) {
        Log.d(TAG, "setFlagOfBadge: i'm changing the badge ");
        switch (selectedFlag){
            case "USA":
                flagBadge.setImageResource(R.drawable.flag_usa);
                break;
            case "Italy":
            case "Italia":
                flagBadge.setImageResource(R.drawable.flag_italy);
                break;
            case "Australia":
                flagBadge.setImageResource(R.drawable.flag_australia);
                break;
            case "Brazil":
            case "Brasile":
                flagBadge.setImageResource(R.drawable.flag_brazil);
                break;
        }
    }

    public static void setFlagOfImageButtonFlag(String s, ImageView badge, ImageButton mImageButtonFlag) {
        Log.d(TAG, "setFlagOfBadge: i'm the imageButton flag");
        switch (s){
            case "USA" :
                mImageButtonFlag.setImageResource(R.drawable.flag_usa);
                break;
            case "Italy":
            case "Italia":
                mImageButtonFlag.setImageResource(R.drawable.flag_italy);
                break;
            case "Australia":
                mImageButtonFlag.setImageResource(R.drawable.flag_australia);
                break;
            case "Brazil":
            case "Brasile":
                mImageButtonFlag.setImageResource(R.drawable.flag_brazil);
                break;
        }
        setFlagOfBadge(s, badge);
    }

    public static void setFlagOfImageButtonLanguage(String selectedLanguage, ImageView imageButtonLanguage){
        switch (selectedLanguage) {
            case "English":
                imageButtonLanguage.setImageResource(R.drawable.flag_usa);
                break;
            case "Italian":
                imageButtonLanguage.setImageResource(R.drawable.flag_italy);
                break;
        }
    }

    public static String translateScenario(String scenarioSelected){
        scenarioSelected = scenarioSelected.toLowerCase();
        if(scenarioSelected.equals("russo") ){
            scenarioSelected = "russian";
        }else if(scenarioSelected.equals("classico")){
            scenarioSelected = "classic";
        }
        return scenarioSelected;
    }
}
