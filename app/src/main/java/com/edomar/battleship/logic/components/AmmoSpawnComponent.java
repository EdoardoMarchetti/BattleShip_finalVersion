package com.edomar.battleship.logic.components;


import android.util.Log;

import com.edomar.battleship.logic.transforms.Transform;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;

public class AmmoSpawnComponent implements SpawnComponent {
    private static final String TAG = "AmmoSpawnComponent";

    @Override
    public void spawn(Transform t, int  row, int column) {
        Log.d("AmmoSpawnComponent", "spawn: spawn completed ");
        Log.d("BattleFieldCPU", "spawncomponent: column ="+column);
        Log.d("BattleFieldCPU", "spawncomponent: row ="+row);
        t.setLocation(0 - (t.getObjectWidth()+t.getBlockDimension()), row * t.getBlockDimension());
        Log.d(TAG, "spawn: ");
    }

}
