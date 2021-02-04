package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

public class SubMarineSpec extends ObjectSpec {

    private static final String tag = "Ship";
    private static final String bitmapName = "ship_sub_marine";
    private static final PointF blocksOccupied= new PointF(1,5);
    private static final  String[] components = new String[]{"ShipGraphicsComponent",
            "ShipSpawnComponent",
            "ShipInputComponent",
            "ShipUpdateComponent"};

    public SubMarineSpec() {
        super(tag, bitmapName, blocksOccupied, components);
    }
}
