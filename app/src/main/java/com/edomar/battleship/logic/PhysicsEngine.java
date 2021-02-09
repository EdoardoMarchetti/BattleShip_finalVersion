package com.edomar.battleship.logic;


import android.graphics.PointF;
import android.util.Log;
import com.edomar.battleship.Grid;
import com.edomar.battleship.utils.SoundEngine;

import java.util.ArrayList;

public class PhysicsEngine {
    private static final String TAG = "PhysicsEngine";

    public boolean update(long fps, ParticleSystem ps, ArrayList<GameObject> objects, Grid grid){

        int count = 0;
        for (GameObject o: objects) {
            if(o.checkActive()){
                Log.d(TAG, "update: object number= "+count);
                o.update(fps, grid);
            }
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
    private void hitCoordinates(Grid grid, ParticleSystem ps, GameObject missile){

        Log.d(TAG, "hitCoordinates: ");

        //Prima faccio precipitare il razzo nell'ultimo colpo eseguito
        int row = grid.getLastHit().x;
        int column = grid.getLastHit().y;
        String[][] gridConfig = grid.getGridConfiguration();


        if(missile.getTransform().getLocation().x >= grid.getBlockDimension()*(column) && missile.checkActive()
               ){
            Log.d(TAG, "hitCoordinates: in if");
            missile.setInactive();

            //Verificare dove il missile è caduto
            if (gridConfig[row][column] == "X"){
                ps.mShipHit = false;
            }else{
                ps.mShipHit = true;
            }


            ps.emitParticles(
                    new PointF(
                            (float)(grid.getBlockDimension()*(column+0.5)),
                            (float) (grid.getBlockDimension()*(row+0.5)))
            );
            if(ps.mShipHit){
                SoundEngine.playExplosion();
                Log.d("Esplosione", "hitCoordinates: playExplosion");
            }else{
                SoundEngine.playSplash();
                Log.d("Esplosione", "hitCoordinates: playSplash");
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
