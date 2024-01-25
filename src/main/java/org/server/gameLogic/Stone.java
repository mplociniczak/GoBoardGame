package org.server.gameLogic;

/**
 * The Stone class represents an intersection on a Go game board and extends StoneProperties.
 * It includes methods to place, remove, and delete stones at the intersection.
 */
public class Stone extends StoneProperties {

    /**
     * Places a stone at the intersection with the specified color.
     *
     * @param color The color of the stone to be placed.
     * @throws IllegalStateException If the intersection is already occupied.
     */
    public void placeStone(StoneColor color) {
        if (this.getState() == IntersectionState.EMPTY) {
            this.setColor(color);
            this.setState(IntersectionState.OCCUPIED);
        } else {
            throw new IllegalStateException("The intersection is already occupied.");
        }
    }

    /**
     * Removes the stone from the intersection.
     *
     * @throws IllegalStateException If there is no stone to remove at this intersection.
     */
    public void removeStone() {
        if (getState() == IntersectionState.OCCUPIED) {
            this.setColor(StoneColor.REMOVED);
            this.setState(IntersectionState.EMPTY);
        } else {
            throw new IllegalStateException("No stone to remove at this intersection.");
        }
    }

    /**
     * Deletes the stone at the intersection, setting it to an empty state.
     */
    public void deleteStone() {
        this.setColor(StoneColor.N);
        this.setState(IntersectionState.EMPTY);
    }

}
