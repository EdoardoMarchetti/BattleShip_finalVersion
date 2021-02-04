package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

public class RescueSpec extends ObjectSpec {

    private static final String tag = "Ship";
    private static final String bitmapName = "ship_rescue";
    private static final PointF blocksOccupied= new PointF(1,3);
    private static final  String[] components = new String[]{"ShipGraphicsComponent",
            "ShipSpawnComponent",
            "ShipInputComponent",
            "ShipUpdateComponent"};

    public RescueSpec() {
        super(tag, bitmapName, blocksOccupied, components);
    }
}
