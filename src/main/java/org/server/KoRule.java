package org.server;
public class KoRule implements GoRuleFactory {
    @Override
    public boolean check(Board board, int X, int Y) {
        StringBuilder boardStateBuilder = new StringBuilder();

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                boardStateBuilder.append(board.fields[i][j].getState().toString());
            }
        }

        String currentBoardState = boardStateBuilder.toString();

        // Logika sprawdzania zasady Ko
        if (board.previousBoardStates.contains(currentBoardState)) {
            // Jeżeli aktualny stan planszy już wystąpił wcześniej, to naruszenie zasady Ko
            System.out.println("Naruszenie zasady Ko!");
            return false;
        }

        // Zapisz aktualny stan planszy do zbioru poprzednich stanów
        board.previousBoardStates.add(currentBoardState);

        return true;
    }
}

