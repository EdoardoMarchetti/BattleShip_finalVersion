package com.edomar.battleship.logic.components;

import android.graphics.PointF;
import android.util.Log;

import com.edomar.battleship.Grid;
import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.components.interfaces.MovementComponent;

public class ShipMovementComponent implements MovementComponent {

    private static final String TAG = "ShipMovementComponent";

    @Override
    public boolean move(long fps, Transform t, Grid grid) {

        //Check if is rotatable and then rotate
        if (t.isRotatable()) {
            if (t.getObjectHeight() + t.getLocation().x > grid.getGridDimension()) {
                float difference = (t.getObjectHeight() + t.getLocation().x) - grid.getGridDimension();
                int differenceInBlocks = (int) (difference / grid.getBlockDimension());
                Log.d(TAG, "handleInput: differenceInBlocks = " + differenceInBlocks);
                PointF oldLocation = t.getLocation();
                t.setLocation(oldLocation.x - (grid.getBlockDimension() * differenceInBlocks)
                        , oldLocation.y);

            } else if (t.getObjectHeight() + t.getLocation().y > grid.getGridDimension()) {
                float difference = (t.getObjectHeight() + t.getLocation().y) - grid.getGridDimension();
                int differenceInBlocks = (int) (difference / grid.getBlockDimension());
                Log.d(TAG, "handleInput: differenceInBlocks = " + differenceInBlocks);
                PointF oldLocation = t.getLocation();
                t.setLocation(oldLocation.x
                        , oldLocation.y - (grid.getBlockDimension() * differenceInBlocks));
            }

            t.updateCollider();
            t.notRotatable();

            //Setta la nuova posizione sulla griglia

        }
        return true;
    }
}
