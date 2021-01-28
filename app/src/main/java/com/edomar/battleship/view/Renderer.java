package com.edomar.battleship.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.edomar.battleship.R;
import com.edomar.battleship.logic.GameState;

import androidx.annotation.LongDef;

public class Renderer  {

    private static final String TAG = "Renderer";

    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    public Renderer(SurfaceView sh) {
        this.mSurfaceHolder = sh.getHolder();
        this.mPaint = new Paint();
    }

    public void draw(Grid grid) {
        Log.d(TAG, "draw: start draw method in renderer");
        Log.d(TAG, "draw: mSurfaceHolder.getSurface().isValid() = "+ mSurfaceHolder.getSurface().isValid());
        if (mSurfaceHolder.getSurface().isValid()) {
            Log.d(TAG, "draw: in if block ");
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);


            grid.drawGrid(mCanvas, mPaint);

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
