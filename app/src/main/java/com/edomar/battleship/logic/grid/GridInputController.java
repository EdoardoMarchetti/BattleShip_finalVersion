package com.edomar.battleship.logic.grid;

import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.edomar.battleship.battlefield.interfacesImplemented.AmmoSpawner;
import com.edomar.battleship.battlefield.IBattleField;
import com.edomar.battleship.battlefield.InputObserver;
import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.levels.LevelManager;
import com.edomar.battleship.logic.transforms.ShipTransform;

import java.util.ArrayList;

public class GridInputController implements InputObserver {

    private static final String TAG = "GridController";
    private AmmoSpawner mAS;

    public GridInputController(IBattleField b){
        b.addObserver(this);
        mAS = b;
    }

    @Override
    public void handleInput(MotionEvent event, Grid grid, LevelManager levelManager) {
        //Prima di tutto controllare se sia è in preMatch o durante una partita (if statement da aggiungere)
        //PreMatch -> l'input non è mai rivolta alla griglia
        //Match -> l'input è sempre rivolto ala griglia -> verificare se si ha già fatto quel colpo
        //                                              -> far partire l'animazione del razzo
        //                                              -> definire se si è colpito una nave o meno
        //                                              -> indicare nella model della griglia il colpo effettuato

        boolean preMatch = true;


        if(!preMatch && !levelManager.getObjects().get(LevelManager.MISSILE).checkActive()){


            int i = event.getActionIndex();
            int x = (int) event.getX(i);
            int y = (int) event.getY(i);

            int row = (int) (y / grid.getBlockDimension());
            int column = (int) (x / grid.getBlockDimension());
            String[][] gridConfiguration = grid.getGridConfiguration();
            Log.d(TAG, "handleInput: row = "+ row+ " column= "+column);

            int eventType = event.getAction() & MotionEvent.ACTION_MASK;
            Log.d(TAG, "handleInput: envenType= "+eventType);

            if(eventType == MotionEvent.ACTION_UP ||
                    eventType == MotionEvent.ACTION_POINTER_UP) {
                Log.d(TAG, "handleInput: envenType corretto");
                Log.d(TAG, "handleInput: gridConfiguration["+row+"]["+column+"]"+ gridConfiguration[row][column]);
                //Verifica se il tocco è avvenuto in coordinate ammesse e se il colpo era già stato fatto
                if (row >= 0 && row < 10 && column >= 0 && column < 10 && !gridConfiguration[row][column].equals("S") && !gridConfiguration[row][column].equals("X")) {

                    //Se si è nell'if il colpo non è stato ancora eseguito
                    Log.d(TAG, "handleInput: colpo valido");

                    ArrayList<GameObject> objects = levelManager.getObjects();
                    boolean hit = false;
                    int j = 0;
                    //Verifica se è stata colpita una nave
                    while (!hit && j < levelManager.getNumShipsInLevel()) {
                        RectF objectCollider = objects.get(j) //nave
                                .getTransform()               //transform
                                .getCollider();               //collider

                        if (objectCollider.contains(x, y)) {
                            //nave colpita
                            hit = true;
                            //((ShipTransform)objects.get(j).getTransform()).shipHit(); //dovrebbe andare nel physic engine

                        }

                        j++;

                    }

                    //se al termine del ciclo for hit=false nessuna nave è stata colpita e quindi sulla griglia verrà disegnata una X quando l'animazione del razzo terminerà
                    grid.setLastHit(row, column, hit);
                    Log.d(TAG, "handleInput: colpo aggiornato");

                    Log.d("SpawnMissile", "handleInput: start spawn Missile");
                    //fai partire l'animazione del razzo
                    mAS.spawnAmmo(row, column); //a spawn ammo devono essere passate le coordinate colpite per posizionare il missile

                }
            }

        }else{
            //Do nothing
        }
    }
}
