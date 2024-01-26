package org.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientServerConnectionManager {
    public Socket clientSocket;
    public ObjectOutputStream clientOutput;
    public ObjectInputStream clientInput;

    public void initializeSocket(ServerSocket serverSocket) throws IOException {
        clientSocket = serverSocket.accept();

        clientInput = new ObjectInputStream(clientSocket.getInputStream());
        clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public int gameMode() throws IOException {
        return clientInput.readInt();
    }

}
