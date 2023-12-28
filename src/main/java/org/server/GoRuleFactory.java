package org.server;

// Interfejs fabryki dla zasad gry Go
public interface GoRuleFactory {
    boolean check(Board board, int X, int Y);
}
