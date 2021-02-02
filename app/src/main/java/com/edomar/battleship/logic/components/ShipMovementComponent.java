package com.edomar.battleship.logic.components;

import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.components.interfaces.MovementComponent;

public class ShipMovementComponent implements MovementComponent {
    @Override
    public boolean move(long fps, Transform t, Transform playerTransform) {
        return true;
    }
}
