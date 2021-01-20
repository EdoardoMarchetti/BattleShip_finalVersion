package com.edomar.battleship.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.edomar.battleship.R;

public class Renderer implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = Renderer.class.getSimpleName();

    private Activity mActivity;

    /** To manage SurfaceView**/
    private SurfaceView mGridSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private int mGridSurfaceViewWidth;
    private int mGridSurfaceViewHeight;

    /** To manage Coordinates' ImageViews**/
    private ImageView letters;
    private ImageView numbers;
    private Point lettersImageViewSize = new Point();

    /** To draw **/
    private Paint mPaint;
    private Canvas mCanvas;
    private Point gridSize= new Point();
    private Point blockSize= new Point();

    /** To manage the thread**/
    private boolean mRunning;
    private Thread mRendererThread;



    public Renderer(Activity activity) {
        mActivity = activity;
        init();
    }

    private void init(){
        /**Surface view init**/ //Possibili problemi su riutilizzo della classe per R.id.grid
        mGridSurfaceView = (SurfaceView) mActivity.findViewById(R.id.grid);
        mSurfaceHolder = mGridSurfaceView.getHolder();
        mGridSurfaceView.setZOrderOnTop(true);
        mGridSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mGridSurfaceView.getHolder().addCallback(this);



        /**Coordinates ImageView init**/
        letters = (ImageView) mActivity.findViewById(R.id.letters);
        numbers = (ImageView) mActivity.findViewById(R.id.numbers);





        /** Paint initialization**/
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
        Log.d(String.valueOf(R.string.debugging), "init:  paint configurato");

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated: ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    public void pause() {
        mRunning = false;
        Log.d("Thread", "pause: ");
        try {
            // Stop the thread (rejoin the main thread)
            mRendererThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        Log.d("Thread", "start: ");
        mRunning = true;
        mRendererThread = new Thread(this);
        mRendererThread.start();
    }



    @Override
    public void run() {

        /** Coordinates are drew only once and never updated **/
        lettersImageViewSize.y = letters.getLayoutParams().height;
        lettersImageViewSize.x = letters.getLayoutParams().width;
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("Handler", "run handler: ");
                Log.d(String.valueOf(R.string.debugging), "run: imageViewSize.x = " + lettersImageViewSize.x + " imageViewSize.y= " + lettersImageViewSize.y);

                Bitmap lettersBitmap = Bitmap.createBitmap(lettersImageViewSize.x, lettersImageViewSize.y, Bitmap.Config.ARGB_8888);
                Bitmap numbersBitmap = Bitmap.createBitmap(lettersImageViewSize.y, lettersImageViewSize.x, Bitmap.Config.ARGB_8888);
                Grid.drawCoordinates(lettersBitmap, numbersBitmap, lettersImageViewSize);
                letters.setImageBitmap(lettersBitmap);
                numbers.setImageBitmap(numbersBitmap);
            }
        });

        gridSize.x = mGridSurfaceView.getLayoutParams().width;
        gridSize.y = mGridSurfaceView.getLayoutParams().height;
        blockSize.x = gridSize.x/10;
        blockSize.y = gridSize.y/10;
        while (mRunning) {

            if (mSurfaceHolder.getSurface().isValid() && (gridSize.x != 0 && gridSize.y!=0) ){
                mCanvas = mGridSurfaceView.getHolder().lockCanvas();

                /** draw grid**/
                Grid.drawGrid(mCanvas, mPaint, gridSize);

                /** draw object**/
                Bitmap prova = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ship_battleship);
                prova = Bitmap.createScaledBitmap(prova, blockSize.x, blockSize.y * 4, false);
                mCanvas.drawBitmap(prova, blockSize.x, blockSize.y, mPaint);



                mGridSurfaceView.getHolder().unlockCanvasAndPost(mCanvas);
            }

        }

    }

    public void drawGridCoordinates(Point size, ImageView letters, ImageView numbers){
        //DRAW THE COORDINATES
        Bitmap lettersBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
        Bitmap numbersBitmap = Bitmap.createBitmap(size.y, size.x, Bitmap.Config.ARGB_8888);
        Grid.drawCoordinates(lettersBitmap, numbersBitmap, size);
        letters.setImageBitmap(lettersBitmap);
        numbers.setImageBitmap(numbersBitmap);
    }
}
