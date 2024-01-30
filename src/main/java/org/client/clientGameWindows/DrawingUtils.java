package org.client.clientGameWindows;

import org.constants.ConstantVariables;
import org.server.gameLogic.StoneColor;

import javax.swing.*;

public class DrawingUtils implements Utils, ConstantVariables {

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
        // Pobierz centralny kwadrat, który znajduje się na przecięciu czterech sąsiadujących kwadratów
        JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(Y * size + X);

        // Usunięcie wcześniejszych komponentów z centralnego kwadratu
        centralSquare.removeAll();
        //squareToPaintRockOn.removeAll();

        // Oblicz położenie do narysowania koła na środku przecięcia
        int tileSize = gameBoardPanel.getWidth() / size;
        int centerX = X * tileSize + tileSize / 2;
        int centerY = Y * tileSize + tileSize / 2;

        // Dodanie nowego komponentu reprezentującego kamień jako okrąg na środku przecięcia
        StoneComponent stoneComponent = new StoneComponent(color);
        int componentSize = stoneComponent.getPreferredSize().width;

        // Ustawienie rozmiaru kamienia
        stoneComponent.setSize(componentSize, componentSize);

        // Ustawienie pozycji kamienia na środku przecięcia
        int componentX = centerX - componentSize / 2;
        int componentY = centerY - componentSize / 2;

        stoneComponent.setBounds(componentX, componentY, componentSize, componentSize);
        centralSquare.add(stoneComponent);

        centralSquare.revalidate();
        centralSquare.repaint();

    }
}
