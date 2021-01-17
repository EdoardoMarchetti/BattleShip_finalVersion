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


import com.edomar.battleship.R;
import com.edomar.battleship.databinding.ActivityHudBinding;
import com.edomar.battleship.logic.IGameState;
import com.edomar.battleship.utils.HomeWatcher;
import com.edomar.battleship.utils.MusicService;
import com.edomar.battleship.utils.SoundEngine;
import com.edomar.battleship.utils.UserInteractionListener;
import com.edomar.battleship.view.menuFragments.MainMenuFragment;
import com.edomar.battleship.view.menuFragments.SettingsFragment;


public class HudActivity extends AppCompatActivity {

    public static final String TAG = HudActivity.class.getSimpleName();

    public IGameState gameState;
    public SoundEngine mSoundEngine;
    HomeWatcher mHomeWatcher;

    /** SharedPreference**/
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private UserInteractionListener userInteractionListener;

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

        mSoundEngine = new SoundEngine(this);
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

        final Intent srcIntent= getIntent();
        if(srcIntent != null){
            gameState = (IGameState) srcIntent.getParcelableExtra("player_name");
        }
        binding.setPlayer(gameState);

    }



    @Override
    protected void onResume() {
        Log.d(String.valueOf(R.string.debugging), "onResume: I'm trying to start background music  ");
        //Start the background music only if background_music_key value is true
        if(sp.getBoolean(getString(R.string.background_music_key), true)) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
            Log.d(String.valueOf(R.string.debugging), "onResume: back_ground_music enabled");
        }

        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(String.valueOf(R.string.debugging), "onPause: ");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;

        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //stop Music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        stopService(music);


    }

    public void setUserInteractionListener(UserInteractionListener userInteractionListener) {
        this.userInteractionListener = userInteractionListener;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (userInteractionListener != null)
            userInteractionListener.onUserInteraction();
    }

    public void showFragment ( final View selectedMenu){
        final int viewID = selectedMenu.getId();
        Fragment nextFragment;

        switch (viewID) {
            case (R.id.left_button):
                Log.d("Pressed button", "changeActivity: left");
                nextFragment = new SettingsFragment();
                break;
            case (R.id.central_button):
                Log.d("Pressed button", "changeActivity: central");
                nextFragment = new MainMenuFragment();
                break;
            /*case (R.id.right_button):
                nextFragment = new FleetFragment();
                Log.d("Pressed button", "changeActivity: right");
                break;*/
            default:
                throw new IllegalArgumentException("No Fragment for the given item");
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.anchor_point, nextFragment, TAG)
                .commit();

    }

    //--------------------------------------
    //METHODS FOR BACKGROUND MUSIC
    //--------------------------------------
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
        editor.putBoolean(getString(R.string.background_music_key), true);
        editor.apply();
        return super.startService(service);
    }


    @Override
    public boolean stopService(Intent name) {
        editor.putBoolean(getString(R.string.background_music_key), false);
        editor.apply();
        return super.stopService(name);
    }

}