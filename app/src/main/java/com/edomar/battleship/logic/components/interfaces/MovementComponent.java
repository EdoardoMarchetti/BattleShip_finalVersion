package com.edomar.battleship.logic.components.interfaces;

import com.edomar.battleship.logic.Transform;

public interface MovementComponent {

    /** Chiamato direttamente dal GameObject**/
    boolean move(long fps,
                 Transform t,
                 Transform playerTransform); //aggiunto senza personalizzarlo
}
