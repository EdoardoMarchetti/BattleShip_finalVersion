package com.edomar.battleship.logic.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.utils.BitmapStore;
import com.edomar.battleship.view.Grid;

import androidx.constraintlayout.widget.ConstraintSet;

public class ShipGraphicsComponent implements InterfaceGraphicsComponent {

    private String mBitmapName;

    @Override
    public void initialize(Context c, ObjectSpec spec, PointF objectSize) {
        mBitmapName = spec.getBitmapName();

        BitmapStore.addBitmap(c, mBitmapName, objectSize,true);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Transform t, Grid grid) {
        Bitmap bitmap;

        if(t.isVertical()){
            bitmap = BitmapStore.getVerticalBitmap(mBitmapName);
        }else{
            bitmap = BitmapStore.getHorizontalBitmap(mBitmapName);
        }

        canvas.drawBitmap(bitmap,
                t.getLocation().x,
                t.getLocation().y,
                paint);
    }
}
