package com.edomar.battleship.logic.components;

import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.transforms.Transform;
import com.edomar.battleship.logic.components.interfaces.UpdateComponent;

public class ShipUpdateComponent implements UpdateComponent {

    private static final String TAG = "ShipMovementComponent";

    @Override
    public boolean update(long fps, Transform t, Grid grid) {
        //Qui si va a posizionare la nave sulla griglia
        //TODO
        return true;
    }
}
