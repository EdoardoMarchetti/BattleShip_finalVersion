package com.edomar.battleship.logic.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.edomar.battleship.logic.transform.Transform;
import com.edomar.battleship.logic.components.interfaces.GraphicsComponent;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.utils.BitmapStore;

public class AmmoGraphicsComponent implements GraphicsComponent {

    private String mBitmapName;

    @Override
    public void initialize(Context c, ObjectSpec spec, PointF objectSize) {
        mBitmapName = spec.getBitmapName();

        BitmapStore.addBitmap(c, mBitmapName, objectSize,true);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Transform t) {

        //Draw the ship
        Bitmap bitmap;

        bitmap = BitmapStore.getHorizontalBitmap(mBitmapName);

        canvas.drawBitmap(bitmap,
                t.getLocation().x,
                t.getLocation().y,
                paint);


    }
}
