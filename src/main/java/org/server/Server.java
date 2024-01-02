package org.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static final int port = 6660;
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

        int mode;
        while(true) {
            try {
                //Client creates a new game, each game has separate thread
                firstClientSocket = serverSocket.accept();
                System.out.println("First client connected");

                ObjectInputStream firstClientInput = new ObjectInputStream(firstClientSocket.getInputStream());
                ObjectOutputStream firstClientOutput = new ObjectOutputStream(firstClientSocket.getOutputStream());

                mode = firstClientInput.readInt();
                System.out.println(mode);

                if(mode == 1) {
                    BotGameThread currentBotGame = new BotGameThread(firstClientInput, firstClientOutput);

                    new Thread(currentBotGame).start();

                    continue;
                }

                secondClientSocket = serverSocket.accept();
                System.out.println("Second client connected");

                ObjectInputStream secondClientInput = new ObjectInputStream(secondClientSocket.getInputStream());
                ObjectOutputStream secondClientOutput = new ObjectOutputStream(secondClientSocket.getOutputStream());

                mode = secondClientInput.readInt();

                if(mode == 1) {
                    BotGameThread currentBotGame = new BotGameThread(firstClientInput, firstClientOutput);

                    new Thread(currentBotGame).start();

                    continue;
                }

                GameThread currentGame = new GameThread(firstClientInput, firstClientOutput, secondClientInput, secondClientOutput);

                new Thread(currentGame).start();

            } catch (IOException e) {
                System.out.println("I/O error");
            }

        }
    }
}