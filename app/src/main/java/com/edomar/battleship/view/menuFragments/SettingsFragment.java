package com.edomar.battleship.view.menuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.edomar.battleship.R;
import com.edomar.battleship.utils.MusicService;
import com.edomar.battleship.view.HudActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;


public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {



    public static final String TAG = SettingsFragment.class.getSimpleName();

    private HudActivity mActivity;

    /** SharedPreference**/
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    /**Background music switch**/
    private SwitchCompat mBackgroundMusicSwitch;

    /**Sound effects switch*/
    private SwitchCompat mSoundEffectsSwitch;


    /**About button*/
    private Button mAboutButton;

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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        /** Initialize SharedPreference value **/
        sp = this.getActivity().getSharedPreferences(getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);
        editor =  sp.edit();
        //end initialization

        return view;
        
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: ");

        /**Background Music switch configuration **/
        mBackgroundMusicSwitch = (SwitchCompat) getActivity().findViewById(R.id.background_music_switch);
        mBackgroundMusicSwitch.setOnCheckedChangeListener(this);
        mBackgroundMusicSwitch.setChecked(sp.getBoolean(mActivity.getString(R.string.background_music_key), true));

        /**Animation sounds switch configuration**/
        mSoundEffectsSwitch = (SwitchCompat) getActivity().findViewById(R.id.sound_effects_switch);
        mSoundEffectsSwitch.setOnCheckedChangeListener(this);
        mSoundEffectsSwitch.setChecked(sp.getBoolean(mActivity.getString(R.string.animation_sound_key), true));
        //end Animation sounds configuration

        /** About button configuration **/
        mAboutButton = (Button) getActivity().findViewById(R.id.about_button);
        mAboutButton.setOnClickListener(this);
        //end About button configuration

    }

    /**Switches' method**/
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.background_music_switch: //Background

                if(isChecked){
                    Log.d(String.valueOf(R.string.debugging), "onCheckedChanged: switch background " + true);
                    mActivity.doBindService();
                    Intent music = new Intent();
                    music.setClass(getContext(), MusicService.class);
                    mActivity.startService(music);
                    editor.putBoolean(mActivity.getString(R.string.background_music_key),
                            true);
                    editor.apply();
                    Log.d(String.valueOf(R.string.debugging), "onCheckedChanged: background_music value = "
                            + sp.getBoolean(mActivity.getString(R.string.background_music_key), true));
                }else{
                    Log.d(String.valueOf(R.string.debugging), "onCheckedChanged: switch background " + false);
                    mActivity.doUnbindService();
                    Intent music = new Intent();
                    music.setClass(getContext(), MusicService.class);
                    mActivity.stopService(music);
                    editor.putBoolean(mActivity.getString(R.string.background_music_key),
                            false);
                    editor.apply();
                    Log.d(String.valueOf(R.string.debugging), "onCheckedChanged: background_music value = "
                            + sp.getBoolean(mActivity.getString(R.string.background_music_key), true));
                }
                break;

            case R.id.sound_effects_switch: //Animation
                if(isChecked){
                    Log.d(TAG, "onCheckedChanged: switch sound effects " + true);
                    mActivity.mSoundEngine.enableSoundEffect();
                    //Change SharedPreference value with animation_sound_key
                    editor.putBoolean(mActivity.getString(R.string.animation_sound_key), true);
                    editor.apply();

                }else{
                    Log.d(TAG, "onCheckedChanged: switch sound effects " + false);
                    mActivity.mSoundEngine.disableSoundEffect();
                    //Change SharedPreference value with animation_sound_key
                    editor.putBoolean(mActivity.getString(R.string.animation_sound_key), false);
                    editor.apply();
                }
                break;
        }
    }
    //ens switches' method

    /** About button method **/
    @Override
    public void onClick(View view) {
        mActivity.mSoundEngine.playShoot();
    }
    //end about button method
}
