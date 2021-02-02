package com.edomar.battleship.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.edomar.battleship.logic.components.interfaces.GraphicsComponent;
import com.edomar.battleship.logic.components.interfaces.InputComponent;
import com.edomar.battleship.logic.components.interfaces.MovementComponent;
import com.edomar.battleship.logic.components.interfaces.SpawnComponent;
import com.edomar.battleship.logic.specifications.ObjectSpec;

public class GameObject { //Probabilmente dovrà diventare una view per effettuare il drag&drop

    private Transform mTransform;
    private  boolean isActive = false; //forse da inserire nel transform
    private String mTag;

    private GraphicsComponent graphicsComponent;
    private MovementComponent movementComponent;
    private SpawnComponent spawnComponent;

    /** Setters**/

    public void setSpawner(SpawnComponent s){
        spawnComponent = s;
    }

    public void setGraphics(GraphicsComponent g, Context c,
                            ObjectSpec spec, PointF objectSize){

        graphicsComponent = g;
        g.initialize(c, spec, objectSize);
    }

    public void setMovement(MovementComponent m){
        movementComponent = m;
    }

    public void setInput(InputComponent s){
        s.setTransform(mTransform);
    }

    public void setTag(String tag){
        mTag = tag;
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

    /** Metodi che invocano i component **/
    void draw(Canvas canvas, Paint paint){
        graphicsComponent.draw(canvas, paint, mTransform);
    }

    void update(long fps, Transform playerTransform) { //Da modificare in base alle necessità
        if (!(movementComponent.move(fps,
                mTransform, playerTransform))) {
            // Component returned false
            isActive = false;
        }
    }

    boolean spawn(Transform playerTransform) { //Da modificare in base alle necessità
        // Only spawnComponent if not already active
        if (!isActive) {
            spawnComponent.spawn(playerTransform, mTransform);
            isActive = true;
            return true;
        }
        return false;
    }

}
