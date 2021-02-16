package com.edomar.battleship.logic.transform;

import android.graphics.PointF;

public class ShipTransform extends Transform {


    private boolean mIsMovable= false; //solo alla nave
    private boolean mRotatable = false; //solo alla nave
    private boolean mRotated = false; //solo alla nave


    public ShipTransform(float objectWidth, float objectHeight, PointF startLocation, float gridDimension) {
        super(objectWidth, objectHeight, startLocation, gridDimension);
    }


    public boolean checkMovable(){
        return mIsMovable;
    }

    public boolean checkRotatable(){
        return mRotatable;
    }

    public void setMovable(){
        mIsMovable = true;
    }

    public void setImmovable(){
        mIsMovable = false;
    }


    public boolean isRotated() {
        return mRotated;
    }

    public void isNotRotated(){
        mRotated = false;
    }

    public void setRotatable(){
        mRotatable = true;
    }

    public void setNotRotatable(){
        mRotatable = false;
    }






}
