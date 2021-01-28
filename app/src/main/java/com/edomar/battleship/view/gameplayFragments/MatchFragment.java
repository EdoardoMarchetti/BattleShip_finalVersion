package com.edomar.battleship.view.gameplayFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.view.GameActivity;
import com.edomar.battleship.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MatchFragment extends Fragment {
    private static final String TAG = "MatchFragment";

    private GameActivity mActivity;


    /**Shared Preference**/
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    /**Start Match Button**/
    private Button mButton;

    /** BattleField Instance**/
    private BattleField mBattleField;

    /** Count Down Timer **/
    private TextView timer;
    private TextView timerTextView;
    private CountDownTimer mCounterDownTimer;


    public MatchFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(activity);
        mActivity = (GameActivity) activity;

    }



    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
        mActivity= null;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        /** Initialize SharedPreference value **/
        sp = this.getActivity().getSharedPreferences(getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);
        editor =  sp.edit();
        //end initialization


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: in the Match Fragment");
        super.onActivityCreated(savedInstanceState);

        //Timer
        timer = (TextView) mActivity.findViewById(R.id.timer);
        timerTextView = (TextView) mActivity.findViewById(R.id.timer_text);
        long duration = TimeUnit.SECONDS.toMillis(30);
        mCounterDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "onTick: "+ TimeUnit.MILLISECONDS.toSeconds(l));
                String sDuration = String.format(getResources().getConfiguration().locale,"%02d",
                        TimeUnit.MILLISECONDS.toSeconds(l));
                if(TimeUnit.MILLISECONDS.toSeconds(l) < 10){
                    timer.setText(sDuration);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    timer.setTextColor(Color.RED);
                    timerTextView.setTypeface(null, Typeface.BOLD);
                }else{
                    timer.setText(sDuration);
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: ");
                mActivity.changeFragment(MatchFragment.this);
            }
        }.start();

        //ImageView delle coordinate
        ImageView letters = (ImageView) mActivity.findViewById(R.id.letters);
        ImageView numbers = (ImageView) mActivity.findViewById(R.id.numbers);

        //Creazione SurfaceView
        mBattleField = (BattleField) mActivity.findViewById(R.id.battle_field);
        mBattleField.setZOrderOnTop(true);
        mBattleField.init();
        mBattleField.setImageViewsForCoordinates(letters, numbers);


    }



    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        //inizia a disegnare
        //mRenderer.resume();
        mBattleField.startThread();
    }


    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        //ferma il disegno
        //mRenderer.pause();
        mBattleField.stopThread();
        mCounterDownTimer.cancel();
    }
}
