package com.edomar.battleship.logic.components;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.edomar.battleship.battlefield.IBattleField;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.battlefield.InputObserver;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.logic.transforms.ShipTransform;
import com.edomar.battleship.logic.transforms.Transform;
import com.edomar.battleship.logic.components.interfaces.InputComponent;

/** Per migliorare il D&D bisognerebbe avere anche mFPS da Battlefield**/

public class ShipInputComponent implements InputComponent, InputObserver
        //,UpdateComponent
        {

    private static final String TAG = "ShipInputComponent";
   
    private ShipTransform mTransform;

    float mDownX;
    float mDownY;

    float mCurrentX;
    float mCurrentY;


    float SCROLL_THRESHOLD;
    boolean isOnClick;

    //For test
    boolean dragging = false;
    private boolean dropped = false;
    private long mFps;


    //Il costruttore viene chiamato dal GameObjectFactory
    public ShipInputComponent(IBattleField bf) {
        bf.addObserver(this);
    }
   
    @Override
    public void setTransform(Transform t) {
        Log.d(TAG, "setTransform: transfmor setted");
        Log.d(TAG, "setTransform: is t null? "+ (t == null));
        mTransform = (ShipTransform) t;
        Log.d(TAG, "setTransform: is mTransform null? "+ (mTransform == null));

    }

    @Override
    public void handleInput(MotionEvent event, Grid grid, LevelManager levelManager) {
        //Primo check sullo stato del gioco (if statement da inserire)
        //Match -> l'input non è mai rivolto alla nave
        //PreMatch -> l'input è sempre rivolto alla nave -> verificare se si la nave è già "attiva"
        //                                              -> verificare tipo di evento 
        //                                              -> ruota / D&D
        //                                              -> indicare nella model della griglia la nuova posizione della nave
        
        boolean preMatch = true;

        SCROLL_THRESHOLD = mTransform.getBlockDimension() / 10;

        RectF shipCollider = mTransform.getCollider();



        int eventType = event.getAction() & MotionEvent.ACTION_MASK;

        if(eventType == MotionEvent.ACTION_DOWN && preMatch){
            mDownX = event.getX();
            mDownY = event.getY();
            mCurrentX = mDownX;
            mCurrentY = mDownY;


            if(shipCollider.contains(mDownX,mDownY) && levelManager.getCurrentLevel().transformInMovement == null) {
                mTransform.setMovable();
                levelManager.getCurrentLevel().transformInMovement = mTransform;
                isOnClick = true;

            }else{
                mTransform.setImmovable();
                mTransform.setNotRotatable();
            }

        }


        if(levelManager.getCurrentLevel().transformInMovement == mTransform){
            //Log.d(TAG, "handleInput: i'm movable");
            switch (eventType){

                case MotionEvent.ACTION_MOVE:

                    if(mTransform.checkMovable()){
                        //Drag the ship
                        drag(event.getX(), event.getY());

                        //test
                        //dragging = true;

                        mCurrentX = event.getX();
                        mCurrentY = event.getY();
                    }

                    if(Math.abs(mDownX-mCurrentX) > SCROLL_THRESHOLD || Math.abs(mDownY-mCurrentY ) > SCROLL_THRESHOLD){
                        isOnClick = false;
                    }

                    break;

                case MotionEvent.ACTION_UP:

                    //test
                    //dragging = false;

                    if(isOnClick){ //rotate or active
                        //Log.d(TAG, "simple touch");

                        if(mTransform.checkMovable() && mTransform.checkRotatable()){
                            mTransform.rotate();
                            rotate();
                            //Log.d(TAG, "handleInput: nave ruotata");
                        }

                    }else{
                        //Drop the ship
                        drop(event.getX(), event.getY());

                        //dropped = true;
                    }

                    mTransform.setRotatable();
                   //Log.d("LOCKINGTRAN", "4) handleInput: transformInMovement= "+ levelManager.getCurrentLevel().transformInMovement);
                    if(!(levelManager.getCurrentLevel().transformInMovement == null)) {
                        levelManager.getCurrentLevel().transformInMovement = null;
                        //Log.d("ProvaDelCollider", "5) handleInput: transformInMovement= " + levelManager.getCurrentLevel().transformInMovement);
                    }

                    levelManager.checkCorrectFleetConfiguration();

                    break;

            }

            
        }
        
    }



    private void drag(float eventX, float eventY) {
        //First check if the ship is outside the BattleField
        Log.d("Dragging", "drag: ");
        float differenceX = (eventX - mCurrentX);
        float differenceY = (eventY - mCurrentY);
        Log.d("Differenza", "drag: (eventX - mCurrentX) = "+(eventX - mCurrentX));
        Log.d("Differenza", "drag: (eventX - mCurrentX) / fps = "+(eventX - mCurrentX)/mFps);
        Log.d("Differenza", "fps = "+mFps);

        PointF oldLocation = mTransform.getLocation();
        PointF newLocation = new PointF(oldLocation.x + differenceX, oldLocation.y + differenceY);

        //Left
        if(newLocation.x <= 0){
            newLocation.x = 0;
        }
        //Top
        if(newLocation.y <= 0){
            newLocation.y = 0;
        }
        //Right
        if(newLocation.x + mTransform.getObjectWidth() >= mTransform.getGridDimension()){
            newLocation.x = mTransform.getGridDimension() - mTransform.getObjectWidth();
        }
        //Bottom
        if(newLocation.y + mTransform.getObjectHeight() >= mTransform.getGridDimension()){
            newLocation.y = mTransform.getGridDimension() - mTransform.getObjectHeight();
        }

        mTransform.setLocation(newLocation.x, newLocation.y);

    }

    private void rotate() {
        PointF newLocation = new PointF( );
        double x = mTransform.getLocation().x;
        double y = mTransform.getLocation().y;


        if(x + mTransform.getObjectWidth() >= mTransform.getGridDimension()){
            x = mTransform.getGridDimension() - mTransform.getObjectWidth();
        }

        if(y + mTransform.getObjectHeight() >= mTransform.getGridDimension()){
            y = mTransform.getGridDimension() - mTransform.getObjectHeight();
        }

        x = x / mTransform.getBlockDimension();
        y = y / mTransform.getBlockDimension();

        if(Math.round(x) >= 10 || Math.round(y) >= 10){
            newLocation.x = mTransform.getBlockDimension() * (int) x;
            newLocation.y = mTransform.getBlockDimension() * (int) y;
        }else{
            newLocation.x = mTransform.getBlockDimension() * Math.round(x);
            newLocation.y = mTransform.getBlockDimension() * Math.round(y);
        }

        Log.d(TAG, "rotate: newLocation.x = "+newLocation.x+ " newLocation.y = "+ newLocation.y);

        mTransform.setLocation(newLocation.x, newLocation.y);
    }

    private void drop(float eventX, float eventY) {
        PointF oldLocation = mTransform.getLocation();
        PointF newLocation = new PointF();


        double x =  (oldLocation.x / mTransform.getBlockDimension());
        double y =  (oldLocation.y / mTransform.getBlockDimension());

        Log.d(TAG, "drop: new coo \n"+
                "x= "+x+
                ", y= "+y);

        if(Math.round(x) >= 10 || Math.round(y) >= 10){
            newLocation.x = mTransform.getBlockDimension() * (int) x;
            newLocation.y = mTransform.getBlockDimension() * (int) y;
        }else{
            newLocation.x = mTransform.getBlockDimension() * Math.round(x);
            newLocation.y = mTransform.getBlockDimension() * Math.round(y);
        }

        mTransform.setLocation(newLocation.x, newLocation.y);
    }


    /*@Override
    public boolean update(long fps, Transform t, Grid grid) {
        Log.d("Update", "update: i'm moving the ship");

        mFps = fps;
        Log.d(TAG, "update: mFps = " + mFps);
    }*/
}
