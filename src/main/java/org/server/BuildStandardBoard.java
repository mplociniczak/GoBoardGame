package org.server;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The BuildStandardBoard class is responsible for implementing the BoardBuilder interface
 * and providing standard functionality for checking the state of intersections on the Go board.
 */
public class BuildStandardBoard extends BoardBuilder{
    private boolean isStoneRemovedFlag = false;
    private Set<String> previousBoardStates = new HashSet<>();

    /**
     * Checks whether the intersection at the specified coordinates is empty.
     *
     * @param X The X-coordinate of the intersection.
     * @param Y The Y-coordinate of the intersection.
     * @return True if the intersection is empty, false otherwise.
     */
    @Override
    public boolean isIntersectionEmpty(int X, int Y) {
        return Board.fields[X][Y].getState().equals(IntersectionState.EMPTY);
    }

    /**
     * Checks if a stone at the specified coordinates has breathing space (liberties).
     *
     * @param X The X-coordinate of the stone.
     * @param Y The Y-coordinate of the stone.
     * @return True if the stone has breathing space, false otherwise.
     */
    @Override
    public boolean isStoneBreathing(int X, int Y) {
        int size = Board.size;

        boolean leftEmpty = (X - 1 >= 0) && Board.fields[X - 1][Y].getState().equals(IntersectionState.EMPTY);
        boolean rightEmpty = (X + 1 < size) && Board.fields[X + 1][Y].getState().equals(IntersectionState.EMPTY);
        boolean upEmpty = (Y - 1 >= 0) && Board.fields[X][Y - 1].getState().equals(IntersectionState.EMPTY);
        boolean downEmpty = (Y + 1 < size) && Board.fields[X][Y + 1].getState().equals(IntersectionState.EMPTY);

        return leftEmpty || rightEmpty || upEmpty || downEmpty || isStoneRemovedFlag;
    }

    /**
     * Checks if a move violates the Ko rule by repeating a previous board state.
     *
     * @return True if the move violates Ko, false otherwise.
     */
    @Override
    public boolean isKoViolation() {
        StringBuilder boardStateBuilder = new StringBuilder();
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                boardStateBuilder.append(Board.fields[i][j].getState().toString());
            }
        }
        return !previousBoardStates.add(boardStateBuilder.toString());
    }

    /**
     * Checks if the given coordinates are valid on the board.
     *
     * @param x The X-coordinate.
     * @param y The Y-coordinate.
     * @return True if the coordinates are valid, false otherwise.
     */
    @Override
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < Board.size && y >= 0 && y < Board.size;
    }

    /**
     * Searches for and removes adjacent enemy stones at the specified coordinates.
     *
     * @param X          The X-coordinate to start searching from.
     * @param Y          The Y-coordinate to start searching from.
     * @param enemyColor The color of the enemy stones.
     * @param allyColor  The color of the ally stones.
     */
    @Override
    public void searchForAdjacentEnemyStones(int X, int Y, StoneColor enemyColor, StoneColor allyColor) {
        stoneRemover(X+1, Y, enemyColor, allyColor);
        stoneRemover(X-1, Y, enemyColor, allyColor);
        stoneRemover(X, Y+1, enemyColor, allyColor);
        stoneRemover(X, Y-1, enemyColor, allyColor);
    }

    /**
     * Removes stones at the specified coordinates if they are surrounded.
     *
     * @param X          The X-coordinate of the stone to remove.
     * @param Y          The Y-coordinate of the stone to remove.
     * @param enemyColor The color of the enemy stones.
     * @param allyColor  The color of the ally stones.
     */
    @Override
    public void stoneRemover(int X, int Y, StoneColor enemyColor, StoneColor allyColor) {
        Set<Point> surroundedStones = new HashSet<>();
        Set<Point> visited = new HashSet<>();

        if(isValidCoordinate(X, Y) && Board.fields[X][Y].getColor().equals(enemyColor)) {
            if(checkIfSurrounded(X, Y, allyColor, surroundedStones, visited)){
                for(Point p : surroundedStones) {
                    Board.fields[p.x][p.y].removeStone();
                }
                isStoneRemovedFlag = true;
            }
        }
    }


    /**
     * Checks if stones at the specified coordinates are surrounded by ally stones.
     *
     * @param X          The X-coordinate to start checking from.
     * @param Y          The Y-coordinate to start checking from.
     * @param enemyColor The color of the enemy stones.
     * @param surroundedStones The set to store surrounded stones.
     * @param visited    The set to store visited coordinates.
     * @return True if the stones are surrounded, false otherwise.
     */
    @Override
    public boolean checkIfSurrounded(int X, int Y, StoneColor enemyColor, Set<Point> surroundedStones, Set<Point> visited) {
        boolean isSurrounded;

        visited.add(new Point(X , Y));
        isStoneRemovedFlag = false;

        if((isValidCoordinate(X+1, Y) && Board.fields[X+1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X-1, Y) && Board.fields[X-1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y+1) && Board.fields[X][Y+1].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y-1) && Board.fields[X][Y-1].getState().equals(IntersectionState.EMPTY))) {
            return false;
        }

        if(isValidCoordinate(X+1, Y) && !Board.fields[X+1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X+1, Y))) {
            isSurrounded = checkIfSurrounded(X+1, Y, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X-1, Y) && !Board.fields[X-1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X-1, Y))) {
            isSurrounded = checkIfSurrounded(X-1, Y, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X, Y+1) && !Board.fields[X][Y+1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y+1))) {
            isSurrounded = checkIfSurrounded(X, Y+1, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X, Y-1) && !Board.fields[X][Y-1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y-1))) {
            isSurrounded = checkIfSurrounded(X, Y-1, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }

        surroundedStones.add(new Point(X, Y));

        return true;
    }
}
