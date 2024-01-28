package org.client;

import org.server.gameLogic.IntersectionState;
import org.server.gameLogic.Stone;
import org.server.gameLogic.StoneColor;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.server.gameLogic.Board.size;

class ScoreCalculator {
    //private Stone[][] fields;
    private JLabel ter_B;
    private JLabel ter_W;
    private JLabel pris_B;
    private JLabel pris_W;
    private JLabel scr_B;
    private JLabel scr_W;
    FloodFill ff;

    public ScoreCalculator(Stone[][] fields, JLabel ter_B, JLabel ter_W, JLabel pris_B, JLabel pris_W, JLabel scr_B, JLabel scr_W) {
        //this.fields = fields;
        this.ter_B = ter_B;
        this.ter_W = ter_W;
        this.pris_B = pris_B;
        this.pris_W = pris_W;
        this.scr_B = scr_B;
        this.scr_W = scr_W;
        ff = new FloodFill(fields);
    }

    public void updateScoreLabels() {
        SwingUtilities.invokeLater(() -> {

            int blackTerritory = calculateTerritory(StoneColor.BLACK, StoneColor.WHITE);
            int whiteTerritory = calculateTerritory(StoneColor.WHITE, StoneColor.BLACK);

            //Set<Point> deadStonesBlack = identifyDeadStones(StoneColor.BLACK);
            //Set<Point> deadStonesWhite = identifyDeadStones(StoneColor.WHITE);

            //int damePoints = countNeutralPoints();

            int blackScore = 100;
            int whiteScore = 100;
            //int blackScore = blackTerritory - deadStonesBlack.size() + damePoints;
            //int whiteScore = whiteTerritory - deadStonesWhite.size() + damePoints;

            //int blackPrisoners = countPrisoners(StoneColor.BLACK);
            //int whitePrisoners = countPrisoners(StoneColor.WHITE);

            //int blackScore = blackTerritory + blackPrisoners;
            //int whiteScore = whiteTerritory + whitePrisoners;

            // Update JLabels with the calculated scores
            ter_B.setText(String.valueOf(blackTerritory));
            ter_W.setText(String.valueOf(whiteTerritory));
            //pris_B.setText(String.valueOf(blackPrisoners));
            //pris_W.setText(String.valueOf(whitePrisoners));
            scr_B.setText(String.valueOf(blackScore));
            scr_W.setText(String.valueOf(whiteScore));
        });
    }
    private int calculateTerritory(StoneColor color, StoneColor enemyColor) {
        int territoryCount = 0;
        //boolean[][] visited = new boolean[size][size];

        Set<Point> visited = new HashSet<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    //Set<Point> territory = new HashSet<>();
                    //ff.floodFill(territory, visited, i, j, enemyColor);
                    /*
                    if (isTerritory(i, j, color, enemyColor, visited, territory)) {
                        territoryCount += territory.size();
                    }
                    */
                    //territoryCount += territory.size();
            }
        }
        System.out.println("Enemy points: " + territoryCount + " " + enemyColor);
        return territoryCount;
    }
    /*
    private boolean isTerritory(int x, int y, StoneColor color, StoneColor enemyColor, boolean[][] visited, Set<Point> territory) {
        if (x < 0 || x >= size || y < 0 || y >= size) return true;
        if (visited[x][y]) return true;

        visited[x][y] = true;

        if (fields[x][y].getState().equals(IntersectionState.OCCUPIED)) {
            return fields[x][y].getColor().equals(color);
        }

        territory.add(new Point(x, y));

        return isTerritory(x + 1, y, color, enemyColor, visited, territory) &&
                isTerritory(x - 1, y, color, enemyColor, visited, territory) &&
                isTerritory(x, y + 1, color, enemyColor, visited, territory) &&
                isTerritory(x, y - 1, color, enemyColor, visited, territory);
    }

    private Set<Point> identifyDeadStones(StoneColor color) {
        Set<Point> deadStones = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fields[i][j].getState().equals(IntersectionState.OCCUPIED) &&
                        fields[i][j].getColor().equals(color) &&
                        isDeadStone(i, j, color)) {
                    deadStones.add(new Point(i, j));
                }
            }
        }
        return deadStones;
    }

    private boolean isDeadStone(int x, int y, StoneColor color) {
        // Jeśli kamień został już oznaczony jako usunięty, uznaj go za martwy
        if (fields[x][y].getColor().equals(StoneColor.REMOVED)) {
            return true;
        }

        boolean[][] visited = new boolean[size][size];
        return !hasLiberty(x, y, color, visited);
    }


    private boolean hasLiberty(int x, int y, StoneColor color, boolean[][] visited) {
        if (x < 0 || x >= size || y < 0 || y >= size) return false;
        if (visited[x][y]) return false;

        visited[x][y] = true;

        if (fields[x][y].getState().equals(IntersectionState.EMPTY)) return true;
        if (fields[x][y].getState().equals(IntersectionState.OCCUPIED) && !fields[x][y].getColor().equals(color)) return false;

        return hasLiberty(x + 1, y, color, visited) ||
                hasLiberty(x - 1, y, color, visited) ||
                hasLiberty(x, y + 1, color, visited) ||
                hasLiberty(x, y - 1, color, visited);
    }

    private int countNeutralPoints() {
        int neutralPointCount = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fields[i][j].getState().equals(IntersectionState.EMPTY) && isNeutralPoint(i, j)) {
                    neutralPointCount++;
                }
            }
        }
        return neutralPointCount;
    }

    private boolean isNeutralPoint(int x, int y) {
        boolean nextToBlack = false;
        boolean nextToWhite = false;

        if (x > 0 && fields[x-1][y].getState().equals(IntersectionState.OCCUPIED)) {
            if (fields[x-1][y].getColor().equals(StoneColor.BLACK)) nextToBlack = true;
            if (fields[x-1][y].getColor().equals(StoneColor.WHITE)) nextToWhite = true;
        }
        if (x < size-1 && fields[x+1][y].getState().equals(IntersectionState.OCCUPIED)) {
            if (fields[x+1][y].getColor().equals(StoneColor.BLACK)) nextToBlack = true;
            if (fields[x+1][y].getColor().equals(StoneColor.WHITE)) nextToWhite = true;
        }
        if (y > 0 && fields[x][y-1].getState().equals(IntersectionState.OCCUPIED)) {
            if (fields[x][y-1].getColor().equals(StoneColor.BLACK)) nextToBlack = true;
            if (fields[x][y-1].getColor().equals(StoneColor.WHITE)) nextToWhite = true;
        }
        if (y < size-1 && fields[x][y+1].getState().equals(IntersectionState.OCCUPIED)) {
            if (fields[x][y+1].getColor().equals(StoneColor.BLACK)) nextToBlack = true;
            if (fields[x][y+1].getColor().equals(StoneColor.WHITE)) nextToWhite = true;
        }

        return nextToBlack && nextToWhite;
    }
*/









/*
    public int calculateTerritory(StoneColor color, StoneColor enemyColor) {
        int territoryCount = 0;
        Set<Point> surrounded = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fields[i][j].getColor().equals(color)) {
                    // Perform DFS to count territorY
                    if(checkIfSurrounded(i, j, surrounded, new HashSet<>(), enemyColor)){
                        canBeSurrounded = true;
                        break;
                    } else {
                        canBeSurrounded = true;
                        surrounded.clear();
                    }
                }
            }
        }

        return surrounded.size();
    }
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    boolean canBeSurrounded = true;
    private boolean checkIfSurrounded(int X, int Y, Set<Point> surrounded, Set<Point> visited, StoneColor enemyColor) {

        visited.add(new Point(X , Y));

        if(isValidCoordinate(X, Y) && fields[X][Y].getColor().equals(enemyColor)) {
            canBeSurrounded = false;
            return false;
        }

        if(isValidCoordinate(X+1, Y) && fields[X+1][Y].getState().equals(IntersectionState.EMPTY) && !visited.contains(new Point(X+1, Y)) && canBeSurrounded) {
             if(checkIfSurrounded(X+1, Y, surrounded, visited, enemyColor))
                 surrounded.add(new Point(X+1, Y));
        }
        if(isValidCoordinate(X-1, Y) && fields[X-1][Y].getState().equals(IntersectionState.EMPTY) && !visited.contains(new Point(X-1, Y)) && canBeSurrounded) {
            if(checkIfSurrounded(X-1, Y, surrounded, visited, enemyColor))
                surrounded.add(new Point(X-1, Y));
        }
        if(isValidCoordinate(X, Y+1) && fields[X][Y+1].getState().equals(IntersectionState.EMPTY) && !visited.contains(new Point(X, Y+1)) && canBeSurrounded) {
            if(checkIfSurrounded(X, Y+1, surrounded, visited, enemyColor))
                surrounded.add(new Point(X, Y+1));
        }
        if(isValidCoordinate(X, Y-1) && fields[X][Y-1].getState().equals(IntersectionState.EMPTY) && !visited.contains(new Point(X, Y-1)) && canBeSurrounded) {
            if(checkIfSurrounded(X, Y-1, surrounded, visited, enemyColor))
                surrounded.add(new Point(X, Y-1));
        }

        return canBeSurrounded;
    }


    private int countPrisoners(StoneColor color) {
        int prisonerCount = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //always false
                if (fields[i][j].getColor().equals(StoneColor.REMOVED)) {
                    // Increment the prisoner count for stones of the specified color marked as prisoners
                    prisonerCount++;
                }
            }
        }

        return prisonerCount;
    }

 */
}
