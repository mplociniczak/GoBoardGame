import org.client.ConnectionHandler;
import org.junit.Test;

import org.server.Server;

import java.awt.*;

import static org.junit.Assert.*;


public class TestClientServerConnection {

    @Test
    public void testClientServerConnection() {
        // uruchomienie serwera
        Thread serverThread = new Thread(() -> {
            Server.main(null);
        });
        serverThread.start();

        // czekaj żeby serwer się uruchomił
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // wątek pierwszego klienta
        Thread clientThread = new Thread(() -> {
            // tworzenie pierwszego klienta
            ConnectionHandler clientConnection = new ConnectionHandler("localhost", 6670);

            // test wysłania współrzednych
            clientConnection.sendCoordinates(1, 8);

            // test odebranie współrzednych
            Point receivedCoordinates = clientConnection.receiveCoordinates();
            assertNotNull(receivedCoordinates);

            // test czy tury są przechwytywane
            int turn = clientConnection.receiveTurn();
            assertTrue(turn == 1 || turn == 2);
        });
        clientThread.start();

        // czas dla testów na kliencie
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // zatrzymanie
        serverThread.interrupt();
        clientThread.interrupt();
    }
}

