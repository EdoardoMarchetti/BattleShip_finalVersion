package com.edomar.battleship.view.gameplayFragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edomar.battleship.R;
import com.edomar.battleship.view.Renderer;
//import com.edomar.battleship.view.SinglePlayerGameActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/** Questa è una versione solamente di test per veder se la griglia funziona sempre nello stesso modo**/

/*public class ScenarioSelectionFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ScenarioSelectionFragment.class.getSimpleName();

    private SinglePlayerGameActivity mActivity;

    private Button russianButton, classicalButton, standardButton;
    private Renderer mRenderer;

    public ScenarioSelectionFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mActivity = (SinglePlayerGameActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity= null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenario_selection, container, false);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRenderer = new Renderer(mActivity); //create a renderer as callback class
        Log.d(String.valueOf(R.string.debugging), "onActivityCreated: renderer created");
    }


    @Override
    public void onResume() {
        Log.d("Thread", "onResume: ");
        super.onResume();
        //inizia a disegnare
        mRenderer.resume();
    }


    @Override
    public void onPause() {
        Log.d("Thread", "onPause: ");
        super.onPause();
        //ferma il disegno
        mRenderer.pause();
    }

    @Override
    public void onClick(View view) {

    }
}*/
