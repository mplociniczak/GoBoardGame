package org.server;

import java.io.*;
import java.net.Socket;

public class GameThread implements Runnable {
    Socket firstClientSocket;
    Socket secondClientSocket;
    private final static int first = 1;
    private final static int second = 0;
    private static int turn = first;

    private Board board;

    public GameThread(Socket firstClientSocket, Socket secondClientSocket) {
        this.firstClientSocket = firstClientSocket;
        this.secondClientSocket = secondClientSocket;
        board = new Board();
    }
    @Override
    public void run() {

        try{
            InputStream firstClientInput = firstClientSocket.getInputStream();
            BufferedReader firstClientRead = new BufferedReader(new InputStreamReader(firstClientInput));

            InputStream secondClientInput = secondClientSocket.getInputStream();
            BufferedReader secondClientRead = new BufferedReader(new InputStreamReader(secondClientInput));

            OutputStream firstClientOutput = firstClientSocket.getOutputStream();
            PrintWriter firstClientWrite = new PrintWriter(firstClientOutput, true);

            OutputStream secondClientOutput = secondClientSocket.getOutputStream();
            PrintWriter secondClientWrite = new PrintWriter(secondClientOutput, true);

            //info to client which is which
            firstClientWrite.println("first");
            secondClientWrite.println("second");

            String coordinates;
            while(true) {
                if(turn == first) {
                    //Receiving
                    coordinates = firstClientRead.readLine();
                    //Sending
                    secondClientWrite.println(coordinates);

                    //board.checkIfCorrect(coordinates);
                    turn = second;
                }

                if(turn == second) {
                    coordinates = secondClientRead.readLine();
                    firstClientWrite.println(coordinates);
                    turn = first;
                }
            }
        } catch (IOException ex) {
            //TODO: handle
        }
    }
}
