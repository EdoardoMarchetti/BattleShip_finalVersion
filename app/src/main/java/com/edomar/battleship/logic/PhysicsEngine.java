package com.edomar.battleship.logic;

import com.edomar.battleship.Grid;

import java.util.ArrayList;

public class PhysicsEngine {

    public boolean update(long fps, ParticleSystem ps, ArrayList<GameObject> objects, Grid grid){

        for (GameObject o: objects) {
            if(o.checkActive()){
                o.update(fps, grid);
            }
        }

        if(ps.mIsRunning){
            ps.update(fps);
        }

        return false;
    }

    //Codice per la gestione dell'esito del colpo
    private void coordinatesHit(){}
}
