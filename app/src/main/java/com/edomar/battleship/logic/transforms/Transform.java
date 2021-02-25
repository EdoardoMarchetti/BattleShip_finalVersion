package com.edomar.battleship.logic.transforms;

import android.graphics.PointF;
import android.util.Log;

public class Transform { //Pagina 528 e 708

    private static final String TAG = "Transform";

    //public boolean stopOther = false;

    /** Private Variables **/
    private Collider mCollider;
    private PointF mLocation;
    private float mObjectHeight; //ridimensionata rispetto alla dimensione dei blocchi
    private float mObjectWidth; //ridimensionata rispetto ai blocchi
    private boolean mIsVertical = true; //solo alla nave



    /*
    private boolean mIsMovable= false; //solo alla nave
    private boolean mRotatable = false; //solo alla nave
    private boolean mRotated = false; //solo alla nave*/


    /** Variabili di utilità **/
    private static float sGridDimension;
    private static float sBlockDimension;



    public Transform (float objectWidth, float objectHeight, PointF startLocation, float gridDimension){
        mCollider = new Collider();
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

    public Collider getCollider(){
        return mCollider;
    }



    /** Setters **/

    public void setVertical(){
        mIsVertical = true;
    }

    public void setHorizontal(){
        mIsVertical = false;
    }

    public void rotate(){
        mIsVertical = !mIsVertical;
        invertDimension();
    }

    protected void invertDimension() {
        Log.d("SpawnConLettura", "invertDimension: " +
                "\nwidth: "+mObjectWidth+
                "\nheight: "+mObjectHeight);
        float support = mObjectWidth;
        mObjectWidth = mObjectHeight;
        mObjectHeight = support;
        Log.d("SpawnConLettura", "invertDimension: " +
                "\nwidth: "+mObjectWidth+
                "\nheight: "+mObjectHeight);
    }


    public void setLocation(float horizontal, float vertical){
        mLocation = new PointF(horizontal, vertical);
        updateCollider();
        Log.d(TAG, "setLocation: ");
    }

    public void setStartLocation(float horizontalCoo, float verticalCoo, boolean isV){
        mLocation = new PointF(horizontalCoo, verticalCoo);
        mIsVertical = isV;

        if(!mIsVertical){
            invertDimension();
        }
        updateCollider();
    }


    public void updateCollider(){
        mCollider.top = mLocation.y;
        mCollider.left = mLocation.x;
        mCollider.bottom = mLocation.y + mObjectHeight;
        mCollider.right = mLocation.x + mObjectWidth;



        /*Log.d("ProvaDelCollider", "updateCollider:  \n" +
                "mCollider.top = "+mCollider.top / sBlockDimension+
                "\nmCollider.left = "+mCollider.left / sBlockDimension+
                "\nmCollider.bottom = "+mCollider.bottom / sBlockDimension+
                "\nmCollider.right = "+mCollider.right / sBlockDimension);*/

    }



}
