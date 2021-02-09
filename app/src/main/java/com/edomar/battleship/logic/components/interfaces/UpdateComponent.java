package com.edomar.battleship.logic.components.interfaces;

import com.edomar.battleship.Grid;
import com.edomar.battleship.logic.transform.Transform;

public interface UpdateComponent {

    /** Chiamato direttamente dal GameObject**/
    boolean update(long fps,
                 Transform t,
                 Grid grid); //aggiunto senza personalizzarlo
}
