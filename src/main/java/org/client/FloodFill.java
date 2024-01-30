package org.client;

import org.server.gameLogic.IntersectionState;
import org.server.gameLogic.Stone;
import org.server.gameLogic.StoneColor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.server.gameLogic.Board.size;

public class FloodFill {
    Stone[][] fields;
    public FloodFill(Stone[][] fields) {
        this.fields = fields;
    }
    public int calculateTerritory(StoneColor color, StoneColor enemyColor) {
        int territoryCount = 0;
        Set<Point> surrounded = new HashSet<>();
        Set<Point> visited = new HashSet<>();
        Set<Point> globalVisited = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                canBeSurrounded = true;

                if (fields[i][j].getState().equals(IntersectionState.EMPTY) && !globalVisited.contains(new Point(i, j))) {

                    checkIfSurrounded(i, j, surrounded, visited, globalVisited, enemyColor, color);

                    if(!canBeSurrounded) surrounded.clear();

                    visited.clear();

                    territoryCount += surrounded.size();

                }
            }
        }

        return territoryCount;
    }
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    boolean canBeSurrounded = true;
    private void checkIfSurrounded(int X, int Y, Set<Point> surrounded, Set<Point> visited, Set<Point> globalVisited, StoneColor enemyColor, StoneColor color) {

        if(!isValidCoordinate(X, Y) || !canBeSurrounded) return;

        if(fields[X][Y].getColor().equals(color)) return;

        if(fields[X][Y].getColor().equals(enemyColor)) {
            canBeSurrounded = false;
            return;
        }

        visited.add(new Point(X, Y));
        globalVisited.add(new Point(X, Y));

        if(!visited.contains(new Point(X+1, Y)) && canBeSurrounded)
            checkIfSurrounded(X+1, Y, surrounded, visited, globalVisited, enemyColor, color);

        if(!visited.contains(new Point(X-1, Y)) && canBeSurrounded)
            checkIfSurrounded(X-1, Y, surrounded, visited, globalVisited, enemyColor, color);

        if(!visited.contains(new Point(X, Y+1)) && canBeSurrounded)
            checkIfSurrounded(X, Y+1, surrounded, visited, globalVisited, enemyColor, color);

        if(!visited.contains(new Point(X, Y-1)) && canBeSurrounded)
            checkIfSurrounded(X, Y-1, surrounded, visited, globalVisited, enemyColor, color);

        if(fields[X][Y].getState().equals(IntersectionState.EMPTY) && canBeSurrounded) {
            surrounded.add(new Point(X, Y));
        }
    }
}
