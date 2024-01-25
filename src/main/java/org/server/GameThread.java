package org.server;

import org.server.gameLogic.Board;
import org.server.gameLogic.StoneColor;

import java.awt.*;
import java.io.*;

import static org.server.Server.*;

public class GameThread implements Runnable {
    private final static int first = 1;
    private final static int second = 2;
    private static int turn = first;
    ObjectOutputStream firstClientOutput;
    ObjectOutputStream secondClientOutput;
    ObjectInputStream firstClientInput;
    ObjectInputStream secondClientInput;
    private Board board;
    public GameThread(ObjectInputStream firstClientInput, ObjectOutputStream firstClientOutput, ObjectInputStream secondClientInput, ObjectOutputStream secondClientOutput) {
        this.firstClientOutput = firstClientOutput;
        this.secondClientOutput = secondClientOutput;
        this.firstClientInput = firstClientInput;
        this.secondClientInput = secondClientInput;
        board = new Board();
    }

    private void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        System.out.println(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }
    private void handleUserInputAndSendBackCoordinates(ObjectInputStream clientInput, int passCounter, StoneColor color) throws IOException, ClassNotFoundException {

        Point coordinates = (Point) clientInput.readObject();

        StoneColor enemyColor = (color == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;

        if(coordinates.x == passCode) {

            sendMove(secondClientOutput, passCode, passCode);

            sendMove(firstClientOutput, passCode, passCode);

            passCounter++;

        } else if (board.buildBoard.isIntersectionEmpty(coordinates.x, coordinates.y)) {

            board.placeStone(coordinates.x, coordinates.y, color, enemyColor);

            sendMove(secondClientOutput, coordinates.x, coordinates.y);

            sendMove(firstClientOutput, coordinates.x, coordinates.y);

        } else {

            sendMove(firstClientOutput, errorCode, errorCode);

            sendMove(secondClientOutput, errorCode, errorCode);
        }
    }
    @Override
    public void run() {

        int passCounter = 0;

        addGame(this);
        System.out.println("Running...");

        try{
            firstClientOutput.writeInt(first);
            firstClientOutput.flush();

            secondClientOutput.writeInt(second);
            secondClientOutput.flush();

            while (true) {
                if (turn == first) {
                    handleUserInputAndSendBackCoordinates(firstClientInput, passCounter, StoneColor.BLACK);

                    if(passCounter == 2) {
                        Server.removeGame(this);
                        break;
                    }

                    turn = second;
                }

                if (turn == second) {
                    handleUserInputAndSendBackCoordinates(secondClientInput, passCounter, StoneColor.WHITE);

                    if(passCounter == 2) {
                        Server.removeGame(this);
                        break;
                    }

                    turn = first;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            //TODO: handle
        }
    }
}
