package com.edomar.battleship.logic.components;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.Grid;
import com.edomar.battleship.InputObserver;
import com.edomar.battleship.logic.Level;
import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.components.interfaces.InputComponent;

/** Per il D&D tenere a mente ACTION_MOVE **/

public class ShipInputComponent implements InputComponent, InputObserver {

    private static final String TAG = "ShipInputComponent";
   
    private Transform mTransform;

    float mDownX;
    float mDownY;
    float mCurrentX;
    float mCurrentY;
    float SCROLL_THRESHOLD;
    boolean isOnClick;

    //Il costruttore viene chiamato dal GameObjectFactory
    public ShipInputComponent(BattleField bf) {
        bf.addObserver(this);
    }
   
    @Override
    public void setTransform(Transform t) {
        mTransform = t;
    }

    @Override
    public void handleInput(MotionEvent event, Grid grid, Level level) {
        //Primo check sullo stato del gioco (if statement da inserire)
        //Match -> l'input non è mai rivolto alla nave
        //PreMatch -> l'input è sempre rivolto alla nave -> verificare se si la nave è già "attiva"
        //                                              -> verificare tipo di evento 
        //                                              -> ruota / D&D
        //                                              -> indicare nella model della griglia la nuova posizione della nave
        
        boolean preMatch = true;


        
        if(!preMatch){
            //Do nothing
            mTransform.setImmovable();
        }else{
            //Prima ottengo le coordinate dell'eveto
            int i = event.getActionIndex();
            int x = (int) event.getX(i);
            int y = (int) event.getY(i);
            SCROLL_THRESHOLD = grid.getBlockDimension()/2;


            int row = (int) (y / grid.getBlockDimension());
            int column = (int) (x / grid.getBlockDimension());
            String[][] gridConfiguration = grid.getGridConfiguration();
            RectF shipCollider = mTransform.getCollider();

            int eventType = event.getAction() & MotionEvent.ACTION_MASK;

            switch (eventType){
                case MotionEvent.ACTION_DOWN:

                    mDownX = event.getX();
                    mDownY = event.getY();
                    mCurrentX = mDownX;
                    mCurrentY = mDownY;

                    if(shipCollider.contains(mDownX,mDownY)) {
                        mTransform.setMovable();
                        Log.d(TAG, "handleInput: nave attivata");
                    }else{
                        mTransform.setImmovable();
                    }

                    break;

                case MotionEvent.ACTION_MOVE:

                    if(mTransform.checkMovable()){
                        //Drag the ship

                        drag(event.getX(), event.getY());

                        mCurrentX = event.getX();
                        mCurrentY = event.getY();
                    }

                    break;

                case MotionEvent.ACTION_UP:

                    if(!(Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)){ //rotate or active
                        Log.d(TAG, "simple touch");

                        if(mTransform.checkMovable() && mTransform.checkRotatable()){
                            mTransform.rotate();
                            rotate();
                            Log.d(TAG, "handleInput: nave ruotata");
                        }

                    }else{
                        //Drop the ship
                        //TODO -- drop()
                    }

                    mTransform.setRotatable();
                    break;

            }

            
        }
        
    }



    private void drag(float eventX, float eventY) {
        //First check if the ship is outside the BattleField

        float differenceX = eventX - mCurrentX ;
        float differenceY = eventY - mCurrentY ;

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
        PointF newLocation = new PointF(mTransform.getLocation().x, mTransform.getLocation().y);

        if(newLocation.x + mTransform.getObjectWidth() >= mTransform.getGridDimension()){
            newLocation.x = mTransform.getGridDimension() - mTransform.getObjectWidth();
        }

        if(newLocation.y + mTransform.getObjectHeight() >= mTransform.getGridDimension()){
            newLocation.y = mTransform.getGridDimension() - mTransform.getObjectHeight();
        }

        mTransform.setLocation(newLocation.x, newLocation.y);
    }
}
