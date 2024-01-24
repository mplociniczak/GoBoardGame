package org.server;

import org.server.gameLogic.Board;
import org.server.gameLogic.StoneColor;

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
    @Override
    public void run() {

        int X;
        int Y;
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
                    X = firstClientInput.readInt();
                    Y = firstClientInput.readInt();

                    if(X == passCode && Y == passCode) {
                        sendMove(secondClientOutput, passCode, passCode);
                        sendMove(firstClientOutput, passCode, passCode);
                        passCounter++;
                    } else if (board.buildBoard.isIntersectionEmpty(X, Y)) {
                        board.placeStone(X, Y, StoneColor.BLACK, StoneColor.WHITE);
                        sendMove(secondClientOutput, X, Y);
                        sendMove(firstClientOutput, X, Y);
                    } else {
                        sendMove(firstClientOutput, errorCode, errorCode);
                        sendMove(secondClientOutput, errorCode, errorCode);
                    }

                    if(passCounter == 2) {
                        Server.removeGame(this);
                    }

                    turn = second;
                }

                if (turn == second) {
                    X = secondClientInput.readInt();
                    Y = secondClientInput.readInt();

                    if(X == passCode && Y == passCode) {
                        sendMove(firstClientOutput, passCode, passCode);
                        sendMove(secondClientOutput, passCode, passCode);
                        passCounter++;
                    } else if (board.buildBoard.isIntersectionEmpty(X, Y)) {
                        board.placeStone(X, Y, StoneColor.WHITE, StoneColor.BLACK);
                        sendMove(firstClientOutput, X, Y);
                        sendMove(secondClientOutput, X, Y);
                    } else {
                        sendMove(secondClientOutput, errorCode, errorCode);
                        sendMove(firstClientOutput, errorCode, errorCode);
                    }

                    if(passCounter == 2) {
                        Server.removeGame(this);
                    }

                    turn = first;
                }
            }
        } catch (IOException ex) {
            //TODO: handle
        }
    }
}