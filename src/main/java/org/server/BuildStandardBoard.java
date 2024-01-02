package org.server;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BuildStandardBoard extends BoardBuilder{
    private boolean isStoneRemovedFlag = false;
    private Set<String> previousBoardStates = new HashSet<>();
    @Override
    public boolean isIntersectionEmpty(int X, int Y) {
        return Board.fields[X][Y].getState().equals(IntersectionState.EMPTY);
    }

    @Override
    public boolean isStoneBreathing(int X, int Y) {
        return Board.fields[X + 1][Y].getState().equals(IntersectionState.EMPTY) ||
                Board.fields[X - 1][Y].getState().equals(IntersectionState.EMPTY) ||
                Board.fields[X][Y + 1].getState().equals(IntersectionState.EMPTY) ||
                Board.fields[X][Y - 1].getState().equals(IntersectionState.EMPTY) ||
                isStoneRemovedFlag;
    }

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

    @Override
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < Board.size && y >= 0 && y < Board.size;
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

        if(isValidCoordinate(X, Y) && Board.fields[X][Y].getColor().equals(enemyColor)) {
            if(checkIfSurrounded(X, Y, allyColor, surroundedStones, visited)){
                for(Point p : surroundedStones) {
                    Board.fields[p.x][p.y].removeStone();
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
