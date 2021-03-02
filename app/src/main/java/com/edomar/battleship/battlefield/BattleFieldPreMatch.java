package com.edomar.battleship.battlefield;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.utils.WriterReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BattleFieldPreMatch extends IBattleField {

    /** Costruttori **/
    public BattleFieldPreMatch(Context context) {
        super(context);
    }

    public BattleFieldPreMatch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public BattleFieldPreMatch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public void addObserver(InputObserver o) {//Probabilmente solo battleField giocatore e fleetFragment sempre
        inputObservers.add(o);
    }



    @Override
    public void run() {
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                //Qua va il codice per disegnare le coordinate
                mRenderer.drawGridCoordinates(mLetters, mNumbers, mLettersDimen);
            }
        });


        while (mRunning){
            long frameStartTime = System.currentTimeMillis();
            ArrayList<GameObject> objects = mLevelManager.getObjects();

            /** Update the objects **/
            mPhysicsEngine.update(mFPS, mParticleSystem,
                    mGrid, this);


            Log.d("NotifyNumber", "run: notifyNumber = "+notifyNumber);
            /** Draw objects **/
            mRenderer.draw(mGrid, objects, mParticleSystem, mGameStateReference.getGameState());

            // Measure the frames per second in the usual way
            long timeThisFrame = System.currentTimeMillis()
                    - frameStartTime;
            if (timeThisFrame >= 1) {
                final int MILLIS_IN_SECOND = 1000;
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }

        }
    }



    @Override
    public boolean saveFleet(String levelToLoad, int playerNumber) {
        Log.d("Saving", "saveDefaultFleet: ");
        ArrayList<GameObject> objects = mLevelManager.getObjects();
        mGrid.clearGrid();

        if(mLevelManager.checkCorrectFleetConfiguration()) {

            for (int i = 0; i < mLevelManager.getNumShipsInLevel(); i++) {
                int startRow = (int) (objects.get(i)
                        .getTransform()
                        .getLocation().y / mGrid.getBlockDimension());



                int startColumn = (int) (objects.get(i)
                        .getTransform()
                        .getLocation().x / mGrid.getBlockDimension());

                float shipWidth = objects.get(i)
                        .getTransform()
                        .getObjectWidth();

                float shipHeight = objects.get(i)
                        .getTransform()
                        .getObjectHeight();

                boolean shipIsVertical;
                int blockOccupied;

                if (shipWidth >= shipHeight) {
                    shipIsVertical = false;
                    blockOccupied = (int) (shipWidth / mGrid.getBlockDimension());
                } else {
                    shipIsVertical = true;
                    blockOccupied = (int) (shipHeight / mGrid.getBlockDimension());
                }

                String gridTag = objects.get(i)
                        .getGridTag() + String.valueOf(i);

                mGrid.positionShip(startRow, startColumn, blockOccupied, shipIsVertical, gridTag);
            }

            List<String[]> gridRows = new ArrayList<>();

            //Transform the matrix in an ArrayList
            gridRows.addAll(Arrays.asList(mGrid.getGridConfiguration()));

            //write on file
            Log.d("WriterReader", "saveFleet:from PreMatch "+levelToLoad);
            WriterReader.getInstance().write(gridRows, "match"+ playerNumber,levelToLoad);
            return true;

        }else{
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (InputObserver io : inputObservers) {
            io.handleInput(event,
                    mGrid, mLevelManager,
                    0, notifyNumber);
        }
        return true;
    }

    @Override
    public void repositionShips(String level, int playerNumber) {

        ArrayList<GameObject> objects = mLevelManager.getObjects();
        boolean[] shipsInError = mLevelManager.getShipsInError();
        boolean corrected = false;

        for (int i = 0; i < shipsInError.length && !corrected; i++) {
            Log.d("RepositionShip", "repositionShips: ship["+i+"] ="  +shipsInError[i]);
            if(shipsInError[i]){
                GameObject ship = objects.get(i);
                RectF shipCollider = objects.get(i).getTransform().getCollider();
                PointF newLocation =  ship.getTransform().getLocation();
                float blockSize = ship.getTransform().getBlockDimension();

                Random random = new Random();
                int x = random.nextInt(9);
                int y = random.nextInt(9);
                Log.d("RepositionShip", "repositionShips: x= "+x+" y= "+y);
                
                if((x*blockSize + shipCollider.right) <= this.getLayoutParams().width && (y*blockSize + shipCollider.bottom ) <= this.getLayoutParams().height){
                    Log.d("RepositionShip", "repositionShips: oldPosition.x = "+newLocation.x);
                    newLocation.x = (x*blockSize);
                    Log.d("RepositionShip", "repositionShips: newPosition.x = "+newLocation.x);
                    Log.d("RepositionShip", "repositionShips: oldPosition.y = "+newLocation.y);
                    newLocation.y = (y*blockSize);
                    Log.d("RepositionShip", "repositionShips: newPosition.y = "+newLocation.y);
                }

                ship.getTransform().setLocation(newLocation.x, newLocation.y);
                corrected = true;
            }

        }

        if(!saveFleet(level, playerNumber)){
            repositionShips(level, playerNumber);
        }
    }
}
