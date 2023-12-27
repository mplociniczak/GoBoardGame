package org.server;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class GameThread implements Runnable {
    Socket firstClientSocket;
    Socket secondClientSocket;
    public final static int first = 1;
    public final static int second = 2;
    private static int turn = first;
    private Board board;
    public GameThread(Socket firstClientSocket, Socket secondClientSocket) {
        this.firstClientSocket = firstClientSocket;
        this.secondClientSocket = secondClientSocket;
        board = new Board();
    }

    private void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        System.out.println(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }
    @Override
    public void run() {

        int X = 0;
        int Y = 0;

        Server.addGame(this);
        System.out.println("Running...");

        try{
            ObjectOutputStream firstClientOutput = new ObjectOutputStream(firstClientSocket.getOutputStream());
            ObjectInputStream firstClientInput = new ObjectInputStream(firstClientSocket.getInputStream());

            ObjectOutputStream secondClientOutput = new ObjectOutputStream(secondClientSocket.getOutputStream());
            ObjectInputStream secondClientInput = new ObjectInputStream(secondClientSocket.getInputStream());

//            DataInputStream firstClientInput = new DataInputStream(firstClientSocket.getInputStream());
//            DataOutputStream firstClientOutput = new DataOutputStream(firstClientSocket.getOutputStream());
//
//            DataInputStream secondClientInput = new DataInputStream(secondClientSocket.getInputStream());
//            DataOutputStream secondClientOutput = new DataOutputStream(secondClientSocket.getOutputStream());

            firstClientOutput.writeInt(first);
            firstClientOutput.flush();

            secondClientOutput.writeInt(second);
            secondClientOutput.flush();

            while (true) {
                if (turn == first) {
                    X = firstClientInput.readInt();
                    Y = firstClientInput.readInt();

                    System.out.println("First turn: " + X + " " + Y);

                    if (board.checkIfMoveCorrect(X, Y)) {

                        board.placeWhiteStone(X, Y);

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

                    System.out.println("Second turn: " + X + " " + Y);

                    if (board.checkIfMoveCorrect(X, Y)) {

                        board.placeBlackStone(X, Y);

                        sendMove(firstClientOutput, X, Y);
                        sendMove(secondClientOutput, X, Y);


                    } else {
                        sendMove(secondClientOutput, -1, -1);
                        sendMove(firstClientOutput, -1, -1);
                    }

                    turn = first;

                }

                board.printBoardToHelpDebugging();

            }

        } catch (IOException ex) {
            //TODO: handle
        }
    }
}
