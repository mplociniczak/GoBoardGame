package org.server;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Board {
    public static final int size = 19;
    public static Stone[][] fields;
    public BoardBuilder buildBoard;

    public Board(){
        fields = new Stone[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = new Stone();
            }
        }
        //previousBoardStates = new HashSet<>();
        buildBoard = new BuildStandardBoard();
    }

    public StringBuilder BoardToStringBuilderWithStoneColors(int X, int Y) {
        StringBuilder boardStateBuilder = new StringBuilder();

        boardStateBuilder.append(X);
        boardStateBuilder.append(" ");
        boardStateBuilder.append(Y);
        boardStateBuilder.append(" ");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardStateBuilder.append(fields[i][j].getColor().toString());
                boardStateBuilder.append(" ");
            }
        }
        return boardStateBuilder;
    }

    public void placeBlackStone(int X, int Y) {
        fields[X][Y].placeStone(StoneColor.BLACK);

        buildBoard.searchForAdjacentEnemyStones(X, Y, StoneColor.WHITE, StoneColor.BLACK);
        //searchForAdjacentEnemyStones(X, Y, StoneColor.WHITE, StoneColor.BLACK);

        if(!buildBoard.isStoneBreathing(X, Y)) {
            fields[X][Y].removeStone();
            X = -1;
            Y = -1;
        }
    }

    public void placeWhiteStone(int X, int Y) {
        fields[X][Y].placeStone(StoneColor.WHITE);

        buildBoard.searchForAdjacentEnemyStones(X, Y, StoneColor.BLACK, StoneColor.WHITE);

        if(!buildBoard.isStoneBreathing(X, Y)) {
            fields[X][Y].removeStone();
            X = -1;
            Y = -1;
        }
    }
}

