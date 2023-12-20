package org.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int port = 6670;
    public static ArrayList<GameThread> allCurrentGames = new ArrayList<>();
    public static void addGame(GameThread currentGame) { allCurrentGames.add(currentGame); }
    public static void removeGame(GameThread finishedGame) { allCurrentGames.remove(finishedGame); }
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
                System.out.println("First client connected");

                secondClientSocket = serverSocket.accept();
                System.out.println("Second client connected");

                GameThread currentGame = new GameThread(firstClientSocket, secondClientSocket);
                //GameThread currentGame = new GameThread(firstClientSocket);
                new Thread(currentGame).start();

            } catch (IOException e) {
                System.out.println("I/O error");
            }

        }
    }
}