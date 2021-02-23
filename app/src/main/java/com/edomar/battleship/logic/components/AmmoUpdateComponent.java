package com.edomar.battleship.logic.components;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.transforms.AmmoTransform;
import com.edomar.battleship.logic.transforms.Transform;
import com.edomar.battleship.logic.components.interfaces.UpdateComponent;

public class AmmoUpdateComponent implements UpdateComponent {
    private static final String TAG = "AmmoUpdateComponent";

    @Override
    public boolean update(long fps, Transform t, Grid grid) {
        AmmoTransform at = (AmmoTransform) t;
        Log.d(TAG, "update: ");
        // Laser can only travel two screen widths
        float range = t.getGridDimension();
        //Which is the target?
        Point target = grid.getLastHit();
        //Where is the Ammo?
        PointF location = at.getLocation();
        //How fast is going?
        float speed = at.getSpeed();

        location.x += speed / fps;

        if(location.x > range){
            //disable the ammo
            return false;
        }

        at.updateCollider();

        return true;
    }

}
