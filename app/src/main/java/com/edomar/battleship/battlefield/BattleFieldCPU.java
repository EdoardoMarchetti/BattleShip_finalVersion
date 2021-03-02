package com.edomar.battleship.battlefield;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.edomar.battleship.logic.AlternativeArtificialPlayer;
import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.utils.WriterReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BattleFieldCPU extends IBattleField {

    private static final String TAG = "BattleFieldCPU";


    private AlternativeArtificialPlayer mAlterArtificialPlayer;

    private int lastRow;
    private int lastColumn;

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

    @Override
    public void init(String level, int playerNumber) {
        super.init(level, playerNumber);

        mAlterArtificialPlayer = new AlternativeArtificialPlayer();
    }


    public void shot(){
        Log.d("AlternativeArtificialPl", "shot: ");
        Point coordinates = mAlterArtificialPlayer.shot();
        int column = coordinates.y;
        int row = coordinates.x;
        Log.d("AlternativeArtificialPl", "shot: coordinates x= "+row+" y ="+column);
        Log.d("AlternativeArtificialPl", "shot: "+mGrid.getGridConfiguration()[row][column]);
        if(!mGrid.getGridConfiguration()[row][column].equalsIgnoreCase("S") && !mGrid.getGridConfiguration()[row][column].equalsIgnoreCase("X")){
            if(mGrid.getGridConfiguration()[row][column].equalsIgnoreCase("0")){
                mGrid.setLastHit(row, column, false);
                mAlterArtificialPlayer.updateList(row, column, false);
            }else{
                mGrid.setLastHit(row, column, true);
                mAlterArtificialPlayer.updateList(row, column, true);
            }
        }

        lastRow = row;
        lastColumn = column;

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
                }else if(notifyNumber == 3 ){
                    mFragmentReference.notifyHit(mGrid.getLastHitCoordinates().x,mGrid.getLastHitCoordinates().y, mGrid.getLastHitResult());
                    notificated = true;
                    if(mLevelManager.needDistance()) {
                        mAlterArtificialPlayer.updateListForDistance(lastRow, lastColumn);
                    }
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
