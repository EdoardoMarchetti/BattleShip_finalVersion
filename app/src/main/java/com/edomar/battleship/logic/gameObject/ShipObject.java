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
        }
    }

    @Override
    public boolean spawn(int row, int column, int gameState) {
        if (!isActive) {
            spawnComponent.spawn(mTransform, row, column);
            if(gameState == 0){
                isActive = true;
            }
            return true;
        }
        return false;
    }
}
