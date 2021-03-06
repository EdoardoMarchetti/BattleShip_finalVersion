package com.edomar.battleship.logic.levels;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;


import com.edomar.battleship.battlefield.IBattleField;
import com.edomar.battleship.logic.gameObject.GameObject;
import com.edomar.battleship.logic.gameObject.GameObjectFactory;
import com.edomar.battleship.logic.specifications.BattleshipSpec;
import com.edomar.battleship.logic.specifications.CarrierSpec;
import com.edomar.battleship.logic.specifications.CruiserSpec;
import com.edomar.battleship.logic.specifications.DestroyerSpec;
import com.edomar.battleship.logic.specifications.MissileSpec;
import com.edomar.battleship.logic.specifications.PatrolSpec;
import com.edomar.battleship.logic.specifications.RescueSpec;
import com.edomar.battleship.logic.specifications.SubMarineSpec;
import com.edomar.battleship.logic.transforms.Collider;
import com.edomar.battleship.utils.Utils;

import java.util.ArrayList;



public class LevelManager { //Pagina 694

    private static final String TAG = "LevelManager";

    public static int MISSILE;
    public static int sNumShipsInLevel;

    private ArrayList<GameObject> objects;
    private Level currentLevel;
    private GameObjectFactory factory;
    private int mNumShipsRemains;

    private boolean[] shipsInError;

    public LevelManager(Context context, float gridSize, IBattleField bf, String level){

        objects = new ArrayList<>();
        factory = new GameObjectFactory(context, gridSize, bf);

        setCurrentLevel(level);
        buildGameObject();
        shipsInError = new boolean[sNumShipsInLevel];
    }

    public void setCurrentLevel(String level) {
        level = Utils.translateScenario(level);
        switch (level){
            case "russian":
                currentLevel = new LevelRussian();
                break;
            case "classic":
                currentLevel = new LevelClassic();
                break;
            case "standard":
                currentLevel = new LevelStandard();
                break;
            case "test":
                currentLevel = new LevelTest();
                break;
            default:
                Log.e(TAG, "setCurrentLevel: errore nel caricare il livello = "+level);
                break;

        }
    }

    public void buildGameObject(){

        objects.clear();
        ArrayList<String> objectsToLoad = currentLevel.getLevelObjects();

        for(int i = 0; i < objectsToLoad.size(); i++){

            switch (objectsToLoad.get(i)){

                case "battleship":
                    objects.add(factory.create(new BattleshipSpec()));
                    Log.d(TAG, "buildGameObject: battleship");
                    break;
                case "carrier":
                    objects.add(factory.create(new CarrierSpec()));
                    break;
                case "cruiser":
                    objects.add(factory.create(new CruiserSpec()));
                    break;
                case "destroyer":
                    objects.add(factory.create(new DestroyerSpec()));
                    break;
                case "patrol":
                    objects.add(factory.create(new PatrolSpec()));
                    break;
                case "rescue":
                    objects.add(factory.create(new RescueSpec()));
                    break;
                case "submarine":
                    objects.add(factory.create(new SubMarineSpec()));
                    break;
                case "missile":
                    objects.add(factory.create(new MissileSpec()));
                    MISSILE = i;
                    sNumShipsInLevel = i;
                    mNumShipsRemains = i;
                    break;
                default:
                    Log.e(TAG, "buildGameObject: oggetto non riconosciuto");
                    break;
            }

        }

        Log.d(TAG, "buildGameObject: objects size = "+ objects.size());
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }

    public int getNumShipsInLevel(){return sNumShipsInLevel;}

    public boolean checkCorrectFleetConfiguration() {

        boolean goodPosition = true;
        float blockSize = objects.get(0).getTransform().getBlockDimension();

        if(currentLevel.mDistance){//NON devono essere VICINE
            Log.d("ControlloPosizione", "checkCorrectFleetConfiguration: non vicine ");
            RectF collider1Expanded = new RectF();
            RectF collider2Expanded = new RectF();

            for (int i = 0; i < sNumShipsInLevel; i++) {

                Collider collider1= objects.get(i)
                        .getTransform().getCollider();


                collider1Expanded.top = collider1.top - (blockSize/2);
                collider1Expanded.left = collider1.left - (blockSize/2);
                collider1Expanded.bottom = collider1.bottom + (blockSize/2);
                collider1Expanded.right = collider1.right + (blockSize/2);



                boolean collides = false;

                for (int j = 0; j < sNumShipsInLevel; j++) {
                    if(i != j) {
                        Log.d("CicloVicine", "checkCorrectFleetConfiguration: nave ["+i+"]["+j+"]");
                        Collider collider2 = objects.get(j)
                                .getTransform().getCollider();


                        collider2Expanded.top = collider2.top - (blockSize/10);
                        collider2Expanded.left = collider2.left - (blockSize/10);
                        collider2Expanded.bottom = collider2.bottom + (blockSize/10);
                        collider2Expanded.right = collider2.right + (blockSize/10);

                        if (RectF.intersects(collider1Expanded, collider2Expanded)) {
                            Log.d("ControlloPosizione", "checkCorrectFleetConfiguration: troppo vicine");
                            collides = true;
                            goodPosition = false;

                        }
                    }

                }

                if(collides){
                    collider1.setColorRed();
                    shipsInError[i] = true;
                }else{
                    collider1.setColorGreen();
                    shipsInError[i] = false;
                }
            }


        }else{//NON devono essere SOVRAPPOSTE

            for (int i = 0; i < sNumShipsInLevel; i++) {

                Collider collider1= objects.get(i)
                        .getTransform().getCollider();

                boolean collides = false;

                for (int j = 0; j < sNumShipsInLevel; j++) {
                    if(i != j) {
                        Collider collider2 = objects.get(j)
                                .getTransform().getCollider();

                        if (RectF.intersects(collider1, collider2)) {
                            collides = true;
                            goodPosition = false;
                        }
                    }

                }

                if(collides){
                    collider1.setColorRed();
                    shipsInError[i] = true;
                }else{
                    collider1.setColorGreen();
                    shipsInError[i] = false;
                }
            }

        }

        return goodPosition;
    }

    public void shipLost() {
        mNumShipsRemains--;
    }


    public int getShipsRemains() {
        return  mNumShipsRemains;
    }

    public boolean needDistance() {
        return currentLevel.mDistance;
    }

    public boolean[] getShipsInError(){
        return shipsInError;
    }
}
