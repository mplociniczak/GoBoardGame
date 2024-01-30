package org.server;

import org.server.gameLogic.Board;
import org.server.gameLogic.StoneColor;
import org.server.database.*;

import java.awt.*;
import java.io.*;
import java.util.Date;

import static org.constants.ConstantVariables.*;

public class GameThread implements Runnable, iGameThread {
    private final static int first = 1;
    private final static int second = 2;
    private static int turn = first;
    ObjectOutputStream firstClientOutput;
    ObjectOutputStream secondClientOutput;
    ObjectInputStream firstClientInput;
    ObjectInputStream secondClientInput;
    private Board board;
    private int passCounter = 0;
    private GameDAO gameDao;
    private Game currentGame;
    private boolean endGame = false;

    public GameThread(ObjectInputStream firstClientInput, ObjectOutputStream firstClientOutput, ObjectInputStream secondClientInput, ObjectOutputStream secondClientOutput) {
        this.firstClientOutput = firstClientOutput;
        this.secondClientOutput = secondClientOutput;
        this.firstClientInput = firstClientInput;
        this.secondClientInput = secondClientInput;
        board = new Board();
        gameDao = new GameDAO();
    }

    private void saveMoveToDatabase(int x, int y, StoneColor color) {
        Move move = new Move();
        move.setGame(currentGame);
        move.setPositionX(x);
        move.setPositionY(y);
        move.setStoneColor(color);
        move.setMoveTime(new Date());

        new MoveDAO().saveMove(move);
    }

    public void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }

    Point coordinates = new Point();
    int[] playersMove = new int[2];
    private void handleUserInputAndSendBackCoordinates(ObjectInputStream clientInput, StoneColor color) throws IOException, ClassNotFoundException {

        coordinates = (Point) clientInput.readObject();

        //TODO: test it
        //saveMoveToDatabase(coordinates.x, coordinates.y, color);

        StoneColor enemyColor = (color == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;

        board.buildBoard.setStoneRemovedFlagToFalse();

        if(coordinates.x == endgameCode) {

            int playerWon = color.equals(StoneColor.BLACK) ? second : first;

            sendMove(secondClientOutput, endgameCode, playerWon);

            sendMove(firstClientOutput, endgameCode, playerWon);

            endGame = true;

        } else if(coordinates.x == passCode) {

            sendMove(secondClientOutput, passCode, passCode);

            sendMove(firstClientOutput, passCode, passCode);

            passCounter++;

        } else if (board.buildBoard.isIntersectionEmpty(coordinates.x, coordinates.y)) {

            playersMove[0] = coordinates.x;
            playersMove[1] = coordinates.y;;

            board.placeStone(playersMove, color, enemyColor);

            sendMove(secondClientOutput, playersMove[0], playersMove[1]);

            sendMove(firstClientOutput, playersMove[0], playersMove[1]);

            passCounter = 0;

        } else {

            sendMove(secondClientOutput, errorCode, errorCode);

            sendMove(firstClientOutput, errorCode, errorCode);

            passCounter = 0;
        }
    }

    void checkIfTwoPlayersPassed() throws IOException {
        if(passCounter == 2) {
            sendMove(secondClientOutput, endgameCode, endgameCode);
            sendMove(firstClientOutput, endgameCode, endgameCode);
            endGame = true;
        }
    }
    @Override
    public void run() {

        System.out.println("Running...");

        // zapisywanie do bazy przy rozpoczęciu gry
/*
        currentGame = Game.getInstance();
        currentGame.setStartTime(new Date());
        gameDao.saveGame(currentGame);

 */

        try{
            firstClientOutput.writeInt(first);
            firstClientOutput.flush();

            secondClientOutput.writeInt(second);
            secondClientOutput.flush();

            while (true) {
                if (turn == first) {

                    handleUserInputAndSendBackCoordinates(firstClientInput, StoneColor.BLACK);

                    checkIfTwoPlayersPassed();

                    if(endGame) break;

                    turn = second;
                }

                if (turn == second) {

                    handleUserInputAndSendBackCoordinates(secondClientInput, StoneColor.WHITE);

                    checkIfTwoPlayersPassed();

                    if(endGame) break;

                    turn = first;
                }

            }

            System.out.println("Game ended");

        } catch (IOException | ClassNotFoundException ex) {
            //TODO: handle
        }

        // Przy zakończeniu gry
/*
        currentGame.setEndTime(new Date());
        gameDao.updateGame(currentGame);

 */

    }
}
