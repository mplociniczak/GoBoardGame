package org.server;

import java.io.*;
import java.net.Socket;

public class GameThread implements Runnable {
    Socket firstClientSocket;
    Socket secondClientSocket;
    private final static int first = 1;
    private final static int second = 0;
    private static int turn = first;
    private int X;
    private int Y;
    private Board board;
    public GameThread(Socket firstClientSocket, Socket secondClientSocket) {
        this.firstClientSocket = firstClientSocket;
        this.secondClientSocket = secondClientSocket;
    }
//public GameThread(Socket firstClientSocket) {
//    this.firstClientSocket = firstClientSocket;
//    //this.secondClientSocket = secondClientSocket;
//}
    @Override
    public void run() {

        //Server.allCurrentGames.add(this);
        System.out.println("Running...");

        try{
            DataInputStream firstClientInput = new DataInputStream(firstClientSocket.getInputStream());
            DataOutputStream firstClientOutput = new DataOutputStream(firstClientSocket.getOutputStream());

            DataInputStream secondClientInput = new DataInputStream(secondClientSocket.getInputStream());
            DataOutputStream secondClientOutput = new DataOutputStream(secondClientSocket.getOutputStream());

            while(true) {
                if(turn == first) {
                    X = firstClientInput.readInt();
                    Y = firstClientInput.readInt();
                    System.out.println(X + " " + Y);

                    secondClientOutput.writeInt(X);
                    secondClientOutput.writeInt(Y);
                    turn = second;
                }

                if(turn == second) {
                    X = secondClientInput.readInt();
                    Y = secondClientInput.readInt();
                    System.out.println(X + " " + Y);

                    firstClientOutput.writeInt(X);
                    firstClientOutput.writeInt(Y);
                    turn = first;
                }
            }

        } catch (IOException ex) {
            //TODO: handle
        }
    }
}
