package org.client;

import org.server.*;

import javax.swing.*;

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
            int blackTerritory = calculateTerritory(StoneColor.BLACK);
            int whiteTerritory = calculateTerritory(StoneColor.WHITE);

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

    public int calculateTerritory(StoneColor color) {
        int territoryCount = 0;

        // Create a 2D array to track visited positions
        boolean[][] visited = new boolean[19][19];

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (fields[i][j].getState() == IntersectionState.EMPTY && !visited[i][j]) {
                    // Perform DFS to count territory
                    int emptyTerritorySize = dfs(i, j, visited, color);
                    territoryCount += emptyTerritorySize;
                }
            }
        }

        return territoryCount;
    }

    private int dfs(int x, int y, boolean[][] visited, StoneColor color) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19 || visited[x][y] || fields[x][y].getState() != IntersectionState.EMPTY) {
            return 0;
        }

        visited[x][y] = true;

        int emptyTerritorySize = 1;

        emptyTerritorySize += dfs(x + 1, y, visited, color);
        emptyTerritorySize += dfs(x - 1, y, visited, color);
        emptyTerritorySize += dfs(x, y + 1, visited, color);
        emptyTerritorySize += dfs(x, y - 1, visited, color);

        return emptyTerritorySize;
    }


    private int countPrisoners(StoneColor color) {
        int prisonerCount = 0;

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (fields[i][j].getColor() == color && fields[i][j].getColor().equals(StoneColor.REMOVED)) {
                    // Increment the prisoner count for stones of the specified color marked as prisoners
                    prisonerCount++;
                }
            }
        }

        return prisonerCount;
    }
}
