package com.edomar.battleship.logic.components.interfaces;

import com.edomar.battleship.logic.transforms.Transform;

public interface SpawnComponent {

    /** Chiamato direttamente dal GameObject**/
    void spawn(Transform t,
               int row,
               int column); //aggiunto senza personalizzarlo
}
