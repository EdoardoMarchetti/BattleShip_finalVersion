package com.edomar.battleship.battlefield;

import android.view.MotionEvent;

import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.levels.LevelManager;

public interface InputObserver {

    void handleInput(MotionEvent event, Grid grid,
                     LevelManager levelManager, int gameState,
                     int notifyNumber);

}
