package org.server.gameLogic;

import static org.constants.ConstantVariables.*;

/**
 * The Board class represents the game board in a Go game. It contains methods for managing the board state,
 * placing stones, and converting the board state to a string representation.
 */
public class Board{

    /**
     * 2D array representing the intersections of the game board, each containing a Stone object.
     */
    public static Stone[][] fields;

    /**
     * BoardBuilder instance responsible for initializing the board with the standard configuration.
     */
    public BoardBuilder buildBoard;

    /**
     * Constructs a new Board object, initializing the game board and using a BuildStandardBoard object.
     */
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


    /**
     * Converts the current state of the board, along with the specified stone coordinates, to a StringBuilder.
     *
     * @param X The x-coordinate of the last placed stone.
     * @param Y The y-coordinate of the last placed stone.
     * @return StringBuilder containing the board state and the coordinates of the last placed stone.
     */
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

    /**
     * Places a stone on the game board at the specified coordinates and updates the board state accordingly.
     *
     * @param X           The x-coordinate where the stone is placed.
     * @param Y           The y-coordinate where the stone is placed.
     * @param color       The color of the stone being placed.
     * @param enemyColor  The color of the opponent's stones.
     */
    public void placeStone(int X, int Y, StoneColor color, StoneColor enemyColor) {
        fields[X][Y].placeStone(color);

        buildBoard.searchForAdjacentEnemyStones(X, Y, enemyColor, color);

        if(!buildBoard.isStoneBreathing(X, Y) || buildBoard.isKoViolation()) {
            fields[X][Y].deleteStone();
        }
    }
    boolean isMoveValid;
    public void placeStone(int[] coordinates, StoneColor color, StoneColor enemyColor) {

        isMoveValid = buildBoard.searchForAdjacentEnemyStones(coordinates[0], coordinates[1], color, enemyColor);

        if(!isMoveValid || !buildBoard.isStoneBreathing(coordinates[0], coordinates[1])) {
            coordinates[0] = errorCode;
            coordinates[1] = errorCode;
        }
    }
}

