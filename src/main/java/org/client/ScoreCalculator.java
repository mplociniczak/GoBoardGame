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
    private Stone[][] fields;
    private JLabel ter_B;
    private JLabel ter_W;
    private JLabel pris_B;
    private JLabel pris_W;
    private JLabel scr_B;
    private JLabel scr_W;

    public ScoreCalculator(Stone[][] fields, JLabel ter_B, JLabel ter_W, JLabel pris_B, JLabel pris_W, JLabel scr_B, JLabel scr_W) {
        this.fields = fields;
        this.ter_B = ter_B;
        this.ter_W = ter_W;
        this.pris_B = pris_B;
        this.pris_W = pris_W;
        this.scr_B = scr_B;
        this.scr_W = scr_W;
    }

    public void updateScoreLabels() {
        SwingUtilities.invokeLater(() -> {

            int blackTerritory = calculateTerritory(StoneColor.BLACK, StoneColor.WHITE);
            int whiteTerritory = calculateTerritory(StoneColor.WHITE, StoneColor.BLACK);

            int blackPrisoners = countPrisoners(StoneColor.BLACK);
            int whitePrisoners = countPrisoners(StoneColor.WHITE);

            int blackScore = blackTerritory + blackPrisoners;
            int whiteScore = whiteTerritory + whitePrisoners;

            // Update JLabels with the calculated scores
            ter_B.setText(String.valueOf(blackTerritory));
            ter_W.setText(String.valueOf(whiteTerritory));
            pris_B.setText(String.valueOf(blackPrisoners));
            pris_W.setText(String.valueOf(whitePrisoners));
            scr_B.setText(String.valueOf(blackScore));
            scr_W.setText(String.valueOf(whiteScore));
        });
    }

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
}
