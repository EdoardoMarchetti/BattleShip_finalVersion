package com.edomar.battleship.logic.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.edomar.battleship.logic.transform.ShipTransform;
import com.edomar.battleship.logic.transform.Transform;
import com.edomar.battleship.logic.components.interfaces.GraphicsComponent;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.utils.BitmapStore;

public class ShipGraphicsComponent implements GraphicsComponent {

    private String mBitmapName;

    @Override
    public void initialize(Context c, ObjectSpec spec, PointF objectSize) {
        mBitmapName = spec.getBitmapName();

        BitmapStore.addBitmap(c, mBitmapName, objectSize,true);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Transform t) {

        Paint paintState = paint;
        ShipTransform sp = (ShipTransform) t;

        //Draw the collider if is movable
        if(sp.checkMovable()){
            RectF shipCollider = t.getCollider();
            paint.setColor(Color.GREEN);
            canvas.drawRect(shipCollider, paint);
        }

        paint = paintState;

        //Draw the ship
        Bitmap bitmap;

        if(sp.isVertical()){
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
