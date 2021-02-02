package com.edomar.battleship.logic;

import android.graphics.PointF;
import android.graphics.RectF;

public class Transform {

    /** Variables **/
    private RectF mCollider;
    private PointF mLocation;
    private boolean isVertical = true;
    private float mObjectHeight; //ridimensionata rispetto alla dimensione dei blocchi
    private float mObjectWidth; //ridimensionata rispetto ai blocchi

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
        return isVertical;
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

    /** Setters **/

    public void rotate(){
        isVertical = !isVertical;
    }

    public void setLocation(float horizontal, float vertical){
        mLocation = new PointF(horizontal, vertical);
        updateCollider();
    }



    public void updateCollider(){
        mCollider.top = mLocation.y;
        mCollider.left = mLocation.x;
        mCollider.bottom = mLocation.y + mObjectHeight;
        mCollider.right = mLocation.x + mObjectWidth;
    }
}
