package org.server;

// Interfejs fabryki abstrakcyjnej dla zasad gry Go
public interface GoRuleFactory {
    GoRule createRule();
}