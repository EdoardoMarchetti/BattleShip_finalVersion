package com.edomar.battleship.logic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class Transform {

    private static final String TAG = "Transform";

    /** Variables **/
    private RectF mCollider;
    private PointF mLocation;
    private boolean mIsVertical = false;
    private float mObjectHeight; //ridimensionata rispetto alla dimensione dei blocchi
    private float mObjectWidth; //ridimensionata rispetto ai blocchi
    private boolean mIsMovable= false;
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

    public boolean isRotatable(){
        return mRotated;
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
        mRotated = true;
    }

    public void notRotatable(){
        mRotated = false;
    }

    public void setLocation(float horizontal, float vertical){
        mLocation = new PointF(horizontal, vertical);
        updateCollider();
    }



    public void updateCollider(){
        if(mIsVertical){
            mCollider.top = mLocation.y;
            mCollider.left = mLocation.x;
            mCollider.bottom = mLocation.y + mObjectHeight;
            mCollider.right = mLocation.x + mObjectWidth;
        }else{
            mCollider.top = mLocation.y;
            mCollider.left = mLocation.x;
            mCollider.bottom = mLocation.y + mObjectWidth;
            mCollider.right = mLocation.x + mObjectHeight;
        }




        Log.d(TAG, "updateCollider: vertical \n" +
                "mCollider.top = "+mCollider.top / sBlockDimension+
                "\nmCollider.left = "+mCollider.left / sBlockDimension+
                "\nmCollider.bottom = "+mCollider.bottom / sBlockDimension+
                "\nmCollider.right = "+mCollider.right / sBlockDimension);

    }
}
