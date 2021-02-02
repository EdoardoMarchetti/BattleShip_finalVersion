package com.edomar.battleship.logic;

import android.content.Context;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.logic.specifications.BattleshipSpec;

import java.util.ArrayList;

public class Level {

    public static final int BATTLESHIP_INDEX = 0 ;
    // This will hold all the instances of GameObject
    private ArrayList<GameObject> objects;

    public Level(Context c, float gridSize, BattleField bf){
        this.objects = new ArrayList<>();
        GameObjectFactory factory = new GameObjectFactory(c, gridSize, bf);

        buildGameObjects(factory);
    }

    private ArrayList<GameObject> buildGameObjects(GameObjectFactory factory) {

        objects.clear();

        //Create some ships
        objects.add(BATTLESHIP_INDEX, factory.create(new BattleshipSpec()));

        return objects;
    }

    public ArrayList<GameObject> getGameObject(){
        return objects;
    }

}
