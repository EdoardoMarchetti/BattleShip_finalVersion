package com.edomar.battleship.view.menuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.edomar.battleship.R;
import com.edomar.battleship.view.Grid;
import com.edomar.battleship.view.HudActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FleetFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = FleetFragment.class.getSimpleName();

    private HudActivity mActivity;
    private ImageView iv, letters, numbers;
    private Point gridDimension = new Point();

    /**Shared Preference**/
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    /**Save Button**/
    private Button mButton;


    public FleetFragment() {
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
        mActivity= null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fleet, container, false);

        /** Initialize SharedPreference value **/
        sp = this.getActivity().getSharedPreferences(getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);
        editor =  sp.edit();
        //end initialization

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int finalHeight, finalWidth;
        iv = (ImageView) mActivity.findViewById(R.id.grid);
        letters = (ImageView) mActivity.findViewById(R.id.letters);
        numbers = (ImageView) mActivity.findViewById(R.id.numbers);

        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                gridDimension.x = iv.getMeasuredHeight();
                gridDimension.y = iv.getMeasuredWidth();

                //DRAW THE GRID
                Bitmap grid = Bitmap.createBitmap(gridDimension.x, gridDimension.y, Bitmap.Config.ARGB_8888);
                Grid.drawGrid(grid, gridDimension);
                iv.setImageBitmap(grid);

                //DRAW THE COORDINATES
                Bitmap lettersBitmap = Bitmap.createBitmap(gridDimension.x, gridDimension.y /10, Bitmap.Config.ARGB_8888);
                Bitmap numbersBitmap = Bitmap.createBitmap(gridDimension.x / 10, gridDimension.y, Bitmap.Config.ARGB_8888);
                Grid.drawCoordinates(lettersBitmap, numbersBitmap, gridDimension);
                letters.setImageBitmap(lettersBitmap);
                numbers.setImageBitmap(numbersBitmap);

                return true;
            }
        });

        mButton = (Button) getActivity().findViewById(R.id.save_button);
        mButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        mActivity.mSoundEngine.playShoot();
    }
}
