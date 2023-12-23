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
//public GameThread(Socket firstClientSocket) {
//    this.firstClientSocket = firstClientSocket;
//    //this.secondClientSocket = secondClientSocket;
//}
    private void sendMove(DataOutputStream out, int X, int Y) throws IOException {
        out.writeInt(X);
        out.writeInt(Y);
    }

    public static int getTurn() {
        return turn;
    }

    public static void setTurn(int newTurn) {
        turn = newTurn;
    }

    @Override
    public void run() {

        int X = 0;
        int Y = 0;

        Server.addGame(this);
        System.out.println("Running...");

        try{
            DataInputStream firstClientInput = new DataInputStream(firstClientSocket.getInputStream());
            DataOutputStream firstClientOutput = new DataOutputStream(firstClientSocket.getOutputStream());

            DataInputStream secondClientInput = new DataInputStream(secondClientSocket.getInputStream());
            DataOutputStream secondClientOutput = new DataOutputStream(secondClientSocket.getOutputStream());

            firstClientOutput.writeInt(first);
            secondClientOutput.writeInt(second);

            while (true) {
                if (turn == first) {
                    X = firstClientInput.readInt();
                    Y = firstClientInput.readInt();

                    if (board.isMoveValidForFirstPlayer(X, Y)) {
                        board.placeWhiteStone(X, Y);

                        sendMove(secondClientOutput, X, Y);
                        sendMove(firstClientOutput, X, Y);
                        System.out.println("First turn: " + X + " " + Y);

                        turn = second;
                    } else {
                        sendMove(firstClientOutput, -1, -1);
                    }
                }

                if (turn == second) {
                    X = secondClientInput.readInt();
                    Y = secondClientInput.readInt();

                    if (board.isMoveValidForSecondPlayer(X, Y)) {
                        board.placeBlackStone(X, Y);

                        sendMove(firstClientOutput, X, Y);
                        sendMove(secondClientOutput, X, Y);
                        System.out.println("Second turn: " + X + " " + Y);

                        turn = first;
                    } else {
                        sendMove(secondClientOutput, -1, -1);
                    }
                }
            }

        } catch (IOException ex) {
            //TODO: handle
        }
    }
}
