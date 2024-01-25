package org.client;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket = null;
    private ObjectInputStream input;
    private ObjectOutputStream output;

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
    public void sendCoordinates(int X, int Y){
        try{
            output.writeObject(new Point(X, Y));
            //output.writeInt(X);
            //output.writeInt(Y);
            output.flush();
        } catch (IOException ex) {
            //TODO: handle
        }
    }

    public StringBuilder receiveCoordinates() {
        try{
            return (StringBuilder) input.readObject();
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
}
