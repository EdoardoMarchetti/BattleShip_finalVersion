package com.edomar.battleship.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.edomar.battleship.R;

public class Grid  {
    private static final String TAG = "Grid";

    private int mGridDimension;
    private int mBlockDimension;
    private int strokeWidth;
    private static int textDimension;

    public Grid(int gridDimension) {
        mGridDimension = gridDimension;
        mBlockDimension = gridDimension / 10;
        strokeWidth = gridDimension / 175;
        textDimension = strokeWidth * 10;
        Log.d(TAG, "Grid: gridDimension = "+mGridDimension);
        Log.d(TAG, "Grid: mBlockDimension = "+mBlockDimension);
    }

    public void drawGrid (Canvas canvas, Paint paint){
        Log.d(TAG, "drawGrid: draw method of grid ");
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(strokeWidth);
        //Horizontal lines
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(0, mBlockDimension * i, mGridDimension , mBlockDimension * i, paint);
        }
        //Vertical lines
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(mBlockDimension * i, 0, mBlockDimension * i, mGridDimension, paint);
        }
    }

    public static void drawCoordinates(Bitmap letters, Bitmap numbers, Point size){

        final Rect textBounds = new Rect();

        //First draw letters
        Canvas canvas = new Canvas(letters);
        Paint paint = new Paint();
        Point unit = new Point();
        Log.d("Handler", "run: imageViewSize.x = "+ size.x + " imageViewSize.y= "+ size.y);
        unit.x = size.x/10;
        unit.y = size.y;
        paint.setColor(Color.BLACK);


        Log.d("Coo", "drawCoordinates:unit.x = "+unit.x );
        Log.d("Coo", "drawCoordinates:unit.y = "+unit.y );
        //draw letters
        String[] lettersSymbols = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textDimension);
        for (int i = 0; i < 10; i++) {
            Log.d("letters", "drawCoordinates: "+ lettersSymbols[i]);
            paint.getTextBounds(lettersSymbols[i], 0, lettersSymbols[i].length(), textBounds);
            canvas.drawText(lettersSymbols[i], (float) (unit.x * (i+ 0.5)), unit.y / 2 - textBounds.exactCenterY(), paint);
        }
        Log.d("Draw", "drawCoordinates: end letters ");
        //Now draw numbers
        canvas.setBitmap(numbers);


        String[] numbersSymbol = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        for (int i = 0; i < 10; i++) {
            Log.d("letters", "drawCoordinates: "+ numbersSymbol[i]);
            paint.getTextBounds(numbersSymbol[i], 0, numbersSymbol[i].length(), textBounds);
            canvas.drawText(numbersSymbol[i], unit.x / 2 , (float) (unit.y * (i+ 0.5))- textBounds.exactCenterY(),paint);
        }


    }
}
