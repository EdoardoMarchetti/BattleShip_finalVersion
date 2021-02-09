package com.edomar.battleship;

import android.view.MotionEvent;

import com.edomar.battleship.logic.Level;

public interface InputObserver {

    void handleInput(MotionEvent event, Grid grid, Level level);

}
