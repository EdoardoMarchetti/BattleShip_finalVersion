package com.edomar.battleship.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.edomar.battleship.R;
import com.edomar.battleship.databinding.ActivityHudBinding;
import com.edomar.battleship.logic.IGameState;
import com.edomar.battleship.utils.HomeWatcher;
import com.edomar.battleship.utils.MusicService;
import com.edomar.battleship.utils.SoundEngine;
import com.edomar.battleship.utils.Utils;
import com.edomar.battleship.view.menuFragments.FleetFragment;
import com.edomar.battleship.view.menuFragments.MainMenuFragment;
import com.edomar.battleship.view.menuFragments.SettingsFragment;

import java.util.Locale;


public class HudActivity extends AppCompatActivity {

    public static final String TAG = HudActivity.class.getSimpleName();

    public IGameState gameState;
    //public SoundEngine mSoundEngine;
    HomeWatcher mHomeWatcher;


    /** SharedPreference**/
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    /**Badge**/
    public ImageView ImageViewBadge;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHudBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_hud);
        if(savedInstanceState == null){
            final MainMenuFragment mainMenuFragment = new MainMenuFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor_point, mainMenuFragment, MainMenuFragment.TAG)
                    .commit();
        }

        sp = getSharedPreferences(getString(R.string.configuration_preference_key), MODE_PRIVATE);
        editor = sp.edit();

        /**Sound config**/
        Log.d(String.valueOf(R.string.debugging), "onCreate: I'm trying to start background music");
        if(sp.getBoolean(getString(R.string.background_music_key), true)) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
            Log.d(String.valueOf(R.string.debugging), "onCreate: back_ground_music enabled");
        }

        //mSoundEngine = SoundEngine.getInstance(this);
        //end sound config

        //homeWatcher to stop music if home button is pressed
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if(mServ != null){
                    mServ.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if(mServ != null){
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

        /**Badge Configuration**/
        ImageViewBadge = findViewById(R.id.badge_image_view);
        //setFlagOfImageButtonFlag(sp.getString(getString(R.string.flag_key), "USA"));
        Utils.setFlagOfBadge(sp.getString(getString(R.string.flag_key), "USA"), ImageViewBadge);





    }



    @Override
    protected void onResume() {
        Log.d("lifecycle", "onResume: I'm trying to start background music  ");
        //Start the background music only if background_music_key value is true
        if(sp.getBoolean(getString(R.string.background_music_key), true)) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
            Log.d(String.valueOf(R.string.debugging), "onResume: back_ground_music enabled");
        }
        Locale current = getResources().getConfiguration().locale;
        Toast.makeText(this, String.valueOf(current), Toast.LENGTH_LONG).show();
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause: ");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;

        if(pm != null){
            isScreenOn = pm.isScreenOn();
        }


        if(!isScreenOn){
            if(mServ != null){
                mServ.pauseMusic();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy: ");
        //stop Music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        stopService(music);


    }



    public void showFragment ( final View selectedMenu){
        final int viewID = selectedMenu.getId();
        Fragment nextFragment;

        switch (viewID) {
            case (R.id.left_button): //Go to settings menu
                Log.d("Pressed button", "changeActivity: left");
                nextFragment = new SettingsFragment();
                break;
            case (R.id.central_button): //Go to main menu
                Log.d("Pressed button", "changeActivity: central");
                nextFragment = new MainMenuFragment();
                break;
            case (R.id.right_button): //Go to fleet menu
                nextFragment = new FleetFragment();
                Log.d("Pressed button", "changeActivity: right");
                break;
            default:
                throw new IllegalArgumentException("No Fragment for the given item");
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.anchor_point, nextFragment, TAG)
                .commit();

    }



    /**--------------------------------------
        METHODS FOR BACKGROUND MUSIC
    -------------------------------------**/
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    public void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    public void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }


    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
    //end METHODS FOR BACKGROUND MUSIC


    /**--------------------------------------
                  UTILITY METHODS
     -------------------------------------**/
    /**Badge Management**/
    private void setFlagOfImageButtonFlag(String selectedFlag) {
        switch (selectedFlag){
            case "USA":
                ImageViewBadge.setImageResource(R.drawable.flag_usa);
                break;
            case "Italy":
            case "Italia":
                ImageViewBadge.setImageResource(R.drawable.flag_italy);
                break;
            case "Australia":
                ImageViewBadge.setImageResource(R.drawable.flag_australia);
                break;
            case "Brazil":
            case "Brasile":
                ImageViewBadge.setImageResource(R.drawable.flag_brazil);
                break;
        }
    }

}