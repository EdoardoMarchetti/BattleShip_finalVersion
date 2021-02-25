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
    public long mFPS;


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
            Log.d("ObjectActivation ", "deSpawnRespawn: object inactive = "+o.getGridTag());
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



    public void setLevel(String levelToLoad) {
        inputObservers.clear();
        inputObservers.add(mGridController);

        mLevelManager = new LevelManager(getContext(), this.getLayoutParams().width, this, levelToLoad);
        List<String[]> gridRows = WriterReader.getInstance().read(levelToLoad);
        mGrid = new Grid(this.getLayoutParams().width, gridRows);
        deSpawnRespawn(); //indica ad ogni nave dove posizionarsi
        Log.d(TAG, "setLevel: inputObservers "+inputObservers.size());
    }


    /**METODI ASTRATTI**/

    @Override
    public abstract void addObserver(InputObserver o);//Usato in FLEET_FRAGMENT, MATCH_FRAGMEN e PRE_MATCHFRAGMENT

    @Override
    public abstract void run();//Usato in tutti ma forse in modo diverso in ciascuno; da valutare se lascaire una versione di defualt e eventualmente modificarlo nella classe acui serve

    public abstract boolean saveDefaultFleet(String levelToLoad);//Usato in FLEET_FRAGMENT

    @Override
    public abstract boolean onTouchEvent(MotionEvent event);//Usato in FLEET_FRAGMENT, MATCH_FRAGMENT e PRE_MATCHFRAGMENT


}
