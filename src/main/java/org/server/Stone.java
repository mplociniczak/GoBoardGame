package org.server;

public class Stone {
    private StoneColor color;
    private IntersectionState intersectionState;

    public Stone() {
        this.color = StoneColor.N;
        this.intersectionState = IntersectionState.EMPTY;
    }

    public void placeStone(StoneColor color) {
        if (intersectionState == IntersectionState.EMPTY) {
            this.color = color;
            this.intersectionState = IntersectionState.OCCUPIED;
        } else {
            throw new IllegalStateException("The intersection is already occupied.");
        }
    }

    public void removeStone() {
        if (intersectionState == IntersectionState.OCCUPIED) {
            this.color = StoneColor.N;
            this.intersectionState = IntersectionState.EMPTY;
        } else {
            throw new IllegalStateException("No stone to remove at this intersection.");
        }
    }

    public StoneColor getColor() {
        return color;
    }
    public void setColor(String color) { this.color = StoneColor.valueOf(color); }
    public IntersectionState getState() {
        return intersectionState;
    }
    public void setIntersectionState(IntersectionState state) { this.intersectionState = state; }
}