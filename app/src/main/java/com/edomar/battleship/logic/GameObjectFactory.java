package com.edomar.battleship.logic;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;

import com.edomar.battleship.BattleField;
import com.edomar.battleship.logic.components.ShipGraphicsComponent;
import com.edomar.battleship.logic.components.ShipInputComponent;
import com.edomar.battleship.logic.components.ShipMovementComponent;
import com.edomar.battleship.logic.components.ShipSpawnComponent;
import com.edomar.battleship.logic.specifications.ObjectSpec;

public class GameObjectFactory { //pagina 559 e 657


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

        // First give the game object the
        // right kind of transform
        /*switch(object.getTag()){
            case "Ship":
            case "Ammo":
            default:
        }
         */

        PointF objectSize = new PointF(mBlockSize * spec.getBlocksOccupied().x,
                mBlockSize * spec.getBlocksOccupied().y);

        PointF location = new PointF(HIDDEN, HIDDEN);

        object.setTransform(new Transform(objectSize.x, objectSize.y, location, mGridSize));

        for(int i=0 ; i < numComponents ; i++){
            switch (spec.getComponents()[i]){

                case "ShipGraphicsComponent":
                    object.setGraphics(new ShipGraphicsComponent(),
                            mContext, spec, objectSize);
                    break;

                case "ShipSpawnComponent":
                    //TODO
                    object.setSpawner(new ShipSpawnComponent());
                    break;

                case "ShipInputComponent":
                    //TODO
                    object.setInput(new ShipInputComponent(mBattlefieldReference));
                    break;

                case "ShipMovementComponent":
                    //TODO
                    object.setMovement(new ShipMovementComponent());
                    break;
            }
        }


        return object;
    }

}
