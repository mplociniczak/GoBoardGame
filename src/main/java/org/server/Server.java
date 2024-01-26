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
    public static final int endgameCode = -3;

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

        ArrayList<ClientServerConnectionManager> waitingClients = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

        } catch (IOException ex) {
            System.out.println("server exception" + ex.getMessage());
        }


        while(true) {
            try {
                //Client creates a new game, each game has separate thread
                ClientServerConnectionManager client = new ClientServerConnectionManager();

                assert serverSocket != null;
                client.initializeSocket(serverSocket);

                int mode = client.gameMode();

                if(mode == 1) {

                    BotGameThread currentBotGame = new BotGameThread(client.clientInput, client.clientOutput);

                    new Thread(currentBotGame).start();

                } else if(mode == 0) {
                    waitingClients.add(client);
                }

                if(waitingClients.size() == 2) {

                    GameThread currentGame = new GameThread(waitingClients.get(0).clientInput, waitingClients.get(0).clientOutput,
                            waitingClients.get(1).clientInput, waitingClients.get(1).clientOutput);

                    new Thread(currentGame).start();

                    waitingClients.clear();
                }

            } catch (IOException e) {
                System.out.println("I/O error");
            }

        }
    }
}