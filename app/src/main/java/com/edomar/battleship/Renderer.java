package com.edomar.battleship;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.edomar.battleship.logic.GameObject;
import com.edomar.battleship.logic.ParticleSystem;

import java.util.ArrayList;

public class Renderer  {

    private static final String TAG = "Renderer";


    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    public Renderer(SurfaceView sh) {
        this.mSurfaceHolder = sh.getHolder();
        this.mPaint = new Paint();
    }

    public void draw(Grid grid, ArrayList<GameObject> objects, ParticleSystem ps) {
        Log.d(TAG, "draw: start draw method in renderer");
        Log.d(TAG, "draw: mSurfaceHolder.getSurface().isValid() = "+ mSurfaceHolder.getSurface().isValid());
        if (mSurfaceHolder.getSurface().isValid()) {
            Log.d(TAG, "draw: in if block ");
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);

            /**Grid**/
            grid.drawGrid(mCanvas, mPaint);

            /** Objects **/
            for (GameObject object: objects) {
                if(object.checkActive()) {
                    object.draw(mCanvas, mPaint);
                }
            }

            /**Particles**/
            if(ps.mIsRunning){
                ps.draw(mCanvas, mPaint);
            }

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void drawGridCoordinates(ImageView letters, ImageView numbers, Point size){
        //DRAW THE COORDINATES
        Bitmap lettersBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
        Bitmap numbersBitmap = Bitmap.createBitmap(size.y, size.x, Bitmap.Config.ARGB_8888);
        Grid.drawCoordinates(lettersBitmap, numbersBitmap, size);
        letters.setImageBitmap(lettersBitmap);
        numbers.setImageBitmap(numbersBitmap);
    }
}
