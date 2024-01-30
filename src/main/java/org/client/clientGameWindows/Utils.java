package org.client.clientGameWindows;

import org.server.gameLogic.StoneColor;

import javax.swing.*;

public interface Utils {
    void updateStoneGraphics(int X, int Y, StoneColor color, JPanel gameBoardPanel);
    void drawEmptyGameBoard(JPanel gameBoardPanel);
}
