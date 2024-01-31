package org.client.clientGameWindows;

import static org.constants.ConstantVariables.*;
import org.server.gameLogic.StoneColor;

import javax.swing.*;
import java.awt.*;

public class DrawingUtils implements Utils{

    /**
     * Updates the graphical representation of a stone on the game board.
     *
     * @param X     The x-coordinate of the stone on the board.
     * @param Y     The y-coordinate of the stone on the board.
     * @param color The color of the stone.
     * @param gameBoardPanel panel on which everything is being drawn
     */
    @Override
    public void updateStoneGraphics(int X, int Y, StoneColor color, JPanel gameBoardPanel) {

        JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(Y * size + X);

        centralSquare.removeAll();

        int tileSize = gameBoardPanel.getWidth() / size;
        int centerX = X * tileSize + tileSize / 2;
        int centerY = Y * tileSize + tileSize / 2;

        StoneComponent stoneComponent = new StoneComponent(color);
        int componentSize = stoneComponent.getPreferredSize().width;

        stoneComponent.setSize(componentSize, componentSize);

        int componentX = centerX - componentSize / 2;
        int componentY = centerY - componentSize / 2;

        stoneComponent.setBounds(componentX, componentY, componentSize, componentSize);
        centralSquare.add(stoneComponent);

        centralSquare.revalidate();
        centralSquare.repaint();

    }

    @Override
    public void drawEmptyGameBoard(JPanel gameBoardPanel) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JPanel square = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {

                        super.paintComponent(g);

                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setColor(Color.BLACK);

                        g2d.setStroke(new BasicStroke(2.0f));
                        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

                        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
                    }
                };

                square.setBackground(Color.ORANGE);

                square.setPreferredSize(new Dimension(25, 25));

                gameBoardPanel.add(square);
            }
        }
    }
}
