package com.edomar.battleship.logic.transforms;

import android.graphics.PointF;
import android.util.Log;

import com.edomar.battleship.view.gameplayFragments.MatchFragment;

public class ShipTransform extends Transform {

    private static final String TAG = "ShipTransform";

    private boolean mIsMovable= false; //solo alla nave
    private boolean mRotatable = false; //solo alla nave
    private int mLives = 0;



    public ShipTransform(float objectWidth, float objectHeight, PointF startLocation, float gridDimension) {
        super(objectWidth, objectHeight, startLocation, gridDimension);
        mLives = (int) (Math.max(objectWidth, objectHeight) / super.getBlockDimension()) ;
        Log.d(TAG, "ShipTransform: mLives = "+mLives);
    }


    public void setMovable(){
        mIsMovable = true;
    }

    public void setImmovable(){
        mIsMovable = false;
    }

    public boolean checkMovable(){
        return mIsMovable;
    }


    public void setRotatable(){
        mRotatable = true;
    }

    public void setNotRotatable(){
        mRotatable = false;
    }

    public boolean checkRotatable(){
        return mRotatable;
    }


    public int getLives(){
        return mLives;
    }

    public void shipHit(){
        Log.d("Colpo", "shipHit: vite prima del colpo = " +getLives());
        mLives = mLives-1;
        Log.d("Colpo", "shipHit: vite dopo colpo = "+getLives());
    }








}
