package com.edomar.battleship.logic.components.interfaces;

import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.transforms.Transform;

public interface UpdateComponent {

    /** Chiamato direttamente dal GameObject**/
    boolean update(long fps,
                 Transform t,
                 Grid grid); //aggiunto senza personalizzarlo


}
