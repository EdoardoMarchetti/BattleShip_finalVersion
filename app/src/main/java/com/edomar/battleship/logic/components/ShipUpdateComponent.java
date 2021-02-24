package com.edomar.battleship.logic.components;

import android.util.Log;

import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.transforms.ShipTransform;
import com.edomar.battleship.logic.transforms.Transform;
import com.edomar.battleship.logic.components.interfaces.UpdateComponent;

public class ShipUpdateComponent implements UpdateComponent {

    private static final String TAG = "ShipMovementComponent";

    @Override
    public boolean update(long fps, Transform t, Grid grid) {
        //Controlla se la nave è ancora viva
        //TODO
        ShipTransform sp = (ShipTransform) t;
        /**
         * if(viva){
         * return false; //nascondi all'avversario
         * else{
         * //se il livello lo richiede si dovranno indicare come colpiti tutti i blocchi intorno
         * return true; //nave abbattuta e quindi l'avversario può vederla
         */

        Log.d("Colpo", "update: vite rimanenti = "+ sp.getLives());

        if(sp.getLives() == 0){
            //Nave affondata
            Log.d("Ultimotentativo", "update: affondata");
            return true;
        }else{
            //nave ancora viva
            return false;
        }

        //return true;
    }


}
