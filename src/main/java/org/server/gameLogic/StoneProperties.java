package org.server.gameLogic;

/**
 * The StoneProperties class represents the properties of a stone on a Go game board.
 * It includes information about the stone's color and intersection state.
 */
public class StoneProperties {
    private StoneColor color;
    private IntersectionState intersectionState;

    /**
     * Constructs a new StoneProperties object with initial values.
     * The color is set to EMPTY and the intersection state is set to EMPTY.
     */
    public StoneProperties() {
        this.color = StoneColor.N;
        this.intersectionState = IntersectionState.EMPTY;
    }

    /**
     * Gets the color of the stone.
     *
     * @return The color of the stone.
     */
    public StoneColor getColor() {
        return color;
    }

    /**
     * Sets the color of the stone.
     *
     * @param color The color to set for the stone.
     */
    public void setColor(StoneColor color) {
        this.color = color;
    }

    /**
     * Gets the intersection state of the stone.
     *
     * @return The intersection state of the stone.
     */
    public IntersectionState getState() {
        return intersectionState;
    }

    /**
     * Sets the intersection state of the stone.
     *
     * @param state The intersection state to set for the stone.
     */
    public void setState(IntersectionState state) {
        this.intersectionState = state;
    }
}
