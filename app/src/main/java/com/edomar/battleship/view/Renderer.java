package com.edomar.battleship.view;

import android.app.Activity;
import android.graphics.Bitmap;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.edomar.battleship.R;
import com.edomar.battleship.view.menuFragments.FleetFragment;

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
    private Point imageViewSize = new Point();

    /** To draw **/
    private Paint mPaint;
    private Point size = new Point();
    private Canvas mCanvas;

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
        Log.d(String.valueOf(R.string.debugging), "onPreDraw: prima di vto  ");
        ViewTreeObserver vto = letters.getViewTreeObserver(); //This is necessary to get the dimension of letters (and then of numbers)
        Log.d(String.valueOf(R.string.debugging), "onPreDraw: vto is null? "+ vto.equals(null));
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { //Problema nel disegnare un'altra griglia -> Non entra in PreDraw
            public boolean onPreDraw() {
                Log.d(String.valueOf(R.string.debugging), "onPreDraw: sto misurando");
                letters.getViewTreeObserver().removeOnPreDrawListener(this);

                imageViewSize.y = letters.getMeasuredHeight();
                imageViewSize.x = letters.getMeasuredWidth();

                return true;
            }
        });
        Log.d(String.valueOf(R.string.debugging), "onPreDraw:  misure completate");

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
        mGridSurfaceViewWidth = width;
        mGridSurfaceViewHeight = height;
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


        /** Coordinates are drew only once **/
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                boolean fatto = false;
                int count = 0;
                while(!fatto) {
                    count++;
                    Log.d("Handler", "run handler: ");
                    Log.d(String.valueOf(R.string.debugging), "run: imageViewSize.x = " + imageViewSize.x + " imageViewSize.y= " + imageViewSize.y+"\ncicli= "+count);

                    if(imageViewSize.x != 0 && imageViewSize.y != 0) {
                        Bitmap lettersBitmap = Bitmap.createBitmap(imageViewSize.x, imageViewSize.y, Bitmap.Config.ARGB_8888);
                        Bitmap numbersBitmap = Bitmap.createBitmap(imageViewSize.y, imageViewSize.x, Bitmap.Config.ARGB_8888);
                        Grid.drawCoordinates(lettersBitmap, numbersBitmap, imageViewSize);
                        letters.setImageBitmap(lettersBitmap);
                        numbers.setImageBitmap(numbersBitmap);
                        fatto = true;
                    }
                }
            }
        });

        while (mRunning) {
            size.x = mGridSurfaceViewWidth;
            size.y = mGridSurfaceViewHeight;

            if (mSurfaceHolder.getSurface().isValid() && (size.x != 0 && size.y!=0) ){
                mCanvas = mGridSurfaceView.getHolder().lockCanvas();

                /** draw grid**/
                Grid.drawGrid(mCanvas, mPaint, size);
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
