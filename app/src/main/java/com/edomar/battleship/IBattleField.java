package com.edomar.battleship;

import android.view.SurfaceHolder;

public interface IBattleField extends Runnable,
        SurfaceHolder.Callback,
        BattleFieldBroadcaster,
        AmmoSpawner {


}
