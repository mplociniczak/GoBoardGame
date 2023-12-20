package org.server;

public class Board {
    Stone[][] fields;
    public Board(){
        fields = new Stone[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j] = new Stone();
            }
        }
    }
    public boolean checkIfMoveCorrect(int X, int Y){
        if(fields[X][Y].getState().equals(IntersectionState.EMPTY)){
            return true;
        } else
            return false;
        //TODO: implement
    }

    public void placeBlackStone(int X, int Y){
        fields[X][Y].placeStone(StoneColor.BLACK);
    }
    public void placeWhiteStone(int X, int Y){
        fields[X][Y].placeStone(StoneColor.WHITE);
    }
}

