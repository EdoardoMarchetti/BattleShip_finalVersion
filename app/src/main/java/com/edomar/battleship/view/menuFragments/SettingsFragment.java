package com.edomar.battleship.view.menuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.edomar.battleship.R;
import com.edomar.battleship.utils.SoundEngine;
import com.edomar.battleship.view.HudActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String SHARED_PREFERENCES_NAME = "SettingsFragment";

    private HudActivity mActivity;
    

    private interface Keys{
        String BACKGROUND_MUSIC_SWITCH = "background_music";
        String ANIMATION_EFFECT_SWITCH = "animation_effect";
        String LANGUAGE_LIST = "language";
        String BADGE_LIST = "badge";
    }

    public SettingsFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mActivity = (HudActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity= null;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean backgroundMusicSwitch = prefs.getBoolean(Keys.BACKGROUND_MUSIC_SWITCH, true );
        boolean animationEffectSwitch  = prefs.getBoolean(Keys.ANIMATION_EFFECT_SWITCH, true );

        //mActivity.mSoundEngine.setSoundEffectOn(animationEffectSwitch);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
