package com.edomar.battleship.utils;

import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.edomar.battleship.view.HudActivity;

import java.util.Locale;

public class Util {

    private static final String UTIL = Util.class.getSimpleName();

    //set the language after the selection
    /*private void setLocale(String lang){
        Log.d(SETTING_FRAGMENT, "setLocale: start");
        String languageToLoad = "";

        switch (lang) {
            case "English":
                languageToLoad = "en";
                break;
            case "Italian":
                languageToLoad = "it";
                break;
        }
        Log.d(SETTING_FRAGMENT, "setLocale: SELECTED LANGUAGE= "+ languageToLoad);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        mActivity.getBaseContext().getResources().updateConfiguration(config,
                mActivity.getBaseContext().getResources().getDisplayMetrics());
        Intent refresh = new Intent(getContext(), HudActivity.class);
        mActivity.overridePendingTransition(0, 0);
        refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mActivity.finish();
        mActivity.overridePendingTransition(0, 0);
        startActivity(refresh);


        Log.d(SETTING_FRAGMENT, "setLocale: end");
    }*/
}
