package org.server.gameLogic;

public class Stone extends StoneProperties {
    public void placeStone(StoneColor color) {
        if (this.getState() == IntersectionState.EMPTY) {
            this.setColor(color);
            this.setState(IntersectionState.OCCUPIED);
        } else {
            throw new IllegalStateException("The intersection is already occupied.");
        }
    }
    public void removeStone() {
        if (getState() == IntersectionState.OCCUPIED) {
            this.setColor(StoneColor.REMOVED);
            this.setState(IntersectionState.EMPTY);
        } else {
            throw new IllegalStateException("No stone to remove at this intersection.");
        }
    }
    public void deleteStone() {
        this.setColor(StoneColor.N);
        this.setState(IntersectionState.EMPTY);
    }

}
