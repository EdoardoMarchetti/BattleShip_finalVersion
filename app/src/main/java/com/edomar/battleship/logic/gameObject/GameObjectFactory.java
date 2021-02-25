package com.edomar.battleship.logic.gameObject;

import android.content.Context;
import android.graphics.PointF;


import com.edomar.battleship.battlefield.IBattleField;
import com.edomar.battleship.logic.components.AmmoGraphicsComponent;
import com.edomar.battleship.logic.components.AmmoSpawnComponent;
import com.edomar.battleship.logic.components.AmmoUpdateComponent;
import com.edomar.battleship.logic.components.ShipGraphicsComponent;
import com.edomar.battleship.logic.components.ShipInputComponent;
import com.edomar.battleship.logic.components.ShipSpawnComponent;
import com.edomar.battleship.logic.components.ShipUpdateComponent;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.logic.transforms.AmmoTransform;
import com.edomar.battleship.logic.transforms.ShipTransform;


public class GameObjectFactory { //pagina 559 e 657

    private static final String TAG = "GameObjectFactory";

    private Context mContext;
    private float mGridSize;
    private float mBlockSize;
    private IBattleField mBattlefieldReference;

    public GameObjectFactory (Context c, float gridSize, IBattleField bf){
        this.mContext = c;
        this.mGridSize = gridSize;
        this.mBlockSize = gridSize / 10;
        mBattlefieldReference = bf;
    }

    public GameObject create(ObjectSpec spec){
        //GameObject object = new GameObject();
        GameObject object = null;

        int numComponents = spec.getComponents().length;
        //object.setTag(spec.getTag());

        final float HIDDEN = -2000f;

        PointF objectSize = new PointF(mBlockSize * spec.getBlocksOccupied().x,
                mBlockSize * spec.getBlocksOccupied().y);

        PointF location = new PointF(HIDDEN, HIDDEN);

        // First give the game object the
        // right kind of transform

        switch(spec.getTag()){
            case "Ship":
                //TODO--Crea oggetto nave
                object = new ShipObject();
                object.setGridTag(spec.getGridTag());
                object.setTransform(new ShipTransform(objectSize.x, objectSize.y, location, mGridSize));
                break;
            case "Ammo":
                //TODO--Crea oggetto generico
                object = new GameObject();
                object.setTag(spec.getTag());
                float speed = mGridSize / spec.getSpeed();
                object.setTransform(new AmmoTransform(speed, objectSize.x, objectSize.y, location, mGridSize));
                break;
            default:
                //TODO
                break;
        }





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
                //Test
                /*case "ShipMovementComponent":
                    ShipInputComponent sic = new ShipInputComponent(mBattlefieldReference);
                    object.setInput(sic);
                    object.setUpdater(sic);*/

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
