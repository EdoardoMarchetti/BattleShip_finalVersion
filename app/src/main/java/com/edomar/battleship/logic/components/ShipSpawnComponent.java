package com.edomar.battleship.logic.components;

import android.util.Log;

import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;

/** Alcuni appunti:
 *      -per fare lo spawn senza configurazione si può usare l'indice che occupa la nave nel livello
 */

public class ShipSpawnComponent implements SpawnComponent {

    private static final String TAG = "ShipSpawnComponent";

    @Override
    public void spawn(Transform playerTransform, Transform t, int posizioneInGriglia) {

        boolean isVertical = true;
        //t.setStartLocation(0,t.getBlockDimension() * 8, isVertical); //prova orizzontale
        t.setStartLocation(t.getBlockDimension() * 8,0, isVertical); //prova verticale
        Log.d(TAG, "spawn: blockDimension = " +t.getBlockDimension());
        Log.d(TAG, "spawn: horizontal= "+t.getLocation().x+" vertical= "+t.getLocation().y);
    }
}
