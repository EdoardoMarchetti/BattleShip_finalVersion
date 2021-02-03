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
        RectF shipCollider = mTransform.getCollider();

        if(shipCollider.right+differenceX >= mTransform.getGridDimension() && eventX >= 0){
            if(mTransform.isVertical()){
                mTransform.setLocation(mTransform.getGridDimension()-mTransform.getObjectWidth(),oldLocation.y+ differenceY);
            }else {
                mTransform.setLocation(mTransform.getGridDimension() - mTransform.getObjectHeight(), oldLocation.y + differenceY);
            }



        }else if(shipCollider.bottom+differenceY >= mTransform.getGridDimension() && eventY >= 0){
            if(mTransform.isVertical()){
                mTransform.setLocation(oldLocation.x + differenceX, mTransform.getGridDimension() - mTransform.getObjectHeight());
            }else {
                mTransform.setLocation(oldLocation.x+ differenceX,mTransform.getGridDimension()-mTransform.getObjectWidth());

            }


        }else if(shipCollider.left+differenceX <= 0 && eventX <= 0){
            mTransform.setLocation(0,oldLocation.y+differenceY);

        }else if(shipCollider.top+differenceY <= 0 && eventY <= 0){
            mTransform.setLocation(oldLocation.x + differenceX,0);

        }else{
            mTransform.setLocation(oldLocation.x + differenceX,oldLocation.y + differenceY);
        }

    }
}
