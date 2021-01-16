package com.edomar.battleship.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.edomar.battleship.R;
import com.edomar.battleship.databinding.ActivityHudBinding;
import com.edomar.battleship.logic.IGameState;
import com.edomar.battleship.utils.SoundEngine;
import com.edomar.battleship.view.menuFragments.MainMenuFragment;
import com.edomar.battleship.view.menuFragments.SettingsFragment;


public class HudActivity extends AppCompatActivity {

    public static final String TAG = HudActivity.class.getSimpleName();

    public IGameState gameState;
    public SoundEngine mSoundEngine;

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

        mSoundEngine = new SoundEngine(this);

        final Intent srcIntent= getIntent();
        if(srcIntent != null){
            gameState = (IGameState) srcIntent.getParcelableExtra("player_name");
        }
        binding.setPlayer(gameState);

    }


    public void showFragment(final View selectedMenu){
        final int viewID = selectedMenu.getId();
        Fragment nextFragment;

        switch (viewID){
            case (R.id.left_button):
                Log.d("Pressed button", "changeActivity: left");
                nextFragment = new SettingsFragment();

                break;
            case (R.id.central_button):
                Log.d("Pressed button", "changeActivity: central");
                nextFragment=new MainMenuFragment();
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



}