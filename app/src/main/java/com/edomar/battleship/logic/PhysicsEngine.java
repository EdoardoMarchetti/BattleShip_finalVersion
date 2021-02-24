package com.edomar.battleship.logic;


import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;


import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.logic.transforms.ShipTransform;
import com.edomar.battleship.utils.SoundEngine;

import java.util.ArrayList;

public class PhysicsEngine {
    private static final String TAG = "PhysicsEngine";

    public boolean update(long fps, ParticleSystem ps, ArrayList<GameObject> objects, Grid grid){

        int count = 0;
        for (GameObject o: objects) {
            if(o.checkActive()){
                //Log.d(TAG, "update: object number= "+count);
                o.update(fps, grid);
            }*/
            o.update(fps, grid);
            count++;
        }

        hitCoordinates(grid, ps, objects.get(Level.MISSILE));

        if(ps.mIsRunning){
            ps.update(fps);
        }

        //hitCoordinates(grid, ps, objects.get(Level.MISSILE));

        return false;
    }

    //Codice per la gestione dell'esito del colpo
    private void hitCoordinates(Grid grid, ParticleSystem ps, ArrayList<GameObject> objects){

        //Log.d(TAG, "hitCoordinates: ");

        //Prima faccio precipitare il razzo nell'ultimo colpo eseguito
        int row = grid.getLastHitCoordinates().x;
        int column = grid.getLastHitCoordinates().y;
        boolean result = grid.getLastHitResult();
        GameObject missile = objects.get(LevelManager.MISSILE);

        if(missile.getTransform().getLocation().x >= grid.getBlockDimension()*(column) && missile.checkActive()
               ){
            //Log.d(TAG, "hitCoordinates: in if");
            missile.setInactive();

            float cooX = (float)(grid.getBlockDimension()*(column+0.5));
            float cooY = (float) (grid.getBlockDimension()*(row+0.5));

            ps.emitParticles(
                    new PointF(
                            (float)(grid.getBlockDimension()*(column+0.5)),
                            (float) (grid.getBlockDimension()*(row+0.5)))

            );

            if(result){
                ps.mShipHit = true;
            }else {
                ps.mShipHit = false;
            }

            //Modifica la griglia in base al risultato
            grid.setHit(row, column, result);

            if(ps.mShipHit){
                SoundEngine.playExplosion();
            }else{
                SoundEngine.playSplash();
            }

        }



        /*int row = grid.getLastHit().x;
        int column = grid.getLastHit().y;
        String[][] gridConfig = grid.getGridConfiguration();

        if (gridConfig[row][column] == "0"){
            ps.mShipHit = false;
        }else{
            ps.mShipHit = true;
        }*/



    }
}
