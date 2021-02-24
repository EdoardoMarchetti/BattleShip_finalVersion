package com.edomar.battleship.logic.gameObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.edomar.battleship.logic.components.interfaces.GraphicsComponent;
import com.edomar.battleship.logic.components.interfaces.InputComponent;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;
import com.edomar.battleship.logic.components.interfaces.UpdateComponent;
import com.edomar.battleship.logic.grid.Grid;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.logic.transforms.ShipTransform;
import com.edomar.battleship.logic.transforms.Transform;

public class GameObject {

    private static final String TAG = "GameObject";

    protected Transform mTransform;
    protected  boolean isActive = false;
    protected boolean isDrawable = true;
    protected String mTag;
    protected String mGridTag;

    protected GraphicsComponent graphicsComponent;
    protected UpdateComponent updateComponent;
    protected SpawnComponent spawnComponent;


    /** Setters**/

    public void setSpawner(SpawnComponent s){
        spawnComponent = s;
    }

    public void setGraphics(GraphicsComponent g, Context c,
                            ObjectSpec spec, PointF objectSize){
        //Set the graphics component and initialize it
        graphicsComponent = g;
        g.initialize(c, spec, objectSize);
    }

    public void setUpdater(UpdateComponent u){
        updateComponent = u;
    }

    public void setInput(InputComponent s){
        s.setTransform(mTransform);
    }

    public void setTag(String tag){
        mTag = tag;
    }

    public void setGridTag(String gridTag){
        mGridTag = gridTag;
    }

    public void setTransform(Transform t){
        mTransform = t;
    }

    public void setInactive(){
        isActive = false;
        isDrawable = false;
    }



    /** Getters**/

    public Transform getTransform(){
        return mTransform;
    }

    public boolean checkActive() {
        return isActive;
    }

    public boolean checkDrawable(){ return isDrawable;}

    public String getTag() {
        return mTag;
    }

    public String getGridTag() {
        return mGridTag;
    }

    /** Metodi che invocano i component **/
    public void draw(Canvas canvas, Paint paint){
        graphicsComponent.draw(canvas, paint, mTransform);
    }

    public void update(long fps, Grid grid) { //Da modificare in base alle necessità
        if (!(updateComponent.update(fps,
                mTransform, grid))) {
            // Component returned false
            isActive = false;
        }

    }

    public boolean spawn(int row, int column) { //Da modificare in base alle necessità
        // Only spawnComponent if not already active
        if (!isActive) {
            Log.d("SpawnMissile", "spawn: in gameObject");
            Log.d("ObjectActivation", "spawn: object activated= "+getGridTag());
            spawnComponent.spawn(mTransform, row, column);
            isActive = true;
            isDrawable = true;
            return true;
        }
        return false;
    }


    public void setActive() {
        isActive = true;
        isDrawable = true;
    }
}
