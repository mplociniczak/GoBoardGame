import org.client.ConnectionHandler;
import org.junit.Test;
import org.server.Server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestTwoClientsServerConnection {

    @Test
    public void testClientServerConnection() {
        // Start the server
        Thread serverThread = new Thread(() -> {
            Server.main(null);
        });
        serverThread.start();

        // Wait for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // First client thread
        Thread firstClientThread = new Thread(() -> {
            ConnectionHandler firstClientConnection = new ConnectionHandler("localhost", 6660, 0);
            firstClientConnection.sendCoordinates(1, 8);
            StringBuilder receivedCoordinates = firstClientConnection.receiveCoordinates();
            assertNotNull(receivedCoordinates);
            int turn = firstClientConnection.receiveTurn();
            assertTrue(turn == 1 || turn == 2);
        });
        firstClientThread.start();

        // Second client thread
        Thread secondClientThread = new Thread(() -> {
            ConnectionHandler secondClientConnection = new ConnectionHandler("localhost", 6660, 0);
            secondClientConnection.sendCoordinates(2, 5);
            StringBuilder receivedCoordinates = secondClientConnection.receiveCoordinates();
            assertNotNull(receivedCoordinates);
            int turn = secondClientConnection.receiveTurn();
            assertTrue(turn == 1 || turn == 2);
        });
        secondClientThread.start();

        // Wait for the test to complete
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the server and client threads
        serverThread.interrupt();
        firstClientThread.interrupt();
        secondClientThread.interrupt();
    }
}
