package com.edomar.battleship.logic.gameObject;

import android.util.Log;

import com.edomar.battleship.logic.grid.Grid;

public class ShipObject extends GameObject {



    @Override
    public void update(long fps, Grid grid) {
        if (!(updateComponent.update(fps,
                mTransform, grid))) {
            // Component returned false
            isActive = false;
        }else{
            isDrawable = true;
            isActive = true;
            Log.d("Ultimotentativo", "update: da ora puoi disegnare");
        }

    }
}
