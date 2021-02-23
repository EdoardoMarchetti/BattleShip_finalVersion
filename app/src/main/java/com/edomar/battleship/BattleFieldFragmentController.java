package com.edomar.battleship;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public abstract class BattleFieldFragmentController extends SurfaceView implements SurfaceHolder.Callback {

    public BattleFieldFragmentController(Context context) {
        super(context);
    }

    public BattleFieldFragmentController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BattleFieldFragmentController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void startThread();

    public abstract void stopThread();

    public abstract void init(String levelToLoad);

    public abstract void setImageViewsForCoordinates(ImageView letters, ImageView numbers);

    public abstract boolean saveDefaultFleet(String levelToSave);

    public abstract void setLevel(String levelToSet);
}
