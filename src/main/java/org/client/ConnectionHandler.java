package org.client;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket = null;
    private DataInputStream input;
    private DataOutputStream output;

    public ConnectionHandler(String address, int port) {
        try {
            System.out.println("Connecting to server: " + address + ":" + port);
            socket = new Socket(address, port);

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

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
        } catch (IOException ex) {
            //TODO: handle
        }
    }

    public Point receiveCoordinates() {
        try{
            int X = input.readInt();
            int Y = input.readInt();
            System.out.println("Coordinates: " + X + " " + Y);
            return new Point(X, Y);
        } catch (IOException ex) {
            // Handle the exception properly
            ex.printStackTrace();
            return null;
        }
    }
    public int receiveTurn() {
        try{
            int turn =  input.readInt();
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
