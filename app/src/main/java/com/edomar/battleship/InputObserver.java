package com.edomar.battleship;

import android.view.MotionEvent;

import com.edomar.battleship.logic.GameObject;
import com.edomar.battleship.logic.Level;
import com.edomar.battleship.logic.Transform;

import java.util.ArrayList;

public interface InputObserver {

    void handleInput(MotionEvent event, Grid grid, Level level);

}
