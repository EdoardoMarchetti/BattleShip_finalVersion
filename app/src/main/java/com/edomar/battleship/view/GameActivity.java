package com.edomar.battleship.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.edomar.battleship.R;
import com.edomar.battleship.view.gameplayFragments.MatchFragment;
import com.edomar.battleship.view.gameplayFragments.OnFragmentInteractionListener;
import com.edomar.battleship.view.gameplayFragments.PreMatchFragment;

public class GameActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String TAG = "VerificaGameActivity";
    public static final String FIRST_MATCH_FRAGMENT_TAG = "FirstFrag";
    public static final String SECOND_MATCH_FRAGMENT_TAG = "SecondFrag";
    public static final String PROVA = "PROVA";


    /** Fragment Manager **/
    private FragmentManager mFM;
    private FragmentTransaction mFT;

    /** ViewPager **/
    //private CustomFragmentStateAdapter mCustomFragmentStateAdapter;
    private ViewPager mViewPager;



    /** Match Fragments **/
    private MatchFragment mMatchFragment1;
    private MatchFragment mMatchFragment2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(savedInstanceState == null){
            PreMatchFragment preMatchFragment = new PreMatchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, preMatchFragment, PROVA)
                    .commit();
        }



        mFM = getSupportFragmentManager();
        mFT = mFM.beginTransaction();

        mMatchFragment1 = MatchFragment.newInstance("giocatore1", 1);
        Log.d(TAG, "onCreate: mMatchFragment1 playerName = "+mMatchFragment1.getName());
        mMatchFragment2 = MatchFragment.newInstance("giocatore2", 2);
        Log.d(TAG, "onCreate: mMatchFragment2 playerName = "+mMatchFragment2.getName());



    }

    /** Metodo per ViewPager **/
    /*private void setUpViewPager(ViewPager vp){
        mCustomFragmentStateAdapter.addFragment(PreMatchFragment.newInstance());
        mCustomFragmentStateAdapter.addFragment(BlankFragment.newInstance("giocatore1", 1));
        ((BlankFragment)mCustomFragmentStateAdapter.getItem(1)).invisible();
        vp.setAdapter(mCustomFragmentStateAdapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }*/

    /**Questo metodo è invocato in due casi:
     *      -a fine timer
     *      -una volta premuto il pulsante start nel PreMatchFragment
     */
    public void startMatch() {
        Log.d(PROVA, "startMatch: i'm going in MatchFragment");
        /*((BlankFragment)mCustomFragmentStateAdapter.getItem(1)).visible();
        mViewPager.setCurrentItem(1);*/


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mMatchFragment1, FIRST_MATCH_FRAGMENT_TAG)
                .commit();

        /*getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mMatchFragment2)
                .hide(mMatchFragment2)
                .setMaxLifecycle(mMatchFragment2, Lifecycle.State.STARTED)
                .commit();*/



    }

    /** Questo metodo alterna i due fragment
     * è invocato quando:
     *      -un giocatore tenta un colpo
     *      -passano 30 secondi (tempo max di un turno)**/
    public void changeFragment(Fragment fragment){

        Log.d(TAG, "changeFragment: ");
        if (mMatchFragment1.equals(fragment)) {


            /*getSupportFragmentManager().beginTransaction()
                    .hide(mMatchFragment1)
                    .setMaxLifecycle(mMatchFragment1, Lifecycle.State.STARTED)
                    .setMaxLifecycle(mMatchFragment2, Lifecycle.State.RESUMED)
                    .show(mMatchFragment2)
                    .commit();*/

            /*if(mFM.findFragmentByTag(FIRST_MATCH_FRAGMENT_TAG) != null){
                //hide f1
                FragmentTransaction ft = mFM.beginTransaction();
                ft.hide(mFM.findFragmentByTag(FIRST_MATCH_FRAGMENT_TAG));
                ft.setMaxLifecycle(mMatchFragment1, Lifecycle.State.STARTED);
                ft.commit();

            }

            if(mFM.findFragmentByTag(SECOND_MATCH_FRAGMENT_TAG) != null){
                //show f2
                mFM.beginTransaction().setMaxLifecycle(mMatchFragment2, Lifecycle.State.RESUMED)
                        .show(mMatchFragment2)
                        .commit();


            }else{
                //add f2
                mFM.beginTransaction().add(R.id.container, mMatchFragment2, SECOND_MATCH_FRAGMENT_TAG )
                        .commit();


            }*/

            getSupportFragmentManager().beginTransaction()
                    .remove(mMatchFragment1)
                    .add(R.id.container, mMatchFragment2, SECOND_MATCH_FRAGMENT_TAG)
                    .commit();


        }

        else if (mMatchFragment2.equals(fragment)) {

            Log.d("SoloMatchFragment", "changeFragment: from 2 to 1");

            /*getSupportFragmentManager().beginTransaction()
                    .hide(mMatchFragment2)
                    .setMaxLifecycle(mMatchFragment2, Lifecycle.State.STARTED)
                    .setMaxLifecycle(mMatchFragment1, Lifecycle.State.RESUMED)
                    .show(mMatchFragment1)
                    .commit();*/

            /*if(mFM.findFragmentByTag(SECOND_MATCH_FRAGMENT_TAG) != null){
                mFM.beginTransaction().hide(mMatchFragment2)
                        .setMaxLifecycle(mMatchFragment2, Lifecycle.State.STARTED).commit();
            }

            if(mFM.findFragmentByTag(FIRST_MATCH_FRAGMENT_TAG) != null){
                //show f1
                mFM.beginTransaction().setMaxLifecycle(mMatchFragment1, Lifecycle.State.RESUMED)
                        .show(mMatchFragment1)
                        .commit();
            }else{
                mFM.beginTransaction().add(R.id.container, mMatchFragment1, FIRST_MATCH_FRAGMENT_TAG).commit();
            }*/

            getSupportFragmentManager().beginTransaction()
                    .remove(mMatchFragment2)
                    .add(R.id.container, mMatchFragment1, FIRST_MATCH_FRAGMENT_TAG)
                    .commit();


        }

    }




    @Override
    public void onFragmentInteraction(Fragment fragment) {
        changeFragment(fragment);
    }
}