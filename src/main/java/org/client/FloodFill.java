package org.client;

import org.server.gameLogic.IntersectionState;
import org.server.gameLogic.Stone;
import org.server.gameLogic.StoneColor;

import java.awt.*;
import java.util.Set;

public class FloodFill {
    Stone[][] fields;

    public FloodFill(Stone[][] fields) {
        this.fields = fields;
    }
    private void floodFillHelper(Set<Point> score, Set<Point> visited, int x, int y, StoneColor enemyColor){

        if(!fields[x][y].getColor().equals(enemyColor)) {

            visited.add(new Point(x, y));

            score.add(new Point(x, y));

            if(x >= 1) floodFillHelper(score, visited,x - 1, y, enemyColor);

            if(y >= 1) floodFillHelper(score, visited, x, y - 1, enemyColor);

            if(x + 1 < fields.length) floodFillHelper(score, visited, x + 1, y, enemyColor);

            if(y + 1 < fields[0].length) floodFillHelper(score, visited, x, y + 1, enemyColor);
        }
    }

    public void floodFill(Set<Point> score, Set<Point> visited, int x, int y, StoneColor enemyColor){
        if(fields[x][y].getState().equals(IntersectionState.EMPTY) && !visited.contains(new Point(x, y))) {
            floodFillHelper(score, visited, x, y, enemyColor);
        }
    }
}
