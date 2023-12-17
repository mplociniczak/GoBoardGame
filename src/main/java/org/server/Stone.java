package org.server;

import org.server.StoneColor;

public class Stone {
    private StoneColor color;
    private State state;

    public Stone() {
        this.color = null;
        this.state = State.EMPTY;
    }

    public void placeStone(StoneColor color) {
        if (state == State.EMPTY) {
            this.color = color;
            this.state = State.OCCUPIED;
        } else {
            throw new IllegalStateException("The intersection is already occupied.");
        }
    }

    public StoneColor getColor() {
        return color;
    }

    public State getState() {
        return state;
    }
}