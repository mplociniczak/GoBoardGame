package org.server.gameLogic;

/**
 * The IntersectionState enum represents the possible states of a Go board intersection.
 * Each intersection can be either EMPTY or OCCUPIED.
 */
public enum IntersectionState {
    /**
     * Represents an empty intersection on the Go board.
     */
    EMPTY,

    /**
     * Represents an intersection that is occupied by a stone on the Go board.
     */
    OCCUPIED,
}
