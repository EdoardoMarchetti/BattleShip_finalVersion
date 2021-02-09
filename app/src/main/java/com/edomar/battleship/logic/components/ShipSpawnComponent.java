package com.edomar.battleship.logic.components;

import android.graphics.Point;
import android.util.Log;

import com.edomar.battleship.logic.transform.ShipTransform;
import com.edomar.battleship.logic.transform.Transform;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;

/** Alcuni appunti:
 *      -per fare lo spawn senza configurazione si può usare l'indice che occupa la nave nel livello
 */

public class ShipSpawnComponent implements SpawnComponent {

    private static final String TAG = "ShipSpawnComponent";

    @Override
    public void spawn(Transform t, int row, int column) {

        ShipTransform sp = (ShipTransform) t;

        /** Questa è la parte di default**/
        boolean isVertical = true;
        //t.setStartLocation(0,t.getBlockDimension() * 8, isVertical); //prova orizzontale
        sp.setStartLocation(t.getBlockDimension() * column,0, isVertical); //prova verticale
        Log.d(TAG, "spawn: blockDimension = " +t.getBlockDimension());
        Log.d(TAG, "spawn: horizontal= "+t.getLocation().x+" vertical= "+t.getLocation().y);
    }
}
