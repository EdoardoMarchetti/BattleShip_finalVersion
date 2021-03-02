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
import java.util.Random;

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
                if(notifyNumber == 1 || notifyNumber == 3){
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

        mGrid = new Grid(this.getLayoutParams().width);
        List<String[]> gridRows = WriterReader.getInstance().readFleet("match"+playerNumber ,levelToLoad);

        if(gridRows.size() == 0){//La cartella avversaria non esiste e quindi il giocatore è il computer
            autoPosition();
            Log.d("CPUvsHuman", "setLevel: ");
        }else{
            mGrid.setDispositionInGrid(gridRows);
            Log.d("CPUvsHuman", "setLevel: ");
        }


        deSpawnRespawn();
    }


    private void autoPosition() {


        String[] gridTag = new String[mLevelManager.getNumShipsInLevel()];
        Random random = new Random();
        float blockSize = mGrid.getBlockDimension();

        for (int i = 0; i < mLevelManager.getNumShipsInLevel(); i++) {
            gridTag[i] = mLevelManager.getObjects()
                    .get(i)
                    .getGridTag();
        }

        for(int i = 0; i < gridTag.length; i++ ){

            Log.d("ShipPosition", "autoPosition: number "+i);
            boolean isVertical = random.nextBoolean();
            int blockOccupied = (int) (mLevelManager.getObjects()
                                .get(i)
                                .getTransform()
                                .getObjectHeight() / blockSize); //By default all objects are vertical
            Log.d("ShipPosition", "autoPosition: isVertical = "+ isVertical+" blockOccupied= "+blockOccupied);

            boolean goodPosition = true;
            int startColumn;
            int startRaw;

            do{
                goodPosition = true;
                startColumn = random.nextInt(10);
                startRaw = random.nextInt(10);
                Log.d("ShipPosition", "autoPosition: inizialmente x = "+ startColumn+" y= "+startRaw);

                if(isVertical){
                    if(!(startRaw+blockOccupied<=9)){
                        startRaw = startRaw-blockOccupied;
                    }
                }else{
                    if(!(startColumn+blockOccupied<=9)){
                        startColumn = startColumn-blockOccupied;
                    }
                }
                Log.d("ShipPosition", "autoPosition: finali x = "+ startColumn+" y= "+startRaw);

                if(isVertical){//NAVE VERTICALE

                    if(mLevelManager.needDistance()){
                        //controlla il contorno
                        for (int j = startRaw-1; j <= startRaw+blockOccupied+1 && goodPosition; j++) {
                            if(j >= 0 && j<10 ){
                                for (int k = startColumn-1; k <= startColumn+1 && goodPosition; k++) {
                                    if(k >= 0 && k < 10){
                                        Log.d("ShipPosition", "autoPosition: ["+j+"]["+k+"] ="+ mGrid.getGridConfiguration()[j][k]);
                                        if(!mGrid.getGridConfiguration()[j][k].equalsIgnoreCase("0")){
                                            goodPosition=false;
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        for (int j = startRaw; j < startRaw+blockOccupied && goodPosition; j++) {
                            Log.d("ShipPosition", "autoPosition: ["+j+"]["+startColumn+"] ="+ mGrid.getGridConfiguration()[j][startColumn] );
                            if(!mGrid.getGridConfiguration()[j][startColumn].equalsIgnoreCase("0")){
                                goodPosition=false;
                            }
                        }
                    }

                }else{//NAVE ORIZZONTALE
                    if(mLevelManager.needDistance()){
                        //controlla il contorno
                        for (int j = startRaw-1; j <= startRaw+1 && goodPosition; j++) {
                            if(j >= 0 && j<10 ){
                                for (int k = startColumn-1; k <=startColumn+blockOccupied+1 && goodPosition; k++) {
                                    if(k >= 0 && k < 10){
                                        Log.d("ShipPosition", "autoPosition: ["+j+"]["+k+"] ="+ mGrid.getGridConfiguration()[j][k]);
                                        if(!mGrid.getGridConfiguration()[j][k].equalsIgnoreCase("0")){
                                            goodPosition=false;
                                        }
                                    }
                                }
                            }
                        }

                    }else{
                        for (int j = startColumn; j < startColumn+blockOccupied && goodPosition; j++) {
                            Log.d("ShipPosition", "autoPosition: ["+startRaw+"]["+j+"] ="+ mGrid.getGridConfiguration()[startRaw][j] );
                            if(!mGrid.getGridConfiguration()[startRaw][j].equalsIgnoreCase("0")){
                                goodPosition =false;
                            }
                        }
                    }
                }



            }while(!goodPosition);

            Log.d("ShipPosition", "autoPosition: FUORI LOOP x = "+ startColumn+" y= "+startRaw);
            mGrid.positionShip(startRaw,startColumn, blockOccupied, isVertical, gridTag[i]+i);



        }

        String[][] grid= mGrid.getGridConfiguration();
        Log.d("ShipPosition", "onClick: " +
                        "\n1A= " + grid[0][0] + "\t1B=" + grid[0][1] + "\t1C= " + grid[0][2] + "\t1D=" + grid[0][3] + "\t1E= " + grid[0][4] + "\t1F=" + grid[0][5] + "\t1G= " + grid[0][6] + "\t1H=" + grid[0][7] + "\t1I= " + grid[0][8] + "\t1J=" + grid[0][9] +
                        "\n2A= " + grid[1][0] + "\t2B=" + grid[1][1] + "\t2C= " + grid[1][2] + "\t2D=" + grid[1][3] + "\t2E= " + grid[1][4] + "\t2F=" + grid[1][5] + "\t2G= " + grid[1][6] + "\t2H=" + grid[1][7] + "\t2I= " + grid[1][8] + "\t2J=" + grid[1][9] +
                        "\n3A= " + grid[2][0] + "\t3B=" + grid[2][1] + "\t3C= " + grid[2][2] + "\t3D=" + grid[2][3] + "\t3E= " + grid[2][4] + "\t3F=" + grid[2][5] + "\t3G= " + grid[2][6] + "\t3H=" + grid[2][7] + "\t3I= " + grid[2][8] + "\t3J=" + grid[2][9] +
                        "\n4A= " + grid[3][0] + "\t4B=" + grid[3][1] + "\t4C= " + grid[3][2] + "\t4D=" + grid[3][3] + "\t4E= " + grid[3][4] + "\t4F=" + grid[3][5] + "\t4G= " + grid[3][6] + "\t4H=" + grid[3][7] + "\t4I= " + grid[3][8] + "\t4J=" + grid[3][9] +
                        "\n5A= " + grid[4][0] + "\t5B=" + grid[4][1] + "\t5C= " + grid[4][2] + "\t5D=" + grid[4][3] + "\t5E= " + grid[4][4] + "\t5F=" + grid[4][5] + "\t5G= " + grid[4][6] + "\t5H=" + grid[4][7] + "\t5I= " + grid[4][8] + "\t5J=" + grid[4][9] +
                        "\n6A= " + grid[5][0] + "\t6B=" + grid[5][1] + "\t6C= " + grid[5][2] + "\t6D=" + grid[5][3] + "\t6E= " + grid[5][4] + "\t6F=" + grid[5][5] + "\t6G= " + grid[5][6] + "\t6H=" + grid[5][7] + "\t6I= " + grid[5][8] + "\t6J=" + grid[5][9] +
                        "\n7A= " + grid[6][0] + "\t7B=" + grid[6][1] + "\t7C= " + grid[6][2] + "\t7D=" + grid[6][3] + "\t7E= " + grid[6][4] + "\t7F=" + grid[6][5] + "\t7G= " + grid[6][6] + "\t7H=" + grid[6][7] + "\t7I= " + grid[6][8] + "\t7J=" + grid[6][9] +
                        "\n8A= " + grid[7][0] + "\t8B=" + grid[7][1] + "\t8C= " + grid[7][2] + "\t8D=" + grid[7][3] + "\t8E= " + grid[7][4] + "\t8F=" + grid[7][5] + "\t8G= " + grid[7][6] + "\t8H=" + grid[7][7] + "\t7I= " + grid[7][8] + "\t7J=" + grid[7][9] +
                        "\n9A= " + grid[8][0] + "\t9B=" + grid[8][1] + "\t9C= " + grid[8][2] + "\t9D=" + grid[8][3] + "\t9E= " + grid[8][4] + "\t9F=" + grid[8][5] + "\t9G= " + grid[8][6] + "\t9H=" + grid[8][7] + "\t7I= " + grid[8][8] + "\t7J=" + grid[8][9] +
                        "\n10A= " + grid[9][0] + "\t10B=" + grid[9][1] + "\t10C= " + grid[9][2] + "\t10D=" + grid[9][3] + "\t10E= " + grid[9][4] + "\t10F=" + grid[9][5] + "\t10G= " + grid[9][6] + "\t10H=" + grid[9][7] + "\t10I= " + grid[9][8] + "\t10J=" + grid[9][9]);



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

    @Override
    public void repositionShips(String level, int playerNumber) {
        //Do nothing
    }

}
