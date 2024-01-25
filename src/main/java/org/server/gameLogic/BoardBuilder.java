package org.server.gameLogic;

import org.server.gameLogic.StoneColor;

import java.awt.*;
import java.util.Set;

/**
 * The BoardBuilder class provides an abstract blueprint for classes responsible for building and modifying
 * the game board based on stone placement and game rules.
 */
public abstract class BoardBuilder {
    public abstract void setStoneRemovedFlagToFalse();

    /**
     * Checks if the intersection at the specified coordinates is empty.
     *
     * @param X The x-coordinate of the intersection.
     * @param Y The y-coordinate of the intersection.
     * @return True if the intersection is empty, false otherwise.
     */
    public abstract boolean isIntersectionEmpty(int X, int Y);

    /**
     * Checks if the stone at the specified coordinates has breathing space.
     *
     * @param X The x-coordinate of the stone.
     * @param Y The y-coordinate of the stone.
     * @return True if the stone has breathing space, false otherwise.
     */
    public abstract boolean isStoneBreathing(int X, int Y);

    /**
     * Checks if a move violates the Ko rule.
     *
     * @return True if the move violates the Ko rule, false otherwise.
     */
    public abstract boolean isKoViolation();

    /**
     * Checks if the specified coordinates are valid on the game board.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return True if the coordinates are valid, false otherwise.
     */
    public abstract boolean isValidCoordinate(int x, int y);

    /**
     * Searches for adjacent enemy stones and updates the game board based on the specified parameters.
     *
     * @param X          The x-coordinate of the placed stone.
     * @param Y          The y-coordinate of the placed stone.
     * @param enemyColor The color of the opponent's stones.
     * @param allyColor  The color of the player's stones.
     */
    public abstract void searchForAdjacentEnemyStones(int X, int Y, StoneColor enemyColor, StoneColor allyColor);

    /**
     * Removes stones from the board based on the specified parameters.
     *
     * @param X          The x-coordinate of the stone to be removed.
     * @param Y          The y-coordinate of the stone to be removed.
     * @param enemyColor The color of the opponent's stones.
     * @param allyColor  The color of the player's stones.
     */
    public abstract void stoneRemover(int X, int Y, StoneColor enemyColor, StoneColor allyColor);

    /**
     * Checks if a group of stones is surrounded on the board.
     *
     * @param X              The x-coordinate of any stone in the group.
     * @param Y              The y-coordinate of any stone in the group.
     * @param enemyColor     The color of the opponent's stones.
     * @param surroundedStones Set to store surrounded stone coordinates.
     * @param visited        Set to keep track of visited coordinates during the check.
     * @return True if the group is surrounded, false otherwise.
     */
    public abstract boolean checkIfSurrounded(int X, int Y, StoneColor enemyColor, Set<Point> surroundedStones, Set<Point> visited);
}
