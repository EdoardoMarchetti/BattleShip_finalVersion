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

import com.edomar.battleship.battlefield.BattleField;
import com.edomar.battleship.view.GameActivity;
import com.edomar.battleship.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PreMatchFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = PreMatchFragment.class.getSimpleName();

    private GameActivity mActivity;


    /**Shared Preference**/
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    /**Start Match Button**/
    private Button mStartMatchButton;

    /** BattleField Instance**/
    private BattleField mBattleField;

    /** Count Down Timer **/
    private TextView timer;
    private TextView timerTextView;
    private CountDownTimer mCounterDownTimer;


    public PreMatchFragment() {
        //Required empty public constructor
    }

    public static PreMatchFragment newInstance(){
        return new PreMatchFragment();
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

        View view = inflater.inflate(R.layout.fragment_fleet_configuration_pre_match, container, false);

        /** Initialize SharedPreference value **/
        sp = this.getActivity().getSharedPreferences(getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);
        editor =  sp.edit();
        //end initialization


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        //Timer
        timer = (TextView) mActivity.findViewById(R.id.timer);
        timerTextView = (TextView) mActivity.findViewById(R.id.timer_text);
        //textView.setText("Ciao");
        long duration = TimeUnit.SECONDS.toMillis(10);
        mCounterDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {


                String sDuration = String.format(getResources().getConfiguration().locale,"%02d",
                        TimeUnit.MILLISECONDS.toSeconds(l));
                if(TimeUnit.MILLISECONDS.toSeconds(l) < 20){
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
                //Quando il tempo finisce passa al match

                mActivity.startMatch();

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

        //Button
        mStartMatchButton = (Button) mActivity.findViewById(R.id.start_match_button);
        mStartMatchButton.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        //Quando il pulsante viene premuto inizia il match
        mActivity.startMatch();
    }
}
