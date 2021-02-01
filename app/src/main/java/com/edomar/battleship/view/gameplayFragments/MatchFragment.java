package com.edomar.battleship.view.gameplayFragments;

import android.content.Context;
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

import com.edomar.battleship.BattleField;
import com.edomar.battleship.R;
import com.edomar.battleship.view.GameActivity;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchFragment extends Fragment {

    public static final String TAG = "BlankFragment";

    private OnFragmentInteractionListener mListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLAYER_NAME = "playerName";
    private static final String NUMBER = "param2";

    // TODO: Rename and change types of parameters
    private String mPLayerName;
    private int mNumber;

    /** CountDownTimer **/
    private TextView timer;
    private TextView timerTextView;
    private CountDownTimer mCounterDownTimer;
    long duration = TimeUnit.SECONDS.toMillis(20);

    /** Turn Label **/
    private TextView mTurnLabel;

    private BattleField mBattleField;
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
    public static MatchFragment newInstance(String pn, int n) {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_NAME, pn);
        args.putInt(NUMBER, n);
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: number = "+ n);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener) context;
            Log.d(TAG, "onAttach: mListener is not null for number = "+mNumber);
        }else{
            mListener = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPLayerName = getArguments().getString(PLAYER_NAME);
            mNumber = getArguments().getInt(NUMBER);
            Log.d(TAG, "onCreate: getArguments is not null for number = "+mNumber);
            Log.d(TAG, "onCreate: mPlayerName = "+ mPLayerName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(GameActivity.PROVA, "onActivityCreated: number = "+ mNumber);
        super.onActivityCreated(savedInstanceState);


        timer = getActivity().findViewById(R.id.timer);
        timerTextView = getActivity().findViewById(R.id.timer_text);

        String pn = mPLayerName;
        Log.d(TAG, "onActivityCreated: pn = "+ mPLayerName);

        mCounterDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "onTick: is UIThread? "+ (Looper.myLooper() == Looper.getMainLooper()) + "fragment number = "+mNumber);
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
                }
            }

            @Override
            public void onFinish() {
                //TODO
                Log.d(GameActivity.PROVA, "onFinish: number = "+mNumber);
                if(mListener != null){
                    mListener.onFragmentInteraction(MatchFragment.this);
                }else{
                    Toast.makeText(getContext(), "mListener is null", Toast.LENGTH_SHORT).show();
                }
            }
        };

        letters = getActivity().findViewById(R.id.letters);
        numbers = getActivity().findViewById(R.id.numbers);

        mBattleField = getActivity().findViewById(R.id.battle_field);
        mBattleField.setZOrderOnTop(true);
        mBattleField.init();
        mBattleField.setImageViewsForCoordinates(letters, numbers);


        mTurnLabel = getActivity().findViewById(R.id.turn_of_player_name);
        mTurnLabel.setText(mPLayerName);

        Log.d("BOHC", "onActivityCreated: after setTextView playername = "+ mPLayerName);

        mButton = getActivity().findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                mListener.onFragmentInteraction(MatchFragment.this);
            }
        });






        Log.d(GameActivity.PROVA, "onActivityCreated: number = "+mNumber);
    }


    @Override
    public void onResume() {
        Log.d("BOHC", "onResume:mMatchFrag number = "+mNumber );
        Log.d("BOHC", "onResume:mMatchFrag number = "+mPLayerName );
        super.onResume();
        //inizia a disegnare
        //mRenderer.resume();
        mBattleField.startThread();
        mCounterDownTimer.start();
    }

    @Override
    public void onPause() {
        Log.d("BOHC", "onPause: mMatchFrag number = "+mNumber);
        super.onPause();
        //ferma il disegno
        //mRenderer.pause();
        mBattleField.stopThread();
        mCounterDownTimer.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getName() {
        return mPLayerName;
    }

    /*public interface OnFragmentInteractionListener {

        void onFragmentInteraction(BlankFragment fragment);
    }*/


    /*@Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("BlankFragmentOHC", "onHiddenChanged: number = "+ mNumber);
        super.onHiddenChanged(hidden);
        if(hidden){
            Log.d("BOHC", "onHiddenChanged: number "+mNumber+" is hidden ");

        }if(!hidden){
            Log.d("BOHC", "onHiddenChanged: number "+mNumber+" is showed ");
            Log.d("BOHC", "onHiddenChanged: playerName ="+ mPLayerName);
            mBattleField.startThread();
            mCounterDownTimer.start();
        }

    }*/

    @Override
    public void onStart() {
        super.onStart();
        Log.d("BOHC", "onStart: number "+ mNumber+ "is showed");
    }


}