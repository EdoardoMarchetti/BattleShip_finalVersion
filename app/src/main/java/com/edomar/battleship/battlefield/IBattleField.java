package com.edomar.battleship.battlefield;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.edomar.battleship.Renderer;
import com.edomar.battleship.battlefield.interfacesImplemented.AmmoSpawner;
import com.edomar.battleship.battlefield.interfacesImplemented.BattleFieldBroadcaster;
import com.edomar.battleship.logic.ParticleSystem;
import com.edomar.battleship.logic.PhysicsEngine;
import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.grid.GridInputController;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.utils.BitmapStore;
import com.edomar.battleship.utils.WriterReader;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class IBattleField extends SurfaceView implements SurfaceHolder.Callback,
        Runnable,
        AmmoSpawner,
        BattleFieldBroadcaster
{

    private static final String TAG = "BattleField";

    private Thread mThread;
    private long mFPS;


    /** Instances **/
    public Renderer mRenderer;
    public PhysicsEngine mPhysicsEngine;
    public ParticleSystem mParticleSystem;
    public Grid mGrid;
    public boolean mRunning;
    public BitmapStore mBitmapStore;
    public LevelManager mLevelManager;
    public ArrayList<InputObserver> inputObservers = new ArrayList<>();
    public GridInputController mGridController;



    /**Variabili per gestire le coordinate**/
    ImageView mLetters;
    ImageView mNumbers;
    Point mLettersDimen = new Point();




    /** Costruttori **/
    public IBattleField(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public IBattleField(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        Log.d(TAG, "BattleField: in the constructor");
    }



    public IBattleField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    /**Metodi Callback**/
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }


    /** Init Method**/
    public void init(String level){
        mBitmapStore = BitmapStore.getInstance(getContext());
        mGridController = new GridInputController(this); //Probabilmente solo battleField giocatore
        mPhysicsEngine = new PhysicsEngine();
        mParticleSystem = new ParticleSystem();
        mParticleSystem.init(250);
        mRenderer = new Renderer(this);
        setLevel(level);
    }

    /** Adding observers **/
    @Override
    public void addObserver(InputObserver o) {//Probabilmente solo battleField giocatore e fleetFragment sempre
        inputObservers.add(o);
    } //Probabilmente solo battlefield giocatore

    /** Start and stop Thread**/
    public void stopThread() {//in tutti i battlefield
        // New code here soon
        mRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            Log.e("Exception","stopThread()"
                    + e.getMessage());
        }
    }

    public void startThread() {//in tutti i battlefield
        // New code here soon
        mRunning= true; //da cambiare con il gameState
        mThread = new Thread(this);
        mThread.start();
    }

    /**Run**/
    @Override
    public void run() {//in tutti i battlefield

        //Le coordinate sono fuori dal while perchè vengono disegnate solo una volta
        //Il metodo threadHandler.post è necessario in quanto Battlefield deve operare
        //su componenti di tipo ImageView che devono essere trattai su thread pricipale
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
                    objects, mGrid);


            /** Draw objects **/
            mRenderer.draw(mGrid, objects, mParticleSystem);

            // Measure the frames per second in the usual way
            long timeThisFrame = System.currentTimeMillis()
                    - frameStartTime;
            if (timeThisFrame >= 1) {
                final int MILLIS_IN_SECOND = 1000;
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }

    }

    /** set ImageView per le coordinate**/
    public void setImageViewsForCoordinates(ImageView letters, ImageView numbers){//in tutti i battlefield
        mLetters = letters;
        mNumbers = numbers;
        mLettersDimen.x = letters.getLayoutParams().width;
        mLettersDimen.y = letters.getLayoutParams().height;
    }



    //Posiziona le navi in bas alla configurazione della griglia
    public void deSpawnRespawn(){//in tutti i battlefield

        ArrayList<GameObject> objects = mLevelManager.getObjects();
        String[][] gridConfiguration = mGrid.getGridConfiguration();


        for (GameObject o: objects) {
            o.setInactive();
        }


        for(int i = 0; i<mLevelManager.getNumShipsInLevel(); i++) {
            int row = 0;
            int column = 0;
            boolean found = false;

            String gridTag = objects.get(i)
                    .getGridTag() + String.valueOf(i); //String.valueOf(i) mi permette di distinguera tra navi dello stesso tipo


            for (int j = 0; j < gridConfiguration.length && !found; j++) {
                for (int k = 0; k < gridConfiguration.length && !found; k++) {


                    if(gridConfiguration[j][k].equals(gridTag)){

                        found = true;

                        try {


                            if (gridConfiguration[j + 1][k].equals(gridTag)) {
                                objects.get(i)
                                        .getTransform()
                                        .setVertical();
                                row = j;
                                column = k;

                                Log.d("SpawnConLettura", "deSpawnRespawn: found gridTag = "+gridTag+
                                        "\nrow= "+row+
                                        "\ncolumn= "+column+
                                        "\nvertical= ");

                            }
                        }catch (IndexOutOfBoundsException ioobe){
                            ioobe.printStackTrace();
                            Log.e("SpawnConLettura", "deSpawnRespawn: outbound from  ");
                        }

                        try {

                            if (gridConfiguration[j][k + 1].equals(gridTag)) {
                                objects.get(i)
                                        .getTransform()
                                        .setHorizontal();
                                row = j;
                                column = k;

                                Log.d("SpawnConLettura", "deSpawnRespawn: found gridTag = "+gridTag+
                                        "\nrow= "+row+
                                        "\ncolumn= "+column+
                                        "\nhorizontal= ");

                            }

                        }catch (IndexOutOfBoundsException ioobe){
                            ioobe.printStackTrace();
                            Log.e("SpawnConLettura", "deSpawnRespawn: outbound from  ");
                        }
                    }

                }
            }

            if(found) {
                //Esiste il file e quindi disporre secondo la griglia restituita
                objects.get(i)
                        .spawn(row, column);
            }else{
                //Non esiste il file quindi disporre in maniera progressiva
                objects.get(i)
                        .spawn(0,i);
            }


        }


    }

    @Override
    public boolean spawnAmmo(int row, int column) {//solo per battlefield Match

        ArrayList<GameObject> objects = mLevelManager.getObjects();

        Log.d("SpawnMissile", "spawnAmmo: in BattleField");
        objects.get(LevelManager.MISSILE)
                .spawn(row, column);
        Log.d("SpawnMissile", "spawnAmmo: after spawn");


        return true;
    }

    public boolean saveDefaultFleet(String levelToLoad) {//solo per fleetFragment
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
            WriterReader.getInstance().write(gridRows, levelToLoad);
            return true;

        }else{
            return false;
        }

    }

    public void setLevel(String levelToLoad) {
        inputObservers.clear();
        inputObservers.add(mGridController);

        mLevelManager = new LevelManager(getContext(), this.getLayoutParams().width, this, levelToLoad);
        List<String[]> gridRows = WriterReader.getInstance().read(levelToLoad);
        mGrid = new Grid(this.getLayoutParams().width, gridRows);
        deSpawnRespawn(); //indica ad ogni nave dove posizionarsi
        Log.d(TAG, "setLevel: inputObservers "+inputObservers.size());
    }


    /**Metodi per battleField giocatore**/
    @Override
    public abstract boolean onTouchEvent(MotionEvent event);//Probabilmente solo in fase preMatch
}
