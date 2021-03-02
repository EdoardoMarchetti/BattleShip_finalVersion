package com.edomar.battleship.logic.grid;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;



import java.util.List;
import java.util.Random;

public class Grid  {
    private static final String TAG = "Grid";

    private final float mGridDimension;
    private final float mBlockDimension;
    private final int strokeWidth;
    private final String[][] mGridConfiguration;

    private static int textDimension;

    private Point mLastHit= new Point();
    private boolean mLastHitResult = false;


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

    }

    public void drawGrid (Canvas canvas, Paint paint){
        //Log.d(TAG, "drawGrid: draw method of grid ");
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


        //draw letters
        String[] lettersSymbols = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textDimension);
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
            Log.d("letters", "drawCoordinates: "+ numbersSymbol[i]);
            paint.getTextBounds(numbersSymbol[i], 0, numbersSymbol[i].length(), textBounds);
            canvas.drawText(numbersSymbol[i], unit.x / 2 , (float) (unit.y * (i+ 0.5))- textBounds.exactCenterY(),paint);
        }


    }

    public void drawHitCells(Canvas canvas, Paint paint){
        //Draw hit cell
        Paint previousStyle = paint;
        for(int i = 0; i<10; i++){
            for(int j = 0; j<10; j++){
                if(mGridConfiguration[j][i] == "X"){
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.BLACK);
                    canvas.drawLine(mBlockDimension*i, mBlockDimension*j, mBlockDimension*(i+1), mBlockDimension*(j+1), paint);
                    canvas.drawLine(mBlockDimension*i, mBlockDimension*(j+1), mBlockDimension*(i+1), mBlockDimension*j, paint);
                }else if(mGridConfiguration[j][i] == "S"){ // questa parte poi dovraà essere portata fuori da drawGrid poichè i pallini devono andare sopra e le navi
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.RED);
                    canvas.drawCircle((float) (mBlockDimension*(i+ 0.5)), (float) (mBlockDimension*(j+ 0.5)), mBlockDimension/2, paint);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle((float) (mBlockDimension*(i+ 0.5)), (float) (mBlockDimension*(j+ 0.5)), mBlockDimension/4, paint);

                }
            }
        }
        paint = previousStyle;
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

    public Point getLastHitCoordinates(){ return  mLastHit;}

    public boolean getLastHitResult(){ return mLastHitResult;}

    /** Setters **/
    public void clearGrid(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mGridConfiguration[i][j] ="0";
            }
        }
    }

    public void setDispositionInGrid(List<String[]> gridRows){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(gridRows.size() == 0){ //Primo avvio -> non esiste flotta default -> poszioni iniziali
                    mGridConfiguration[i][j] ="0";
                }else{//esiste il file -> esiste una configurazione preferita
                    /*mGridConfiguration[i][j] = (gridRows.get(i))[j];
                    Log.d("Lettura", "Grid:  mGridConfiguration[i][j] "+mGridConfiguration[i][j]);*/
                    mGridConfiguration[i][j] = (gridRows.get(i))[j];
                }

                Log.d("Lettura", "Grid:  mGridConfiguration[i][j] "+mGridConfiguration[i][j]);

            }
        }
    }

    public void setHit(int row, int column, boolean shipHit){
        if(shipHit){
            mGridConfiguration[row][column] = "S";
            Log.d("setHit", "setHit: nave in = "+row+","+column);
        }else{
            mGridConfiguration[row][column] = "X";
            Log.d("setHit", "setHit: acqua in = "+row+","+column);
        }
    }

    public void positionShip(int startRow, int startColumn, int blockOccupied, boolean isVertical, String gridTag){
        if(isVertical){
            Log.d("ShipPosition", "positionShip: verticale");
            for (int i = startRow; i < (startRow+blockOccupied) ; i++) {
                Log.d("ShipPosition", "positionShip: i = "+i+" startColumn= "+startColumn);
                mGridConfiguration[i][startColumn] = gridTag;
            }
        }else{
            Log.d("ShipPosition", "positionShip: orizzontale");
            for (int j = startColumn; j < (startColumn+blockOccupied) ; j++) {
                Log.d("ShipPosition", "positionShip: startRow = "+startRow+" j= "+j);
                mGridConfiguration[startRow][j] = gridTag;
            }
        }
    }

    public void setLastHit(int row, int column, boolean hit) {
        mLastHit.x = row;
        mLastHit.y = column;
        mLastHitResult= hit;
    }

    public void setHitForDistance(RectF shipCollider) {
        int left = (int) (shipCollider.left / mBlockDimension);
        int top = (int) (shipCollider.top / mBlockDimension);
        int right = (int) (shipCollider.right / mBlockDimension);
        int bottom = (int) (shipCollider.bottom / mBlockDimension);



        for(int i = top-1; i<=bottom; i++){
            if(i>=0 && i<10) {
                if(left-1 >= 0){
                    setHit(i, left - 1, false);
                }
                if(right<10){
                    setHit(i, right, false);
                }
            }
        }
        for(int j = left-1; j<=right; j++){
            if(j>=0  && j<10){
                if(top-1 >= 0){
                    setHit(top-1, j, false);
                }
                if(bottom<10){
                    setHit(bottom, j, false);
                }
            }
        }
    }



}
