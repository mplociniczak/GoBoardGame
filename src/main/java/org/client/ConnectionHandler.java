package org.client;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * The ConnectionHandler class manages the client-server connection for the Go game.
 * It establishes a socket connection with the server, sends and receives game-related data.
 */
public class ConnectionHandler {
    private Socket socket = null;
    private ObjectInputStream input;
    private ObjectOutputStream output;


    /**
     * Constructs a ConnectionHandler instance and connects to the server.
     *
     * @param address The IP address or hostname of the server.
     * @param port    The port number to connect to.
     * @param mode    The mode indicating whether the client is a player (0) or a bot (1).
     */
    public ConnectionHandler(String address, int port, int mode) {
        try {
            System.out.println("Connecting to server: " + address + ", " + port);
            socket = new Socket(address, port);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeInt(mode);
            output.flush();

            System.out.println("Connection successful!");


        } catch (IOException ex) {
            ex.printStackTrace();
            // TODO: handle
        }
    }

    /**
     * Sends the X and Y coordinates to the server, representing a player's move.
     *
     * @param X The X-coordinate of the move.
     * @param Y The Y-coordinate of the move.
     */
    public void sendCoordinates(int X, int Y){
        try{
            output.writeObject(new Point(X, Y));
            output.flush();
        } catch (IOException ex) {
            //TODO: handle
        }
    }


    /**
     * Receives the current board state from the server.
     *
     * @return A StringBuilder object representing the received board state.
     */
    public StringBuilder receiveCoordinates() {
        try{
            return (StringBuilder) input.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Receives the current turn information from the server.
     *
     * @return An integer representing the current turn.
     */
    public int receiveTurn() {
        try{
            return input.readInt();
        } catch (IOException ex) {
            ex.getMessage();
        }
        return -1;
    }
}
