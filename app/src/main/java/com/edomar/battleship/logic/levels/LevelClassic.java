package com.edomar.battleship.logic.levels;

import com.edomar.battleship.logic.GameObject;

import java.util.ArrayList;

public class LevelClassic extends Level {

    public LevelClassic(){
        mLevelName = "classic";
        mDistance = false;
        mLevelObjects = new ArrayList<String>();

        //aggiungo le navi
        mLevelObjects.add("battleship"); //5 blocchi
        mLevelObjects.add("cruiser"); //4 blocchi
        mLevelObjects.add("destroyer"); //3 blocchi
        mLevelObjects.add("rescue"); //3 blocchi
        mLevelObjects.add("patrol"); //2 blocchi


        //aggiungo il missile
        mLevelObjects.add("missile");
    }


}
