package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

/** Questa è la calsse che tutte le Spec dovranno estendere**/

public class ObjectSpec {

    private String mTag;
    private String mGridTag;
    private String mBitmapName;
    private float mSpeed;
    private PointF mBlocksOccupied;
    private String[] mComponents;

    public ObjectSpec(String tag, String gTag, String bitmapName, float speed, PointF blocksOccupied, String[] components) {
        this.mTag = tag;
        this.mGridTag = gTag;
        this.mBitmapName = bitmapName;
        this.mSpeed = speed;
        this.mBlocksOccupied = blocksOccupied;
        this.mComponents = components;
    }

    public String getTag() {
        return mTag;
    }

    public String getGridTag() {
        return mGridTag;
    }

    public String getBitmapName() {
        return mBitmapName;
    }

    public float getSpeed(){
        return mSpeed;
    }

    public PointF getBlocksOccupied() {
        return mBlocksOccupied;
    }

    public String[] getComponents() {
        return mComponents;
    }
}
