package org.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int port = 6666;
    private static final int firstPlayer = 1;
    private static final int secondPlayer = 2;
    public static ArrayList<GameThread> allCurrentGames = new ArrayList<>();
    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket firstClientSocket;
        Socket secondClientSocket;

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
                new DataOutputStream(firstClientSocket.getOutputStream()).writeInt(firstPlayer);
                System.out.println("First client connected");

                secondClientSocket = serverSocket.accept();
                new DataOutputStream(firstClientSocket.getOutputStream()).writeInt(secondPlayer);
                System.out.println("Second client connected");

                GameThread currentGame = new GameThread(firstClientSocket, secondClientSocket);
                new Thread(currentGame).start();

                allCurrentGames.add(currentGame);
                System.out.println(allCurrentGames.size());

            } catch (IOException e) {
                System.out.println("I/O error");
            }

        }
    }
}