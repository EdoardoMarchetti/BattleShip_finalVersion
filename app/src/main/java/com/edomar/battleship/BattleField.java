package com.edomar.battleship;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.edomar.battleship.view.Grid;
import com.edomar.battleship.view.Renderer;

public class BattleField extends SurfaceView implements Runnable,SurfaceHolder.Callback {

    private static final String TAG = "BattleField";

    private Thread mThread;

    private Renderer mRenderer;
    private Grid mGrid;
    private boolean mRunning;


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
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }


    /** Init Method**/
    public void init(){
        Log.d(TAG, "init: in init method");
        mRenderer = new Renderer(this);
        mGrid = new Grid(this.getLayoutParams().width);
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
        Log.d(TAG, "run: in battlefield");
        while (mRunning){
            mRenderer.draw(mGrid);
        }

    }
}
