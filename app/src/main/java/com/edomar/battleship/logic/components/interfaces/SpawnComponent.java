package com.edomar.battleship.logic.components.interfaces;

import com.edomar.battleship.logic.Transform;

public interface SpawnComponent {

    /** Chiamato direttamente dal GameObject**/
    void spawn(Transform playerTransform,
               Transform t); //aggiunto senza personalizzarlo
}
