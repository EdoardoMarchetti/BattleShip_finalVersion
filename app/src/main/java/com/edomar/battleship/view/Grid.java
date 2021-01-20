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

    public static void drawGrid (Canvas canvas, Paint paint, Point gridDimension){
        Log.d("Draw", "drawGrid: ");
        Point unit = new Point();
        unit.x = gridDimension.x / 10;
        unit.y = gridDimension.y / 10;
        Log.d("Draw", "drawGrid: unit.x = "+unit.x);
        Log.d("Draw", "drawGrid: unit.y = "+unit.y);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        //Horizontal lines
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(0, unit.y * i, gridDimension.x , unit.y * i, paint);
        }
        //Vertical lines
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(unit.x * i, 0, unit.x * i, gridDimension.y, paint);
        }
    }

    public static void drawCoordinates(Bitmap letters, Bitmap numbers, Point size){

        final Rect textBounds = new Rect();

        //First draw letters
        Canvas canvas = new Canvas(letters);
        Paint paint = new Paint();
        Point unit = new Point();
        /*unit.x = size.x/10 ;
        unit.y = size.y ;*/
        Log.d("Handler", "run: imageViewSize.x = "+ size.x + " imageViewSize.y= "+ size.y);
        unit.x = size.x/10;
        unit.y = size.y;
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);

        Log.d("Coo", "drawCoordinates:unit.x = "+unit.x );
        Log.d("Coo", "drawCoordinates:unit.y = "+unit.y );
        //draw letters
        String[] lettersSymbols = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
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
