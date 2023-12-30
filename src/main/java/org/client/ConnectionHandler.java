package org.client;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket = null;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ConnectionHandler(String address, int port) {
        try {
            System.out.println("Connecting to server: " + address + ", " + port);
            socket = new Socket(address, port);

            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Connection successful!");

        } catch (IOException ex) {
            ex.printStackTrace();
            // TODO: handle
        }
    }

    public void sendCoordinates(int X, int Y){
        try{
            output.writeInt(X);
            output.writeInt(Y);
            output.flush();
        } catch (IOException ex) {
            //TODO: handle
        }
    }

    public StringBuilder receiveCoordinates() {
        try{
            StringBuilder board = (StringBuilder) input.readObject();//BLACK, BLACK, WHITE, NULL itp
            return board;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int receiveTurn() {
        try{
            int turn = input.readInt();
            System.out.println(turn);
            return turn;
        } catch (IOException ex) {
            ex.getMessage();
        }
        return -1;
    }

    public void waitForPlayerAction(boolean waiting) throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }
        waiting = true;
    }
}
