package com.edomar.battleship.view.menuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.R;
import com.edomar.battleship.utils.SoundEngine;
import com.edomar.battleship.view.HudActivity;

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

        //ImageView delle coordinate
        ImageView letters = (ImageView) mActivity.findViewById(R.id.letters);
        ImageView numbers = (ImageView) mActivity.findViewById(R.id.numbers);

        //Creazione SurfaceView
        mBattleField = (BattleField) mActivity.findViewById(R.id.battle_field);
        mBattleField.setZOrderOnTop(true);
        mBattleField.init();
        mBattleField.setImageViewsForCoordinates(letters, numbers);


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
        SoundEngine.getInstance(getContext()).playShoot();
        String[][] grid = mBattleField.saveDefaultFleet();
        //Test
        Log.d("Saving", "onClick: " +
                "\n1A= "+grid[0][0]+"\t1B="+grid[0][1]+"\t1C= "+grid[0][2]+"\t1D="+grid[0][3]+"\t1E= "+grid[0][4]+"\t1F="+grid[0][5]+"\t1G= "+grid[0][6]+"\t1H="+grid[0][7]+"\t1I= "+grid[0][8]+"\t1J="+grid[0][9]+
                "\n2A= "+grid[1][0]+"\t2B="+grid[1][1]+"\t2C= "+grid[1][2]+"\t2D="+grid[1][3]+"\t2E= "+grid[1][4]+"\t2F="+grid[1][5]+"\t2G= "+grid[1][6]+"\t2H="+grid[1][7]+"\t2I= "+grid[1][8]+"\t2J="+grid[1][9]+
                "\n3A= "+grid[2][0]+"\t3B="+grid[2][1]+"\t3C= "+grid[2][2]+"\t3D="+grid[2][3]+"\t3E= "+grid[2][4]+"\t3F="+grid[2][5]+"\t3G= "+grid[2][6]+"\t3H="+grid[2][7]+"\t3I= "+grid[2][8]+"\t3J="+grid[2][9]+
                "\n4A= "+grid[3][0]+"\t4B="+grid[3][1]+"\t4C= "+grid[3][2]+"\t4D="+grid[3][3]+"\t4E= "+grid[3][4]+"\t4F="+grid[3][5]+"\t4G= "+grid[3][6]+"\t4H="+grid[3][7]+"\t4I= "+grid[3][8]+"\t4J="+grid[3][9]+
                "\n5A= "+grid[4][0]+"\t5B="+grid[4][1]+"\t5C= "+grid[4][2]+"\t5D="+grid[4][3]+"\t5E= "+grid[4][4]+"\t5F="+grid[4][5]+"\t5G= "+grid[4][6]+"\t5H="+grid[4][7]+"\t5I= "+grid[4][8]+"\t5J="+grid[4][9]+
                "\n6A= "+grid[5][0]+"\t6B="+grid[5][1]+"\t6C= "+grid[5][2]+"\t6D="+grid[5][3]+"\t6E= "+grid[5][4]+"\t6F="+grid[5][5]+"\t6G= "+grid[5][6]+"\t6H="+grid[5][7]+"\t6I= "+grid[5][8]+"\t6J="+grid[5][9]+
                "\n7A= "+grid[6][0]+"\t7B="+grid[6][1]+"\t7C= "+grid[6][2]+"\t7D="+grid[6][3]+"\t7E= "+grid[6][4]+"\t7F="+grid[6][5]+"\t7G= "+grid[6][6]+"\t7H="+grid[6][7]+"\t7I= "+grid[6][8]+"\t7J="+grid[6][9]+
                "\n8A= "+grid[7][0]+"\t8B="+grid[7][1]+"\t8C= "+grid[7][2]+"\t8D="+grid[7][3]+"\t8E= "+grid[7][4]+"\t8F="+grid[7][5]+"\t8G= "+grid[7][6]+"\t8H="+grid[7][7]+"\t7I= "+grid[7][8]+"\t7J="+grid[7][9]+
                "\n9A= "+grid[8][0]+"\t9B="+grid[8][1]+"\t9C= "+grid[8][2]+"\t9D="+grid[8][3]+"\t9E= "+grid[8][4]+"\t9F="+grid[8][5]+"\t9G= "+grid[8][6]+"\t9H="+grid[8][7]+"\t7I= "+grid[8][8]+"\t7J="+grid[8][9]+
                "\n10A= "+grid[9][0]+"\t10B="+grid[9][1]+"\t10C= "+grid[9][2]+"\t10D="+grid[9][3]+"\t10E= "+grid[9][4]+"\t10F="+grid[9][5]+"\t10G= "+grid[9][6]+"\t10H="+grid[9][7]+"\t10I= "+grid[9][8]+"\t10J="+grid[9][9]);

    }



}
