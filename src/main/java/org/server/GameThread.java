package org.server;

import java.awt.*;
import java.io.*;
import java.net.Socket;

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

        Server.addGame(this);
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

                    if (board.buildBoard.isIntersectionEmpty(X, Y) && !board.buildBoard.isKoViolation()) {

                        board.placeBlackStone(X, Y);

                        sendMove(secondClientOutput, X, Y);
                        sendMove(firstClientOutput, X, Y);

                    } else {
                        sendMove(firstClientOutput, -1, -1);
                        sendMove(secondClientOutput, -1, -1);
                    }

                    turn = second;

                }

                if (turn == second) {
                    X = secondClientInput.readInt();
                    Y = secondClientInput.readInt();

                    if (board.buildBoard.isIntersectionEmpty(X, Y) && !board.buildBoard.isKoViolation()) {

                        board.placeWhiteStone(X, Y);

                        sendMove(firstClientOutput, X, Y);
                        sendMove(secondClientOutput, X, Y);


                    } else {
                        sendMove(secondClientOutput, -1, -1);
                        sendMove(firstClientOutput, -1, -1);
                    }

                    turn = first;

                }
            }

        } catch (IOException ex) {
            //TODO: handle
        }
    }
}
