package org.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class Board {
    public static Stone[][] fields;
    Set<String> previousBoardStates;
    private List<GoRuleFactory> goRules;
    public Board(){
        fields = new Stone[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j] = new Stone();
            }
        }
        previousBoardStates = new HashSet<>();
        goRules = new ArrayList<>();
        // Dodaj reguły do listy
        goRules.add(new KoRule());
        goRules.add(new SurroundingRule());
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
        return fields[X][Y].getState().equals(IntersectionState.EMPTY);
    }

    public boolean checkAllRules(int X, int Y) {
        // Sprawdź wszystkie zasady dla danego ruchu
        for (GoRuleFactory rule : goRules) {
            if (!rule.check(this, X, Y)) {
                // Jeśli któraś zasada nie została spełniona, zwróć false
                return false;
            }
        }
        // Jeśli wszystkie zasady zostały spełnione, zwróć true
        return true;
    }

    /*
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

     */

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
        if (checkIfMoveCorrect(X, Y) && checkAllRules(X, Y)) {
            fields[X][Y].placeStone(StoneColor.BLACK);

        } else {
            // Obsłuż błąd, narazie tak
            System.out.println("Ruch niepoprawny zgodnie z zasadami Go.");
        }
    }

    public void placeWhiteStone(int X, int Y) {
        if (checkIfMoveCorrect(X, Y) && checkAllRules(X, Y)) {
            fields[X][Y].placeStone(StoneColor.WHITE);

        } else {
            // Obsłuż błąd, narazie tak
            System.out.println("Ruch niepoprawny zgodnie z zasadami Go.");
        }
    }




    /*
    boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < 19 && y >= 0 && y < 19;
    }private void searchForAdjacentEnemyStones(int X, int Y, StoneColor enemyColor, StoneColor allyColor) {
        Set<Point> visited = new HashSet<>();

        stoneRemover(X+1, Y, enemyColor, allyColor, visited);
        stoneRemover(X-1, Y, enemyColor, allyColor, visited);
        stoneRemover(X, Y+1, enemyColor, allyColor, visited);
        stoneRemover(X, Y-1, enemyColor, allyColor, visited);

    }

    /**
     * Handles removing groups of stones, written to simplify code
     * @param X first stone in a group to be removed
     * @param Y first stone in a group to be removed
     * @param enemyColor enemy stones
     * @param allyColor ally stones
     * @param visited list of all checked stones to avoid repetitions
     */
    /*
    private void stoneRemover(int X, int Y, StoneColor enemyColor, StoneColor allyColor, Set<Point> visited) {
        Set<Point> surroundedStones = new HashSet<>();

        if(isValidCoordinate(X, Y) && fields[X][Y].getColor().equals(enemyColor)) {
            if(checkIfSurrounded(X, Y, allyColor, surroundedStones, visited)){
                for(Point p : surroundedStones) {
                    fields[p.x][p.y].removeStone();
                }
                surroundedStones.clear();
            }
        }
    }

    /**
     * Don't even try to read this. Total mess but works
     * @param X coordinate of a stone that may be surrounded
     * @param Y coordinate of a stone that may be surrounded
     * @param enemyColor to check if a stone is surrounded by enemies
     * @param surroundedStones set of all surrounded stones that later will be removed
     * @param visited set of all visited stones to avoid infinite recursion
     * @return true if stone is surrounded, false if not
     */
    /*
    private boolean checkIfSurrounded(int X, int Y, StoneColor enemyColor, Set<Point> surroundedStones, Set<Point> visited) {
        boolean isSurrounded;

        visited.add(new Point(X , Y));

        if((isValidCoordinate(X+1, Y) && fields[X+1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X-1, Y) && fields[X-1][Y].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y+1) && fields[X][Y+1].getState().equals(IntersectionState.EMPTY))
                || (isValidCoordinate(X, Y-1) && fields[X][Y-1].getState().equals(IntersectionState.EMPTY))) {
            return false;
        }

        if(isValidCoordinate(X+1, Y) && fields[X+1][Y].getColor().equals(enemyColor)
                && isValidCoordinate(X-1, Y) && fields[X-1][Y].getColor().equals(enemyColor)
                && isValidCoordinate(X, Y+1) && fields[X][Y+1].getColor().equals(enemyColor)
                && isValidCoordinate(X, Y-1) && fields[X][Y-1].getColor().equals(enemyColor)) {
            isSurrounded = true;
        } else {
            if(isValidCoordinate(X+1, Y) && !fields[X+1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X+1, Y))) {
                isSurrounded = checkIfSurrounded(X+1, Y, enemyColor, surroundedStones, visited);
                if(!isSurrounded) return false;
            }
            if(isValidCoordinate(X-1, Y) && !fields[X-1][Y].getColor().equals(enemyColor) && !visited.contains(new Point(X-1, Y))) {
                isSurrounded = checkIfSurrounded(X-1, Y, enemyColor, surroundedStones, visited);
                if(!isSurrounded) return false;
            }
            if(isValidCoordinate(X, Y+1) && !fields[X][Y+1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y+1))) {
                isSurrounded = checkIfSurrounded(X, Y+1, enemyColor, surroundedStones, visited);
                if(!isSurrounded) return false;
            }
            if(isValidCoordinate(X, Y-1) && !fields[X][Y-1].getColor().equals(enemyColor) && !visited.contains(new Point(X, Y-1))) {
                isSurrounded = checkIfSurrounded(X, Y-1, enemyColor, surroundedStones, visited);
                if(!isSurrounded) return false;
            }
            else isSurrounded = true;
        }

        if(isSurrounded) {
            surroundedStones.add(new Point(X, Y));
        }

        return true;

    }

     */
}

