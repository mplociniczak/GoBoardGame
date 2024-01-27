package org.server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface iGameThread {
    void sendMove(ObjectOutputStream out, int X, int Y) throws IOException;
}
