package org.server;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class GameThreadManager implements Runnable {
    private Subject serverSubject;
    public GameThreadManager(){
        serverSubject = new ServerSubject();
    }
    @Override
    public void run() {
        while(true) {
            try {
                for(GameThread t : Server.allCurrentGames) {

                    if(t.firstClientInput.available() > 0)
                        serverSubject.notifyObservers();
                    if(t.secondClientInput.available() > 0)
                        serverSubject.notifyObservers();
                }

                sleep(1000);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
