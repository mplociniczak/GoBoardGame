package org.server;

import java.util.ArrayList;

public class ServerSubject implements Subject{
    private ArrayList<Observer> games;
    public ServerSubject(){
        games = new ArrayList<>();
    }
    @Override
    public void removeObserver(Observer o) {
        int index = games.indexOf(o);
        if(index > 0) {
            games.remove(index);
        }
    }

    @Override
    public void registerObserver(Observer o) {
        games.add(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer game : games) {
            game.update();
        }
    }
}
