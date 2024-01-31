package org.client.calculatePoints;

import org.server.gameLogic.Stone;

import javax.swing.*;

public class ScoreCalculator{
    private JLabel ter_B;
    private JLabel ter_W;
    private JLabel pris_B;
    private JLabel pris_W;
    private JLabel scr_B;
    private JLabel scr_W;
    FloodFill ff;

    public ScoreCalculator(Stone[][] fields, JLabel ter_B, JLabel ter_W, JLabel pris_B, JLabel pris_W, JLabel scr_B, JLabel scr_W) {
        this.ter_B = ter_B;
        this.ter_W = ter_W;
        this.pris_B = pris_B;
        this.pris_W = pris_W;
        this.scr_B = scr_B;
        this.scr_W = scr_W;

        ff = new FloodFill(fields);
    }

    public void updateScoreLabels(int whiteStonesRemoved, int blackStonesRemoved) {

        SwingUtilities.invokeLater(() -> {

            int[] results = new int[2];

            ff.countTerritory(results);

            int blackTerritory = results[0];
            int whiteTerritory = results[1];

            int blackScore = blackTerritory + blackStonesRemoved;
            int whiteScore = whiteTerritory + whiteStonesRemoved;

            // Update JLabels with the calculated scores
            ter_B.setText(String.valueOf(blackTerritory));
            ter_W.setText(String.valueOf(whiteTerritory));
            pris_B.setText(String.valueOf(blackStonesRemoved));
            pris_W.setText(String.valueOf(whiteStonesRemoved));
            scr_B.setText(String.valueOf(blackScore));
            scr_W.setText(String.valueOf(whiteScore));
        });
    }
}
