package com.edomar.battleship.battlefield;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class BattleField extends IBattleField
        {

    private static final String TAG = "BattleField";


    /** Costruttori **/
    public BattleField(Context context) {
        super(context);
    }

    public BattleField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public BattleField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        for (InputObserver io : inputObservers) {
            io.handleInput(event, mGrid, mLevelManager);
        }

        return true;
    }





}
