package org.server;

import java.awt.*;

public class SmartBot {
    private Stone[][] fields;

    public SmartBot(Stone[][] fields) {
        this.fields = fields;
    }

    public Point makeMove() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (fields[i][j].getColor().equals(StoneColor.WHITE)) {
                    // Szukamy pustego miejsca dookoła kamienia przeciwnika
                    Point emptyNeighbor = findEmptyNeighbor(i, j);
                    if (emptyNeighbor != null) {
                        return emptyNeighbor;
                    }
                }
            }
        }

        // Jeśli nie udało się znaleźć odpowiedniego ruchu, wykonujemy losowy ruch
        return getRandomMove();
    }

    private Point findEmptyNeighbor(int x, int y) {
        Point[] neighbors = {
                new Point(x + 1, y),
                new Point(x - 1, y),
                new Point(x, y + 1),
                new Point(x, y - 1)
        };

        for (Point neighbor : neighbors) {
            int neighborX = (int) neighbor.getX();
            int neighborY = (int) neighbor.getY();

            if (isValidCoordinate(neighborX, neighborY) && fields[neighborX][neighborY].getState().equals(IntersectionState.EMPTY)) {
                return new Point(neighborX, neighborY);
            }
        }

        return null;
    }

    private Point getRandomMove() {
        // Dla uproszczenia, bot wybierze losowe wolne pole
        while (true) {
            int x = (int) (Math.random() * 19);
            int y = (int) (Math.random() * 19);

            if (isValidCoordinate(x, y) && fields[x][y].getState().equals(IntersectionState.EMPTY)) {
                return new Point(x, y);
            }
        }
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < 19 && y >= 0 && y < 19;
    }
}

