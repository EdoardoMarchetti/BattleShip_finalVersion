package com.edomar.battleship.logic;

import android.os.Parcel;
import android.os.Parcelable;

public class GameState implements IGameState, Parcelable {


    /**
        Methods for Parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(playerName);
    }

    public static final Parcelable.Creator<GameState> CREATOR =
            new Parcelable.Creator<GameState>(){
        public GameState createFromParcel(Parcel in){
            return new GameState(in);
        }

        @Override
        public GameState[] newArray(int i) {
            return new GameState[0];
        }
    };

    /**
     * Interface to create constant variable
     * to pass object through activities
     */
    public interface Keys{
        String NAME_PLAYER = "name player";
    }

    //end of all utility methods and interfaces





    public String playerName;


    public GameState(final String playerName) {
        this.playerName = playerName;
    }

    private GameState(Parcel in){
        this.playerName = in.readString();
    }

    /**
     * Builder class
     */
    public static class Builder {
        private String mName;
        /*
        add other variables here
         */

        private Builder (final String name){
            this.mName = name;
        }

        public static Builder create (final String playerName){
            return new Builder(playerName);
        }
        /*
        add other builder methods for new variables
         */

        public GameState build(){
            return new GameState(mName);
        }
    }
    //end builder class


    //GET&SET
    private void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
    }
}
