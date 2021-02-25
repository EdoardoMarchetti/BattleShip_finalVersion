package com.edomar.battleship.view.menuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edomar.battleship.R;
import com.edomar.battleship.view.HudActivity;
import com.edomar.battleship.view.ScenarioSelectionActivity;
import com.edomar.battleship.view.gameplayFragments.OnlineMultiplayerFragment;
//import com.edomar.battleship.view.SinglePlayerGameActivity;
//import com.edomar.battleship.view.SinglePlayerGameActivity;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MainMenuFragment extends Fragment {

    public static final String TAG = MainMenuFragment.class.getSimpleName();

    /**Shared Preference**/
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    /**GameModeMenu**/
    private TextView mTextViewGameMode;
    private String[] gameMode;
    private int position;
    private ImageView mLeftArrow;
    private ImageView mRightArrow;
    private Animation mRightArrowAnim, mLeftArrowAnim;


    private HudActivity mActivity;

    public MainMenuFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mActivity = (HudActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        /** Initialize SharedPreference value **/
        sp = this.getActivity().getSharedPreferences(getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);
        editor =  sp.edit();


        gameMode = new String[]{getString(R.string.single_player),
                getString(R.string.local_game),
                getString(R.string.online)};



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Locale current = getResources().getConfiguration().locale;
        Toast.makeText(getContext(), String.valueOf(current), Toast.LENGTH_LONG).show();

        mTextViewGameMode = (TextView) getActivity().findViewById(R.id.game_mode_text);

        position = sp.getInt(getString(R.string.configuration_preference_key),
                0);


        mTextViewGameMode.setText(gameMode[position]);

        mTextViewGameMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedGameMode = ((TextView) view).getText().toString();
                startGame(selectedGameMode);
            }
        });

        mRightArrow = (ImageView) mActivity.findViewById(R.id.right_arrow);
        mRightArrowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.right_arrow_transiction);
        mRightArrow.setClickable(true);
        mRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = (position+1)%3;
                mTextViewGameMode.startAnimation(mRightArrowAnim);
                mTextViewGameMode.setText(gameMode[position]);
                //Per migliorare l'animazione usare due text view sovrapposte una scritta e l'altra vuota in maniera alternata
                //Devi aggiungere un'animazione
            }
        });

        mLeftArrow = (ImageView) mActivity.findViewById(R.id.left_arrow);
        mLeftArrowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.left_arrow_transiction);
        mLeftArrow.setClickable(true);
        mLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = (position<= 0) ? position-1+3 : position-1;
                mTextViewGameMode.startAnimation(mLeftArrowAnim);
                mTextViewGameMode.setText(gameMode[position]);
            }
        });
    }

    private void startGame(String selectedGameMode) {
        Log.d(TAG, "startGame: " + selectedGameMode);
        Intent intent;
        switch (selectedGameMode) {
            case "Single Player":
            case "Giocatore Singolo":
                intent = new Intent(getContext(), ScenarioSelectionActivity.class);
                Locale current = getResources().getConfiguration().locale;
                intent.putExtra("locale", String.valueOf(current));
                intent.putExtra("numGiocatori", 1);
                break;
             case "1vs1":
                intent = new Intent(getContext(), ScenarioSelectionActivity.class);
                 intent.putExtra("numGiocatori", 2);
                break;
            case "Online":
                intent = new Intent(getContext(), OnlineMultiplayerFragment.class);
                break;
            default:
                throw new IllegalArgumentException("No GameMode for the given item");
        }
        startActivity(intent);
        }


    @Override
    public void onPause() {
        super.onPause();
        editor.putInt(mActivity.getString(R.string.game_mode_key), position);
        editor.commit();
    }
}
