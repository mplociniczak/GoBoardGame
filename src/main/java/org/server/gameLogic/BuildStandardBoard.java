package org.server.gameLogic;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.server.gameLogic.Board.*;

public class BuildStandardBoard extends BoardBuilder {
    private boolean isStoneRemovedFlag = false;
    private final Set<String> previousBoardStates = new HashSet<>();
    @Override
    public boolean isIntersectionEmpty(int X, int Y) {
        return fields[X][Y].getState().equals(IntersectionState.EMPTY);
    }

    @Override
    public boolean isStoneBreathing(int X, int Y) {
        return fields[X + 1][Y].getState().equals(IntersectionState.EMPTY) ||
                fields[X - 1][Y].getState().equals(IntersectionState.EMPTY) ||
                fields[X][Y + 1].getState().equals(IntersectionState.EMPTY) ||
                fields[X][Y - 1].getState().equals(IntersectionState.EMPTY) ||
                isStoneRemovedFlag;
        //TODO: handle edges, because now is index out of bounds exception
    }

    @Override
    public boolean isKoViolation() {
        StringBuilder boardStateBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardStateBuilder.append(fields[i][j].getState().toString());
            }
        }
        return !previousBoardStates.add(boardStateBuilder.toString());
    }

    @Override
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    @Override
    public void searchForAdjacentEnemyStones(int X, int Y, StoneColor enemyColor, StoneColor allyColor) {
        stoneRemover(X+1, Y, enemyColor, allyColor);
        stoneRemover(X-1, Y, enemyColor, allyColor);
        stoneRemover(X, Y+1, enemyColor, allyColor);
        stoneRemover(X, Y-1, enemyColor, allyColor);
    }

    @Override
    public void stoneRemover(int X, int Y, StoneColor enemyColor, StoneColor allyColor) {
        Set<Point> surroundedStones = new HashSet<>();
        Set<Point> visited = new HashSet<>();

        if(isValidCoordinate(X, Y) && fields[X][Y].getColor().equals(enemyColor)) {
            if(checkIfSurrounded(X, Y, allyColor, surroundedStones, visited)){
                for(Point p : surroundedStones) {
                    fields[p.x][p.y].removeStone();
                }
                isStoneRemovedFlag = true;
            }
        }
    }

    @Override
    public boolean checkIfSurrounded(int X, int Y, StoneColor enemyColor, Set<Point> surroundedStones, Set<Point> visited) {
        boolean isSurrounded;

        visited.add(new Point(X , Y));
        isStoneRemovedFlag = false;

        if((isValidCoordinate(X+1, Y) && fields[X+1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X-1, Y) && fields[X-1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y+1) && fields[X][Y+1].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y-1) && fields[X][Y-1].getState().equals(IntersectionState.EMPTY))) {
            return false;
        }

        if(isValidCoordinate(X+1, Y) && !fields[X+1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X+1, Y))) {
            isSurrounded = checkIfSurrounded(X+1, Y, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X-1, Y) && !fields[X-1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X-1, Y))) {
            isSurrounded = checkIfSurrounded(X-1, Y, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X, Y+1) && !fields[X][Y+1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y+1))) {
            isSurrounded = checkIfSurrounded(X, Y+1, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }
        if(isValidCoordinate(X, Y-1) && !fields[X][Y-1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y-1))) {
            isSurrounded = checkIfSurrounded(X, Y-1, enemyColor, surroundedStones, visited);
            if(!isSurrounded) return false;
        }

        surroundedStones.add(new Point(X, Y));

        return true;
    }
}
