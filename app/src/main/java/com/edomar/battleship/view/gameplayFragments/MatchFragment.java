package com.edomar.battleship.view.gameplayFragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.edomar.battleship.R;
import com.edomar.battleship.battlefield.IBattleField;
import com.edomar.battleship.view.IFragmentForBattlefield;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchFragment extends Fragment implements IFragmentForBattlefield {

    public static final String TAG = "BlankFragment";

    private OnFragmentInteractionListener mListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLAYER_NAME = "playerName";
    private static final String PLAYER_NUMBER = "player_number";
    private static final String LEVEL_TO_LOAD = "scenario";

    // TODO: Rename and change types of parameters
    private String mPLayerName;
    private int mPlayerNumber;
    private String mLevelToLoad;

    /** CountDownTimer **/
    private TextView timer;
    float defaultTextSize;
    int defaultTextColor;
    private TextView timerTextView;
    private CountDownTimer mCounterDownTimer;
    long duration = TimeUnit.SECONDS.toMillis(20);

    /** Turn Label **/
    private TextView mTurnLabel;

    private IBattleField mBattleField;


    private Button mButton;
    private ImageView letters;
    private ImageView numbers;

    public MatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pn Parameter 1.
     * @param n Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchFragment newInstance(String pn, int n, String levelToLoad) {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_NAME, pn);
        args.putInt(PLAYER_NUMBER, n);
        args.putString(LEVEL_TO_LOAD, levelToLoad);
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: number = "+ n);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener) context;
            Log.d(TAG, "onAttach: mListener is not null for number = "+ mPlayerNumber);
        }else{
            mListener = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Lifecycle", "onCreate: ");
        if (getArguments() != null) {
            mPLayerName = getArguments().getString(PLAYER_NAME);
            mPlayerNumber = getArguments().getInt(PLAYER_NUMBER);
            mLevelToLoad = getArguments().getString(LEVEL_TO_LOAD).toLowerCase();
            Log.d(TAG, "onCreate: getArguments is not null for number = "+ mPlayerNumber);
            Log.d(TAG, "onCreate: mPlayerName = "+ mPLayerName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        if(mPlayerNumber == 1){
            view = inflater.inflate(R.layout.fragment_match, container, false);
            Log.d("Lifecycle", "onCreateView: number "+ mPlayerNumber);
        }else {
            if(mPLayerName == "computer"){
                view = inflater.inflate(R.layout.fragment_match_cpu, container, false);
            }else {
                view = inflater.inflate(R.layout.fragment_match2, container, false);
                Log.d("Lifecycle", "onCreateView: number " + mPlayerNumber);
            }
        }

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.d("Lifecycle", "onActivityCreated: number "+ mPlayerNumber);
        super.onActivityCreated(savedInstanceState);

        FrameLayout fl;
        if(mPlayerNumber == 1){
            fl = (FrameLayout) getActivity().findViewById(R.id.fl1);
        }else{
            if(mPLayerName == "computer"){
                fl = (FrameLayout) getActivity().findViewById(R.id.fl_cpu);
            }else {
                fl = (FrameLayout) getActivity().findViewById(R.id.fl2);
            }

        }

        timer = fl.findViewById(R.id.timer);
        defaultTextSize = timer.getTextSize();
        defaultTextColor = timer.getCurrentTextColor();
        timerTextView = fl.findViewById(R.id.timer_text);


        letters = fl.findViewById(R.id.letters);
        numbers = fl.findViewById(R.id.numbers);

        mBattleField = fl.findViewById(R.id.battle_field);
        mBattleField.setZOrderOnTop(true);
        mBattleField.setFragmentReference(this);
        mBattleField.init(mLevelToLoad, mPlayerNumber);
        mBattleField.setImageViewsForCoordinates(letters, numbers);


        mTurnLabel = fl.findViewById(R.id.turn_of_player_name);
        mTurnLabel.setText(mPLayerName);

        Log.d("BOHC", "onActivityCreated: after setTextView playername = "+ mPLayerName);

        mButton = fl.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                mListener.requestToChangeFragment(MatchFragment.this);
            }
        });


    }


    @Override
    public void onResume() {
        Log.d("BOHC", "onResume:mMatchFrag number = "+ mPlayerNumber);
        Log.d("BOHC", "onResume:mMatchFrag number = "+mPLayerName );
        Log.d("Lifecycle", "onResume: number "+ mPlayerNumber);
        super.onResume();

        mBattleField.setVisibility(View.VISIBLE);
        mBattleField.setZOrderOnTop(true);
        mBattleField.startThread();
        initCountDownTimer();
        mCounterDownTimer.start();
    }

    @Override
    public void onPause() {
        Log.d("BOHC", "onPause: mMatchFrag number = "+ mPlayerNumber);
        Log.d("Lifecycle", "onPause: number " + mPlayerNumber);
        super.onPause();

        mBattleField.stopThread();
        mBattleField.setVisibility(View.INVISIBLE);
        mCounterDownTimer.cancel();
    }

    @Override
    public void onDetach() {
        Log.d("Lifecycle", "onDetach:number "+ mPlayerNumber);
        super.onDetach();
        mListener = null;
    }

    public String getName() {
        return mPLayerName;
    }



    public void initCountDownTimer(){
        mCounterDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "onTick: is UIThread? "+ (Looper.myLooper() == Looper.getMainLooper()) + "fragment number = "+ mPlayerNumber);
                String sDuration = String.format(getResources().getConfiguration().locale,"%02d",
                        TimeUnit.MILLISECONDS.toSeconds(l));
                if(TimeUnit.MILLISECONDS.toSeconds(l) < 10){
                    timer.setText(sDuration);
                    timer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    timer.setTextColor(Color.RED);
                    timerTextView.setTypeface(null, Typeface.BOLD);
                    Log.d("BOHC", "onTick: in if <10");
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
                    mListener.requestToChangeFragment(MatchFragment.this);
                }else{
                    Toast.makeText(getContext(), "mListener is null", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    @Override
    public void notifyHit(int row, int column, boolean result) {
        Log.d("VerificaChangeFragment", "notifyHit: ");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.requestToChangeFragment(MatchFragment.this);
            }
        }, 1000);
    }

    @Override
    public void notifyEndGame() {
        Log.d("VerificaChangeFragment", "notifyEndGame: ");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.endGame(mPLayerName);
            }
        }, 1250);
    }
}