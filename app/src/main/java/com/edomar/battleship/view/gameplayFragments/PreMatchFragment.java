package com.edomar.battleship.view.gameplayFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edomar.battleship.R;
import com.edomar.battleship.battlefield.IBattleField;
import com.edomar.battleship.view.GameActivity;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreMatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreMatchFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLAYER_NUMBER = "player_number";
    private static final String LEVEL_TO_LOAD = "scenario";


    // TODO: Rename and change types of parameters
    private int mPlayerNumber;
    private String mLevelToLoad;



    public static final String TAG = PreMatchFragment.class.getSimpleName();

    private GameActivity getActivity;


    /**Shared Preference**/
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    /**Activity reference to call methods**/
    private OnFragmentInteractionListener mListener;

    /**Start Match Button**/
    private Button mStartMatchButton;

    /** BattleField Instance**/
    private IBattleField mBattleField;


    /** Count Down Timer **/
    private TextView timer;
    float defaultTextSize;
    int defaultTextColor;
    private TextView timerTextView;
    private CountDownTimer mCounterDownTimer;
    long duration = TimeUnit.SECONDS.toMillis(60);



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment NewPreMatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreMatchFragment newInstance(int playerNumber, String levelToLoad) {
        PreMatchFragment fragment = new PreMatchFragment();
        Bundle args = new Bundle();
        args.putInt(PLAYER_NUMBER, playerNumber);
        args.putString(LEVEL_TO_LOAD, levelToLoad);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayerNumber = getArguments().getInt(PLAYER_NUMBER);
            mLevelToLoad = getArguments().getString(LEVEL_TO_LOAD).toLowerCase();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener) context;
        }else{
            mListener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        timer = (TextView) getActivity().findViewById(R.id.timer);
        defaultTextSize = timer.getTextSize();
        defaultTextColor = timer.getCurrentTextColor();
        timerTextView = (TextView) getActivity().findViewById(R.id.timer_text);
        //timer.setText("Ciao");



        //ImageView delle coordinate
        ImageView letters = (ImageView) getActivity().findViewById(R.id.letters);
        ImageView numbers = (ImageView) getActivity().findViewById(R.id.numbers);

        //Creazione SurfaceView
        mBattleField = (IBattleField) getActivity().findViewById(R.id.battle_field);
        mBattleField.setZOrderOnTop(true);
        mBattleField.init(mLevelToLoad, mPlayerNumber);
        mBattleField.setImageViewsForCoordinates(letters, numbers);

        //Button
        mStartMatchButton = (Button) getActivity().findViewById(R.id.start_match_button);
        mStartMatchButton.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        //inizia a disegnare
        //mRenderer.resume();
        mBattleField.setVisibility(View.VISIBLE);
        mBattleField.setZOrderOnTop(true);
        mBattleField.startThread();
        initCountDownTimer();
        mCounterDownTimer.start();
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

    public void initCountDownTimer(){

        mCounterDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {

                String sDuration = String.format(getResources().getConfiguration().locale,"%02d",
                        TimeUnit.MILLISECONDS.toSeconds(l));
                if(TimeUnit.MILLISECONDS.toSeconds(l) < 10){
                    timer.setText(sDuration);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    timer.setTextColor(Color.RED);
                    timerTextView.setTypeface(null, Typeface.BOLD);

                }else{
                    timer.setText(sDuration);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    timer.setTextColor(defaultTextColor);
                    timerTextView.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void onFinish() {
                //TODO
                if(mListener != null){
                    mListener.requestToChangeFragment(PreMatchFragment.this);
                }else{
                    Toast.makeText(getContext(), "mListener is null", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        //Quando il pulsante viene premuto inizia il match
        if(mBattleField.saveFleet(mLevelToLoad, mPlayerNumber)){
            mListener.requestToChangeFragment(PreMatchFragment.this);
        }else{
            Toast.makeText(getContext(), "Errore di avvio", Toast.LENGTH_SHORT).show();
            mBattleField.repositionShips(mLevelToLoad, mPlayerNumber);
        }

    }
}