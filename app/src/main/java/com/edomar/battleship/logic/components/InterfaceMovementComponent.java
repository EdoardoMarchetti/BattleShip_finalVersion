package com.edomar.battleship.logic.components;

import com.edomar.battleship.logic.Transform;

public interface InterfaceMovementComponent {

    boolean move(long fps,
                 Transform t,
                 Transform playerTransform); //aggiunto senza personalizzarlo
}
