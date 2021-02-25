package com.edomar.battleship.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.edomar.battleship.R;
import com.edomar.battleship.view.gameplayFragments.MatchFragment;
import com.edomar.battleship.view.gameplayFragments.OnFragmentInteractionListener;
import com.edomar.battleship.view.gameplayFragments.PreMatchFragment;

public class GameActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String TAG = "VerificaGameActivity";
    public static final String PRE_MATCH_FRAGMENT = "PreMatchFrag";
    public static final String FIRST_MATCH_FRAGMENT_TAG = "FirstFrag";
    public static final String SECOND_MATCH_FRAGMENT_TAG = "SecondFrag";


    /**Level**/
    private String levelToPlay;
    private int numberOfPlayer;

    /** Fragment Manager **/
    private FragmentManager mFM;
    private FragmentTransaction mFT;

    /** PreMatch Fragments**/
    private PreMatchFragment mPreMatchFragment1;
    private PreMatchFragment mPreMatchFragment2;

    /** Match Fragments **/
    private MatchFragment mMatchFragment1;
    private MatchFragment mMatchFragment2;

    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent src = getIntent();
        levelToPlay = src.getStringExtra("scenario");
        numberOfPlayer = src.getIntExtra("numGiocatori", 1);

        if(savedInstanceState == null){
            mPreMatchFragment1 = PreMatchFragment.newInstance(levelToPlay);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mPreMatchFragment1, PRE_MATCH_FRAGMENT)
                    .commit();
        }


        mFM = getSupportFragmentManager();
        mFT = mFM.beginTransaction();



        if(numberOfPlayer == 2){
            mPreMatchFragment2 = PreMatchFragment.newInstance(levelToPlay);
            Log.d("NumeroGiocatori", "onCreate: 2 giocatori");
        }

        mMatchFragment1 = MatchFragment.newInstance("giocatore1", 1, levelToPlay);
        Log.d(TAG, "onCreate: mMatchFragment1 playerName = "+mMatchFragment1.getName());
        mMatchFragment2 = MatchFragment.newInstance("giocatore2", 2, levelToPlay);
        Log.d(TAG, "onCreate: mMatchFragment2 playerName = "+mMatchFragment2.getName());



    }



    /**Questo metodo è invocato in due casi:
     *      -a fine timer
     *      -una volta premuto il pulsante start nel PreMatchFragment
     */
    public void startMatch() {
        Log.d("VerificaChangeFragment", "startMatch: i'm going in MatchFragment");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mMatchFragment1, FIRST_MATCH_FRAGMENT_TAG)
                .commit();


    }

    /** Questo metodo alterna i due fragment
     * è invocato quando:
     *      -un giocatore tenta un colpo
     *      -passano 30 secondi (tempo max di un turno)**/
    public void changeFragment(Fragment fragment){

        switch (fragment.getClass().getSimpleName()){
            case "PreMatchFragment":
                Log.d("VerificaChangeFragment", "changeFragment: devo cambiare PreMatch");
                switchPreMatchFragment(fragment);
                break;
            case "MatchFragment":
                Log.d("VerificaChangeFragment", "changeFragment: devo cambiare MatchFragment");
                switchMatchFragment(fragment);
                break;
        }


    }

    /**Method to help changeFragment**/
    private void switchPreMatchFragment(Fragment fragment){

        if(numberOfPlayer == 1){
            startMatch();
            Log.d("VerificaChangeFragment", "switchPreMatchFragment: ");
        }else if(numberOfPlayer == 2){
            if(fragment.equals(mPreMatchFragment1)){
                Log.d("VerificaChangeFragment", "changeFragment: vado al secodno preMatch");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, mPreMatchFragment2)
                        .commit();
                Log.d("VerificaChangeFragment", "switchPreMatchFragment: sono al secondo preMatch");
            }else{
                Log.d("VerificaChangeFragment", "changeFragment: vado inizio il match");
                startMatch();
            }

        }

    }


    private void switchMatchFragment(Fragment fragment){
        if (mMatchFragment1.equals(fragment)) {
            Log.d("Alternanza", "changeFragment: from 1 to 2 ");
            //nascondi e mostra in momenti diversi
            if(mFM.findFragmentByTag(FIRST_MATCH_FRAGMENT_TAG) != null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMatchFragment1)
                        .setMaxLifecycle(mMatchFragment1, Lifecycle.State.STARTED)
                        .commit();
                Log.d("Alternanza", "changeFragment: hide 1 ");

            }

            if(mFM.findFragmentByTag(SECOND_MATCH_FRAGMENT_TAG) != null){
                //show f2

                getSupportFragmentManager()
                        .beginTransaction()
                        .setMaxLifecycle(mMatchFragment2, Lifecycle.State.RESUMED)
                        .show(mMatchFragment2)
                        .commit();
                Log.d("Alternanza", "changeFragment: show 2");
            }else{
                //add f2

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, mMatchFragment2, SECOND_MATCH_FRAGMENT_TAG)
                        .commit();
                Log.d("Alternanza", "changeFragment: add 2");
            }


        }

        else if (mMatchFragment2.equals(fragment)) {

            Log.d("Alternanza", "changeFragment: from 2 to 1");

            //nascondi e mostra in momenti diversi
            if(mFM.findFragmentByTag(SECOND_MATCH_FRAGMENT_TAG) != null){

                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMatchFragment2)
                        .setMaxLifecycle(mMatchFragment2, Lifecycle.State.STARTED)
                        .commit();
                Log.d("Alternanza", "changeFragment: hide 2 ");
            }

            if(mFM.findFragmentByTag(FIRST_MATCH_FRAGMENT_TAG) != null){
                //show f1

                getSupportFragmentManager()
                        .beginTransaction()
                        .setMaxLifecycle(mMatchFragment1, Lifecycle.State.RESUMED)
                        .show(mMatchFragment1)
                        .commit();
                Log.d("Alternanza", "changeFragment: show 1");
            }else{
                //mFM.beginTransaction().add(R.id.container, mMatchFragment1, FIRST_MATCH_FRAGMENT_TAG).commitNow();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, mMatchFragment1, FIRST_MATCH_FRAGMENT_TAG)
                        .commit();
                Log.d("Alternanza", "changeFragment: add 1");
            }

        }
    }





    @Override
    public void notifyToChangeFragment(Fragment fragment) {
        changeFragment(fragment);
    }

    @Override
    public void notifyToStartMatch() {
        startMatch();
    }
}