package com.edomar.battleship.logic.components;

import com.edomar.battleship.Grid;
import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.components.interfaces.UpdateComponent;

public class AmmoUpdateComponent implements UpdateComponent {

    @Override
    public boolean update(long fps, Transform t, Grid grid) {
        return false;
    }

}
