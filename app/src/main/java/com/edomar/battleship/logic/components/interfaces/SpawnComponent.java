package com.edomar.battleship.logic.components.interfaces;

import android.graphics.Point;

import com.edomar.battleship.logic.transform.Transform;

public interface SpawnComponent {

    /** Chiamato direttamente dal GameObject**/
    void spawn(Transform t,
               int row,
               int column); //aggiunto senza personalizzarlo
}
