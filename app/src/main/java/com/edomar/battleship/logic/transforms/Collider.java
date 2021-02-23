package com.edomar.battleship.logic.transforms;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.constraintlayout.solver.widgets.Rectangle;

public class Collider extends RectF {

    int mColor = Color.GREEN;
    boolean mVisible = true;

    public void setColorRed(){
        mColor = Color.RED;
    }

    public void setColorGreen(){
        mColor = Color.GREEN;
    }

    public void draw (Canvas canvas, Paint paint){
        paint.setColor(mColor);
        canvas.drawRect(this, paint);
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible() {
        mVisible = true;
    }

    public void setInvisible(){
        mVisible = false;
    }

    public String getColor(){
        if(mColor == Color.GREEN){
            return "green";
        }else {
            return "red";
        }
    }
}
