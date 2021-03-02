package com.edomar.battleship.logic;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class AlternativeArtificialPlayer {

    private static final String TAG = "AlternativeArtificialPl";

    List<String> explored;
    List<String> frontier;
    List<String> unexplored;
    List<String> positionShip;


    Random random;

    public AlternativeArtificialPlayer(){

        explored = new ArrayList<String>();
        frontier = new ArrayList<String>();
        unexplored = new ArrayList<String>();
        positionShip = new ArrayList<String>();

        random = new Random();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10 ; j++) {
                unexplored.add(i+","+j);
            }
        }
        Log.d(TAG, "AlternativeArtificialPlayer: unexplored.size() ="+unexplored.size());
    }

    public Point shot(){
        Log.d(TAG, "shot: frontier.size()= "+frontier.size());
        if(frontier.isEmpty()){
            int indice = random.nextInt(unexplored.size());
            String[] coordinates = unexplored.remove(indice).split(",");
            Log.d(TAG, "shot: coordinates "+ coordinates[0]+","+coordinates[1]);
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            return new Point(row, column);
        }else {
            int indice = random.nextInt(frontier.size());
            stampaFrontier();
            String[] coordinates = frontier.get(indice).split(",");

            /*while(explored.contains(frontier.get(indice))){
                frontier.remove(indice);
                Log.d(TAG, "shot: frontier.size()= "+frontier.size());
                indice = random.nextInt(frontier.size());
                coordinates = frontier.get(indice).split(",");
            }*/
            Log.d(TAG, "shot: coordinates "+ coordinates[0]+","+coordinates[1]);
            frontier.remove(indice);

            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            return new Point(row, column);
        }
    }

    private void stampaFrontier() {
        Log.d(TAG, "stampaFrontier: "+frontier.toString());
    }

    public void updateList(int row, int column, boolean good){
        String coordinates = row+","+column;
        explored.add(coordinates);
        unexplored.remove(row+","+column);

        Log.d(TAG, "updateList: aggiorno per "+row+"column");
        if(good) {
            positionShip.add(coordinates);
            Log.d(TAG, "updateList: aggiungo i frontier");
            if (row - 1 >= 0 && !explored.contains(row-1+","+column)) {
                frontier.add((row - 1) + "," + column);
            }

            if (row + 1 < 10 && !explored.contains(row+1+","+column)) {
                frontier.add((row + 1) + "," + column);
            }

            if (column - 1 >= 0 && !explored.contains(row+","+(column-1))) {
                frontier.add(row + "," + (column - 1));
            }

            if (column + 1 < 10 && !explored.contains(row+","+column+1)) {
                frontier.add(row + "," + (column + 1));
            }
        }
    }

    public void updateListForDistance(int row, int column){
        String coordinates = row+","+column;
        List<String> currentShip = new ArrayList<String>();
        currentShip.add(coordinates);
        stampaCurrentShip(currentShip);
        stampaPositionShip();

        int nextRow = -1;
        int nextColumn = -1;
        boolean firstCycle = true;
        int currentRow = 0;
        int currentColumn = 0;

        //La casella (row,column) l'ho già aggiornato in updateList
        while(!(nextRow==currentRow && nextColumn==currentColumn)) {
            if(firstCycle){
                currentRow = row;
                currentColumn = column;
                firstCycle = false;
            }else {
                currentRow = nextRow;
                currentColumn = nextColumn;
            }
            Log.d("Liste", "updateListForDistance coordinate inziali: "+currentRow+" ,"+currentColumn);
            for (int i = currentRow - 1; i <= currentRow + 1; i++) {
                for (int j = currentColumn - 1; j <= currentColumn + 1; j++) {

                    if (!(i == currentRow && j == currentColumn)) {
                        Log.d("Liste", "updateListForDistance: "+i + "," + j);
                        if (positionShip.contains(i + "," + j) && !currentShip.contains(i + "," + j)) {
                            nextRow = i;
                            nextColumn = j;
                            currentShip.add(i + "," + j);
                            stampaCurrentShip(currentShip);
                        }

                        explored.add(i + "," + j);
                        if (frontier.contains(i + "," + j)) {
                            frontier.remove(i + "," + j);
                        }
                        if (unexplored.remove(i + "," + j)) {
                            unexplored.remove(i + "," + j);
                        }

                    }

                }
            }
            Log.d("Liste", "updateListForDistance: "+nextRow + "," + nextColumn);
            Log.d("Liste", "updateListForDistance: "+(nextRow != currentRow) + "," + (nextColumn != currentColumn));
        }



    }

    private void stampaCurrentShip(List<String> currentShip) {
        Log.d("Liste", "stampaPositionShip: "+ currentShip.toString());
    }

    private void stampaPositionShip() {
        Log.d("Liste", "stampaPositionShip: "+ positionShip.toString());
    }
}
