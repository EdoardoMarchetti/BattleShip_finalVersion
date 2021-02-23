package com.edomar.battleship.logic.components;

import android.util.Log;

import com.edomar.battleship.logic.transforms.ShipTransform;
import com.edomar.battleship.logic.transforms.Transform;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;

/** Alcuni appunti:
 *      -per fare lo spawn senza configurazione si può usare l'indice che occupa la nave nel livello
 */

public class ShipSpawnComponent implements SpawnComponent {

    private static final String TAG = "ShipSpawnComponent";

    boolean startSpawn = true;

    @Override
    public void spawn(Transform t, int row, int column) {

        ShipTransform sp = (ShipTransform) t;

        t.setStartLocation(column * t.getBlockDimension(), row * t.getBlockDimension(), t.isVertical());
        Log.d("SpawnConLettura", "spawn: horizontal= " + t.getLocation().x  + " vertical= " + t.getLocation().y);
        Log.d("SpawnConLettura", "spawn: column= "+column+" row = "+row);

    }
}
