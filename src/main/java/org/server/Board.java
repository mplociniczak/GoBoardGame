package org.server;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Board {
    Stone[][] fields;
    public Board(){
        fields = new Stone[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j] = new Stone();
            }
        }
    }
    public boolean checkIfMoveCorrect(int X, int Y){
        if(fields[X][Y].getState().equals(IntersectionState.EMPTY)){
            return true;
        } else
            // Jeśli przecięcie jest już zajęte, wyświetl komunikat "Miejsce zajęte" jako dialog
            JOptionPane.showMessageDialog(null, "Miejsce zajęte. Wybierz inne.", "Błąd ruchu", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public void placeBlackStone(int X, int Y) {
        StoneColor currentColor = StoneColor.BLACK;
        fields[X][Y].placeStone(currentColor);

        // Sprawdź, czy są złapane kamienie przeciwnika
        captureStones(X, Y, currentColor);
    }

    public void placeWhiteStone(int X, int Y) {
        StoneColor currentColor = StoneColor.WHITE;
        fields[X][Y].placeStone(currentColor);

        // Sprawdź, czy są złapane kamienie przeciwnika
        captureStones(X, Y, currentColor);
    }


    /**
     * Metoda do przechwytywania kamieni przeciwnika.
     * @param x Współrzędna X umieszczonego kamienia.
     * @param y Współrzędna Y umieszczonego kamienia.
     * @param color Kolor kamienia umieszczonego na planszy.
     */
    private void captureStones(int x, int y, StoneColor color) {
        // Sprawdź czy współrzędne są poprawne i czy na danym przecięciu znajduje się kamień przeciwnika
        if (!isValidCoordinate(x, y) || fields[x][y].getState() != IntersectionState.OCCUPIED ||
                fields[x][y].getColor() == color) {
            return;  // Wychodzi, jeśli współrzędne są niepoprawne lub kamień jest tego samego koloru
        }

        Set<Point> visited = new HashSet<>();  // Zbior, aby śledzić już odwiedzone przecięcia
        captureAdjacentStones(x, y, visited);
    }

    /**
     * Metoda pomocnicza do rekurencyjnego przechwytywania kamieni przeciwnika.
     * @param x Współrzędna X przecięcia, które sprawdzamy.
     * @param y Współrzędna Y przecięcia, które sprawdzamy.
     * @param visited Zbiór odwiedzonych przecięć, aby uniknąć cyklu.
     */
    private void captureAdjacentStones(int x, int y, Set<Point> visited) {
        // Sprawdź czy współrzędne są poprawne i czy przecięcie nie zostało już odwiedzone
        if (!isValidCoordinate(x, y) || !visited.add(new Point(x, y))) {
            return;  // Wychodzi, jeśli współrzędne są niepoprawne lub już odwiedzone
        }

        StoneColor stoneColor = fields[x][y].getColor();

        // Wychodzi, jeśli przecięcie jest puste
        if (stoneColor == StoneColor.EMPTY) {
            return;
        }

        // Wychodzi, jeśli kamień jest tego samego koloru
        if (stoneColor == StoneColor.BLACK || stoneColor == StoneColor.WHITE) {
            return;
        }

        // Sprawdź, czy kamień stracił oddech
        if (!hasBreath(x, y)) {
            // Złap kamienie przeciwnika
            fields[x][y].removeStone();

            // Wywołaj rekurencyjnie dla sąsiadów
            captureAdjacentStones(x + 1, y, visited);
            captureAdjacentStones(x - 1, y, visited);
            captureAdjacentStones(x, y + 1, visited);
            captureAdjacentStones(x, y - 1, visited);
        }
    }

    /**
     * Metoda sprawdzająca, czy współrzędne są w zakresie planszy.
     * @param x Współrzędna X.
     * @param y Współrzędna Y.
     * @return true, jeśli współrzędne są w zakresie planszy, w przeciwnym razie false.
     */
    private boolean isValidCoordinate(int x, int y) {
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
        if (!isValidCoordinate(x, y) || visited.contains(new Point(x, y))) {
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

