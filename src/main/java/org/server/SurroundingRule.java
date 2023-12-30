package org.server;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import org.client.ClientWithBoard;

import static org.server.Board.fields;

public class SurroundingRule implements GoRuleFactory {
    @Override
    public boolean check(Board board, int X, int Y) {
        StoneColor enemyColor = (fields[X][Y].getColor() == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
        Set<Point> surroundedStones = new HashSet<>();
        Set<Point> visited = new HashSet<>();

        searchForAdjacentEnemyStones(X, Y, enemyColor, fields[X][Y].getColor());

        // Logika sprawdzania zasady otaczania
        if (!surroundedStones.isEmpty()) {
            for (Point p : surroundedStones) {
                System.out.println("Złapane kamienie przeciwnika na pozycji: " + p.x + ", " + p.y);
            }
            return true; // Zasada otaczania spełniona
        }

        return false;
    }

    private void searchForAdjacentEnemyStones(int X, int Y, StoneColor enemyColor, StoneColor allyColor) {
        Set<Point> visited = new HashSet<>();

        stoneRemover(X+1, Y, enemyColor, allyColor, visited);
        stoneRemover(X-1, Y, enemyColor, allyColor, visited);
        stoneRemover(X, Y+1, enemyColor, allyColor, visited);
        stoneRemover(X, Y-1, enemyColor, allyColor, visited);

    }

    private void stoneRemover(int X, int Y, StoneColor enemyColor, StoneColor allyColor, Set<Point> visited) {
        Set<Point> surroundedStones = new HashSet<>();

        if(isValidCoordinate(X, Y) && fields[X][Y].getColor().equals(enemyColor)) {
            if(checkIfSurrounded(X, Y, allyColor, surroundedStones, visited)){
                for(Point p : surroundedStones) {
                    fields[p.x][p.y].removeStone();
                    //usuwanie kamienia na planszy GUI
                    ClientWithBoard.removeStoneFromBoard(p.x, p.y);
                }
                surroundedStones.clear();
            }
        }
    }

    private boolean checkIfSurrounded(int X, int Y, StoneColor enemyColor, Set<Point> surroundedStones, Set<Point> visited) {
        boolean isSurrounded;

        visited.add(new Point(X , Y));

        if((isValidCoordinate(X+1, Y) && fields[X+1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X-1, Y) && fields[X-1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y+1) && fields[X][Y+1].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y-1) && fields[X][Y-1].getState().equals(IntersectionState.EMPTY))) {
            return false;
        }

        if(isValidCoordinate(X+1, Y) && fields[X+1][Y].getColor().equals(enemyColor)
                && isValidCoordinate(X-1, Y) && fields[X-1][Y].getColor().equals(enemyColor)
                && isValidCoordinate(X, Y+1) && fields[X][Y+1].getColor().equals(enemyColor)
                && isValidCoordinate(X, Y-1) && fields[X][Y-1].getColor().equals(enemyColor)) {
            isSurrounded = true;
        } else {
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
            else isSurrounded = true;
        }

        if(isSurrounded) {
            surroundedStones.add(new Point(X, Y));
        }

        return true;

    }

    public static boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < 19 && y >= 0 && y < 19;
    }
}
