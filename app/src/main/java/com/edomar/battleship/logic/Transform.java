package com.edomar.battleship.logic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class Transform {

    private static final String TAG = "Transform";

    /** Variables **/
    private RectF mCollider;
    private PointF mLocation;
    private boolean mIsVertical = true;
    private float mObjectHeight; //ridimensionata rispetto alla dimensione dei blocchi
    private float mObjectWidth; //ridimensionata rispetto ai blocchi
    private boolean mIsMovable= false;
    private boolean mRotatable = false;
    private boolean mRotated = false;

    /** Variabili di utilità **/
    private static float sGridDimension;
    private static float sBlockDimension;



    public Transform (float objectWidth, float objectHeight, PointF startLocation, float gridDimension){
        mCollider = new RectF();
        mObjectHeight = objectHeight;
        mObjectWidth = objectWidth;
        mLocation = startLocation;
        sGridDimension = gridDimension;
        sBlockDimension = gridDimension / 10;
    }

    /** Getters **/

    public float getGridDimension(){
        return sGridDimension;
    }

    public float getBlockDimension(){
        return sBlockDimension;
    }


    public boolean isVertical() {
        return mIsVertical;
    }


    public float getObjectHeight(){
        return mObjectHeight;
    }

    public float getObjectWidth() {
        return mObjectWidth;
    }

    public PointF getLocation() {
        return mLocation;
    }

    public PointF getSize(){
        return new PointF((int) mObjectWidth,
                (int) mObjectHeight);
    }

    public RectF getCollider(){
        return mCollider;
    }

    public boolean checkMovable(){
        return mIsMovable;
    }

    public boolean checkRotatable(){
        return mRotatable;
    }

    /** Setters **/

    public void setMovable(){
        mIsMovable = true;
    }

    public void setImmovable(){
        mIsMovable = false;
    }

    public void rotate(){
        mIsVertical = !mIsVertical;
        invertDimension();
        mRotated = true;
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

    public void setStartLocation(float horizontal, float vertical, boolean isV){
        mLocation = new PointF(horizontal, vertical);
        mIsVertical = isV;
        if(!mIsVertical){
            invertDimension();
        }
        updateCollider();
    }

    private void invertDimension() {
        float support = mObjectWidth;
        mObjectWidth = mObjectHeight;
        mObjectHeight = support;
    }


    public void setLocation(float horizontal, float vertical){
        mLocation = new PointF(horizontal, vertical);
        updateCollider();
        Log.d(TAG, "setLocation: ");
    }



    public void updateCollider(){
        mCollider.top = mLocation.y;
        mCollider.left = mLocation.x;
        mCollider.bottom = mLocation.y + mObjectHeight;
        mCollider.right = mLocation.x + mObjectWidth;



        Log.d(TAG, "updateCollider: vertical \n" +
                "mCollider.top = "+mCollider.top / sBlockDimension+
                "\nmCollider.left = "+mCollider.left / sBlockDimension+
                "\nmCollider.bottom = "+mCollider.bottom / sBlockDimension+
                "\nmCollider.right = "+mCollider.right / sBlockDimension);

    }


}
