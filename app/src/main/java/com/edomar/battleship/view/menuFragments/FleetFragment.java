package com.edomar.battleship.view.menuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.R;
import com.edomar.battleship.view.HudActivity;
import com.edomar.battleship.view.Renderer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class FleetFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = FleetFragment.class.getSimpleName();

    private HudActivity mActivity;


    /**Shared Preference**/
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    /**Save Button**/
    private Button mButton;

   /** BattleField Instance**/
   private BattleField mBattleField;


    public FleetFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(activity);
        mActivity = (HudActivity) activity;

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
        View view = inflater.inflate(R.layout.fragment_fleet, container, false);

        /** Initialize SharedPreference value **/
        sp = this.getActivity().getSharedPreferences(getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);
        editor =  sp.edit();
        //end initialization




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);


        /** Parte giusta**/
        //Creo il renderer per disegnare nel fragment
        // l'activy è necessaria perchè da lì recupero gli id degli oggetti
        //mRenderer = new Renderer(mActivity); //create a renderer as callback class
        Log.d(String.valueOf(R.string.debugging), "onActivityCreated: renderer created");

        mBattleField = (BattleField) mActivity.findViewById(R.id.battle_field);
        mBattleField.setZOrderOnTop(true);
        mBattleField.init();


        //init pulsante
        mButton = (Button) getActivity().findViewById(R.id.save_button);
        mButton.setOnClickListener(this);

    }



    @Override
    public void onResume() {
        Log.d("Thread", "onResume: ");
        super.onResume();
        //inizia a disegnare
        //mRenderer.resume();
        mBattleField.startThread();
    }


    @Override
    public void onPause() {
        Log.d("Thread", "onPause: ");
        super.onPause();
        //ferma il disegno
        //mRenderer.pause();
        mBattleField.stopThread();

    }



    /** Qui va il codice per salvare in memoria la flotta di defualt**/
    @Override
    public void onClick(View view) {
        mActivity.mSoundEngine.playShoot();
    }



}
