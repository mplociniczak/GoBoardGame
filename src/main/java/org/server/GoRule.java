package org.server;

// Interfejs reprezentujący zasadę na planszy Go
public interface GoRule {
    boolean check(Board board, int X, int Y);
}
