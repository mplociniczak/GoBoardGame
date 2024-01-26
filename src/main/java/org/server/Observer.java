package org.server;

import org.server.gameLogic.StoneColor;

import java.io.IOException;
import java.io.ObjectInputStream;

public interface Observer {
    public void update();
}
