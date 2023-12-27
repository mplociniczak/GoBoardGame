package org.server;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Board {
    public static Stone[][] fields;
    private Set<String> previousBoardStates;
    public Board(){
        fields = new Stone[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j] = new Stone();
            }
        }
        previousBoardStates = new HashSet<>();
    }

    public void printBoardToHelpDebugging() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if(fields[i][j].getState().equals(IntersectionState.OCCUPIED))
                System.out.println("row " + i + " " + "col " + j + " " + fields[i][j].getColor());
            }
        }
    }

    public boolean checkIfMoveCorrect(int X, int Y){
        return fields[X][Y].getState().equals(IntersectionState.EMPTY) && !isKoViolation();
    }

    //jeśli kamień został uduszony w ko, nie może udusić kamienia przeciwnika w następnym ruchu
    public boolean isKoViolation() {
        StringBuilder boardStateBuilder = new StringBuilder();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                boardStateBuilder.append(fields[i][j].getState().toString());
            }
        }
        return !previousBoardStates.add(boardStateBuilder.toString());
    }

    public StringBuilder BoardToStringBuilderWithStoneColors(int X, int Y) {
        StringBuilder boardStateBuilder = new StringBuilder();

        boardStateBuilder.append(X);
        boardStateBuilder.append(" ");
        boardStateBuilder.append(Y);
        boardStateBuilder.append(" ");

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                boardStateBuilder.append(fields[i][j].getColor().toString());
                boardStateBuilder.append(" ");
            }
        }
        return boardStateBuilder;
    }

    public void placeBlackStone(int X, int Y) {

        fields[X][Y].placeStone(StoneColor.BLACK);
        // Sprawdź, czy są złapane kamienie przeciwnika
        captureStones(X, Y, StoneColor.WHITE);
    }

    public void placeWhiteStone(int X, int Y) {

        fields[X][Y].placeStone(StoneColor.WHITE);
        // Sprawdź, czy są złapane kamienie przeciwnika
        captureStones(X, Y, StoneColor.BLACK);
    }


    public void captureStones(int X, int Y, StoneColor opponentColor) {
        // Sprawdź wszystkie sąsiednie kamienie przeciwnika
        captureStonesInDirection(X, Y - 1, opponentColor); // kamień po lewej
        captureStonesInDirection(X, Y + 1, opponentColor); // kamień po prawej
        captureStonesInDirection(X - 1, Y, opponentColor); // kamień powyżej
        captureStonesInDirection(X + 1, Y, opponentColor); // kamień poniżej
    }

    private void captureStonesInDirection(int x, int y, StoneColor opponentColor) {
        if (isValidPosition(x, y) && fields[x][y].getColor() == opponentColor) {
            Set<Point> capturedStones = new HashSet<>();
            if (!hasLiberty(x, y, capturedStones)) {
                // Udusz kamienie przeciwnika
                for (Point point : capturedStones) {
                    fields[point.x][point.y].removeStone();
                }
            }
        }
    }

    private boolean hasLiberty(int x, int y, Set<Point> visited) {
        if (!isValidPosition(x, y) || visited.contains(new Point(x, y))) {
            return false;
        }

        visited.add(new Point(x, y));

        if (fields[x][y].getState() == IntersectionState.EMPTY) {
            return true;
        }

        return hasLiberty(x - 1, y, visited) ||
                hasLiberty(x + 1, y, visited) ||
                hasLiberty(x, y - 1, visited) ||
                hasLiberty(x, y + 1, visited);
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 19 && y >= 0 && y < 19;
    }


    /**
     * Metoda sprawdzająca, czy kamień ma oddech.
     * @param x Współrzędna X kamienia.
     * @param y Współrzędna Y kamienia.
     * @param visited Zbiór odwiedzonych przecięć, aby uniknąć cyklu.
     * @return true, jeśli kamień ma oddech, w przeciwnym razie false.
     */
    private boolean hasBreath(int x, int y, Set<Point> visited) {
        // Sprawdź, czy współrzędne są w zakresie planszy i czy przecięcie nie zostało już odwiedzone
        if (visited.contains(new Point(x, y))) {
            return false;
        }

        // Oznacz współrzędne jako odwiedzone
        visited.add(new Point(x, y));

        // Sprawdź, czy przecięcie jest puste (ma oddech)
        if (fields[x][y].getState() == IntersectionState.EMPTY) {
            return true;
        }

        // Sprawdź oddech dla sąsiadów
        return hasBreath(x + 1, y, visited) ||
                hasBreath(x - 1, y, visited) ||
                hasBreath(x, y + 1, visited) ||
                hasBreath(x, y - 1, visited);
    }

    /**
     * Metoda sprawdzająca, czy kamień ma oddech.
     * @param x Współrzędna X kamienia.
     * @param y Współrzędna Y kamienia.
     * @return true, jeśli kamień ma oddech, w przeciwnym razie false.
     */
    private boolean hasBreath(int x, int y) {
        Set<Point> visited = new HashSet<>();
        return hasBreath(x, y, visited);
    }

}

