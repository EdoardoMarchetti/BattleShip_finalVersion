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
import com.edomar.battleship.view.gameplayFragments.MatchFragment;

import java.util.ArrayList;

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
        mRenderer = new Renderer(this);
        mPhysicsEngine = new PhysicsEngine();
        mGrid = new Grid(this.getLayoutParams().width);
        mLevel = new Level(getContext(), this.getLayoutParams().width, this);
        mGridController = new GridInputController(this);
        mParticleSystem = new ParticleSystem();
        mParticleSystem.init(250);
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
        Log.d("InitMethodInRun", "init: in init method for fragment number = "+ mN);
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

        while (mRunning){
            long frameStartTime = System.currentTimeMillis();
            ArrayList<GameObject> objects = mLevel.getGameObject();

            /** Update the objects **/

            mPhysicsEngine.update(mFPS, mParticleSystem, objects, mGrid);

            /*for (GameObject o: objects) {
                if(o.checkActive()){
                    o.update(mFPS, mGrid);
                }
            }*/


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

    public void deSpawnRespawn(){
       ArrayList<GameObject> objects = mLevel.getGameObject();

        for (GameObject o: objects) {
            o.setInactive();
        }


        /*for(int i = 0; i<Level.mNumShipsInLevel; i++){
            objects.get(i)
                    .spawn(objects.get(i).getTransform(), i);
        }*/

        for(int column = 0; column<Level.mNumShipsInLevel; column++) {
            objects.get(column)
                    .spawn(0, column);
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


    public void setNumber (int num){
        mN = num;
    }

    public void setName (String name){
        nome = name;
    }
}
