package com.edomar.battleship.view.menuFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edomar.battleship.R;
import com.edomar.battleship.view.HudActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MainMenuFragment extends Fragment {

    public static final String TAG = MainMenuFragment.class.getSimpleName();

    private LinearLayout mGameModeMenu;
    private TextView mTextViewGameMode;

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

        mTextViewGameMode = (TextView) view.findViewById(R.id.game_mode_text);
        mTextViewGameMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedGameMode = ((TextView) view).getText().toString();
                startGame(selectedGameMode);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void startGame(String selectedGameMode) {
        Log.d(TAG, "startGame: " + selectedGameMode);
        Intent intent;
        /*switch (selectedGameMode) {
            case "Single Player":
            case "Giocatore Singolo":
                intent = new Intent(getContext(), SinglePlayerGameActivity.class);
                break;
            case "1vs1":
                //intent = new Intent(getContext(), LocalMultiplayer.class);
                break;
            case "Online":
                //intent = new Intent(getContext(), OnlineMultiplayer.class);
                break;
            default:
                throw new IllegalArgumentException("No GameMode for the given item");
        }
        startActivity(intent);*/
        }


}
