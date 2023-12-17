package org.server;

public class Board {
    FieldState[][] fields = new FieldState[19][19];
    public Board(){
        for(int i = 0; i < 19; i++) {
            for(int j = 0; j < 19; j++) {
                fields[i][j] = FieldState.EMPTY;
            }
        }
    }
    public boolean checkIfMoveCorrect(int X, int Y){
        //TODO: implement
        return true;
    }

    public void placeBlackStone(int X, int Y){
        fields[X][Y] = FieldState.BLACK;
    }
    public void placeWhiteStone(int X, int Y){
        fields[X][Y] = FieldState.WHITE;
    }
}

