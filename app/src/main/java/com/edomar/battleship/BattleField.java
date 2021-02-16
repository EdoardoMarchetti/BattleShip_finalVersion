package com.edomar.battleship;

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
import android.widget.Toast;

import com.edomar.battleship.logic.GameObject;
import com.edomar.battleship.logic.Level;
import com.edomar.battleship.logic.ParticleSystem;
import com.edomar.battleship.logic.PhysicsEngine;
import com.edomar.battleship.utils.BitmapStore;
import com.edomar.battleship.utils.WriterReader;
import com.edomar.battleship.view.gameplayFragments.MatchFragment;

import java.util.ArrayList;
import java.util.List;

public class BattleField extends SurfaceView implements Runnable,
        SurfaceHolder.Callback,
        BattleFieldBroadcaster,
        AmmoSpawner{

    private static final String TAG = "BattleField";

    private Thread mThread;
    private long mFPS;


    /** Instances **/
    private Renderer mRenderer;
    private PhysicsEngine mPhysicsEngine;
    private ParticleSystem mParticleSystem;
    private Grid mGrid;
    private boolean mRunning;
    private BitmapStore mBitmapStore;
    private Level mLevel;
    private ArrayList<InputObserver> inputObservers = new ArrayList<>();
    private GridInputController mGridController;



    /**Variabili per gestire le coordinate**/
    ImageView mLetters;
    ImageView mNumbers;
    Point mLettersDimen = new Point();

    /**Prova**/
    int mN = 0;
    String nome = "";


    /** Costruttori **/
    public BattleField(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public BattleField(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        Log.d(TAG, "BattleField: in the constructor");
    }



    public BattleField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    /**Metodi Callback**/
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated: now you can draw");
        Log.d("BOHC", "surfaceCreated: "+mN);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("BOHC", "surfaceChanged: "+mN);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("BOHC", "surfaceDestroyed: "+mN);
    }


    /** Init Method**/
    public void init(){
        Log.d("InitMethod", "init: in init method for fragment number = "+ mN);
        Log.d("InitMethod", "init: in init method for fragment name = "+ nome);
        mBitmapStore = BitmapStore.getInstance(getContext());
        mLevel = new Level(getContext(), this.getLayoutParams().width, this);
        List<String[]> gridRows = WriterReader.getInstance().read(mLevel.mName);
        mGrid = new Grid(this.getLayoutParams().width, gridRows);
        //placeTheShips();
        mGridController = new GridInputController(this);
        mPhysicsEngine = new PhysicsEngine();
        mParticleSystem = new ParticleSystem();
        mParticleSystem.init(250);
        mRenderer = new Renderer(this);
    }

    /** Adding observers **/
    @Override
    public void addObserver(InputObserver o) {
        inputObservers.add(o);
    }

    /** Start and stop Thread**/
    public void stopThread() {
        // New code here soon
        mRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            Log.e("Exception","stopThread()"
                    + e.getMessage());
        }
    }

    public void startThread() {
        // New code here soon
        mRunning= true; //da cambiare con il gameState
        mThread = new Thread(this);
        mThread.start();
    }

    /**Run**/
    @Override
    public void run() {

        //Le coordinate sono fuori dal while perchè vengono disegnate solo una volta
        //Il metodo threadHandler.post è necessario in quanto Battlefield deve operare
        //su componenti di tipo ImageView che devono essere trattai su thread pricipale
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                //Qua va il codice per disegnare le coordinate
                mRenderer.drawGridCoordinates(mLetters, mNumbers, mLettersDimen );
            }
        });

        deSpawnRespawn();//Il metodo deSpawnReSpawn dovrà essere invocato solo all'inizio della partita per posizionare le navi
                         //sulla view

        while (mRunning){
            long frameStartTime = System.currentTimeMillis();
            ArrayList<GameObject> objects = mLevel.getGameObject();

            /** Update the objects **/
            mPhysicsEngine.update(mFPS, mParticleSystem, objects, mGrid);


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
    public void setImageViewsForCoordinates(ImageView letters, ImageView numbers){
        mLetters = letters;
        mNumbers = numbers;
        mLettersDimen.x = letters.getLayoutParams().width;
        mLettersDimen.y = letters.getLayoutParams().height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Toast.makeText(getContext(), "toccata", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onTouchEvent: inside touchevent");

        for (InputObserver io : inputObservers) {
            io.handleInput(event, mGrid, mLevel);
        }

        //mParticleSystem.emitParticles( new PointF(event.getX(), event.getY()));

        return true;
    }

    //fa lo spawn in base alla configurazione della griglia
    public void deSpawnRespawn(){

        ArrayList<GameObject> objects = mLevel.getGameObject();
        String[][] gridConfiguration = mGrid.getGridConfiguration();


        for (GameObject o: objects) {
            o.setInactive();
        }


        for(int i = 0; i<Level.mNumShipsInLevel; i++) {
            int row = 0;
            int column = 0;
            boolean found = false;

            String gridTag = objects.get(i)
                    .getGridTag();



            for (int j = 0; j < gridConfiguration.length && !found; j++) {
                for (int k = 0; k < gridConfiguration.length && !found; k++) {
                    if(gridTag == "S")
                        Log.d("Griglia da leggere", "deSpawnRespawn: gridConfiguration["+j+"]["+k+"] "+gridConfiguration[j][k]);

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

            objects.get(i)
                    .spawn(row, column);




        }


    }

    @Override
    public boolean spawnAmmo(int row, int column) {

        ArrayList<GameObject> objects = mLevel.getGameObject();

        Log.d("SpawnMissile", "spawnAmmo: in BattleField");
        objects.get(Level.MISSILE)
                .spawn(row, column);
        Log.d("SpawnMissile", "spawnAmmo: after spawn");



        return true;
    }

    public String[][] saveDefaultFleet() {
        Log.d("Saving", "saveDefaultFleet: ");
        ArrayList<GameObject> objects = mLevel.getGameObject();
        mGrid.clearGrid();

        for (int i = 0; i < Level.mNumShipsInLevel; i++) {
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

            if(shipWidth >= shipHeight){
                shipIsVertical = false;
                blockOccupied = (int) (shipWidth / mGrid.getBlockDimension());
            }else{
                shipIsVertical = true;
                blockOccupied = (int) (shipHeight / mGrid.getBlockDimension());
            }

            String gridTag = objects.get(i)
                    .getGridTag();

            mGrid.positionShip(startRow, startColumn, blockOccupied, shipIsVertical, gridTag );
        }

        return mGrid.getGridConfiguration();

    }



    /** Utility methods **/
    public String getLevelName(){
        return mLevel.mName;
    }
}
