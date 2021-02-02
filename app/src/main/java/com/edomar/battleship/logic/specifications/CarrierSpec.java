package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

public class CarrierSpec extends ObjectSpec {

    private static final String tag = "Ship";
    private static final String bitmapName = "ship_carrier";
    private static final PointF blocksOccupied= new PointF(1,4);
    private static final  String[] components = new String[]{"ShipGraphicsComponent",
            "ShipSpawnComponent",
            "ShipInputComponent",
            "ShipMovementComponent"};

    public CarrierSpec() {
        super(tag, bitmapName, blocksOccupied, components);
    }
}
