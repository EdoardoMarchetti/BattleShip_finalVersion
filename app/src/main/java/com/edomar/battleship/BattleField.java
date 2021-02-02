package com.edomar.battleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
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
import com.edomar.battleship.utils.BitmapStore;
import com.edomar.battleship.view.Grid;
import com.edomar.battleship.view.Renderer;

import java.util.ArrayList;

public class BattleField extends SurfaceView implements Runnable,SurfaceHolder.Callback {

    private static final String TAG = "BattleField";

    private Thread mThread;

    private Renderer mRenderer;
    private Grid mGrid;
    private boolean mRunning;
    private Level mLevel;
    private BitmapStore mBitmapStore;

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

            mRenderer.draw(mGrid, objects);
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



        return super.onTouchEvent(event);
    }

    public void deSpawnRespawn(){
        ArrayList<GameObject> objects = mLevel.getGameObject();

        for (GameObject o: objects) {
            o.setInactive();
        }

        objects.get(Level.BATTLESHIP_INDEX) // Ricavo l'oggetto da far apparire
                .spawn(objects.get(Level.BATTLESHIP_INDEX).getTransform()); //faccio apparire l'oggetto ricavando prima il suo transform


    }
}
