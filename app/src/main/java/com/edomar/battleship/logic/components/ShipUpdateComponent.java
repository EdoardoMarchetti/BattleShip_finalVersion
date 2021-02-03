package com.edomar.battleship.logic.components;

import android.graphics.PointF;
import android.util.Log;

import com.edomar.battleship.Grid;
import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.components.interfaces.UpdateComponent;

public class ShipUpdateComponent implements UpdateComponent {

    private static final String TAG = "ShipMovementComponent";

    @Override
    public boolean update(long fps, Transform t, Grid grid) {

        //Qui si va a posizionare la nave sulla griglia
        return true;
    }
}
