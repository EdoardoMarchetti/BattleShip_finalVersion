package com.edomar.battleship.logic;


import android.graphics.PointF;
import android.util.Log;


import com.edomar.battleship.battlefield.IBattleField;
import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.logic.transforms.ShipTransform;
import com.edomar.battleship.utils.SoundEngine;

import java.util.ArrayList;

public class PhysicsEngine {

    //se update restituisce 0 allora continua nel fragment
    //se update restituisce 1 allora razzo caduto
    //se update restituisce 2 a



    private static final String TAG = "PhysicsEngine";

    private LevelManager mLevelMan;
    private ArrayList<GameObject> objects;

    public PhysicsEngine(LevelManager lvlMan) {
        mLevelMan = lvlMan;
        objects = mLevelMan.getObjects();
    }

    public void update(long fps, ParticleSystem ps,
                      Grid grid,
                      IBattleField bf
                          ){ //forse da aggiungere lo stato del gioco per migliorare efficienza



        for (GameObject o: objects) {
            /*if(o.checkActive()){
                o.update(fps, grid);
            }*/
            o.update(fps, grid);
        }

        /** Da usare una volta inserito il gamestate del campo
        /*
        if(!preMatch){
            colpisci
         }
         */


        if(ps.mIsRunning){
            ps.update(fps);
        }

        if(objects.get(LevelManager.MISSILE).checkActive()){
            Log.d("notifyNumber", "update: prima = "+ bf.notifyNumber);
            hitCoordinates(grid, ps, bf);
            Log.d("notifyNumber", "update: dopo = "+ bf.notifyNumber);
        }

    }

    //Codice per la gestione dell'esito del colpo
    private void hitCoordinates(Grid grid, ParticleSystem ps, IBattleField bf){


        //Prima faccio precipitare il razzo nell'ultimo colpo eseguito
        int row = grid.getLastHitCoordinates().x;
        int column = grid.getLastHitCoordinates().y;
        boolean result = grid.getLastHitResult();

        GameObject missile = objects.get(LevelManager.MISSILE);

        if(missile.getTransform().getLocation().x >= grid.getBlockDimension()*(column) && missile.checkActive()
               ){
            bf.notifyNumber = 1;
            missile.setInactive();

            float cooX = (float)(grid.getBlockDimension()*(column+0.5));
            float cooY = (float) (grid.getBlockDimension()*(row+0.5));

            ps.emitParticles(
                    new PointF(
                            (float)(grid.getBlockDimension()*(column+0.5)),
                            (float) (grid.getBlockDimension()*(row+0.5)))

            );

            if(result){ //Se il colpo è andato a segno verifica se la nave è affondata
                ps.mShipHit = true;
                boolean found = false;

                for(int i = 0; i < mLevelMan.getNumShipsInLevel() && !found ; i++){
                    GameObject ship = objects.get(i);
                    ShipTransform shipTransform = (ShipTransform) ship.getTransform();

                    if(shipTransform.getCollider().contains(cooX, cooY)){
                        shipTransform.shipHit();

                        if(shipTransform.getLives() == 0){
                            ship.setActive();
                            if(mLevelMan.needDistance()){
                                grid.setHitForDistance(shipTransform.getCollider());
                            }
                            mLevelMan.shipLost();
                            if(mLevelMan.getShipsRemains() == 0){
                                bf.notifyNumber = 2;
                            }
                        }
                        found = true;
                    }
                }

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


    }
}
