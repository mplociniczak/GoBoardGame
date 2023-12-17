package org.server;

public class Board {
    Stone[][] fields;
    public Board(){
        fields = new Stone[19][19];
    }
    public boolean checkIfMoveCorrect(int X, int Y){
        //TODO: implement
        return true;
    }

    public void placeBlackStone(int X, int Y){
        fields[X][Y].placeStone(StoneColor.BLACK);
    }
    public void placeWhiteStone(int X, int Y){
        fields[X][Y].placeStone(StoneColor.WHITE);
    }
}

