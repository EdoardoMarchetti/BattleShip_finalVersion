package com.edomar.battleship.logic;

import android.content.Context;
import android.util.Log;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.logic.specifications.BattleshipSpec;
import com.edomar.battleship.logic.specifications.CarrierSpec;
import com.edomar.battleship.logic.specifications.CruiserSpec;
import com.edomar.battleship.logic.specifications.DestroyerSpec;
import com.edomar.battleship.logic.specifications.MissileSpec;
import com.edomar.battleship.logic.specifications.PatrolSpec;
import com.edomar.battleship.logic.specifications.RescueSpec;
import com.edomar.battleship.logic.specifications.SubMarineSpec;
import com.edomar.battleship.logic.transform.Transform;

import java.util.ArrayList;

public class Level {

    private static final String TAG = "Level";

    public static final int BATTLESHIP_INDEX = 0 ;
    public static final int CARRIER_INDEX = 1 ;
    public static final int CRUISER_INDEX = 2 ;
    public static final int DESTROYER_INDEX = 3 ;
    public static final int PATROL_INDEX = 4 ;
    public static final int RESCUE_INDEX = 5 ;
    public static final int SUB_MARINE_INDEX = 6 ;
    public static final int MISSILE = 7;

    // This will hold all the instances of GameObject
    private ArrayList<GameObject> objects;
    public static final int mNumShipsInLevel = 7;
    public Transform transformInMovement = null;


    public Level(Context c, float gridSize, BattleField bf){
        this.objects = new ArrayList<>();
        GameObjectFactory factory = new GameObjectFactory(c, gridSize, bf);

        buildGameObjects(factory);
    }

    private ArrayList<GameObject> buildGameObjects(GameObjectFactory factory) {

        objects.clear();

        //Create some ships
        objects.add(BATTLESHIP_INDEX, factory.create(new BattleshipSpec()));
        objects.add(CARRIER_INDEX, factory.create(new CarrierSpec()));
        objects.add(CRUISER_INDEX, factory.create(new CruiserSpec()));
        objects.add(DESTROYER_INDEX, factory.create(new DestroyerSpec()));
        objects.add(PATROL_INDEX, factory.create(new PatrolSpec()));
        objects.add(RESCUE_INDEX, factory.create(new RescueSpec()));
        objects.add(SUB_MARINE_INDEX, factory.create(new SubMarineSpec()));

        //Create Missile
        objects.add(MISSILE, factory.create(new MissileSpec()));


        return objects;
    }

    public ArrayList<GameObject> getGameObject(){
        return objects;
    }

}
