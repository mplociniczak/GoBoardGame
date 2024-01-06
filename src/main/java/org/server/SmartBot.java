package org.server;

import java.awt.*;

public class SmartBot {

    public Point makeMove() {
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
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

    public Point getRandomMove() {
        // Dla uproszczenia, bot wybierze losowe wolne pole
        while (true) {
            int x = (int) (Math.random() * Board.size);
            int y = (int) (Math.random() * Board.size);

            if (isValidCoordinate(x, y) && Board.fields[x][y].getState().equals(IntersectionState.EMPTY)) {
                return new Point(x, y);
            }
        }
    }

    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < Board.size && y >= 0 && y < Board.size;
    }
}

