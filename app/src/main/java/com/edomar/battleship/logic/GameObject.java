package com.edomar.battleship.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.edomar.battleship.Grid;
import com.edomar.battleship.logic.components.interfaces.GraphicsComponent;
import com.edomar.battleship.logic.components.interfaces.InputComponent;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;
import com.edomar.battleship.logic.components.interfaces.UpdateComponent;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.logic.transform.Transform;

public class GameObject {

    private static final String TAG = "GameObject";

    private Transform mTransform;
    private  boolean isActive = false; //forse da inserire nel transform
    private String mTag;
    private String mGridTag;

    private GraphicsComponent graphicsComponent;
    private UpdateComponent updateComponent;
    private SpawnComponent spawnComponent;

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
    }

    /** Getters**/

    public Transform getTransform(){
        return mTransform;
    }

    public boolean checkActive() {
        return isActive;
    }

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
            spawnComponent.spawn(mTransform, row, column);
            isActive = true;
            return true;
        }
        return false;
    }

}
