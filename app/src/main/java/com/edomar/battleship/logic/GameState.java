package com.edomar.battleship.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.edomar.battleship.view.IActivityForGameState;

public class GameState implements IGameStateForActivity, IGameStateForBattlefield  {

    private static GameState sInstance;

    private int mGameState; // 0 = preMatch
                            // 1 = Match
                            // 2 = gameOver
    private int mTurnOf;

    private IActivityForGameState mActivityReference;

    /**Constructor**/
    private GameState(){
        mGameState = 0;
        mTurnOf = 1;
    }

    public static GameState getInstance() {
        if(sInstance == null){
            sInstance = new GameState();
        }
        return sInstance;
    }


    public void endGame(){
        sInstance = null;
    }


    /**IGameStateForActivity's methods**/
    @Override
    public void startMatch() {
        mGameState = 1;
    }


    /**IGameStateForBattlefield's methods**/
    @Override
    public int getGameState() {
        return mGameState;
    }

    @Override
    public void changeTurn() {
        if(mTurnOf == 1){
            mTurnOf = 2;
        }else if(mTurnOf == 2){
            mTurnOf = 1;
        }
    }


}
