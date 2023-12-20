package org.client;

/**
 * Class with main to start client as a new thread
 */
public class StartClient {
    public static void main(String[] args) {

        Thread t1 = new Thread(new ClientWithBoard());
        t1.start();
    }
}
