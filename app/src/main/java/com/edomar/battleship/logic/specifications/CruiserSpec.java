package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

public class CruiserSpec extends ObjectSpec {

    private static final String tag = "Ship";
    private static final String gridTag = "Cr";
    private static final String bitmapName = "ship_cruiser";
    private static final float speed = 0;
    private static final PointF blocksOccupied= new PointF(1,4);
    private static final  String[] components = new String[]{"ShipGraphicsComponent",
            "ShipSpawnComponent",
            "ShipInputComponent",
            "ShipUpdateComponent"};

    public CruiserSpec() {
        super(tag, gridTag, bitmapName, speed,blocksOccupied, components);
    }

}
