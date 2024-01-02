package org.server;

import java.awt.*;
import java.util.Set;

public abstract class BoardBuilder {
    public abstract boolean isIntersectionEmpty(int X, int Y);
    public abstract boolean isStoneBreathing(int X, int Y);
    public abstract boolean isKoViolation();
    public abstract boolean isValidCoordinate(int x, int y);
    public abstract void searchForAdjacentEnemyStones(int X, int Y, StoneColor enemyColor, StoneColor allyColor);
    public abstract void stoneRemover(int X, int Y, StoneColor enemyColor, StoneColor allyColor);
    public abstract boolean checkIfSurrounded(int X, int Y, StoneColor enemyColor, Set<Point> surroundedStones, Set<Point> visited);
}
