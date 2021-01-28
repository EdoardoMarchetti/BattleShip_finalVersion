package com.edomar.battleship.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.edomar.battleship.R;
import com.edomar.battleship.view.gameplayFragments.MatchFragment;
import com.edomar.battleship.view.gameplayFragments.PreMatchFragment;
import com.edomar.battleship.view.menuFragments.MainMenuFragment;
import com.edomar.battleship.view.menuFragments.SettingsFragment;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    /** Match Fragments **/
    private Fragment mMatchFragment1, mMatchFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(savedInstanceState == null){
            final PreMatchFragment mainMenuFragment = new PreMatchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor_point, mainMenuFragment, MainMenuFragment.TAG)
                    .commit();
        }

        //Preparo i due match Fragements
        mMatchFragment1 = new MatchFragment();
        mMatchFragment2 = new MatchFragment();
    }

    /**Questo metodo è invocato in due casi:
     *      -a fine timer
     *      -una volta premuto il pulsante start nel PreMatchFragment
     */
    public void startMatch() {
        Log.d(TAG, "startMatch: i'm going in MatchFragment");
        mMatchFragment1 = new MatchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.anchor_point, mMatchFragment1, TAG)
                .commit();
    }

    /** Questo metodo alterna i due fragment
     * è invocato quando:
     *      -un giocatore tenta un colpo
     *      -passano 30 secondi (tempo max di un turno)**/
    public void changeFragment(Fragment fragment){
        Log.d(TAG, "changeFragment: ");
        if (mMatchFragment1.equals(fragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.anchor_point, mMatchFragment1, TAG)
                    .commit();
        } else if (mMatchFragment2.equals(fragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.anchor_point, mMatchFragment2, TAG)
                    .commit();
        }

    }
}