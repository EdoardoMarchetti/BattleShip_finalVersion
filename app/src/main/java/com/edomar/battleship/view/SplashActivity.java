package com.edomar.battleship.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.edomar.battleship.logic.GameState;
import com.edomar.battleship.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private SharedPreferences sp;

    private GameState mGameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(initData()){
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this , HudActivity.class);
                    mainIntent.putExtra("player_name", mGameState);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }else{
                    Toast.makeText(SplashActivity.this, R.string.launch_error, Toast.LENGTH_LONG).show();
                    SplashActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private boolean initData() {
        //TODO
        String playerName = "Edoardo";
        mGameState = GameState.Builder
                .create(playerName)
                .build();

        /**Shared Preference initialization **/
        sp = getSharedPreferences(getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);


        return true;
    }


}