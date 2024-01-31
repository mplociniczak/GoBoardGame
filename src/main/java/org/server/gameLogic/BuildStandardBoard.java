package org.server.gameLogic;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.constants.ConstantVariables.*;
import static org.server.deepCopy.DeepCopy.deepCopy;
import static org.server.gameLogic.Board.*;

/**
 * The BuildStandardBoard class is responsible for extending the BoardBuilder abstract class
 * and providing standard functionality for checking the state of intersections on the Go board.
 */
public class BuildStandardBoard extends BoardBuilder {
    private boolean isStoneRemovedFlag = false;
    private final ArrayList<ArrayList<String>> previousStates = new ArrayList<>();
    private Stone[][] localFields = new Stone[size][size];
    @Override
    public void setStoneRemovedFlagToFalse() { isStoneRemovedFlag = false; }
    /**
     * Checks whether the intersection at the specified coordinates is empty.
     *
     * @param X The X-coordinate of the intersection.
     * @param Y The Y-coordinate of the intersection.
     * @return True if the intersection is empty, false otherwise.
     */
    @Override
    public boolean isIntersectionEmpty(int X, int Y) {
        return fields[X][Y].getState().equals(IntersectionState.EMPTY);
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

        boolean leftEmpty = (X - 1 >= 0) && fields[X - 1][Y].getState().equals(IntersectionState.EMPTY);
        boolean rightEmpty = (X + 1 < size) && fields[X + 1][Y].getState().equals(IntersectionState.EMPTY);
        boolean upEmpty = (Y - 1 >= 0) && fields[X][Y - 1].getState().equals(IntersectionState.EMPTY);
        boolean downEmpty = (Y + 1 < size) && fields[X][Y + 1].getState().equals(IntersectionState.EMPTY);

        return leftEmpty || rightEmpty || upEmpty || downEmpty || isStoneRemovedFlag;
    }

    /**
     * Checks if a move violates the Ko rule by repeating a previous board state.
     *
     * @return True if the move violates Ko, false otherwise.
     */
    @Override
    public boolean isKoViolation() {
        //StringBuilder boardStateBuilder = new StringBuilder();
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp.add(localFields[i][j].getState().toString());
            }
        }
        boolean contains = previousStates.contains(temp);

        if(!contains) {
            previousStates.add(temp);
        }

        if(previousStates.size() > 3) {
            previousStates.remove(previousStates.get(0));
        }

        return contains;
    }

    public boolean checkKoRule() {
        if(!isKoViolation()){
            fields = localFields.clone();
            return true;
        } else {
            return false;
        }
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
        return x >= 0 && x < size && y >= 0 && y < size;
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
    public boolean searchForAdjacentEnemyStones(int X, int Y, StoneColor allyColor, StoneColor enemyColor) {
        localFields = deepCopy(fields);
        localFields[X][Y].placeStone(allyColor);

        stoneRemover(X+1, Y, allyColor, enemyColor);
        stoneRemover(X-1, Y, allyColor, enemyColor);
        stoneRemover(X, Y+1, allyColor, enemyColor);
        stoneRemover(X, Y-1, allyColor, enemyColor);

        return checkKoRule();
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
    public void stoneRemover(int X, int Y, StoneColor allyColor, StoneColor enemyColor) {
        Set<Point> surroundedStones = new HashSet<>();
        Set<Point> visited = new HashSet<>();

        if(isValidCoordinate(X, Y) && localFields[X][Y].getColor().equals(enemyColor)) {
            if(checkIfSurrounded(X, Y, allyColor, surroundedStones, visited)){
                for(Point p : surroundedStones) {
                    localFields[p.x][p.y].removeStone();
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

        if((isValidCoordinate(X+1, Y) && localFields[X+1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X-1, Y) && localFields[X-1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y+1) && localFields[X][Y+1].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y-1) && localFields[X][Y-1].getState().equals(IntersectionState.EMPTY))) {
            return false;
        }

        if(isValidCoordinate(X+1, Y) && !localFields[X+1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X+1, Y))) {
            isSurrounded = checkIfSurrounded(X+1, Y, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X-1, Y) && !localFields[X-1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X-1, Y))) {
            isSurrounded = checkIfSurrounded(X-1, Y, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X, Y+1) && !localFields[X][Y+1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y+1))) {
            isSurrounded = checkIfSurrounded(X, Y+1, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X, Y-1) && !localFields[X][Y-1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y-1))) {
            isSurrounded = checkIfSurrounded(X, Y-1, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }

        surroundedStones.add(new Point(X, Y));

        return true;
    }
}
