package org.server;

public interface Subject {
    public void removeObserver(Observer o);
    public void registerObserver(Observer o);
    public void notifyObservers();
}
