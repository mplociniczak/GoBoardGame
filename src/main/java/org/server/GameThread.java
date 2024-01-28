package org.server;

import org.server.gameLogic.Board;
import org.server.gameLogic.StoneColor;
import org.server.database.*;

import java.awt.*;
import java.io.*;
import java.util.Date;

import static org.server.Server.*;

public class GameThread extends Thread implements Runnable, iGameThread {
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

    public GameThread(ObjectInputStream firstClientInput, ObjectOutputStream firstClientOutput, ObjectInputStream secondClientInput, ObjectOutputStream secondClientOutput) {
        this.firstClientOutput = firstClientOutput;
        this.secondClientOutput = secondClientOutput;
        this.firstClientInput = firstClientInput;
        this.secondClientInput = secondClientInput;
        board = new Board();
        gameDao = new GameDAO();
    }

    public void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        System.out.println(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }

    Point coordinates = new Point();
    int[] playersMove = new int[2];
    private void handleUserInputAndSendBackCoordinates(ObjectInputStream clientInput, StoneColor color) throws IOException, ClassNotFoundException {

        coordinates = (Point) clientInput.readObject();

        StoneColor enemyColor = (color == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;

        board.buildBoard.setStoneRemovedFlagToFalse();

        if(coordinates.x == passCode) {

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

            sendMove(firstClientOutput, errorCode, errorCode);

            sendMove(secondClientOutput, errorCode, errorCode);

            passCounter = 0;
        }
    }
    @Override
    public void run() {

        addGame(this);
        System.out.println("Running...");

        // zapisywanie do bazy przy rozpoczęciu gry
        /*
        currentGame = new Game();
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

                    if(passCounter == 2) {
                        Server.removeGame(this);
                        sendMove(firstClientOutput, endgameCode, endgameCode);
                        sendMove(secondClientOutput, endgameCode, endgameCode);
                        break;
                    }

                    turn = second;
                }

                if (turn == second) {

                    handleUserInputAndSendBackCoordinates(secondClientInput, StoneColor.WHITE);

                    if(passCounter == 2) {
                        removeGame(this);
                        sendMove(firstClientOutput, endgameCode, endgameCode);
                        sendMove(secondClientOutput, endgameCode, endgameCode);
                        break;
                    }

                    turn = first;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            //TODO: handle
        }

        // Przy zakończeniu gry
        /*
        currentGame.setEndTime(new Date());
        gameDao.updateGame(currentGame);
        */
        this.interrupt();
    }
}
