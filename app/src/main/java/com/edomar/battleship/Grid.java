package com.edomar.battleship;

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

    private final float mGridDimension;
    private final float mBlockDimension;
    private final int strokeWidth;
    private final String[][] mGridConfiguration;

    private static int textDimension;

    private Point mLastHit= new Point();


    public Grid(float gridDimension) {
        mGridDimension = gridDimension;
        mBlockDimension = gridDimension / 10;
        strokeWidth = (int) gridDimension / 175;

        mGridConfiguration = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mGridConfiguration[i][j] ="0";
            }
        }


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

        //Draw hit cell
        for(int i = 0; i<10; i++){
            for(int j = 0; j<10; j++){
                if(mGridConfiguration[j][i] == "X"){
                    canvas.drawLine(mBlockDimension*i, mBlockDimension*j, mBlockDimension*(i+1), mBlockDimension*(j+1), paint);
                    canvas.drawLine(mBlockDimension*i, mBlockDimension*(j+1), mBlockDimension*(i+1), mBlockDimension*j, paint);
                }else if(mGridConfiguration[j][i] == "O"){ // questa parte poi dovraà essere portata fuori da drawGrid poichè i pallini devono andare sopra e le navi
                    canvas.drawCircle((float) (mBlockDimension*(i+ 0.5)), (float) (mBlockDimension*(j+ 0.5)), mBlockDimension/2, paint);
                }
            }
        }
    }

    public static void drawCoordinates(Bitmap letters, Bitmap numbers, Point size){

        final Rect textBounds = new Rect();

        //First draw letters
        Canvas canvas = new Canvas(letters);
        Paint paint = new Paint();
        Point unit = new Point();

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

    /** Getters **/
    public String[][] getGridConfiguration() {
        return mGridConfiguration;
    }

    public float getGridDimension(){
        return mGridDimension;
    }

    public float getBlockDimension() {
        return mBlockDimension;
    }

    public Point getLastHit(){ return  mLastHit;}

    /** Setters **/
    public void clearGrid(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mGridConfiguration[i][j] ="0";
            }
        }
    }

    public void setHit(int row, int column, boolean shipHit){
        if(shipHit){
            mGridConfiguration[row][column] = "O";
            Log.d("setHit", "setHit: nave in = "+row+","+column);
        }else{
            mGridConfiguration[row][column] = "X";
            Log.d("setHit", "setHit: acqua in = "+row+","+column);
        }
        mLastHit.x = row;
        mLastHit.y = column;
    }

    public void positionShip(int startRow, int startColumn, int blockOccupied, boolean isVertical, String gridTag ){
        if(isVertical){
            for (int i = startRow; i < (startRow+blockOccupied) ; i++) {
                mGridConfiguration[i][startColumn] = gridTag;
            }
        }else{
            for (int j = startColumn; j < (startColumn+blockOccupied) ; j++) {
                mGridConfiguration[startRow][j] = gridTag;
            }
        }
    }
}
