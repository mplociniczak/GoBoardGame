package org.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int port = 6666;

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket firstClientSocket = null;
        Socket secondClientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

        } catch (IOException ex) {
            System.out.println("server exception" + ex.getMessage());
        }

        while(true) {
            try {
                //Client creates a new game, each game has separate thread
                firstClientSocket = serverSocket.accept();
                System.out.println("First client connected");
                System.out.println("Waiting for the second player");

                secondClientSocket = serverSocket.accept();
                System.out.println("Second client connected");

            } catch (IOException e) {
                System.out.println("I/O error");
            }

            GameThread currentGame = new GameThread(firstClientSocket, secondClientSocket);

            new Thread(currentGame);

        }
    }
}