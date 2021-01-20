package com.edomar.battleship.view;

import android.content.Context;
import android.graphics.Point;
import android.view.SurfaceView;

public class GameEngine extends SurfaceView implements Runnable {

    private Thread mThread = null;
    private long mFPS;

    public GameEngine(Context context, Point size) {
        super(context);
    }

    @Override
    public void run() {
        long frameStartTime = System.currentTimeMillis();

        //Measure the frames per second
        long timeThisFrame = System.currentTimeMillis() - frameStartTime;
        if(timeThisFrame >= 1){
            final int MILLIS_IN_SECOND = 1000;
            mFPS = MILLIS_IN_SECOND / timeThisFrame;
        }
    }
}
