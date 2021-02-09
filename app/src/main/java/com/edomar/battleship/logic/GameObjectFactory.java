package com.edomar.battleship.logic;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.logic.components.AmmoGraphicsComponent;
import com.edomar.battleship.logic.components.AmmoSpawnComponent;
import com.edomar.battleship.logic.components.AmmoUpdateComponent;
import com.edomar.battleship.logic.components.ShipGraphicsComponent;
import com.edomar.battleship.logic.components.ShipInputComponent;
import com.edomar.battleship.logic.components.ShipSpawnComponent;
import com.edomar.battleship.logic.components.ShipUpdateComponent;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.logic.transform.AmmoTransform;
import com.edomar.battleship.logic.transform.ShipTransform;
import com.edomar.battleship.logic.transform.Transform;

public class GameObjectFactory { //pagina 559 e 657

    private static final String TAG = "GameObjectFactory";

    private Context mContext;
    private float mGridSize;
    private float mBlockSize;
    private BattleField mBattlefieldReference;

    public GameObjectFactory (Context c, float gridSize, BattleField bf){
        this.mContext = c;
        this.mGridSize = gridSize;
        this.mBlockSize = gridSize / 10;
        mBattlefieldReference = bf;
    }

    public GameObject create(ObjectSpec spec){
        GameObject object = new GameObject();

        int numComponents = spec.getComponents().length;
        object.setTag(spec.getTag());

        final float HIDDEN = -2000f;

        PointF objectSize = new PointF(mBlockSize * spec.getBlocksOccupied().x,
                mBlockSize * spec.getBlocksOccupied().y);

        PointF location = new PointF(HIDDEN, HIDDEN);

        // First give the game object the
        // right kind of transform

        switch(object.getTag()){
            case "Ship":
                object.setGridTag(spec.getGridTag());
                object.setTransform(new ShipTransform(objectSize.x, objectSize.y, location, mGridSize));
                break;
            case "Ammo":
                float speed = mGridSize / spec.getSpeed();
                object.setTransform(new AmmoTransform(speed, objectSize.x, objectSize.y, location, mGridSize));
                break;
            default:
                //TODO
                break;
        }




        //object.setTransform(new Transform(objectSize.x, objectSize.y, location, mGridSize));

        for(int i=0 ; i < numComponents ; i++){
            switch (spec.getComponents()[i]){
                //Ships' component
                case "ShipGraphicsComponent":
                    object.setGraphics(new ShipGraphicsComponent(),
                            mContext, spec, objectSize);
                    break;

                case "ShipSpawnComponent":
                    object.setSpawner(new ShipSpawnComponent());
                    break;

                case "ShipInputComponent":
                    object.setInput(new ShipInputComponent(mBattlefieldReference));
                    break;

                case "ShipUpdateComponent":
                    object.setUpdater(new ShipUpdateComponent());
                    break;

                //Ammos' components
                case "AmmoGraphicsComponent":
                    object.setGraphics(new AmmoGraphicsComponent(), mContext, spec, objectSize);
                    break;

                case "AmmoSpawnComponent":
                    object.setSpawner(new AmmoSpawnComponent());
                    break;

                case "AmmoUpdateComponent":
                    object.setUpdater(new AmmoUpdateComponent());
            }
        }


        return object;
    }

}
