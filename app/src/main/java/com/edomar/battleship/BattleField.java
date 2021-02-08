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
import com.edomar.battleship.logic.components.ShipInputComponent;
import com.edomar.battleship.utils.BitmapStore;

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
        Log.d("BOHC", "surfaceCreated: ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("BOHC", "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("BOHC", "surfaceDestroyed: ");
    }


    /** Init Method**/
    public void init(){
        Log.d(TAG, "init: in init method");
        mBitmapStore = BitmapStore.getInstance(getContext());
        mRenderer = new Renderer(this);
        mGrid = new Grid(this.getLayoutParams().width);
        mLevel = new Level(getContext(), this.getLayoutParams().width, this);
        mGridController = new GridInputController(this);

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

        while (mRunning){
            long frameStartTime = System.currentTimeMillis();
            ArrayList<GameObject> objects = mLevel.getGameObject();

            /** Update the objects **/
            int count=0;
            for (GameObject o: objects) {
                //Log.d("ShipInputComponent", "run: ship number= "+count);
                if(o.getTransform().checkMovable()){
                    o.update(mFPS, mGrid);
                }
                count++;
            }

            /** Draw objects **/
            mRenderer.draw(mGrid, objects);

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

        return true;
    }

    public void deSpawnRespawn(){
       ArrayList<GameObject> objects = mLevel.getGameObject();

        for (GameObject o: objects) {
            o.setInactive();
        }


        for(int i = 0; i<Level.mNumShipsInLevel; i++){
            objects.get(i)
                    .spawn(objects.get(i).getTransform(), i);
        }


    }

    @Override
    public boolean spawnAmmo(int row, int column) {
        return false;
    }
}
