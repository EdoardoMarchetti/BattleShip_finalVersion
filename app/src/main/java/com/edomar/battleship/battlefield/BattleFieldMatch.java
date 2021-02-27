package com.edomar.battleship.battlefield;

import android.content.Context;
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

public class BattleFieldMatch extends IBattleField {

    /** Costruttori **/
    public BattleFieldMatch(Context context) {
        super(context);
    }

    public BattleFieldMatch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public BattleFieldMatch(Context context, AttributeSet attrs, int defStyleAttr) {
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


            if(!notificated){
                if(notifyNumber == 1){
                    mFragmentReference.notifyHit(mGrid.getLastHitCoordinates().x,mGrid.getLastHitCoordinates().y, mGrid.getLastHitResult());
                    notificated = true;
                }else if(notifyNumber == 2){
                    mFragmentReference.notifyEndGame();
                    notificated = true;
                }

            }

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
    public void setLevel(String levelToLoad, int playerNumber) {
        inputObservers.clear();
        inputObservers.add(mGridController);

        mLevelManager = new LevelManager(getContext(), this.getLayoutParams().width, this, levelToLoad);

        if(playerNumber == 1){
            playerNumber = 2;
        }else{
            playerNumber = 1;
        }

        List<String[]> gridRows = WriterReader.getInstance().readFleet("match"+playerNumber ,levelToLoad);
        mGrid = new Grid(this.getLayoutParams().width, gridRows);
        deSpawnRespawn();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (InputObserver io : inputObservers) {
            io.handleInput(event,
                    mGrid, mLevelManager,
                    1, notifyNumber);
        }
        return true;
    }


    /**IBattleField method**/
    @Override
    public boolean saveFleet(String levelToLoad, int playerNumber) {
        //DO NOTHING
        return false;
    }

}
