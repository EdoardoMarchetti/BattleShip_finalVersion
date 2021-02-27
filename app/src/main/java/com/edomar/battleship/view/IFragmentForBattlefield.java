package com.edomar.battleship.view;

public interface IFragmentForBattlefield {

    void notifyHit(int row, int column, boolean result);

    void notifyEndGame();
}
