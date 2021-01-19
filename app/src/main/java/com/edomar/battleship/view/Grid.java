package com.edomar.battleship.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.edomar.battleship.R;

public class Grid {

    public Point gridDimension;

    /**--------------------------------------
            METHODS TO DRAW THE GRID
     -------------------------------------**/
    public static void drawGrid (Bitmap bitmap, Point gridDimension){
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,0, gridDimension.x, gridDimension.y, paint);
        Point unit = new Point();
        unit.x = gridDimension.x / 10;
        unit.y = gridDimension.y / 10;
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        //Horizontal lines
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(0, unit.y * i, gridDimension.x , unit.y * i, paint);
        }
        //Vertical lines
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(unit.x * i, 0, unit.x * i, gridDimension.y, paint);
        }
    }

    public static void drawCoordinates(Bitmap letters, Bitmap numbers, Point gridDimension){

        final Rect textBounds = new Rect();

        //First draw letters
        Canvas canvas = new Canvas(letters);
        Paint paint = new Paint();
        Point unit = new Point();
        unit.x = gridDimension.x / 10;
        unit.y = gridDimension.y / 10;
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        //draw letters
        String[] lettersSymbols = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        for (int i = 0; i < 10; i++) {
            Log.d("letters", "drawCoordinates: "+ lettersSymbols[i]);
            paint.getTextBounds(lettersSymbols[i], 0, lettersSymbols[i].length(), textBounds);
            canvas.drawText(lettersSymbols[i], (float) (unit.x * (i+ 0.5)), unit.y / 2 - textBounds.exactCenterY(), paint);
        }

        //Now draw numbers
        canvas.setBitmap(numbers);

        String[] numbersSymbol = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        for (int i = 0; i < 10; i++) {
            Log.d("letters", "drawCoordinates: "+ lettersSymbols[i]);
            paint.getTextBounds(numbersSymbol[i], 0, lettersSymbols[i].length(), textBounds);
            canvas.drawText(numbersSymbol[i], unit.x / 2 , (float) (unit.y * (i+ 0.5))- textBounds.exactCenterY(),paint);
        }

    }

}
