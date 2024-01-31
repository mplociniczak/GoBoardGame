package org.client.calculatePoints;

import org.server.gameLogic.Stone;
import org.server.gameLogic.StoneColor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.constants.ConstantVariables.*;

public class FloodFill {
    Stone[][] fields;
    public FloodFill(Stone[][] fields) {
        this.fields = fields;
    }
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public void countTerritory(int[] results) {

        Set<Point> visited = new HashSet<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                if (!visited.contains(new Point(x, y))) {

                    Set<Point> territory = new HashSet<>();
                    Set<StoneColor> boundaryColors = new HashSet<>();

                    analyzeTerritory(x, y, visited, territory, boundaryColors);

                    if (boundaryColors.size() == 1) { // Terytorium należy do jednego gracza

                        if (boundaryColors.contains(StoneColor.BLACK)) {

                            results[0] += territory.size();

                        } else {

                            results[1] += territory.size();

                        }
                    }
                }
            }
        }
    }

    private void analyzeTerritory(int x, int y, Set<Point> visited, Set<Point> territory, Set<StoneColor> boundaryColors) {

        if (!isValidCoordinate(x, y) || visited.contains(new Point(x, y))) {
            return;
        }

        visited.add(new Point(x, y));

        if (fields[x][y].getColor().equals(StoneColor.N) || fields[x][y].getColor().equals(StoneColor.REMOVED)) {

            territory.add(new Point(x, y));

            for (int[] direction : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {

                int adjacentX = x + direction[0];
                int adjacentY = y + direction[1];

                if (isValidCoordinate(adjacentX, adjacentY)) {

                    if (fields[adjacentX][adjacentY].getColor().equals(StoneColor.N) || fields[adjacentX][adjacentY].getColor().equals(StoneColor.REMOVED)) {

                        analyzeTerritory(adjacentX, adjacentY, visited, territory, boundaryColors);

                    } else {

                        if (fields[adjacentX][adjacentY].getColor().equals(StoneColor.BLACK) || fields[adjacentX][adjacentY].getColor().equals(StoneColor.WHITE))
                            boundaryColors.add(fields[adjacentX][adjacentY].getColor());

                    }
                }
            }
        }
    }
}
