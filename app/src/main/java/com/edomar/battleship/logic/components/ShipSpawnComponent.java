package com.edomar.battleship.logic.components;

import android.util.Log;

import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;

public class ShipSpawnComponent implements SpawnComponent {

    private static final String TAG = "ShipSpawnComponent";

    @Override
    public void spawn(Transform playerTransform, Transform t) {
        t.setLocation(t.getBlockDimension()*5,t.getBlockDimension()*6);
        Log.d(TAG, "spawn: blockDimension = " +t.getBlockDimension());
        Log.d(TAG, "spawn: horizontal= "+t.getLocation().x+" vertical= "+t.getLocation().y);
    }
}
