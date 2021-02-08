package com.edomar.battleship;

import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.edomar.battleship.logic.GameObject;
import com.edomar.battleship.logic.Level;

import java.util.ArrayList;

public class GridInputController implements InputObserver{

    private static final String TAG = "GridController";
    private AmmoSpawner mAS;

    public GridInputController(BattleField b){
        b.addObserver(this);
        mAS = b;
    }

    @Override
    public void handleInput(MotionEvent event, Grid grid, Level level) {
        //Prima di tutto controllare se sia è in preMatch o durante una partita (if statement da aggiungere)
        //PreMatch -> l'input non è mai rivolta alla griglia
        //Match -> l'input è sempre rivolto ala griglia -> verificare se si ha già fatto quel colpo
        //                                              -> far partire l'animazione del razzo
        //                                              -> definire se si è colpito una nave o meno
        //                                              -> indicare nella model della griglia il colpo effettuato

        boolean preMatch = false;
        if(!preMatch){

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

                //Verifica se il tocco è avvenuto in coordinate ammesse e se il colpo era già stato fatto
                if (row >= 0 && row < 10 && column >= 0 && column < 10) {

                    //Se si è nell'if il colpo non è stato ancora eseguito
                    Log.d(TAG, "handleInput: colpo valido");

                    ArrayList<GameObject> objects = level.getGameObject();
                    boolean hit = false;
                    int j = 0;
                    //Verifica se è stata colpita una nave
                    while (!hit && j < level.mNumShipsInLevel) {
                        RectF objectCollider = objects.get(j) //nave
                                .getTransform()               //transform
                                .getCollider();               //collider

                        if (objectCollider.contains(x, y)) {
                            //nave colpita
                            hit = true;
                            Log.d(TAG, "handleInput: nave colpita");
                            //diminuisci vite della nave
                            //segna sulla griglia il bersaglio colpito
                        }

                        j++;

                    }

                    //se al termine del ciclo for hit=false nessuna nave è stata colpita e quindi sulla griglia verrà disegnata una X
                    grid.setHit(row, column, hit);
                    Log.d(TAG, "handleInput: colpo aggiornato");

                    //fai partire l'animazione del razzo
                    mAS.spawnAmmo(row, column); //a spawn ammo devono essere passate le coordinate colpite per posizionare il missile
                }
            }

        }else{
            //Do nothing
        }
    }
}
