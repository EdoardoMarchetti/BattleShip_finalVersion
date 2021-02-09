package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

public class DestroyerSpec extends ObjectSpec {

    private static final String tag = "Ship";
    private static final String bitmapName = "ship_destroyer";
    private static final float speed = 0;
    private static final PointF blocksOccupied= new PointF(1,3);
    private static final  String[] components = new String[]{"ShipGraphicsComponent",
            "ShipSpawnComponent",
            "ShipInputComponent",
            "ShipUpdateComponent"};

    public DestroyerSpec() {
        super(tag, bitmapName, speed,blocksOccupied, components);
    }
}
