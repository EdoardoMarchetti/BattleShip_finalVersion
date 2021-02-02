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

            int row = (int) (y / grid.getBlockDimension());
            int column = (int) (x / grid.getBlockDimension());
            String[][] gridConfiguration = grid.getGridConfiguration();
            RectF shipCollider = mTransform.getCollider();

            int eventType = event.getAction() & MotionEvent.ACTION_MASK;

            
            if(eventType == MotionEvent.ACTION_UP ||
                    eventType == MotionEvent.ACTION_POINTER_UP){
                Log.d(TAG, "handleInput: eventType corretto");
                
                //Attivo la nave se l'evento è avvenuto nel suo collider
                if(shipCollider.contains(x,y)){
                    mTransform.setMovable();
                    Log.d(TAG, "handleInput: nave attivata");
                }else{
                    mTransform.setImmovable();
                    Log.d(TAG, "handleInput: nave disattivata");
                }
                
                if(mTransform.checkMovable() && shipCollider.contains(x,y)){
                    mTransform.rotate();
                    Log.d(TAG, "handleInput: nave ruotata");
                    //Controllo se dopo la rotazione la nave deve essere spostata (questa parte potrebbe andare nel movement Component)

                    /*if(mTransform.getObjectHeight() + mTransform.getLocation().x > grid.getGridDimension()){
                        float difference = (mTransform.getObjectHeight() + mTransform.getLocation().x) - grid.getGridDimension();
                        int differenceInBlocks = (int) (difference / grid.getBlockDimension());
                        Log.d(TAG, "handleInput: differenceInBlocks = "+differenceInBlocks);
                        PointF oldLocation = mTransform.getLocation();
                        mTransform.setLocation(oldLocation.x - (grid.getBlockDimension() * differenceInBlocks)
                                ,oldLocation.y);

                    }else if(mTransform.getObjectHeight() + mTransform.getLocation().y > grid.getGridDimension()){
                        float difference = (mTransform.getObjectHeight() + mTransform.getLocation().y ) - grid.getGridDimension();
                        int differenceInBlocks = (int) (difference / grid.getBlockDimension());
                        Log.d(TAG, "handleInput: differenceInBlocks = "+differenceInBlocks);
                        PointF oldLocation = mTransform.getLocation();
                        mTransform.setLocation(oldLocation.x
                                ,oldLocation.y - (grid.getBlockDimension() * differenceInBlocks) );
                    }


                    mTransform.updateCollider();*/
                }
                
            }
            
            
        }
        
    }
}
