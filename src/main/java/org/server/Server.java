package org.server;

import org.server.bot.BotGameThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The Server class represents the main server for a simple Go game.
 * It listens for incoming client connections and creates separate game threads for each pair of clients.
 */
public class Server {

    /**
     * The port number on which the server listens for incoming connections.
     */
    public static final int port = 6660;
    public static final int errorCode = -1;
    public static final int passCode = -2;

    /**
     * A list containing all currently active game threads.
     */
    public static ArrayList<GameThread> allCurrentGames = new ArrayList<>();

    /**
     * Adds a game thread to the list of currently active games.
     *
     * @param currentGame The GameThread to be added.
     */
    public static void addGame(GameThread currentGame) {
        allCurrentGames.add(currentGame);
    }

    /**
     * Removes a finished game thread from the list of currently active games.
     *
     * @param finishedGame The GameThread to be removed.
     */
    public static void removeGame(GameThread finishedGame) {
        allCurrentGames.remove(finishedGame);
    }

    /**
     * The main method of the Server class, responsible for accepting client connections
     * and creating game threads for each pair of clients.
     *
     * @param args Command line arguments (not used).
     */
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
                    BotGameThread currentBotGame = new BotGameThread(secondClientInput, secondClientOutput);

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