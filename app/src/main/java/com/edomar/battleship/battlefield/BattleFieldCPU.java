package com.edomar.battleship.battlefield;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.utils.WriterReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleFieldCPU extends IBattleField {

    private static final String TAG = "BattleFieldCPU";

    public BattleFieldCPU(Context context) {
        super(context);
    }

    public BattleFieldCPU(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BattleFieldCPU(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);
        shot();
    }

    private void shot() {
        Log.d(TAG, "shot: ");
        Random random = new Random();

        boolean shot = false;
        int column;
        int row;

        do{
            column = random.nextInt(10);
            row = random.nextInt(10);
            Log.d(TAG, "shot: i shot in row = "+row+ " column = "+column);
            Log.d("BattleFieldCPU", "shot:  mGrid.getGridConfiguration()["+row+"]["+column+"] = "+mGrid.getGridConfiguration()[row][column]);

            if(!mGrid.getGridConfiguration()[row][column].equalsIgnoreCase("S") && !mGrid.getGridConfiguration()[row][column].equalsIgnoreCase("X")){
                if(mGrid.getGridConfiguration()[row][column].equalsIgnoreCase("0")){
                    shot = true;
                    mGrid.setLastHit(row, column, false);
                }else{
                    shot = true;
                    mGrid.setLastHit(row, column, true);
                }
            }

        }while(!shot);

        spawnAmmo(row, column);
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

        mLevelManager = new LevelManager(getContext(), this.getLayoutParams().width, this, levelToLoad);

        mGrid = new Grid(this.getLayoutParams().width);
        List<String[]> gridRows = WriterReader.getInstance().readFleet("match"+1 ,levelToLoad);
        mGrid.setDispositionInGrid(gridRows);

        deSpawnRespawn();
    }

    /**Other IBattlefield methods**/
    @Override
    public boolean saveFleet(String levelToLoad, int playerNumber) {
        //DO Nothing
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Do nothing
        return false;
    }

    @Override
    public void repositionShips(String level, int playerNumber) {
        //Do Nothing
    }

    @Override
    public void addObserver(InputObserver o) {
        //DO Nothing
    }
}
