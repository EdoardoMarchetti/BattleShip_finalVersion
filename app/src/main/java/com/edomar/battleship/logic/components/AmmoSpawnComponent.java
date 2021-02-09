package com.edomar.battleship.logic.components;

import android.graphics.Point;
import android.util.Log;

import com.edomar.battleship.logic.transform.Transform;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;

public class AmmoSpawnComponent implements SpawnComponent {
    private static final String TAG = "AmmoSpawnComponent";

    @Override
    public void spawn(Transform t, int  row, int column) {
        Log.d("AmmoSpawnComponent", "spawn: spawn completed ");
        //t.setLocation(column * t.getBlockDimension(), row * t.getBlockDimension());
        Log.d(TAG, "spawn: column ="+column);
        Log.d(TAG, "spawn: row ="+row);
        t.setLocation(0 - (t.getObjectWidth()+t.getBlockDimension()), row * t.getBlockDimension());
        Log.d(TAG, "spawn: ");
    }

}
