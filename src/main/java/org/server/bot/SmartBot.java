package org.server.bot;

import org.server.gameLogic.Board;
import org.server.gameLogic.IntersectionState;
import org.server.gameLogic.StoneColor;

import static org.constants.ConstantVariables.*;

import java.awt.*;

/**
 * The SmartBot class represents a simple Go game bot that makes moves based on a specific strategy.
 * It searches for enemy stones on the board and attempts to place its stones in empty positions nearby.
 */
public class SmartBot {

    /**
     * Makes a move on the Go game board based on a specific strategy.
     *
     * @return The Point representing the coordinates of the move.
     */
    public Point makeMove() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Board.fields[i][j].getColor().equals(StoneColor.BLACK)) {
                    // Szukamy pustego miejsca dookoła kamienia przeciwnika
                    Point emptyNeighbor = findEmptyNeighbor(i, j);
                    if (emptyNeighbor != null) {
                        Board.fields[emptyNeighbor.x][emptyNeighbor.y].placeStone(StoneColor.WHITE);
                        return emptyNeighbor;
                    }
                }
            }
        }

        // Jeśli nie udało się znaleźć odpowiedniego ruchu, wykonujemy losowy ruch
        Point random = getRandomMove();
        Board.fields[random.x][random.y].placeStone(StoneColor.WHITE);
        return random;
    }

    /**
     * Finds an empty neighbor position around a specified coordinate.
     *
     * @param x The x-coordinate of the reference position.
     * @param y The y-coordinate of the reference position.
     * @return The Point representing the coordinates of the empty neighbor, or null if not found.
     */
    public Point findEmptyNeighbor(int x, int y) {
        Point[] neighbors = {
                new Point(x + 1, y),
                new Point(x - 1, y),
                new Point(x, y + 1),
                new Point(x, y - 1)
        };

        for (Point neighbor : neighbors) {
            if (isValidCoordinate(neighbor.x, neighbor.y) &&
                    Board.fields[neighbor.x][neighbor.y].getState().equals(IntersectionState.EMPTY) &&
                    !Board.fields[neighbor.x][neighbor.y].getColor().equals(StoneColor.REMOVED)) {
                return new Point(neighbor.x, neighbor.y);
            }
        }

        return null;
    }

    /**
     * Generates a random valid move on the Go game board.
     *
     * @return The Point representing the coordinates of the random move.
     */
    public Point getRandomMove() {
        // Dla uproszczenia, bot wybierze losowe wolne pole
        while (true) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);

            if (isValidCoordinate(x, y) && Board.fields[x][y].getState().equals(IntersectionState.EMPTY)) {
                return new Point(x, y);
            }
        }
    }

    /**
     * Checks if the specified coordinates are valid on the game board.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if the coordinates are valid, false otherwise.
     */
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }
}

