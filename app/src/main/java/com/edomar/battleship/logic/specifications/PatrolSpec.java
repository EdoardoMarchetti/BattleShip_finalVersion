package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

public class PatrolSpec extends ObjectSpec {

    private static final String tag = "Ship";
    private static final String bitmapName = "ship_patrol";
    private static final PointF blocksOccupied= new PointF(1,2);
    private static final  String[] components = new String[]{"ShipGraphicsComponent",
            "ShipSpawnComponent",
            "ShipInputComponent",
            "ShipUpdateComponent"};

    public PatrolSpec() {
        super(tag, bitmapName, blocksOccupied, components);
    }
}
