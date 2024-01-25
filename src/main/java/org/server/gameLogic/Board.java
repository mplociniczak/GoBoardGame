package org.server.gameLogic;

import org.server.Server;

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

    public void placeStone(int X, int Y, StoneColor color, StoneColor enemyColor) {
        fields[X][Y].placeStone(color);

        buildBoard.searchForAdjacentEnemyStones(X, Y, enemyColor, color);

        if(!buildBoard.isStoneBreathing(X, Y) || buildBoard.isKoViolation()) {
            fields[X][Y].deleteStone();

        }
    }
}

