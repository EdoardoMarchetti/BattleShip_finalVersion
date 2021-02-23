package com.edomar.battleship.logic.levels;

import com.edomar.battleship.logic.transforms.Transform;

import java.util.ArrayList;

public abstract class Level {

    public ArrayList<String> mLevelObjects;
    public String mLevelName;
    public Transform transformInMovement = null;
    public boolean mDistance;

    public  ArrayList<String> getLevelObjects(){
        return mLevelObjects;
    }


    public String getName() {
        return mLevelName;
    }



}
