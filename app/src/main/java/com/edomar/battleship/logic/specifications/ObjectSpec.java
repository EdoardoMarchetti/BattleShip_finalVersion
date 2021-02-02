package com.edomar.battleship.logic.specifications;

import android.graphics.PointF;

/** Questa è la calsse che tutte le Spec dovranno estendere**/

public class ObjectSpec {

    private String mTag;
    private String mBitmapName;
    private PointF mBlocksOccupied;
    private String[] mComponents;

    public ObjectSpec(String tag, String bitmapName, PointF blocksOccupied, String[] components) {
        this.mTag = tag;
        this.mBitmapName = bitmapName;
        this.mBlocksOccupied = blocksOccupied;
        this.mComponents = components;
    }

    public String getTag() {
        return mTag;
    }

    public String getBitmapName() {
        return mBitmapName;
    }

    public PointF getBlocksOccupied() {
        return mBlocksOccupied;
    }

    public String[] getComponents() {
        return mComponents;
    }
}
