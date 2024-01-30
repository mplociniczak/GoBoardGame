package org.client.calculatePoints;

import org.constants.ConstantVariables;
import org.server.gameLogic.Stone;
import org.server.gameLogic.StoneColor;

import javax.swing.*;

public class ScoreCalculator implements ConstantVariables {
    private Stone[][] fields;
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
        this.fields = fields;
        ff = new FloodFill(fields);
    }

    public void updateScoreLabels() {
        SwingUtilities.invokeLater(() -> {

            int blackTerritory = ff.calculateTerritory(StoneColor.BLACK, StoneColor.WHITE);
            int whiteTerritory = ff.calculateTerritory(StoneColor.WHITE, StoneColor.BLACK);

            //Set<Point> deadStonesBlack = identifyDeadStones(StoneColor.BLACK);
            //Set<Point> deadStonesWhite = identifyDeadStones(StoneColor.WHITE);

            //int damePoints = countNeutralPoints();

            int blackScore = 100;
            int whiteScore = 100;
            //int blackScore = blackTerritory - deadStonesBlack.size() + damePoints;
            //int whiteScore = whiteTerritory - deadStonesWhite.size() + damePoints;

            int blackPrisoners = countPrisoners(StoneColor.BLACK);
            int whitePrisoners = countPrisoners(StoneColor.WHITE);

            //int blackScore = blackTerritory + blackPrisoners;
            //int whiteScore = whiteTerritory + whitePrisoners;

            // Update JLabels with the calculated scores
            ter_B.setText(String.valueOf(blackTerritory));
            ter_W.setText(String.valueOf(whiteTerritory));
            pris_B.setText(String.valueOf(blackPrisoners));
            pris_W.setText(String.valueOf(whitePrisoners));
            scr_B.setText(String.valueOf(blackScore));
            scr_W.setText(String.valueOf(whiteScore));
        });
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
