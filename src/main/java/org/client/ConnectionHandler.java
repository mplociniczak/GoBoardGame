package org.client;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket = null;
    private DataInputStream input;
    private DataOutputStream output;
    private int X;
    private int Y;

    public ConnectionHandler(String address, int port) {
        try {
            socket = new Socket(address, port);

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            //TODO: handle
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

    public void receiveCoordinates(int X, int Y) {
        try{
            X = input.readInt();
            Y = input.readInt();
        } catch (IOException ex) {
            //TODO: handle
        }
    }
    public int receiveTurn() {
        try{
            return input.readInt();
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
